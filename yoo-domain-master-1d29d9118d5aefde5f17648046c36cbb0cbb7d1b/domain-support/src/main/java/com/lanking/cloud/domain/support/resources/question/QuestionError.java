package com.lanking.cloud.domain.support.resources.question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 题目纠错
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "question_error")
public class QuestionError implements Serializable {

	private static final long serialVersionUID = -5176176781148180331L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private long questionId;

	/**
	 * 上报错误用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 类型
	 */
	@Column(name = "types", length = 32)
	private String types;

	@Column(name = "description", length = 512)
	private String description;

	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;

	@Transient
	private List<QuestionErrorType> typeList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public List<QuestionErrorType> getTypeList() {
		if (StringUtils.isBlank(this.types)) {
			return new ArrayList<QuestionErrorType>(0);
		} else {
			String[] typeArr = types.split(",");
			typeList = new ArrayList<QuestionErrorType>(typeArr.length);
			for (String type : typeArr) {
				typeList.add(QuestionErrorType.findByValue(Integer.parseInt(type)));
			}
			return typeList;
		}
	}

	public void setTypeList(List<QuestionErrorType> typeList) {
		if (CollectionUtils.isNotEmpty(typeList)) {
			this.types = "";
			for (QuestionErrorType t : typeList) {
				this.types += t.getValue() + ",";
			}
		}
		this.typeList = typeList;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
