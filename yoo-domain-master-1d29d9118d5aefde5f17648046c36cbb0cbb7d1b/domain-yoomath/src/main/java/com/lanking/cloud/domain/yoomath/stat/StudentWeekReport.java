package com.lanking.cloud.domain.yoomath.stat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 学生周报告
 * 
 * @since 4.6.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月7日
 */
@Getter
@Setter
@Entity
@Table(name = "student_week_report")
public class StudentWeekReport implements Serializable {

	private static final long serialVersionUID = -340675891577269119L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 开始时间
	 */
	@Column(name = "start_date", columnDefinition = "date")
	private Date startDate;

	/**
	 * 结束时间
	 */
	@Column(name = "end_date", columnDefinition = "date")
	private Date endDate;

	/**
	 * <pre>
	 * 优秀 本周学习成绩很好，请继续保持~ 180%~200% 
	 * 良好 学习情况较好，部分薄弱知识点还需巩固 141%~179% 
	 * 中等 学习情况一般，请及时关注知识点的掌握情况 101%~140% 
	 * 较差 作业质量较差，建议重新学习本周知识，再练习一遍作业中的错题 0%~100%
	 * </pre>
	 * 
	 * 注：这段时间内没有作业score为null
	 */
	private Integer score;

	/**
	 * 完成率
	 */
	@Column(name = "completion_rate", scale = 2)
	private BigDecimal completionRate;

	/**
	 * 完成率和上周比较浮动（与完成率排名浮动区分）
	 */
	@Column(name = "completion_rate_float", precision = 5)
	private Integer completionRateFloat;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 正确率和上周比较浮动（与正确率排名浮动区分）
	 */
	@Column(name = "right_rate_float", precision = 5)
	private Integer rightRateFloat;

	/**
	 * 作业情况
	 * 
	 * <pre>
	 * 数据格式:
	 * 	{
	 * 	    "homeworks":[
	 * 			{
	 * 				id:
	 * 				name:
	 * 				date:
	 * 				rightRate:
	 * 			}...
	 *      ],
	 *      "maxRightRate":{
	 *      	id:
	 *      	name:
	 *      	date:
	 *      	rightRate:
	 *      },
	 *      "minRightRate":{
	 *      	id:
	 *      	name:
	 *      	date:
	 *      	rightRate:
	 *      }
	 * 	}
	 * </pre>
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "homework_analysis")
	private String homeworkAnalysis;

	/**
	 * 正确率班级排名情况
	 * 
	 * <pre>
	 * 数据格式：
	 * 第一个班级也是第一个页面需要显示的班级
	 * [
	 * 	{
	 * 		class:{
	 * 			id:
	 * 			name:
	 * 			myRank:
	 * 			stuNum：
	 * 		},
	 * 		ranks:[
	 * 			{
	 * 				id:
	 * 				rightRate:
	 * 				float:
	 * 				me:
	 * 			}
	 * 		]
	 *  }..
	 * ]
	 * </pre>
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "right_rate_class_ranks")
	private String rightRateClassRanks;

	/**
	 * 章节情况
	 * 
	 * <pre>
	 * version=1,无此字段
	 * version=2
	 * 数据格式如下:
	 * [
	 * 	{
	 * 		code:1,
	 *  	name:'集合',
	 * 		score:'100',
	 *     	masterStatus:1
	 *      children:[{
	 *      	code:1,
	 *      	name:'集合',
	 *      	score:'100',
	 *      	masterStatus:1
	 *      }...
	 *      ]     
	 *  }
	 * ]
	 * </pre>
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "section_analysis")
	private String sectionAnalysis;

	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

}
