package com.lanking.cloud.domain.yoo.user;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 学生
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "student")
public class Student extends StudentInfo {

	private static final long serialVersionUID = -1616669788116112826L;

}