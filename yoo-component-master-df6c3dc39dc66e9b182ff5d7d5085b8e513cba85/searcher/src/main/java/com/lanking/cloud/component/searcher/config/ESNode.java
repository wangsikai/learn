package com.lanking.cloud.component.searcher.config;

import java.net.InetAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class ESNode {

	private InetAddress address;
	private String host;
	private int port;
}
