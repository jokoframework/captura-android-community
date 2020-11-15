package py.com.sodep.mobileforms.ui.rendering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import py.com.sodep.mf.form.model.element.MFElement;
import py.com.sodep.mf.form.model.element.filter.MFFilter;
import py.com.sodep.mf.form.model.prototype.MFSelect;
import py.com.sodep.mobileforms.dataservices.documents.Document;
import py.com.sodep.mobileforms.dataservices.lookup.LookupDataSource;
import py.com.sodep.mobileforms.dataservices.sql.SQLLookupDataSource;
import py.com.sodep.mobileforms.ui.rendering.objects.LabelAndValue;
import py.com.sodep.mobileforms.ui.rendering.objects.LookupData;
import android.util.Log;

class LookupTableUtils {

	private static final String LOG_TAG = LookupTableUtils.class.getSimpleName();

	private LookupDataSource lookupDataSource;

	private LookupDataSource getLookupDataSource() {
		if (lookupDataSource == null) {
			lookupDataSource = new SQLLookupDataSource();
		}
		return lookupDataSource;
	}

	// Parses the attributes and gets data from a "lookup table"
	List<LabelAndValue> listSelectOptions(MFElement element, Document document) {
		MFSelect selectProto = (MFSelect) element.getProto();
		Long id = selectProto.getLookupTableId();
		String valueField = selectProto.getLookupValue();
		String labelField = selectProto.getLookupLabel();

		List<LabelAndValue> values = new ArrayList<LabelAndValue>();

		try {
			List<MFFilter> itemListFilters = element.getItemListFilters();
			if (itemListFilters != null && !itemListFilters.isEmpty()) {
				itemListFilters = replaceRightValues(itemListFilters, document);
				values = list(id, labelField, valueField, itemListFilters);
			} else {
				values = list(id, labelField, valueField);
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage(), e);
		}

		return values;
	}

	List<LookupData> listPossibleValues(MFElement element, Document document) {
		Long id = element.getDefaultValueLookupTableId();
		String column = element.getDefaultValueColumn();

		LookupDataSource lookupDS = getLookupDataSource();
		List<LookupData> values = Collections.emptyList();

		try {
			List<MFFilter> defaultValueFilters = element.getDefaultValueFilters();
			if (defaultValueFilters != null && !defaultValueFilters.isEmpty()) {
				defaultValueFilters = replaceRightValues(defaultValueFilters, document);
				List<String> selectColumns = new ArrayList<String>();
				selectColumns.add(column);
				List<LookupData[]> lookupData = lookupDS.list(id, selectColumns, defaultValueFilters);
				int size = lookupData.size();
				if (size > 0) {
					values = new ArrayList<LookupData>(size);
					for (LookupData[] row : lookupData) {
						values.add(row[0]);
					}
				}
			} else {
				// TODO WTF to do?
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, e.getMessage(), e);
		}

		return values;
	}

	private List<MFFilter> replaceRightValues(List<MFFilter> filters, Map<String, String> data) {
		List<MFFilter> ret = new ArrayList<MFFilter>(filters.size());
		for (MFFilter f : filters) {
			MFFilter f2 = new MFFilter();
			f2.setColumn(f.getColumn());
			f2.setOperator(f.getOperator());
			String key = f.getRightValue();

			String value = data.get(key);
			if (value != null) {
				f2.setRightValue(value);
			} else {
				f2.setRightValue("");
			}
			ret.add(f2);
		}
		return ret;
	}

	private List<LabelAndValue> list(Long id, String labelField, String valueField) {
		List<MFFilter> empty = Collections.emptyList();
		return list(id, labelField, valueField, empty);
	}

	private List<LabelAndValue> list(Long id, String labelField, String valueField, List<MFFilter> filters) {
		LookupDataSource lookupDS = getLookupDataSource();
		List<String> fields = new ArrayList<String>();
		fields.add(labelField);
		fields.add(valueField);
		List<LookupData[]> data = lookupDS.list(id, fields, filters);
		List<LabelAndValue> labelAndValueList = new ArrayList<LabelAndValue>();
		for (LookupData[] row : data) {
			LabelAndValue lv = new LabelAndValue(row[0], row[1]);
			labelAndValueList.add(lv);
		}
		return labelAndValueList;
	}

}
