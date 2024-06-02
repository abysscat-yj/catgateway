package com.abysscat.catgateway.plugin;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * abstract gateway plugin.
 *
 * @Author: abysscat-yj
 * @Create: 2024/6/2 19:33
 */
public abstract class AbstractGatewayPlugin implements GatewayPlugin {

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@Override
	public boolean support(ServerWebExchange exchange) {
		return doSupport(exchange);
	}

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, GatewayPluginChain chain) {
		boolean supported = support(exchange);
		System.out.println(" =====>>>> plugin[" + this.getName() + "], support=" + supported);
		exchange.getResponse().getHeaders().add("cat.gw.plugin", getName());
		return supported ? doHandle(exchange, chain) : chain.handle(exchange);
	}

	public abstract Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain);

	public abstract boolean doSupport(ServerWebExchange exchange);
}
