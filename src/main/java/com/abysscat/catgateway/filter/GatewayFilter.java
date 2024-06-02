package com.abysscat.catgateway.filter;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway filter interface.
 *
 * @Author: abysscat-yj
 * @Create: 2024/6/2 20:53
 */
public interface GatewayFilter {

	Mono<Void> filter(ServerWebExchange exchange);

}
