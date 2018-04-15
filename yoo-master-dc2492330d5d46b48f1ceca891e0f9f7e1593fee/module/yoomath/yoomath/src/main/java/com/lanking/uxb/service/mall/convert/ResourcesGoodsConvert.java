package com.lanking.uxb.service.mall.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.examPaper.api.ExamPaperService;
import com.lanking.uxb.service.examPaper.value.VExamPaper;
import com.lanking.uxb.service.mall.api.GoodsService;
import com.lanking.uxb.service.mall.value.VExamPaperGoods;

/**
 * 试卷商品Convert
 *
 * @author zemin.song
 */
@Component
public class ResourcesGoodsConvert extends Converter<VExamPaperGoods, ResourcesGoods, Long> {

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private ExamPaperService examPaperService;

	@Override
	protected Long getId(ResourcesGoods s) {
		return s.getId();
	}

	@Override
	protected VExamPaperGoods convert(ResourcesGoods s) {
		VExamPaperGoods vo = new VExamPaperGoods();
		vo.setId(s.getId());
		return vo;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VExamPaperGoods, ResourcesGoods, Long, Goods>() {

			@Override
			public boolean accept(ResourcesGoods s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ResourcesGoods s, VExamPaperGoods d) {
				return s.getId();
			}

			@Override
			public void setValue(ResourcesGoods s, VExamPaperGoods d, Goods value) {
				d.setPrice(value.getPrice());
				d.setPriceRMB(value.getPriceRMB());
			}

			@Override
			public Goods getValue(Long key) {
				return goodsService.get(key);
			}

			@Override
			public Map<Long, Goods> mgetValue(Collection<Long> keys) {
				return goodsService.mget(keys);
			}

		});

		assemblers.add(new ConverterAssembler<VExamPaperGoods, ResourcesGoods, Long, ExamPaper>() {

			@Override
			public boolean accept(ResourcesGoods s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ResourcesGoods s, VExamPaperGoods d) {
				return s.getResourcesId();
			}

			@Override
			public void setValue(ResourcesGoods s, VExamPaperGoods d, ExamPaper value) {
				if (value == null) {
					return;
				}
				VExamPaper vp = new VExamPaper();
				vp.setDifficulty(value.getDifficulty());
				vp.setId(value.getId());
				vp.setName(value.getName());
				vp.setScore(value.getScore());
				vp.setYear(value.getYear());
				vp.setCreateAt(value.getCreateAt());
				vp.setPhaseCode(value.getPhaseCode());
				vp.setCategoryCode(value.getResourceCategoryCode());
				d.setPaper(vp);
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
