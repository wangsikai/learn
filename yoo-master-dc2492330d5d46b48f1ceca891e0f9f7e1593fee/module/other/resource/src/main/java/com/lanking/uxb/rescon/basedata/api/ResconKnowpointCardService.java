package com.lanking.uxb.rescon.basedata.api;

import com.lanking.cloud.domain.common.resource.card.KnowpointCard;
import com.lanking.uxb.rescon.basedata.form.ResconPointCardForm;

/**
 * 知识卡片service
 * 
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2016年1月12日 上午10:21:45
 */
public interface ResconKnowpointCardService {

	/**
	 * 创建或者编辑知识点卡片
	 * 
	 * @param pointCardForm
	 *            知识点卡片form
	 * @return 知识点卡片
	 */
	KnowpointCard save(ResconPointCardForm pointCardForm);

	/**
	 * 根据知识点code获取 知识卡片
	 * 
	 * @param knowpointCode
	 * @return
	 */
	KnowpointCard getByCode(long knowpointCode);

}
