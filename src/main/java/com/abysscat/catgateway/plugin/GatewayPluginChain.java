package com.abysscat.catgateway.plugin;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway plugin chain interface.
 *
 * @Author: abysscat-yj
 * @Create: 2024/6/2 19:35
 */
public interface GatewayPluginChain {

	Mono<Void> handle(ServerWebExchange exchange);

}
