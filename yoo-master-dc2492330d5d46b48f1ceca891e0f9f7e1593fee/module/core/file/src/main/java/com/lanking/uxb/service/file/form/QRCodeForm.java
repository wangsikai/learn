package com.lanking.uxb.service.file.form;

/**
 * 二维码获取参数
 * 
 * @since 2.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年8月17日
 */
public class QRCodeForm {
	private String data;
	// [10,600]
	private int size = 60;
	// inline|attachment
	private String contentDisposition = "inline";

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getSize() {
		if (size < 10) {
			setSize(10);
		} else if (size > 600) {
			setSize(600);
		}
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getContentDisposition() {
		if ("inline".equals(contentDisposition) || "attachment".equals(contentDisposition)) {
			return contentDisposition;
		} else {
			setContentDisposition("inline");
		}
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

}
