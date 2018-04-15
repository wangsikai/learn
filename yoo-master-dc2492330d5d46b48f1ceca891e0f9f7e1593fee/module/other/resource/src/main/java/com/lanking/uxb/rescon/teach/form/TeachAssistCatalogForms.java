package com.lanking.uxb.rescon.teach.form;

import java.io.Serializable;
import java.util.List;

public class TeachAssistCatalogForms implements Serializable {
	private static final long serialVersionUID = 6953789811011951106L;

	private Long teachassistVersionId;

	private List<TeachAssistCatalogForm> forms;

	private List<Long> deleteCatalogs;

	public Long getTeachassistVersionId() {
		return teachassistVersionId;
	}

	public void setTeachassistVersionId(Long teachassistVersionId) {
		this.teachassistVersionId = teachassistVersionId;
	}

	public List<TeachAssistCatalogForm> getForms() {
		return forms;
	}

	public void setForms(List<TeachAssistCatalogForm> forms) {
		this.forms = forms;
	}

	public List<Long> getDeleteCatalogs() {
		return deleteCatalogs;
	}

	public void setDeleteCatalogs(List<Long> deleteCatalogs) {
		this.deleteCatalogs = deleteCatalogs;
	}
}
