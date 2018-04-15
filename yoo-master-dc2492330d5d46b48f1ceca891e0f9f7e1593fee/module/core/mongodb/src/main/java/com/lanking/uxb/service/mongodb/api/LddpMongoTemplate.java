/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lanking.uxb.service.mongodb.api;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.query.Query;

import com.lanking.cloud.domain.base.mongodb.AbstractMongoObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * mongo的所有操作接口,不用spring-data的MongoTemplate类
 * 
 * @since 2.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年5月20日
 */
public interface LddpMongoTemplate {

	/**
	 * 获取DB对象
	 * 
	 * @since 2.1.0
	 * @return {@link DB}
	 */
	DB $getDb();

	/**
	 * 根据类获取集合名称
	 * 
	 * @since 2.1.0
	 * @param entityClass
	 *            collection type 类
	 * @return 集合名称
	 */
	String $getCollectionName(Class<? extends AbstractMongoObject> entityClass);

	/**
	 * 创建集合
	 * 
	 * @since 2.1.0
	 * @param entityClass
	 *            collection type 类
	 * @return {@link DBCollection}
	 */
	DBCollection $createCollection(Class<? extends AbstractMongoObject> entityClass);

	/**
	 * 创建集合
	 * 
	 * @since 2.1.0
	 * @param entityClass
	 *            collection type 类
	 * @param collectionOptions
	 *            集合参数选项
	 * @return {@link DBCollection}
	 */
	DBCollection $createCollection(Class<? extends AbstractMongoObject> entityClass, CollectionOptions collectionOptions);

	/**
	 * 创建集合
	 * 
	 * @since 2.1.0
	 * @param collectionName
	 *            集合名称
	 * @return {@link DBCollection}
	 */
	DBCollection $createCollection(String collectionName);

	/**
	 * 创建集合
	 * 
	 * @since 2.1.0
	 * @param collectionName
	 *            集合名称
	 * @param collectionOptions
	 *            集合参数选项
	 * @return {@link DBCollection}
	 */
	DBCollection $createCollection(String collectionName, CollectionOptions collectionOptions);

	/**
	 * 获取所有collection name set
	 * 
	 * @since 2.1.0
	 * @return collection name set
	 */
	Set<String> $getCollectionNames();

	/**
	 * 根据集合名称获取DBCollection对象
	 * 
	 * @since 2.1.0
	 * @param collectionName
	 * @return {@link DBCollection}
	 */
	DBCollection $getCollection(String collectionName);

	/**
	 * 判断集合是否存在
	 * 
	 * @since 2.1.0
	 * @param entityClass
	 *            collection type 类
	 * @return 是否存在{true|false}
	 */
	boolean $collectionExists(Class<? extends AbstractMongoObject> entityClass);

	/**
	 * 判断集合是否存在
	 * 
	 * @since 2.1.0
	 * @param collectionName
	 *            集合名称
	 * @return 是否存在{true|false}
	 */
	boolean $collectionExists(String collectionName);

	/**
	 * 删除集合
	 * 
	 * @since 2.1.0
	 * @param entityClass
	 *            collection type 类
	 */
	void $dropCollection(Class<? extends AbstractMongoObject> entityClass);

	/**
	 * 删除集合
	 * 
	 * @since 2.1.0
	 * @param collectionName
	 *            集合名称
	 */
	void $dropCollection(String collectionName);

	/**
	 * 保存对象集合
	 * 
	 * @since 2.1.0
	 * @param batchToSave
	 */
	void $insertAll(Collection<? extends AbstractMongoObject> batchToSave);

	/**
	 * 指定集合名称保存对象集合
	 * 
	 * @since 2.1.0
	 * @param batchToSave
	 *            批量保存的对象
	 * @param collectionName
	 *            集合名词
	 */
	void $insert(Collection<? extends AbstractMongoObject> batchToSave, String collectionName);

	/**
	 * 通过主键查询
	 * 
	 * @since 2.1.0
	 * @param id
	 *            ID
	 * @param entityClass
	 *            collection type 类
	 * @return 数据对象
	 */
	<T extends AbstractMongoObject> T $findById(Object id, Class<T> entityClass);

	/**
	 * 查询所有的数据
	 * 
	 * @since 2.1.0
	 * @param entityClass
	 *            collection type 类
	 * @return 数据对象集合
	 */
	<T extends AbstractMongoObject> List<T> $findAll(Class<T> entityClass);

	/**
	 * 查找单个数据对象
	 * 
	 * @since 2.3.0
	 * @param query
	 *            查询条件
	 * @param entityClass
	 *            collection type 类
	 * @return 数据对象集合
	 */
	<T extends AbstractMongoObject> T $findOne(Query query, Class<T> entityClass);

	/**
	 * 根据条件查询数据
	 * 
	 * @since 2.1.0
	 * @param query
	 *            查询条件
	 * @param entityClass
	 *            collection type 类
	 * @return 数据对象集合
	 */
	<T extends AbstractMongoObject> List<T> $find(Query query, Class<T> entityClass);
}
