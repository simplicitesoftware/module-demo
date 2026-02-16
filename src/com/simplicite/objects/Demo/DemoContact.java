package com.simplicite.objects.Demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

import com.simplicite.util.AppLog;
import com.simplicite.util.ObjectDB;
import com.simplicite.util.ObjectField;
import com.simplicite.util.PrintTemplate;
import com.simplicite.util.Tool;
import com.simplicite.util.annotations.BusinessObjectPublication;
import com.simplicite.util.tools.ExcelTool;
import com.simplicite.util.tools.ExcelTool.ExcelRow;
import com.simplicite.util.tools.HTMLTool;
import com.simplicite.util.tools.HTTPTool;
import com.simplicite.util.tools.MustacheTool;
import com.simplicite.webapp.web.WebPage;

/**
 * Contact business object
 */
public class DemoContact extends ObjectDB {
	private static final long serialVersionUID = 1L;

	@Override
	public void postLoad() {
		// Comments are only visible to administrators and users
		if (getGrant().hasResponsibility("DEMO_ADMIN") || getGrant().hasResponsibility("DEMO_USER"))
			getField("demoCtcComments").setVisibility(ObjectField.VIS_FORM);
	}

	/** Publication: HTML using Mustache(R) templating */
	@BusinessObjectPublication
	public Object printHTML(PrintTemplate pt) {
		try {
			WebPage wp = new WebPage(pt.getDisplay());
			wp.appendCSS(HTMLTool.getResourceCSSContent(getGrant(), "DEMO_PRINT_STYLES")); // Inlined styles
			wp.append(MustacheTool.apply(this, "PRINT"));
			return wp.toString();
		} catch (Exception e) { // Unexpected error => text file with error message
			AppLog.error("Unable to publish " + pt.getName(), e, getGrant());
			pt.setMIMEType(HTTPTool.MIME_TYPE_TXT);
			pt.setFilename(getGrant().T("ERROR") + ".txt");
			return e.getMessage();
		}
	}

	/** Publication: Microsoft Excel(R) sheet */
	@BusinessObjectPublication
	public Object printExcel(PrintTemplate pt) {
		try {
			// Build rows from selected IDs or from current filters
			List<String[]>rows = new ArrayList<>();
			List<String> ids = getSelectedIds();
			if (!Tool.isEmpty(ids)) {
				for (int k = 0; k < ids.size(); k++)
					if (select(ids.get(k)))
						rows.add(getValues());
			} else {
				rows = search(false);
			}

			ExcelTool xls = new ExcelTool(true); // true = XLSX format
			Sheet sheet = xls.addSheet("Contacts");
			for (int i = 0; i < rows.size(); i++) {
				ExcelRow r = new ExcelRow(i);
				String[] row = rows.get(i);
				for (int j = 0; j < row.length; j++)
					r.add(xls.newCell(j, row[j]));
				xls.addRow(sheet, r);
			}

			return xls.generateToByteArray();
		} catch (Exception e) { // Unexpected error => text file with error message
			AppLog.error("Unable to publish " + pt.getName(), e, getGrant());
			pt.setMIMEType(HTTPTool.MIME_TYPE_TXT);
			pt.setFilename(getGrant().T("ERROR") + ".txt");
			return e.getMessage();
		}
	}

	/** Hook override: hide history records on tree view */
	@Override
	public boolean canReference(String objName, String fkFieldName) {
		return !isTreeviewInstance() || "DemoContactHistoric".equals(objName);
	}
}
