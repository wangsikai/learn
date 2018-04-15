package com.lanking.cloud.component.mq.producer;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class DelayMQ implements Serializable {

	private static final long serialVersionUID = -5002410554545882854L;

	private Object data;
	private String ex;
	private String rk;

}
