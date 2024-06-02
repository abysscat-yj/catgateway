package com.abysscat.catgateway.plugin;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway plugin interface.
 *
 * @Author: abysscat-yj
 * @Create: 2024/6/2 19:31
 */
public interface GatewayPlugin {

	String GATEWAY_PREFIX = "/gw";

	void start();

	void stop();

	String getName();

	boolean support(ServerWebExchange exchange);

	Mono<Void> handle(ServerWebExchange exchange, GatewayPluginChain chain);

}
