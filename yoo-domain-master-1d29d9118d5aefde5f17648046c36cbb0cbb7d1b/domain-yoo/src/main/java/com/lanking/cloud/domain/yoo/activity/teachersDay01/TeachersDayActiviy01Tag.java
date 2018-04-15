package com.lanking.cloud.domain.yoo.activity.teachersDay01;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.domain.yoo.user.Sex;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "teachersday_activity_01_tag")
public class TeachersDayActiviy01Tag implements Serializable {

	private static final long serialVersionUID = 178341697659738869L;

	@Id
	@Column(name = "code")
	private Long code;

	@Column(name = "name", length = 128)
	private String name;

	@Column(precision = 3, name = "sex")
	private Sex sex;
}
