package com.lanking.cloud.domain.yoomath.stat;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 * 学生纸质报告
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月25日
 */
@Getter
@Setter
@Entity
@Table(name = "student_paper_report")
public class StudentPaperReport implements Serializable {

	private static final long serialVersionUID = 7461380341942492977L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 对应的记录ID
	 */
	@Column(name = "record_id")
	private long recordId;

	/**
	 * 班级ID（冗余字段，和对应student_paper_report_record里面一致）
	 */
	@Column(name = "class_id")
	private long classId;

	/**
	 * 开始时间（冗余字段，和对应student_paper_report_record里面一致）
	 */
	@Column(name = "start_date", columnDefinition = "date")
	private Date startDate;

	/**
	 * 结束时间（冗余字段，和对应student_paper_report_record里面一致）
	 */
	@Column(name = "end_date", columnDefinition = "date")
	private Date endDate;

	/**
	 * 学生ID
	 */
	@Column(name = "student_id")
	private long studentId;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", precision = 3)
	private Integer rightRate;

	/**
	 * 班级正确率
	 */
	@Column(name = "class_right_rate", precision = 3)
	private Integer classRightRate;

	/**
	 * 完成率
	 */
	@Column(name = "completion_rate", precision = 3)
	private Integer completionRate;

	/**
	 * 班级完成率
	 */
	@Column(name = "class_completion_rate", precision = 3)
	private Integer classCompletionRate;

	/**
	 * 正确率排名
	 */
	@Column(name = "right_rate_rank", precision = 5)
	private Integer rightRateRank;

	/**
	 * <pre>
	 *  能力图谱(章节掌握情况图谱),JSON类型,实现者自己定义,添加数据格式注释
	 *  		     [
	 *  				{
	 *  			        code:
	 *  			        name:
	 *  			        stu_status:
	 *  					stu_masterRate:
	 *  					class_status:
	 *  					class_masterRate：
	 *  				}
	 *  				{
	 *  					code:
	 *  					name:
	 *  					stu_status:
	 *  					stu_masterRate:
	 *  					class_status：
	 *  					class_masterRate：
	 *  				}...
	 *  			 ]
	 * </pre>
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "section_map")
	private String sectionMap;

	/**
	 * 点评,JSON类型,实现者自己定义,添加数据格式注释<br>
	 * 
	 * <pre>
	 * 1.若掌握程度均为良好，此字段为空
	 * 2.excellent对应掌握优秀的小章节，weak对应掌握薄弱和一般的小章节(分别对应字段章节code，章节名称，掌握情况，掌握度)
	 * 3.可能只会有其中一个,有几个展示几个
	 * </pre>
	 * 
	 * 
	 * <pre>
	 *    		     [
	 *    				excellent:[
	 *    				  {
	 *  			          code:
	 *  			          name:
	 *  			          status:
	 *  					  rate:
	 *  				   }...
	 *    				],
	 *    				weak:[
	 *    				  {
	 *  			          code:
	 *  			          name:
	 *  			          status:
	 *  					  rate:
	 *  				   }...
	 *    				],
	 *    				good:[
	 *    				  {
	 *  			          code:
	 *  			          name:
	 *  			          status:
	 *  					  rate:
	 *  				   }...
	 *    				]
	 *  			 ]
	 * </pre>
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "comment")
	private String comment;

	/**
	 * 综合统计数据，包含正确率（JSON）.
	 * 
	 * <pre>
	 * 分三部分
	 * 1.班级整体使用情况clazzSituation
	 * 2.学生整体使用情况stuSituation
	 * 3.近期作业正确率统计hkList
	 * </pre>
	 * 
	 * 
	 * <pre>
	 *    		     {
	 *    				clazzSituation:[
	 *  			          hkCount:
	 *  			          questionCount:
	 *  			          completionRate:
	 *  					  rightRate:
	 *   					  selfDoCount:
	 *    				],
	 *    				stuSituation:[
	 *  			          hkCount:
	 *  			          questionCount:
	 *  			          completionRate:
	 *  					  rightRate:
	 *   					  selfDoCount:
	 *    				],
	 *    				hkList:[
	 *    				  {
	 *  			          homeworkId:
	 *  			          name:
	 *  			          time:
	 *  					  classRate:
	 *  					  stuRate:
	 *  				   }...
	 *    				]
	 *  			 }
	 * </pre>
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "comprehensive_map")
	private String comprehensiveMap;

	/**
	 * 知识点统计数据（JSON）.
	 * 
	 * 
	 * 
	 * <pre>
	 *  		     [
	 *  				{
	 *  			        code:
	 *  			        name:
	 *  			        difficulty:
	 *  					questionCount:
	 *  					stu_masterRate:
	 *  					class_masterRate:
	 *  				}...
	 *  			 ]
	 * </pre>
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "knowledges")
	private String knowledges;
}
