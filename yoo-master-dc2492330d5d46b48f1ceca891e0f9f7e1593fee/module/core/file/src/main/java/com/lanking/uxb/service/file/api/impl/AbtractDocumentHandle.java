package com.lanking.uxb.service.file.api.impl;

import java.io.File;
import java.io.IOException;

import com.lanking.uxb.service.file.api.DocumentHandle;
import com.lanking.uxb.service.file.util.Pdf2ImageUtil;

abstract class AbtractDocumentHandle implements DocumentHandle {

	@Override
	public File pdf2Image(File file) throws IOException {
		return Pdf2ImageUtil.setupOne(file);
	}
}
