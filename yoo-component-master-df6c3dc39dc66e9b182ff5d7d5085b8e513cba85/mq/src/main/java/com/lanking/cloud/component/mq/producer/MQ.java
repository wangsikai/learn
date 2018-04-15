package com.lanking.cloud.component.mq.producer;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <pre>
 * 所有MQ都发送次对象 <br>
 * 1.交换机可以定义为延迟队列,MqSender接口提供相应的延迟接口
 * 2.也可以单独在MQ对象里面设置消息延迟时间
 * </pre>
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年10月13日
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MQ implements Serializable {

	private static final long serialVersionUID = -5002410554545882854L;

	/**
	 * 针对某个消息的延迟
	 */
	private long delay = 0;

	private Object data;

	public static MQBuilder builder() {
		return new MQBuilder();
	}

	public static class MQBuilder {
		MQ mq = new MQ();

		public MQBuilder builder() {
			return new MQBuilder();
		}

		public MQ build() {
			return mq;
		}

		public MQBuilder delay(long millisecond) {
			mq.setDelay(millisecond);
			return this;
		}

		public MQBuilder data(Object data) {
			mq.setData(data);
			return this;
		}

	}
}
