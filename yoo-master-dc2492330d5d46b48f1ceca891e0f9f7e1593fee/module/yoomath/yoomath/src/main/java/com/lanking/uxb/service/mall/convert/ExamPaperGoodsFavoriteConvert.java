package com.lanking.uxb.service.mall.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsFavorite;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.examPaper.api.ExamPaperQuestionService;
import com.lanking.uxb.service.examPaper.api.ExamPaperService;
import com.lanking.uxb.service.mall.api.GoodsService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsService;
import com.lanking.uxb.service.mall.value.VExamPaperGoodsFavorite;

/**
 * 试卷商品Convert
 *
 * @author zemin.song
 */
@Component
public class ExamPaperGoodsFavoriteConvert extends Converter<VExamPaperGoodsFavorite, ResourcesGoodsFavorite, Long> {

	@Autowired
	private ExamPaperService examPaperService;
	@Autowired
	private ResourcesGoodsService resourcesGoodsService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private ExamPaperQuestionService examPaperQuestionService;
	@Autowired
	private ExamPaperGoodsConvert examPaperGoodsConvert;

	@Override
	protected Long getId(ResourcesGoodsFavorite s) {
		return s.getId();
	}

	@Override
	protected VExamPaperGoodsFavorite convert(ResourcesGoodsFavorite s) {
		VExamPaperGoodsFavorite vo = new VExamPaperGoodsFavorite();
		vo.setId(s.getId());
		vo.setFavoriteAt(s.getCreateAt());
		return vo;
	}

	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VExamPaperGoodsFavorite, ResourcesGoodsFavorite, Long, ExamPaper>() {
			@Override
			public boolean accept(ResourcesGoodsFavorite s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ResourcesGoodsFavorite s, VExamPaperGoodsFavorite d) {
				return s.getResourcesId();
			}

			@Override
			public void setValue(ResourcesGoodsFavorite s, VExamPaperGoodsFavorite d, ExamPaper value) {
				if (value != null) {
					ExamPaperGoodsConvertOption ep = new ExamPaperGoodsConvertOption();
					ep.setInitGoodsInfo(true);
					d.setGoods(examPaperGoodsConvert.to(value, ep));
				}
			}

			@Override
			public ExamPaper getValue(Long key) {
				return examPaperService.get(key);
			}

			@Override
			public Map<Long, ExamPaper> mgetValue(Collection<Long> keys) {
				return examPaperService.mget(keys);
			}

		});

	}
}
