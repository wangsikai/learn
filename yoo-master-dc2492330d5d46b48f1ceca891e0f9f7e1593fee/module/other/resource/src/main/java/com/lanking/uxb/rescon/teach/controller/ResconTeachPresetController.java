package com.lanking.uxb.rescon.teach.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContent;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentFallibleDifficult;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentFallibleDifficultExample;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentPreview;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.UnSupportedOperationException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSystemService;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgePointConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgeSystemConvert;
import com.lanking.uxb.rescon.basedata.value.VKnowledgeSystem;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistPresetContentFallibleDifficultExampleService;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistPresetContentFallibleDifficultService;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistPresetContentPreviewService;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistPresetContentService;
import com.lanking.uxb.rescon.teach.convert.ResconTeachAssistPresetContentConvert;
import com.lanking.uxb.rescon.teach.convert.ResconTeachAssistPresetContentFallibleDifficultConvert;
import com.lanking.uxb.rescon.teach.convert.ResconTeachAssistPresetContentFallibleDifficultExampleConvert;
import com.lanking.uxb.rescon.teach.convert.ResconTeachAssistPresetContentPreviewConvert;
import com.lanking.uxb.rescon.teach.form.TeachAssistPresetContentFallibleDifficultForm;
import com.lanking.uxb.rescon.teach.form.TeachAssistPresetContentForm;
import com.lanking.uxb.rescon.teach.form.TeachAssistPresetContentPreviewForm;
import com.lanking.uxb.rescon.teach.value.VTeachAssistPresetContentFallibleDifficult;
import com.lanking.uxb.rescon.teach.value.VTeachAssistPresetContentFallibleDifficultExample;
import com.lanking.uxb.rescon.teach.value.VTeachAssistPresetContentPreview;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 预置内容库
 * 
 * @since V1.3
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping(value = "/rescon/teach/preset")
public class ResconTeachPresetController {

	@Autowired
	private ResconTeachAssistPresetContentService contentService;
	@Autowired
	private ResconTeachAssistPresetContentPreviewService previewService;
	@Autowired
	private ResconTeachAssistPresetContentFallibleDifficultService fallibleService;
	@Autowired
	private ResconTeachAssistPresetContentConvert contentConvert;
	@Autowired
	private ResconTeachAssistPresetContentPreviewConvert previewConvert;
	@Autowired
	private ResconTeachAssistPresetContentFallibleDifficultConvert fallibleConvert;
	@Autowired
	private ResconQuestionConvert questionConvert;
	@Autowired
	private ResconTeachAssistPresetContentFallibleDifficultExampleService exampleService;
	@Autowired
	private ResconTeachAssistPresetContentFallibleDifficultExampleConvert exampleConvert;
	@Autowired
	private ResconKnowledgePointService resconNewComKsService;
	@Autowired
	private ResconKnowledgeSystemConvert ksConvert;
	@Autowired
	private ResconKnowledgePointConvert kpConvert;
	@Autowired
	private ResconKnowledgeSystemService knowledgeSystemService;
	@Autowired
	private ResconVendorUserManage vendorUserManage;

	/**
	 * 通过知识专项查询对应的预置内容
	 * 
	 * @param code
	 *            知识专项code
	 * @return
	 */
	@RequestMapping(value = "getPresetContent", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getPresetContent(Long code) {
		Map<String, Object> data = new HashMap<String, Object>();
		TeachAssistPresetContent t = contentService.getByKsCode(code);
		if (t != null) {
			data.put("content", contentConvert.to(t));
			List<TeachAssistPresetContentPreview> previewList = previewService.getByPresetId(t.getId());
			data.put("preview", previewConvert.to(previewList));
			List<TeachAssistPresetContentFallibleDifficult> fallList = fallibleService.getByPresetId(t.getId());
			data.put("fallible", fallibleConvert.to(fallList));
		}
		return new Value(data);
	}

	/**
	 * 根据条件查询对应的知识体系列表
	 * 
	 * @param phaseCode
	 * @param subjectCode
	 * @param pcode
	 *            父级code
	 * @param level
	 *            等级(公共的三级在knowledge_system里)
	 * @return
	 */
	@RequestMapping(value = "findAll", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findAll(Integer phaseCode, Integer subjectCode,
			@RequestParam(value = "pcode", defaultValue = "0") Long pcode,
			@RequestParam(value = "level", defaultValue = "0") Integer level,
			@RequestParam(value = "common", defaultValue = "0") String val) {
		// 查询知识点
		if (level == 3) {
			List<KnowledgePoint> list = resconNewComKsService.findPoint(phaseCode, subjectCode, pcode, null);
			return new Value(kpConvert.to(list));
		} else {
			List<KnowledgeSystem> list = knowledgeSystemService.findAll(phaseCode, subjectCode, pcode, level);
			List<VKnowledgeSystem> vlist = ksConvert.to(list);
			if (level == 2) {
				for (VKnowledgeSystem v : vlist) {
					TeachAssistPresetContent t = contentService.getByKsCode(v.getId());
					if (t != null) {
						Long count1 = previewService.nopassCount(t.getId());
						Long count2 = fallibleService.nopassCount(t.getId());
						if (count1 > 0 || count2 > 0) {
							v.setHasNoPass(true);
						}
					}
				}
			}
			return new Value(vlist);
		}
	}

	/**
	 * 编辑查询单个预习点详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "queryPreview", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryPreview(Long id) {
		TeachAssistPresetContentPreview t = previewService.get(id);
		VTeachAssistPresetContentPreview v = previewConvert.to(t);
		v.setVpreviewQuestions(questionConvert.mgetList(v.getPreviewQuestions()));
		v.setVselfTestQuestions(questionConvert.mgetList(v.getSelfTestQuestions()));
		return new Value(v);
	}

	/**
	 * 编辑查询单个易错难点详情
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "queryFallible", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryFallible(Long id) {
		TeachAssistPresetContentFallibleDifficult t = fallibleService.get(id);
		VTeachAssistPresetContentFallibleDifficult v = fallibleConvert.to(t);
		v.setvTargetedTrainingQuestions(questionConvert.mgetList(v.getTargetedTrainingQuestions()));
		List<TeachAssistPresetContentFallibleDifficultExample> list = exampleService.findListByFallId(id);
		if (CollectionUtils.isNotEmpty(list)) {
			TeachAssistPresetContentFallibleDifficultExample example = list.get(0);
			VTeachAssistPresetContentFallibleDifficultExample vExample = exampleConvert.to(example);
			vExample.setQuestion(questionConvert.get(vExample.getQuestionId()));
			v.setExample(vExample);
		}
		return new Value(v);
	}

	/**
	 * 保存预置内容(学习目标/解题方法及技巧小结)
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "savePresetContent", method = { RequestMethod.POST, RequestMethod.GET })
	public Value savePresetContent(TeachAssistPresetContentForm form) {
		form.setUserId(Security.getUserId());
		contentService.save(form);
		return new Value();
	}

	/**
	 * 保存预习点
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "savePreview", method = { RequestMethod.POST, RequestMethod.GET })
	public Value savePreview(TeachAssistPresetContentPreviewForm form) {
		form.setUserId(Security.getUserId());
		previewService.save(form);
		return new Value();
	}

	/**
	 * 提交预习点，状态变成未校验
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "submitPreview", method = { RequestMethod.POST, RequestMethod.GET })
	public Value submitPreview(Long id) {
		previewService.check(id, CardStatus.EDITING);
		return new Value();
	}

	/**
	 * 提交易错点，状态变成未校验
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "submitFallible", method = { RequestMethod.POST, RequestMethod.GET })
	public Value submitFallible(Long id) {
		fallibleService.check(id, CardStatus.EDITING);
		return new Value();
	}

	/**
	 * 保存易错难点
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "saveFallible", method = { RequestMethod.POST, RequestMethod.GET })
	public Value saveFallible(TeachAssistPresetContentFallibleDifficultForm form) {
		form.setUserId(Security.getUserId());
		fallibleService.save(form);
		return new Value();
	}

	/**
	 * 校验预习点
	 * 
	 * @param status
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "checkPreview", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkPreview(CardStatus status, Long id) {
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		if (user.getType() == UserType.VENDOR_BUILD) {
			return new Value(new UnSupportedOperationException());
		}
		previewService.check(id, status);
		return new Value();
	}

	/**
	 * 删除预习点
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delPreview", method = { RequestMethod.POST, RequestMethod.GET })
	public Value delPreview(Long id) {
		previewService.delete(id);
		return new Value();
	}

	/**
	 * 校验易错疑难
	 * 
	 * @param status
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "checkFallible", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkFallible(CardStatus status, Long id) {
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		if (user.getType() == UserType.VENDOR_BUILD) {
			return new Value(new UnSupportedOperationException());
		}
		fallibleService.check(id, status);
		return new Value();
	}

	/**
	 * 删除易错疑难
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delFallible", method = { RequestMethod.POST, RequestMethod.GET })
	public Value delFallible(Long id) {
		fallibleService.delete(id);
		return new Value();
	}
}
