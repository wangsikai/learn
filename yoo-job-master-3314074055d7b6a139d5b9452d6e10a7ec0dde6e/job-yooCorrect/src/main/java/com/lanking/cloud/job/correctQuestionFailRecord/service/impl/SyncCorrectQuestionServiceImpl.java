package com.lanking.cloud.job.correctQuestionFailRecord.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.domain.yoomath.correct.CorrectQuestionFailRecord;
import com.lanking.cloud.job.correctQuestionDistribute.service.CorrectQuestionService;
import com.lanking.cloud.job.correctQuestionFailRecord.service.CorrectConfigService;
import com.lanking.cloud.job.correctQuestionFailRecord.service.CorrectQuestionFailRecordService;
import com.lanking.cloud.job.correctQuestionFailRecord.service.SyncCorrectQuestionService;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.microservice.domain.yoocorrect.CorrectConfig;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestionCategory;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestionCategoryWeightConfig;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestionSource;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestionSourceWeightConfig;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestionType;

/**
 * @author pengcheng.yu
 * @date 2018年3月19日
 * @since 小优秀快批
 */
@Service
public class SyncCorrectQuestionServiceImpl implements SyncCorrectQuestionService {

	@Autowired
	CorrectQuestionFailRecordService correctQuestionFailRecordService;
	
	@Autowired
	CorrectQuestionService correctQuestionService;
	
	@Autowired
	CorrectConfigService correctConfigService;
	@Override
	@Transactional("yooCorrectTransactionManager")
	public void sync() {
		List<CorrectQuestionFailRecord> failRecordList = correctQuestionFailRecordService.queryCorrectQuestionFailRecords();
		if(null != failRecordList && failRecordList.size()>0){
			List<Long> failRecordIdList = new ArrayList<Long>();
			//查询习题权重规则配置
			CorrectConfig config = correctConfigService.getCorrectConfigs();
			//习题类型权重
			List<CorrectQuestionCategoryWeightConfig> categoryWeights = config.getCategoryWeightConfigs();
			//习题来源权重
			List<CorrectQuestionSourceWeightConfig> sourceWeights = config.getSourceWeightConfigs();
			//习题权重值map，key:习题来源或习题分类，value:习题来源或习题分类对应的权重值
			Map<Object,Double> weightMap = new HashMap<Object,Double>();
			for(CorrectQuestionCategoryWeightConfig categoryWeight : categoryWeights){
				weightMap.put(categoryWeight.getCategory(), categoryWeight.getWeight());
			}
			for(CorrectQuestionSourceWeightConfig sourceWeight : sourceWeights){
				weightMap.put(sourceWeight.getSource(), sourceWeight.getWeight());
			}
			
			List<CorrectQuestion> correctQuestionList = new ArrayList<CorrectQuestion>();
			
			for(CorrectQuestionFailRecord failRecord : failRecordList){
				failRecordIdList.add(failRecord.getId());
				CorrectQuestion correctQuestion = new CorrectQuestion();
				correctQuestion.setQuestionId(failRecord.getQuestionId());
				correctQuestion.setStudentId(failRecord.getStudentId());
				
				CorrectQuestionType type = CorrectQuestionType.NULL;
				switch (failRecord.getType().getValue()){
				case 1:
					type = CorrectQuestionType.SINGLE_CHOICE;
					break;
				case 2:
					type = CorrectQuestionType.MULTIPLE_CHOICE;
					break;
				case 3:
					type = CorrectQuestionType.FILL_BLANK;
					break;
				case 4:
					type = CorrectQuestionType.TRUE_OR_FALSE;
					break;
				case 5:
					type = CorrectQuestionType.QUESTION_ANSWERING;
					break;
				case 6:
					type = CorrectQuestionType.COMPOSITE;
					break;
				default:
					type = CorrectQuestionType.NULL;
				}
				correctQuestion.setType(type);
				
				CorrectQuestionSource source = CorrectQuestionSource.HOMEWORK;
				switch(failRecord.getSource()){
				case 0:
					source = CorrectQuestionSource.HOMEWORK;
					break;
				case 1:
					source = CorrectQuestionSource.AMEND;
					break;
				case 2:
					source = CorrectQuestionSource.PRACTICE;
					break;
				default:
					source = CorrectQuestionSource.HOMEWORK;
				}
				correctQuestion.setSource(source);
				
				CorrectQuestionCategory category = null;
				switch(failRecord.getCategory()){
				case 0:
					category = CorrectQuestionCategory.BLANK_QUESTION_UNKNOW;
					break;
				case 1:
					category = CorrectQuestionCategory.ANSWER_QUESTION;
					break;
				case 2:
					category = CorrectQuestionCategory.BLANK_QUESTION_APPEAL;
					break;
				case 3:
					category = CorrectQuestionCategory.ANSWER_QUESTION_APPEAL;
					break;
						
				}
				correctQuestion.setCategory(category);
				
				correctQuestion.setBizId(failRecord.getBizId());
				correctQuestion.setWeight(getQuestionWeight(weightMap,source,category));
				correctQuestion.setCreateAt(new Date());
				correctQuestionList.add(correctQuestion);
			}
			
			correctQuestionService.batchSave(correctQuestionList);
			try{
				correctQuestionFailRecordService.batchSave(failRecordIdList);
			}catch(Exception e){
				 throw new RuntimeException();
			 }
		}
	}

	private Double getQuestionWeight(Map<Object,Double> weightMap,CorrectQuestionSource source,CorrectQuestionCategory category){
		Double sourceWeight = weightMap.get(source);
		Double categoryWeight = weightMap.get(category);
		if(null == sourceWeight || null == categoryWeight){
			return null;
		}
		BigDecimal sourceWeight1 = new BigDecimal(Double.toString(sourceWeight));
		BigDecimal categoryWeight1 = new BigDecimal(Double.toString(categoryWeight));
		return sourceWeight1.multiply(categoryWeight1).doubleValue();
		
	}
	public static void main(String[] args) {
		Map<Object,Double> weightMap = new HashMap<Object,Double>();
		weightMap.put(CorrectQuestionSource.AMEND, 0.9);
		weightMap.put(CorrectQuestionSource.HOMEWORK, 1d);
		weightMap.put(CorrectQuestionSource.PRACTICE, 0.8);
		weightMap.put(CorrectQuestionCategory.BLANK_QUESTION_UNKNOW, 1d);
		weightMap.put(CorrectQuestionCategory.ANSWER_QUESTION, 0.9);
		weightMap.put(CorrectQuestionCategory.BLANK_QUESTION_APPEAL, 0.8);
		weightMap.put(CorrectQuestionCategory.BLANK_QUESTION_APPEAL, 0.7);
//		System.out.println(getQuestionWeight(weightMap, CorrectQuestionSource.HOMEWORK, CorrectQuestionCategory.BLANK_QUESTION_APPEAL));
	}
}
