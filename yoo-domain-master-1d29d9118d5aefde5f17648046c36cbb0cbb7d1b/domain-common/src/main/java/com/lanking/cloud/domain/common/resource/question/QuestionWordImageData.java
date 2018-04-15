package com.lanking.cloud.domain.common.resource.question;

import java.io.Serializable;

/**
 * 图片本地存储.
 * 
 * <pre>
 * 目前图片路径有3种：<br>
 * 一种是普通URL，形如 http://www.yoomath.com/fs/250549590862864384<br>
 * 一种是特殊URL，形如：http://www.yoomath.com/dd?fn=1408473489979.png<br>
 * 一种是直接base64数据，形如：data:image/png;base64,iVBORw0KGg...<br>
 * 前两种直接通过FileUtils获得本地存储路径，第三种直接存储base64数据
 * </pre>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
public class QuestionWordImageData implements Serializable {

	private static final long serialVersionUID = -8617150372798275987L;

	/**
	 * WORD使用的rid标识.
	 */
	private String rid;

	/**
	 * 本地存储路径.
	 */
	private String localPath;

	/**
	 * 源URL.
	 */
	private String url;

	/**
	 * BASE64数据.
	 */
	private String base64;

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
