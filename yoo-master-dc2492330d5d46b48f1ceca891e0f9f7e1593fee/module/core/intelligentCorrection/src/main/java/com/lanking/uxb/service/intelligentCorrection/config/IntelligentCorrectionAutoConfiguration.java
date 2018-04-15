package com.lanking.uxb.service.intelligentCorrection.config;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lanking.uxb.service.intelligentCorrection.api.AnswerArchiveService;
import com.lanking.uxb.service.intelligentCorrection.api.impl.AutoCorrectIntelligentCorrectionHandle;
import com.lanking.uxb.service.intelligentCorrection.api.impl.BeforeCorrectFilterHandle;
import com.lanking.uxb.service.intelligentCorrection.api.impl.HistoryDataIntelligentCorrectionHandle;
import com.lanking.uxb.service.intelligentCorrection.api.impl.IntelligentCorrectionHandleChain;
import com.lanking.uxb.service.intelligentCorrection.api.impl.TextComparisonIntelligentCorrectionHandle;

@Configuration
@EnableConfigurationProperties(IntelligentCorrectionProperties.class)
public class IntelligentCorrectionAutoConfiguration {

	@Autowired
	private IntelligentCorrectionProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public BeforeCorrectFilterHandle beforeCorrectFilterHandle() throws Exception {
		BeforeCorrectFilterHandle handle = new BeforeCorrectFilterHandle();
		return handle;
	}

	@Bean
	@ConditionalOnMissingBean
	public TextComparisonIntelligentCorrectionHandle textComparisonIntelligentCorrectionHandle() throws Exception {
		TextComparisonIntelligentCorrectionHandle handle = new TextComparisonIntelligentCorrectionHandle();
		return handle;
	}

	@Bean
	@ConditionalOnMissingBean
	public HistoryDataIntelligentCorrectionHandle historyDataIntelligentCorrectionHandle(
			AnswerArchiveService answerArchiveService) throws Exception {
		HistoryDataIntelligentCorrectionHandle handle = new HistoryDataIntelligentCorrectionHandle();
		handle.setAnswerArchiveService(answerArchiveService);
		return handle;
	}

	@Bean
	@ConditionalOnMissingBean
	public AutoCorrectIntelligentCorrectionHandle autoCorrectIntelligentCorrectionHandle(HttpClient httpClient)
			throws Exception {
		AutoCorrectIntelligentCorrectionHandle handle = new AutoCorrectIntelligentCorrectionHandle();
		handle.setAutoCorrectRestAPI(properties.getRestAPIUSA());
		handle.setHttpClient(httpClient);
		return handle;
	}

	@Bean
	@ConditionalOnMissingBean
	public IntelligentCorrectionHandleChain chain(BeforeCorrectFilterHandle beforeCorrectFilterHandle,
			TextComparisonIntelligentCorrectionHandle textComparisonIntelligentCorrectionHandle,
			HistoryDataIntelligentCorrectionHandle historyDataIntelligentCorrectionHandle,
			AutoCorrectIntelligentCorrectionHandle autoCorrectIntelligentCorrectionHandle) throws Exception {
		IntelligentCorrectionHandleChain chain = new IntelligentCorrectionHandleChain();
		chain.addHandle(beforeCorrectFilterHandle).addHandle(textComparisonIntelligentCorrectionHandle)
				.addHandle(historyDataIntelligentCorrectionHandle).addHandle(autoCorrectIntelligentCorrectionHandle);
		return chain;
	}

}
