package com.abysscat.catgateway.plugin;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * default gateway plugin chain.
 *
 * @Author: abysscat-yj
 * @Create: 2024/6/2 19:37
 */
public class DefaultGatewayPluginChain implements GatewayPluginChain {

	List<GatewayPlugin> plugins;

	int index = 0;

	public DefaultGatewayPluginChain(List<GatewayPlugin> plugins) {
		this.plugins = plugins;
	}

	@Override
	public Mono<Void> handle(ServerWebExchange exchange) {
		return Mono.defer(() -> {
			if (index >= plugins.size()) {
				return Mono.empty();
			}
			return plugins.get(index++).handle(exchange, this);
		});
	}
}
