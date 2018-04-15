package com.lanking.cloud.domain.yoo.user;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 家长
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "parent")
public class Parent extends UserInfo {

	private static final long serialVersionUID = 7994256026180664409L;

}
