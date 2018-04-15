package com.lanking.uxb.zycon.operation.value;

import java.io.Serializable;

import com.lanking.cloud.sdk.bean.Status;

/**
 * correctUser vo
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月14日 下午5:32:42
 */
public class CCorrectUser implements Serializable {

	private static final long serialVersionUID = 3464884046240675091L;

	private long id;
	private String name;
	private String tel;
	private Status status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
