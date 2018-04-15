package com.lanking.cloud.component.mq.common;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
public class MetadataAssembler implements BeanPostProcessor, ApplicationContextAware {

	private Map<String, Exchange> exchanges = Maps.newHashMap();
	private Map<String, Queue> queues = Maps.newHashMap();
	private Map<String, Binding> bindings = Maps.newHashMap();
	private List<Listener> listeners = Lists.newArrayList();
	private Environment env;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		for (Class<?> targetClass = AopUtils.getTargetClass(bean); targetClass != null; targetClass = targetClass
				.getSuperclass()) {
			com.lanking.cloud.component.mq.common.annotation.Exchange annotationExchang = AnnotationUtils
					.getAnnotation(targetClass, com.lanking.cloud.component.mq.common.annotation.Exchange.class);
			if (annotationExchang != null) {
				// exchange
				if (!exchanges.containsKey(annotationExchang.name())) {
					Exchange exchange = null;
					if (ExchangeTypes.DIRECT.equals(annotationExchang.type())) {
						exchange = new DirectExchange(annotationExchang.name(), annotationExchang.durable(),
								annotationExchang.autoDelete());
						((DirectExchange) exchange).setDelayed(annotationExchang.delayed());
						((DirectExchange) exchange).setInternal(annotationExchang.internal());
					} else if (ExchangeTypes.FANOUT.equals(annotationExchang.type())) {
						exchange = new FanoutExchange(annotationExchang.name(), annotationExchang.durable(),
								annotationExchang.autoDelete());
						((FanoutExchange) exchange).setDelayed(annotationExchang.delayed());
						((FanoutExchange) exchange).setInternal(annotationExchang.internal());
					} else if (ExchangeTypes.TOPIC.equals(annotationExchang.type())
							|| ExchangeTypes.HEADERS.equals(annotationExchang.type())) {
						// TODO 后续支持
					}
					if (exchange != null) {
						exchanges.put(exchange.getName(), exchange);
					}
				}
				// queue
				for (Method method : targetClass.getDeclaredMethods()) {
					com.lanking.cloud.component.mq.common.annotation.Listener annotationListener = AnnotationUtils
							.getAnnotation(method, com.lanking.cloud.component.mq.common.annotation.Listener.class);
					if (annotationListener != null) {
						String queueName = annotationListener.queue();
						if (queueName.startsWith("${") && queueName.endsWith("}")) {
							queueName = env.getProperty(
									queueName.substring(0, queueName.length() - 1).replaceFirst("\\$\\{", ""));
						}
						Queue queue = new Queue(queueName, annotationListener.queueDurable(),
								annotationListener.queueExclusive(), annotationListener.queueAutoDelete());
						queues.put(queueName, queue);

						Binding binding = new Binding(queue.getName(), DestinationType.QUEUE, annotationExchang.name(),
								annotationListener.routingKey(), null);
						bindings.put(queueName, binding);

						Listener listener = new Listener();
						listener.setQueue(queue.getName());
						listener.setSeries(annotationListener.series());
						listener.setPrefetchCount(annotationListener.prefetchCount());

						listener.setAutoAck(annotationListener.autoAck());
						listener.setRequeue(annotationListener.requeue());
						if (annotationListener.series()) {
							listener.setExclusive(annotationListener.exclusive());
						} else {
							listener.setExclusive(false);
						}
						listener.setConsume(annotationListener.consume());

						listener.setHandler(bean);
						listener.setMethod(method);
						listeners.add(listener);
					}
				}

			}
		}
		return bean;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		env = applicationContext.getEnvironment();
	}
}
