package com.lanking.cloud.domain.common.resource.question;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.lanking.cloud.domain.common.resource.question.Question.Type;

/**
 * 习题WordML转换缓存存储
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "question_wordml")
public class QuestionWordMLData implements Serializable {
	private static final long serialVersionUID = -7614073678724609311L;

	/**
	 * 题目ID
	 */
	@Id
	private long id;

	/**
	 * 题目代码
	 */
	@Column(name = "code", length = 40)
	private String code;

	/**
	 * 题目类型
	 */
	@Column(name = "type", precision = 3)
	private Type type;

	/**
	 * 题目题干ML
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "content_ml")
	private String contentML;

	/**
	 * 题目解析ML.
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "analysis_ml")
	private String analysisML;

	/**
	 * 题目提示ML
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "hint_ml")
	private String hintML;

	/**
	 * 答案ML
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "answers_ml")
	private String answersML;

	/**
	 * ML中用到的图片本地存储集合JSON.
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "images_json")
	private String imagesJson;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * ML中用到的图片本地存储集合.
	 */
	@Transient
	private List<QuestionWordImageData> imageDatas;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getContentML() {
		return contentML;
	}

	public void setContentML(String contentML) {
		this.contentML = contentML;
	}

	public String getAnalysisML() {
		return analysisML;
	}

	public void setAnalysisML(String analysisML) {
		this.analysisML = analysisML;
	}

	public String getHintML() {
		return hintML;
	}

	public void setHintML(String hintML) {
		this.hintML = hintML;
	}

	public String getAnswersML() {
		return answersML;
	}

	public void setAnswersML(String answersML) {
		this.answersML = answersML;
	}

	public String getImagesJson() {
		return imagesJson;
	}

	public void setImagesJson(String imagesJson) {
		this.imagesJson = imagesJson;
		if (StringUtils.isNotBlank(this.imagesJson)) {
			this.imageDatas = JSONArray.parseArray(this.imagesJson, QuestionWordImageData.class);
		}
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public List<QuestionWordImageData> getImageDatas() {
		if (imageDatas == null && StringUtils.isNotBlank(this.imagesJson)) {
			this.imageDatas = JSONArray.parseArray(this.imagesJson, QuestionWordImageData.class);
		}
		return imageDatas;
	}

	public void setImageDatas(List<QuestionWordImageData> imageDatas) {
		this.imageDatas = imageDatas;
		if (null != imageDatas && imageDatas.size() > 0) {
			this.imagesJson = JSONArray.toJSONString(imageDatas);
		}
	}
}
