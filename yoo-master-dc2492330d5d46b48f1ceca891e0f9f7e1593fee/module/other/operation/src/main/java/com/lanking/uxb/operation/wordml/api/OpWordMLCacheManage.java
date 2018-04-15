package com.lanking.uxb.operation.wordml.api;

/**
 * WordML缓存服务接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月5日
 */
public interface OpWordMLCacheManage {

	/**
	 * 重建习题缓存.
	 * 
	 * @param type
	 *            重建方式
	 * @param host
	 *            主机host
	 * @param minId
	 *            最小ID，全部重建使用
	 */
	void rebuildQuestionWordML(int type, String host, Long minId);
}
