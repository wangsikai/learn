package com.lanking.uxb.service.thirdparty.eduyun.response;

import java.io.Serializable;
import java.util.List;

import com.lanking.uxb.service.thirdparty.TokenInfo;

/**
 * 教育云用户.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年7月3日
 */
public class YunUser implements Serializable {
	private static final long serialVersionUID = 7320189857103379504L;

	/**
	 * TOKEN.
	 */
	private TokenInfo tokenInfo;

	/**
	 * sessionID.
	 */
	private String usessionid;

	/**
	 * 用户ID.
	 */
	private String personid;

	/**
	 * 邮箱地址.
	 */
	private String email;

	/**
	 * 电话号码.
	 */
	private String mobnum;

	/**
	 * 生日.
	 */
	private String birthday;

	/**
	 * 姓名.
	 */
	private String name;

	/**
	 * 昵称.
	 */
	private String nickname;

	/**
	 * 用户归属地区.
	 */
	private String areacode;

	/**
	 * 性别，0：女，1：男.
	 */
	private Integer gender;

	/**
	 * 用户类型. 0：学生，1：老师，2：家长，3：机构，4： 学校，5：学校工作人员，6：机构工作人员
	 */
	private Integer usertype;

	/**
	 * 通讯地址.
	 */
	private String address;

	private String updateTime;

	/**
	 * 头像地址.
	 */
	private String logourl;

	/**
	 * 头像地址列表.
	 */
	private List<Logourl> userlogolist;

	/**
	 * 帐号.
	 */
	private String account;

	/**
	 * 原帐号.
	 */
	private String oaccount;

	/**
	 * 如登录用户为教师，则返回所教学科.
	 */
	private List<YunClass> teachesubjectlist;

	/**
	 * 学科.
	 */
	private String subjectid;

	/**
	 * 学科名称.
	 */
	private String subjectname;

	/**
	 * 教材id.
	 */
	private String teachmaterialid;

	/**
	 * 教材名称.
	 */
	private String teachmaterialname;

	/**
	 * 所在班级ID.
	 */
	private String classid;

	/**
	 * 班级名称.
	 */
	private String classname;

	/**
	 * 年级.
	 */
	private String grade;

	/**
	 * 阶段.
	 */
	private String studysection;

	public TokenInfo getTokenInfo() {
		return tokenInfo;
	}

	public void setTokenInfo(TokenInfo tokenInfo) {
		this.tokenInfo = tokenInfo;
	}

	public String getUsessionid() {
		return usessionid;
	}

	public void setUsessionid(String usessionid) {
		this.usessionid = usessionid;
	}

	public String getPersonid() {
		return personid;
	}

	public void setPersonid(String personid) {
		this.personid = personid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUsertype() {
		return usertype;
	}

	public void setUsertype(Integer usertype) {
		this.usertype = usertype;
	}

	public String getMobnum() {
		return mobnum;
	}

	public void setMobnum(String mobnum) {
		this.mobnum = mobnum;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAreacode() {
		return areacode;
	}

	public void setAreacode(String areacode) {
		this.areacode = areacode;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLogourl() {
		return logourl;
	}

	public void setLogourl(String logourl) {
		this.logourl = logourl;
	}

	public List<YunClass> getTeachesubjectlist() {
		return teachesubjectlist;
	}

	public void setTeachesubjectlist(List<YunClass> teachesubjectlist) {
		this.teachesubjectlist = teachesubjectlist;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public List<Logourl> getUserlogolist() {
		return userlogolist;
	}

	public void setUserlogolist(List<Logourl> userlogolist) {
		this.userlogolist = userlogolist;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getOaccount() {
		return oaccount;
	}

	public void setOaccount(String oaccount) {
		this.oaccount = oaccount;
	}

	public String getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(String subjectid) {
		this.subjectid = subjectid;
	}

	public String getSubjectname() {
		return subjectname;
	}

	public void setSubjectname(String subjectname) {
		this.subjectname = subjectname;
	}

	public String getTeachmaterialid() {
		return teachmaterialid;
	}

	public void setTeachmaterialid(String teachmaterialid) {
		this.teachmaterialid = teachmaterialid;
	}

	public String getTeachmaterialname() {
		return teachmaterialname;
	}

	public void setTeachmaterialname(String teachmaterialname) {
		this.teachmaterialname = teachmaterialname;
	}

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getStudysection() {
		return studysection;
	}

	public void setStudysection(String studysection) {
		this.studysection = studysection;
	}
}
