package com.lanking.uxb.service.file.api.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.lanking.cloud.domain.base.file.api.FileExt;
import com.lanking.cloud.ex.core.NotImplementedException;

@Component("windowDocumentHandle")
public class WindowDocumentHandle extends AbtractDocumentHandle {

	private Logger logger = LoggerFactory.getLogger(WindowDocumentHandle.class);

	static final int WORD_FORMATPDF = 17;
	static final int PPT_SAVEAS_PDF = 32;

	@Override
	public void convertDOC2PDF(String filePath, com.lanking.cloud.domain.base.file.File f) {
		String ext = f.getExt();
		if (WORD.contains(ext)) {
			word2Pdf(filePath, filePath.split("\\.")[0] + "." + FileExt.PDF);
		} else if (EXCEL.contains(ext)) {
			excel2Pdf(filePath, filePath.split("\\.")[0] + "." + FileExt.PDF);
		} else if (POWERPOINT.contains(ext)) {
			powerpoint2Pdf(filePath, filePath.split("\\.")[0] + "." + FileExt.PDF);
		}
	}

	@Override
	public void convertPDF2SWF(String filePath, com.lanking.cloud.domain.base.file.File f) {
		throw new NotImplementedException();
	}

	@Override
	public void convertDOC2SWF(String filePath, com.lanking.cloud.domain.base.file.File f) {
		throw new NotImplementedException();

	}

	private void word2Pdf(String source, String target) {
		ActiveXComponent app = null;
		try {
			app = new ActiveXComponent("Word.Application");
			app.setProperty("Visible", false);
			Dispatch docs = app.getProperty("Documents").toDispatch();
			Dispatch doc = Dispatch.call(docs, "Open", source, false, true).toDispatch();
			new File(target);
			Dispatch.call(doc, "SaveAs", target, WORD_FORMATPDF);
			Dispatch.call(doc, "Close", false);
		} catch (Exception e) {
			logger.error("word->pdf:", e);
		} finally {
			if (app != null)
				app.invoke("Quit", 0);
		}
	}

	private void excel2Pdf(String source, String target) {
		ActiveXComponent app = new ActiveXComponent("Excel.Application");
		try {
			app.setProperty("Visible", false);
			Dispatch workbooks = app.getProperty("Workbooks").toDispatch();
			Dispatch workbook = Dispatch.invoke(workbooks, "Open", Dispatch.Method,
					new Object[] { source, new Variant(false), new Variant(false) }, new int[3]).toDispatch();
			Dispatch.invoke(workbook, "SaveAs", Dispatch.Method, new Object[] { target, new Variant(57),
					new Variant(false), new Variant(57), new Variant(57), new Variant(false), new Variant(true),
					new Variant(57), new Variant(true), new Variant(true), new Variant(true) }, new int[1]);
			Variant f = new Variant(false);
			Dispatch.call(workbook, "Close", f);
		} catch (Exception e) {
			logger.error("excel->pdf:", e);
		} finally {
			if (app != null) {
				app.invoke("Quit", new Variant[] {});
			}
		}
	}

	private void powerpoint2Pdf(String source, String target) {
		ActiveXComponent app = null;
		try {
			app = new ActiveXComponent("Powerpoint.Application");
			Dispatch presentations = app.getProperty("Presentations").toDispatch();
			Dispatch presentation = Dispatch.call(presentations, "Open", source, true, true, false).toDispatch();
			new File(target);
			Dispatch.call(presentation, "SaveAs", target, PPT_SAVEAS_PDF);
			Dispatch.call(presentation, "Close");
		} catch (Exception e) {
			logger.error("powerpoint->pdf:", e);
		} finally {
			if (app != null)
				app.invoke("Quit");
		}
	}
}
