package com.lanking.cloud.domain.common.resource.card;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 知识点卡片
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "knowledge_point_card")
public class KnowledgePointCard implements Serializable {

	private static final long serialVersionUID = -4877235397753337794L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 知识点代码
	 */
	@Column(name = "knowpoint_code")
	private long knowpointCode;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 描述
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "description")
	private String description;

	/**
	 * 详细描述
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "detail_description")
	private String detailDescription;

	/**
	 * 提示,以json array方式存储
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "hints")
	private String hints;

	/**
	 * 例题
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 4000)
	private List<Long> questions = Lists.newArrayList();

	/**
	 * 例题数量
	 */
	@Column(name = "question_count")
	private int questionCount;

	/**
	 * 校验状态
	 */
	@Column(name = "check_status", precision = 3, nullable = false)
	private CardStatus checkStatus = CardStatus.EDITING;

	/**
	 * 删除状态
	 */
	@Column(name = "del_status", precision = 3, nullable = false)
	private Status delStatus = Status.ENABLED;

	/**
	 * 创建人ID
	 */
	@Column(name = "create_id")
	private long createId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新人ID
	 */
	@Column(name = "update_id")
	private long updateId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getKnowpointCode() {
		return knowpointCode;
	}

	public void setKnowpointCode(long knowpointCode) {
		this.knowpointCode = knowpointCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetailDescription() {
		return detailDescription;
	}

	public void setDetailDescription(String detailDescription) {
		this.detailDescription = detailDescription;
	}

	public String getHints() {
		return hints;
	}

	public void setHints(String hints) {
		this.hints = hints;
	}

	public List<Long> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Long> questions) {
		this.questions = questions;
	}

	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}

	public CardStatus getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CardStatus checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
	}

	public long getCreateId() {
		return createId;
	}

	public void setCreateId(long createId) {
		this.createId = createId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(long updateId) {
		this.updateId = updateId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
