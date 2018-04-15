package com.lanking.uxb.service.index.api.impl.listener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.annotation.AnnotationUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.index.api.IndexMapping;
import com.lanking.uxb.service.index.api.IndexType;
import com.lanking.uxb.service.search.api.IndexConfigService;
import com.lanking.uxb.service.search.api.IndexInfo;

public class MappingListenerProcessor implements BeanPostProcessor, SmartLifecycle {

	private Logger logger = LoggerFactory.getLogger(MappingListenerProcessor.class);

	private boolean running = false;
	private IndexConfigService indexConfigService;

	public MappingListenerProcessor(IndexConfigService indexConfigService) {
		this.indexConfigService = indexConfigService;
	}

	@Override
	public void start() {
		running = true;
	}

	@Override
	public void stop() {
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public int getPhase() {
		return Integer.MAX_VALUE - 1;
	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		running = false;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		for (Class targetClass = AopUtils.getTargetClass(bean); targetClass != null; targetClass = targetClass
				.getSuperclass()) {
			IndexType indexType = AnnotationUtils.getAnnotation(targetClass, IndexType.class);
			if (indexType != null) {
				try {
					XContentBuilder source = XContentFactory.jsonBuilder().startObject().startObject("properties");
					List<Field> fields = Lists.newArrayList(targetClass.getDeclaredFields());
					Class c = targetClass.getSuperclass();
					while (c != null) {
						fields.addAll(Lists.newArrayList(c.getDeclaredFields()));
						c = c.getSuperclass();
					}
					for (Field field : fields) {
						if (field.getName().equals("serialVersionUID")) {
							continue;
						}
						IndexMapping indexMapping = null;
						boolean skip = false;
						for (Annotation annotation : field.getAnnotations()) {
							if (annotation.annotationType() == JSONField.class) {
								// if JSONField,skip it
								skip = true;
							}
							if (annotation.annotationType() == IndexMapping.class) {
								indexMapping = (IndexMapping) annotation;
								if (indexMapping.ignore()) {
									skip = true;
								}
							}
						}
						if (skip) {
							continue;
						}
						source.startObject(field.getName());
						if (indexMapping != null) {
							if (indexMapping.type() == null) {
								throw new IllegalArgException("type");
							}
							if (StringUtils.isNotBlank(indexMapping.analyzer())) {
								source.field("analyzer", indexMapping.analyzer());
							}
							if (StringUtils.isNotBlank(indexMapping.searchAnalyzer())) {
								source.field("search_analyzer", indexMapping.searchAnalyzer());
							}
							if (StringUtils.isNotBlank(indexMapping.index())) {
								source.field("index", indexMapping.index());
							}
						}
						source.field("type", indexMapping.type().type());
						if (!indexMapping.includeInAll()) {
							source.field("include_in_all", false);
						}
						source.endObject();
					}
					source.endObject().endObject();

					if (Env.getBoolean("index.mapping")) {
						indexConfigService.mapping(indexType.type(), source);
					} else {
						logger.info("mapping script,type:{},source:{}", indexType.type(), source.string());
					}
					IndexInfo.typeScripts.put(indexType.type(), source.string());
				} catch (Exception e) {
					logger.error("create index type {} error:", indexType.type(), e);
				}
			}
		}
		return bean;
	}
}
