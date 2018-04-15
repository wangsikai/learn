package com.lanking.cloud.domain.yoo.user;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 教师
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "teacher")
public class Teacher extends TeacherInfo {

	private static final long serialVersionUID = -7947600594513664820L;

}
