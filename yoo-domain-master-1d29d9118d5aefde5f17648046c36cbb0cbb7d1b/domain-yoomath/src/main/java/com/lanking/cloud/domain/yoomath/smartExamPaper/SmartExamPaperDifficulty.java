package com.lanking.cloud.domain.yoomath.smartExamPaper;

/**
 * 智能出卷难度类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public enum SmartExamPaperDifficulty {
	/**
	 * 三星
	 */
	STAR_3(0, "（0.6-0.8】", 3, "A.《%s》综合试卷一"),
	/**
	 * 四星
	 */
	STAR_4(1, "（0.3-0.6】", 4, "B.《%s》综合试卷二"),
	/**
	 * 五星
	 */
	STAR_5(2, "（0-0.3】", 5, "C.《%s》综合试卷三");

	private int value;
	private String difficult;
	private int star;
	private String title;

	private SmartExamPaperDifficulty(int value, String difficult, int star, String title) {
		this.value = value;
		this.difficult = difficult;
		this.star = star;
		this.title = title;
	}

	public int getValue() {
		return value;
	}

	public String getDifficult() {
		return difficult;
	}

	public int getStar() {
		return star;
	}

	public String getTitle() {
		return title;
	}

	public static SmartExamPaperDifficulty findByValue(int value) {
		switch (value) {
		case 0:
			return STAR_3;
		case 1:
			return STAR_4;
		case 2:
			return STAR_5;
		default:
			return null;
		}
	}

}
