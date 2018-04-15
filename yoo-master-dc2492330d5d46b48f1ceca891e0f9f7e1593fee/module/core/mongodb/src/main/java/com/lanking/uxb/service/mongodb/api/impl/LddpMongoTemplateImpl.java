package com.lanking.uxb.service.mongodb.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;

import com.lanking.cloud.domain.base.mongodb.AbstractMongoObject;
import com.lanking.uxb.service.mongodb.api.LddpMongoTemplate;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class LddpMongoTemplateImpl extends MongoTemplate implements LddpMongoTemplate {

	public LddpMongoTemplateImpl(Mongo mongo, String databaseName, UserCredentials userCredentials) {
		super(mongo, databaseName, userCredentials);
	}

	public LddpMongoTemplateImpl(Mongo mongo, String databaseName) {
		super(mongo, databaseName);
	}

	public LddpMongoTemplateImpl(MongoDbFactory mongoDbFactory, MongoConverter mongoConverter) {
		super(mongoDbFactory, mongoConverter);
	}

	public LddpMongoTemplateImpl(MongoDbFactory mongoDbFactory) {
		super(mongoDbFactory);
	}

	@Override
	public DB $getDb() {
		return super.getDb();
	}

	@Override
	public String $getCollectionName(Class<? extends AbstractMongoObject> entityClass) {
		return super.getCollectionName(entityClass);
	}

	@Override
	public DBCollection $createCollection(Class<? extends AbstractMongoObject> entityClass) {
		return super.createCollection(entityClass);
	}

	@Override
	public DBCollection $createCollection(Class<? extends AbstractMongoObject> entityClass,
			CollectionOptions collectionOptions) {
		return super.createCollection(entityClass, collectionOptions);
	}

	@Override
	public DBCollection $createCollection(String collectionName) {
		return super.createCollection(collectionName);
	}

	@Override
	public DBCollection $createCollection(String collectionName, CollectionOptions collectionOptions) {
		return super.createCollection(collectionName, collectionOptions);
	}

	@Override
	public Set<String> $getCollectionNames() {
		return super.getCollectionNames();
	}

	@Override
	public DBCollection $getCollection(String collectionName) {
		return super.getCollection(collectionName);
	}

	@Override
	public boolean $collectionExists(Class<? extends AbstractMongoObject> entityClass) {
		return super.collectionExists(entityClass);
	}

	@Override
	public boolean $collectionExists(String collectionName) {
		return super.collectionExists(collectionName);
	}

	@Override
	public void $dropCollection(Class<? extends AbstractMongoObject> entityClass) {
		super.dropCollection(entityClass);
	}

	@Override
	public void $dropCollection(String collectionName) {
		super.dropCollection(collectionName);
	}

	@Override
	public void $insert(Collection<? extends AbstractMongoObject> batchToSave, String collectionName) {
		super.insert(batchToSave, collectionName);
	}

	@Override
	public void $insertAll(Collection<? extends AbstractMongoObject> batchToSave) {
		super.insertAll(batchToSave);
	}

	@Override
	public <T extends AbstractMongoObject> T $findById(Object id, Class<T> entityClass) {
		return super.findById(id, entityClass);
	}

	@Override
	public <T extends AbstractMongoObject> List<T> $findAll(Class<T> entityClass) {
		return super.findAll(entityClass);
	}

	@Override
	public <T extends AbstractMongoObject> T $findOne(Query query, Class<T> entityClass) {
		return super.findOne(query, entityClass);
	}

	@Override
	public <T extends AbstractMongoObject> List<T> $find(Query query, Class<T> entityClass) {
		return super.find(query, entityClass);
	}
}
