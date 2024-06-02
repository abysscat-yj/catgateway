package com.abysscat.catgateway.web.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * gateway post web filter.
 *
 * @Author: abysscat-yj
 * @Create: 2024/6/2 0:54
 */
@Component
public class GatewayPostWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return chain.filter(exchange)
				// 后置处理
				.doFinally(
						s -> {
							System.out.println("===>>> post filter");
							exchange.getAttributes().forEach((k, v) -> System.out.println(k + ":" + v));
						}
				);
	}
}
