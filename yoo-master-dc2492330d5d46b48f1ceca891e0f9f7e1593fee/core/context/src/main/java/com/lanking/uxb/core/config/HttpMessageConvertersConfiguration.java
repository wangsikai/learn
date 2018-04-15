package com.lanking.uxb.core.config;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.lanking.cloud.sdk.util.Charsets;

@Configuration
@ConditionalOnClass(HttpMessageConverter.class)
public class HttpMessageConvertersConfiguration {

	@Autowired(required = false)
	private final List<HttpMessageConverter<?>> converters = Collections.emptyList();

	@Bean(name = "messageConverters")
	public HttpMessageConverters messageConverters() {
		return new HttpMessageConverters(this.converters);
	}

	@Configuration
	protected static class ObjectMappers {
		@Bean("fastJsonHttpMessageConverter")
		@Qualifier("fastJsonHttpMessageConverter")
		public AbstractHttpMessageConverter<Object> fastJsonHttpMessageConverter(ObjectMapper objectMapper) {
			FastJsonHttpMessageConverter4 converter = new FastJsonHttpMessageConverter4();

			converter.setSupportedMediaTypes(Lists.newArrayList(MediaType.APPLICATION_JSON_UTF8));

			FastJsonConfig fastJsonConfig = new FastJsonConfig();

			fastJsonConfig.setCharset(Charset.forName(Charsets.UTF8));
			fastJsonConfig.setFeatures(Feature.DisableCircularReferenceDetect, Feature.DisableASM);
			fastJsonConfig.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);

			ParserConfig parserConfig = new ParserConfig();
			parserConfig.setAsmEnable(false);
			fastJsonConfig.setParserConfig(parserConfig);

			SerializeConfig serializeConfig = new SerializeConfig();
			serializeConfig.setAsmEnable(false);
			fastJsonConfig.setSerializeConfig(serializeConfig);

			converter.setFastJsonConfig(fastJsonConfig);

			return converter;
		}
	}
}
