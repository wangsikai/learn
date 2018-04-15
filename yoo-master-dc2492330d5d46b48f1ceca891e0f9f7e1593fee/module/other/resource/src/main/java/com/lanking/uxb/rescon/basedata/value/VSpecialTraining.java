package com.lanking.uxb.rescon.basedata.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingStatus;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSubject;

/**
 * 针对性训练VO
 * 
 * @since 2.0.1
 * @author wangsenhao
 *
 */
public class VSpecialTraining implements Serializable {

	private static final long serialVersionUID = 4951200097055570088L;
	private Long id;
	private Long knowpointCode;
	private Double difficulty;
	private String name;
	private SpecialTrainingStatus status;
	private String createUser;
	private Date createAt;
	private List<VSpecialTrainingOperateLog> logList;
	private VPhase phase;
	private VSubject subject;
	private String catalog;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SpecialTrainingStatus getStatus() {
		return status;
	}

	public void setStatus(SpecialTrainingStatus status) {
		this.status = status;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public List<VSpecialTrainingOperateLog> getLogList() {
		return logList;
	}

	public void setLogList(List<VSpecialTrainingOperateLog> logList) {
		this.logList = logList;
	}

	public VPhase getPhase() {
		return phase;
	}

	public void setPhase(VPhase phase) {
		this.phase = phase;
	}

	public VSubject getSubject() {
		return subject;
	}

	public void setSubject(VSubject subject) {
		this.subject = subject;
	}

	public Long getKnowpointCode() {
		return knowpointCode;
	}

	public void setKnowpointCode(Long knowpointCode) {
		this.knowpointCode = knowpointCode;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

}
