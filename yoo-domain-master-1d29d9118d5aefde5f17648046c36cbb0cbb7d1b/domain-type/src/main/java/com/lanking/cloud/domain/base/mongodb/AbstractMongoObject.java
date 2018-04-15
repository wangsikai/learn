package com.lanking.cloud.domain.base.mongodb;

import java.io.Serializable;

/**
 * mongo db 数据基类，所有以对象方式存储到mongodb中的必须继承此类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public abstract class AbstractMongoObject implements Serializable {

	private static final long serialVersionUID = 8632583775302108885L;

}
