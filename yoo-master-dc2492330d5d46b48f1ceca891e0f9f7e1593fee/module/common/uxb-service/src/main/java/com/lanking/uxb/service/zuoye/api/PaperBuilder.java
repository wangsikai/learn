package com.lanking.uxb.service.zuoye.api;

/**
 * 试卷生成器
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月16日
 */
public interface PaperBuilder {

	Paper generate(PaperParam param);

}
