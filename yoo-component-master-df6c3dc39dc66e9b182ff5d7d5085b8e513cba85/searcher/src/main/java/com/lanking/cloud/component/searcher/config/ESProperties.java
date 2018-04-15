package com.lanking.cloud.component.searcher.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class ESProperties {

	/**
	 * 节点信息
	 * 
	 * <pre>
	 * 	配置格式:host1:9200,host2:9200,端口需要注意client的方式和实际配置的端口
	 * </pre>
	 */
	private String nodes;
	/**
	 * 集群名称
	 */
	private String cluster;
	/**
	 * 默认index
	 * 
	 * <pre>
	 * 相关api若没有设置index则使用此配置的index,也是为了兼容已经有的接口代码
	 * </pre>
	 */
	private String index;

	public List<ESNode> getESNode() throws NumberFormatException, UnknownHostException {
		List<ESNode> esHosts = Lists.newArrayList();
		for (String node : nodes.split(",")) {
			String[] nodeArr = node.split(":");
			esHosts.add(new ESNode(InetAddress.getByName(nodeArr[0]), nodeArr[0], Integer.parseInt(nodeArr[1])));
		}
		return esHosts;
	}

}
