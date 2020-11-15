package py.com.sodep.mobileforms.net.sync;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import py.com.sodep.mf.form.model.MFForm;
import py.com.sodep.mf.form.model.MFPage;
import py.com.sodep.mf.form.model.element.MFElement;

public class Document2JsonWriter {

	static void writeDocumentSaveRequest(JsonGenerator g, MFForm mfform, Map<String, String> data)
			throws JsonGenerationException, IOException {
		g.writeStartObject();
		g.writeNumberField("formId", mfform.getId());
		g.writeNumberField("version", mfform.getVersion());
        g.writeStringField("savedAt", formatDate(data.get("saved_at")));
		String location = data.get("location");
		if (location != null) {
			g.writeStringField("location", location);
		}
		writePageDataField(g, mfform, data);
		g.writeEndObject();
		g.flush();
	}

    /**
     * CAP-152
     * We add this method as a workaround to parse the time,
     * which was not sent with the right TimeZone to the server.
     *
     * @param savedDateStr
     * @return
     */
    private static String formatDate(String savedDateStr) {
        try {
            DateFormat dateFormatISO8601 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormatISO8601.setTimeZone(TimeZone.getTimeZone("GMT"));
            // the string has a time with incorrect timezone, so:
            // 1) We parse it
            Date savedDate = dateFormatISO8601.parse(savedDateStr);

            DateFormat dateFormatISO8601Default = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // 2) Format it again to yield the right time in which the
            // document was saved.
            String dateStrForSerialization = dateFormatISO8601Default.format(savedDate);
            return dateStrForSerialization;
        } catch (ParseException e) {
           throw new RuntimeException(e);
        }
    }

    private static void writePageDataField(JsonGenerator g, MFForm mfform, Map<String, String> data)
			throws JsonGenerationException, IOException {
		g.writeFieldName("pageData");
		g.writeStartArray();
		List<MFPage> pages = mfform.getPages();
		Set<String> keySet = data.keySet();
		Map<String, String> pageData = new HashMap<String, String>();
		for (MFPage page : pages) {
			List<MFElement> elements = page.getElements();
			boolean dataFromPage = false;
			pageData.clear();
			for (MFElement element : elements) {
				String instanceId = element.getInstanceId();
				boolean contains = keySet.contains(instanceId);
				if (contains) {
					String elementData = data.get(instanceId);
					if (elementData != null && elementData.trim().length() > 0) {
						// Do not include empty fields as this will cause
						// an Exception in the server and the document sync 
						// will be marked as FAILED
						pageData.put(instanceId, data.get(instanceId));
						dataFromPage = true;
					}
				}
			}

			if (dataFromPage) {
				g.writeStartObject();
				g.writeNumberField("pageId", page.getId());
				g.writeFieldName("data");
				writeMap(g, mfform, pageData);
				g.writeEndObject();
			}
		}
		g.writeEndArray();
		g.flush();
	}

	private static void writeMap(JsonGenerator g, MFForm mfform, Map<String, String> data) throws IOException,
			JsonGenerationException {
		Map<String, MFElement> elementsMap = mfform.elementsMappedByName();
		g.writeStartObject();
		writeFields(g, elementsMap, data);
		g.writeEndObject();
		g.flush();
	}

	/**
	 * If the element is a file, then the value is the key. In the multiplexed
	 * file, the content is added with that key as the file's name
	 * 
	 * @param g
	 * @param elementsMap
	 * @param doc
	 * @throws IOException
	 * @throws JsonGenerationException
	 */
	private static void writeFields(JsonGenerator g, Map<String, MFElement> elementsMap, Map<String, String> doc)
			throws IOException, JsonGenerationException {
		for (String key : doc.keySet()) {
			MFElement element = elementsMap.get(key);
			String value = doc.get(key);
			if (element != null) {
				if (element.getProto().isFile()) {
					File f = new File(value);
					if (f.exists() && f.length() > 0) {
						// Does the file exist and is not empty?
						g.writeStringField(key, key); // why the key? Because The key is going to be the name of the file
					} else {
						//FIXME Just ignore if there's a missing file?...
					}
				} else {
					g.writeStringField(key, value);
				}
			} else {
				g.writeStringField(key, value);
			}
		}
	}
}
