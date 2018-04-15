package com.lanking.uxb.service.zuoye.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.lanking.cloud.domain.common.resource.question.Question.Type;

/**
 * 教师错题查询条件
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月16日
 */
public class ZyTeacherFallibleQuestionQuery {

	private long teacherId;
	private BigDecimal leRightRate;
	private BigDecimal reRightRate;
	private BigDecimal leDifficulty;
	private BigDecimal reDifficulty;
	private Integer textbookCode;
	private Set<Long> sectionCodes;
	private Long sectionCode;
	private Boolean isUpdateAtDesc;
	private Boolean isCreateAtDesc;
	private Boolean isDifficultyDesc;
	private Boolean isRightRateDesc;
	private boolean leftOpen = false;
	private boolean rightOpen = false;
	private boolean rateleftOpen = false;
	private boolean raterightOpen = false;
	private String key;
	private Long cursor; // 游标
	/**
	 * 时间范围:不设置表示全部,1:表示最近一周2:表示最近一个月3:表示本学年(跟设计交流中,感觉这个随便可能变,具有很大的不确定性,所以不用枚举了)
	 */
	private Integer timeRange;
	/**
	 * 题目类型
	 */
	private Long typeCode;

	/**
	 * 题目类型
	 */
	private List<Long> typeCodes;
	private List<Type> types;
	/**
	 * 知识点(多个list)
	 */
	private List<Integer> metaKnowpointCodes;
	/**
	 * 知识点
	 */
	private Integer metaKnowpoint;
	/**
	 * 新知识点
	 */
	private List<Long> newKnowpointCodes;

	private boolean searchInSection;

	private Boolean newKeyQuery = false;

	public long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}

	public BigDecimal getLeRightRate() {
		return leRightRate;
	}

	public void setLeRightRate(BigDecimal leRightRate) {
		this.leRightRate = leRightRate;
	}

	public BigDecimal getReRightRate() {
		return reRightRate;
	}

	public void setReRightRate(BigDecimal reRightRate) {
		this.reRightRate = reRightRate;
	}

	public BigDecimal getLeDifficulty() {
		return leDifficulty;
	}

	public void setLeDifficulty(BigDecimal leDifficulty) {
		this.leDifficulty = leDifficulty;
	}

	public BigDecimal getReDifficulty() {
		return reDifficulty;
	}

	public void setReDifficulty(BigDecimal reDifficulty) {
		this.reDifficulty = reDifficulty;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public Set<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(Set<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Boolean getIsUpdateAtDesc() {
		return isUpdateAtDesc;
	}

	public void setIsUpdateAtDesc(Boolean isUpdateAtDesc) {
		this.isUpdateAtDesc = isUpdateAtDesc;
	}

	public Boolean getIsCreateAtDesc() {
		return isCreateAtDesc;
	}

	public void setIsCreateAtDesc(Boolean isCreateAtDesc) {
		this.isCreateAtDesc = isCreateAtDesc;
	}

	public Boolean getIsDifficultyDesc() {
		return isDifficultyDesc;
	}

	public void setIsDifficultyDesc(Boolean isDifficultyDesc) {
		this.isDifficultyDesc = isDifficultyDesc;
	}

	public Boolean getIsRightRateDesc() {
		return isRightRateDesc;
	}

	public void setIsRightRateDesc(Boolean isRightRateDesc) {
		this.isRightRateDesc = isRightRateDesc;
	}

	public Integer getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(Integer timeRange) {
		this.timeRange = timeRange;
	}

	public Long getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Long typeCode) {
		this.typeCode = typeCode;
	}

	public List<Integer> getMetaKnowpointCodes() {
		return metaKnowpointCodes;
	}

	public void setMetaKnowpointCodes(List<Integer> metaKnowpointCodes) {
		this.metaKnowpointCodes = metaKnowpointCodes;
	}

	public Integer getMetaKnowpoint() {
		return metaKnowpoint;
	}

	public void setMetaKnowpoint(Integer metaKnowpoint) {
		this.metaKnowpoint = metaKnowpoint;
	}

	public boolean isLeftOpen() {
		return leftOpen;
	}

	public void setLeftOpen(boolean leftOpen) {
		this.leftOpen = leftOpen;
	}

	public boolean isRightOpen() {
		return rightOpen;
	}

	public void setRightOpen(boolean rightOpen) {
		this.rightOpen = rightOpen;
	}

	public boolean isRateleftOpen() {
		return rateleftOpen;
	}

	public void setRateleftOpen(boolean rateleftOpen) {
		this.rateleftOpen = rateleftOpen;
	}

	public boolean isRaterightOpen() {
		return raterightOpen;
	}

	public void setRaterightOpen(boolean raterightOpen) {
		this.raterightOpen = raterightOpen;
	}

	public boolean isSearchInSection() {
		return searchInSection;
	}

	public void setSearchInSection(boolean searchInSection) {
		this.searchInSection = searchInSection;
	}

	public List<Long> getTypeCodes() {
		return typeCodes;
	}

	public void setTypeCodes(List<Long> typeCodes) {
		this.typeCodes = typeCodes;
	}

	public List<Type> getTypes() {
		return types;
	}

	public void setTypes(List<Type> types) {
		this.types = types;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<Long> getNewKnowpointCodes() {
		return newKnowpointCodes;
	}

	public void setNewKnowpointCodes(List<Long> newKnowpointCodes) {
		this.newKnowpointCodes = newKnowpointCodes;
	}

	public Boolean getNewKeyQuery() {
		return newKeyQuery;
	}

	public void setNewKeyQuery(Boolean newKeyQuery) {
		this.newKeyQuery = newKeyQuery;
	}

	public Long getCursor() {
		return cursor;
	}

	public void setCursor(Long cursor) {
		this.cursor = cursor;
	}

}
