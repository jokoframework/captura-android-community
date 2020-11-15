package py.com.sodep.mobileforms.dataservices.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This subclass of SQLiteOpenHelper is the responsible of creating the schema
 * to save the Projects, Forms, etc. etc.
 * 
 * @author Miguel
 * 
 */
public class SodepSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String TAG_LOG = SodepSQLiteOpenHelper.class.getSimpleName();

	private static final String DATABASE_NAME = "forms.db";
	private static final int DATABASE_VERSION = 126;
	static final String APPLICATIONS_TABLE = "applications";
	static final String PROJECTS_TABLE = "projects";
	static final String FORMS_TABLE = "forms";
	static final String FORMS_DATA_TABLE = "user_data";
	static final String FORMS_DATA_FIELDS_TABLE = "user_data_fields";

	static final String LOOKUP_DEFINITION_TABLE = "lookup_def";
	static final String LOOKUP_DATA_TABLE = "lookup_data";
	static final String LOOKUP_MAPPING_TABLE = "lookup_mapping";
	static final String LOOKUP_TX_TABLE = "lookup_tx";
	static final String LOOKUP_TX_DML_TABLE = "lookup_tx_dml";
	static final int MAX_LOOKUP_COLUMNS = 30;

	private int initialDocumentId = 0;

	private Context context;

	public SodepSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createDB(db);
	}

	private void createDB(SQLiteDatabase db) {
		String sql = initScript();
		executeMultiple(db, sql);
		if (initialDocumentId > 0) { // need to mantain the documentId sequence
			ContentValues values = new ContentValues();
			values.put("name", FORMS_DATA_TABLE);
			values.put("seq", initialDocumentId);
			db.insert("sqlite_sequence", null, values);
		}
	}

	private String initScript() {
		// 0. Applications, Projects and Forms Tables
		StringBuilder sb = new StringBuilder();
		sb.append("PRAGMA foreign_keys = ON;\n\n");
		sb.append("CREATE TABLE ");
		sb.append(APPLICATIONS_TABLE);
		sb.append("(id INTEGER PRIMARY KEY,\n");
		sb.append("label TEXT NOT NULL,\n");
		sb.append("visible BOOLEAN NOT NULL DEFAULT TRUE,\n");
		sb.append("device_verified BOOLEAN NOT NULL DEFAULT FALSE,\n");
		sb.append("description TEXT);\n\n"); // create Applications table

		sb.append("CREATE TABLE ");
		sb.append(PROJECTS_TABLE);
		sb.append("(id INTEGER PRIMARY KEY,\n");
		sb.append("application INTEGER NOT NULL,\n");
		sb.append("label TEXT NOT NULL,\n");
		sb.append("description TEXT,\n");
		sb.append("visible BOOLEAN NOT NULL DEFAULT TRUE,\n");
		sb.append("FOREIGN KEY(application) REFERENCES " + APPLICATIONS_TABLE + "(id) ON DELETE CASCADE\n");
		sb.append(");\n\n");

		sb.append("CREATE TABLE ");
		sb.append(FORMS_TABLE);
		sb.append("(id INTEGER NOT NULL,\n");
		sb.append("version INTEGER NOT NULL DEFAULT -1,\n");
		sb.append("label TEXT NOT NULL,\n");
		sb.append("description TEXT DEFAULT '',\n");
		sb.append("project INTEGER NOT NULL,\n");
		sb.append("definition TEXT DEFAULT '',\n");
		sb.append("visible BOOLEAN NOT NULL DEFAULT TRUE,\n");
		sb.append("sent INTEGER NOT NULL DEFAULT 0,\n"); // number of wfs sent
		sb.append("errors INTEGER NOT NULL DEFAULT 0,\n"); // number of wfs that
															// had an error
		sb.append("saved INTEGER NOT NULL DEFAULT 0,\n"); // number of wfs saved
		sb.append("required_lookuptables TEXT,\n"); // a comma separated list of
													// lookuptables which are
													// needed by this Form
		sb.append("PRIMARY KEY(id, version),\n");
		sb.append("FOREIGN KEY(project) REFERENCES " + PROJECTS_TABLE + "(id) ON DELETE CASCADE\n");
		sb.append(");\n\n");

		// 1. Data Table
		// All data from all forms, lookup tables, etc. etc. is here
		// Data is serialized to String
		sb.append("CREATE TABLE ");
		sb.append(FORMS_DATA_TABLE);
		sb.append("(_ID INTEGER PRIMARY KEY AUTOINCREMENT,\n");
		sb.append("status int NOT NULL DEFAULT 0,\n");
		sb.append("form INTEGER NOT NULL,\n");
		sb.append("version INTEGER NOT NULL,\n");
		sb.append("data TEXT NOT NULL,\n");
		sb.append("saved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n");
		sb.append("synced_at TIMESTAMP,\n");
		sb.append("failed_at TIMESTAMP,\n");
		sb.append("upload_attempts INTEGER NOT NULL DEFAULT 0,\n");
		sb.append("response_code INTEGER NOT NULL DEFAULT 0,\n");
		sb.append("fail_message TEXT,\n");
		sb.append("md5 TEXT,\n");
		sb.append("FOREIGN KEY(form, version) REFERENCES " + FORMS_TABLE + "(id, version) ON DELETE CASCADE\n");
		sb.append(");\n\n");

		// 2. Meta Data Table.
		// This table maps a form field with a column of the Data Table
		sb.append("CREATE TABLE ");
		sb.append(FORMS_DATA_FIELDS_TABLE);
		sb.append("(_ID INTEGER PRIMARY KEY AUTOINCREMENT,\n");
		sb.append("form INTEGER NOT NULL,\n");
		sb.append("version INTEGER NOT NULL DEFAULT 1,\n");// TODO FK
		sb.append("field_name TEXT NOT NULL,\n");
		sb.append("data_type INTEGER NOT NULL DEFAULT 1,\n");
		sb.append("FOREIGN KEY(form, version) REFERENCES " + FORMS_TABLE + "(id, version) ON DELETE CASCADE\n");
		sb.append(");\n\n");

		sb.append("CREATE TABLE ");
		sb.append(LOOKUP_DEFINITION_TABLE);
		sb.append("(id INTEGER PRIMARY KEY,\n");
		sb.append("metadata_ref TEXT NOT NULL,\n");
		sb.append("version INTEGER NOT NULL,\n");
		sb.append("tx_info TEXT,\n");
		sb.append("synced BOOLEAN NOT NULL DEFAULT 0\n");
		sb.append(");\n\n");

		sb.append("CREATE TABLE ");
		sb.append(LOOKUP_DATA_TABLE);
		sb.append("(_ID INTEGER PRIMARY KEY AUTOINCREMENT,\n");
		sb.append("lookuptable INTEGER NOT NULL,\n");
		sb.append("row_id INTEGER NOT NULL,\n");
		sb.append("row_version INTEGER NOT NULL DEFAULT 1,\n"); // is it ok to
																// set default
																// 1?
		for (int i = 0; i < MAX_LOOKUP_COLUMNS; i++) {
			sb.append("data");
			sb.append(i);
			sb.append(" TEXT,\n");
		}
		sb.append("modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n");
		sb.append("FOREIGN KEY(lookuptable) REFERENCES " + LOOKUP_DEFINITION_TABLE + "(id) ON DELETE CASCADE\n");
		sb.append(");\n\n");

		sb.append("CREATE TABLE ");
		sb.append(LOOKUP_MAPPING_TABLE);
		sb.append("(_ID INTEGER PRIMARY KEY AUTOINCREMENT,\n");
		sb.append("lookuptable INTEGER NOT NULL,\n");
		sb.append("fieldname TEXT NOT NULL,\n");
		sb.append("data_type INTEGER NOT NULL,\n");
		sb.append("data_index INTEGER NOT NULL,");
		sb.append("FOREIGN KEY(lookuptable) REFERENCES " + LOOKUP_DEFINITION_TABLE + "(id) ON DELETE CASCADE\n");
		sb.append(");\n\n");

		sb.append("CREATE TABLE ");
		sb.append(LOOKUP_TX_TABLE);
		sb.append("(id TEXT PRIMARY KEY,");
		sb.append("modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP");
		sb.append(");\n\n");

		sb.append("CREATE TABLE ");
		sb.append(LOOKUP_TX_DML_TABLE);
		sb.append("(_ID INTEGER PRIMARY KEY AUTOINCREMENT,\n");
		sb.append("tx_id TEXT NOT NULL,\n");
		sb.append("lookuptable INTEGER NOT NULL,\n");
		sb.append("dml TEXT NOT NULL,"); // an instance of MFDMLTransport
											// serialized
		sb.append("modified_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,");
		sb.append("FOREIGN KEY(tx_id) REFERENCES " + LOOKUP_TX_TABLE + "(id) ON DELETE CASCADE,\n");
		sb.append("FOREIGN KEY(lookuptable) REFERENCES " + LOOKUP_DEFINITION_TABLE + "(id) ON DELETE CASCADE\n");
		sb.append(");\n\n");

		// 3. Creates the indexes
		// Forms Indexes
		sb.append("CREATE INDEX IF NOT EXISTS forms_on_projects_IDX ON ");
		sb.append(FORMS_TABLE);
		sb.append(" (project);\n\n");
		// Data indexes
		sb.append("CREATE INDEX IF NOT EXISTS form_data_on_form_version_IDX ON ");
		sb.append(FORMS_DATA_TABLE);
		sb.append(" (form, version);\n\n");
		// User Data Mapping indexes
		sb.append("CREATE INDEX IF NOT EXISTS form_data_fields_on_form_version_IDX ON ");
		sb.append(FORMS_DATA_FIELDS_TABLE);
		sb.append(" (form, version);\n\n");
		// Lookup data
		sb.append("CREATE INDEX IF NOT EXISTS lookup_data_on_lookuptable_IDX ON ");
		sb.append(LOOKUP_DATA_TABLE);
		sb.append(" (lookuptable);\n\n");
		
		sb.append("CREATE INDEX IF NOT EXISTS lookup_data_on_lookuptable_IDX2 ON ");
		sb.append(LOOKUP_DATA_TABLE);
		sb.append(" (lookuptable, row_id);\n\n");
		// Lookup data mapping
		sb.append("CREATE INDEX IF NOT EXISTS lookup_mapping_on_lookuptable_IDX ON ");
		sb.append(LOOKUP_MAPPING_TABLE);
		sb.append(" (lookuptable);\n\n");
		// Lookup data mapping
		sb.append("CREATE INDEX IF NOT EXISTS lookup_tx_dml_on_tx_IDX ON ");
		sb.append(LOOKUP_TX_DML_TABLE);
		sb.append(" (tx_id);\n\n");

		return sb.toString();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		drop(db);
		createDB(db);
	}

	// Before dropping the documents table FORMS_DATA_TABLE, get the max _id
	// and then update the autoincrement
	// http://stackoverflow.com/questions/692856/set-start-value-for-autoincrement-in-sqlite
	private void drop(SQLiteDatabase db) {
		Cursor cursor = db
				.rawQuery("SELECT seq FROM SQLITE_SEQUENCE WHERE name = ?", new String[] { FORMS_DATA_TABLE });
		if (cursor.moveToFirst()) {
			if (!cursor.isNull(0)) {
				int int1 = cursor.getInt(0);
				initialDocumentId = int1;
			}
		}

		String sql = dropScript();
		executeMultiple(db, sql);
	}

	private void executeMultiple(SQLiteDatabase db, String sql) {
		String[] statements = sql.split(";");
		for (String stmt : statements) {
			stmt = stmt.trim();
			if (stmt.length() > 0)
				try {
					db.execSQL(stmt);
				} catch (Exception e) {
					Log.e(TAG_LOG, e.getMessage(), e);
				}
		}
	}

	private String dropScript() {
		StringBuilder sb = new StringBuilder();
		sb.append("drop index forms_on_projects_IDX;\n");
		sb.append("drop index form_data_on_form_version_IDX;\n");
		sb.append("drop index form_data_fields_on_form_version_IDX;\n\n");
		sb.append("drop index lookup_data_on_lookuptable_IDX;\n\n");
		sb.append("drop index lookup_mapping_on_lookuptable_IDX;\n\n");
		sb.append("drop index lookup_tx_dml_on_tx_IDX;\n\n");
		sb.append("drop table " + FORMS_DATA_TABLE + ";\n ");
		sb.append("drop table " + FORMS_DATA_FIELDS_TABLE + ";\n");
		sb.append("drop table " + FORMS_TABLE + ";\n");
		sb.append("drop table " + PROJECTS_TABLE + ";\n");
		sb.append("drop table " + APPLICATIONS_TABLE + ";\n");
		sb.append("drop table " + LOOKUP_MAPPING_TABLE + ";\n");
		sb.append("drop table " + LOOKUP_DATA_TABLE + ";\n");
		sb.append("drop table " + LOOKUP_DEFINITION_TABLE + ";\n");
		sb.append("drop table " + LOOKUP_TX_DML_TABLE + ";\n");
		sb.append("drop table " + LOOKUP_TX_TABLE + ";\n");
		return sb.toString();
	}

	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		SQLiteDatabase writableDB = super.getWritableDatabase();
		writableDB.execSQL("PRAGMA foreign_keys = ON;");
		return writableDB;
	}

	public String copyDataBase() throws IOException {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
		InputStream in = null;
		OutputStream os = null;
		try {
			in = new FileInputStream("data/data/" + context.getPackageName() + "/databases/" + DATABASE_NAME);

			File externalFilesDir = context.getExternalFilesDir(null);
			File dir = new File(externalFilesDir.getAbsolutePath() + "/mobileforms/databases");
			dir.mkdirs();
			String fileName = "mf-" + sdf.format(new Date()) + ".sqlite";

			File file = new File(dir, fileName);
			os = new FileOutputStream(file);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			return file.getAbsolutePath();
		} finally {
			if (in != null) {
				in.close();
			}
			if (os != null) {
				os.close();
			}

		}
	}

}
