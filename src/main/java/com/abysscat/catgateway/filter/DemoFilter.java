package com.abysscat.catgateway.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * demo filter.
 *
 * @Author: abysscat-yj
 * @Create: 2024/6/2 20:53
 */
@Component("demoFilter")
public class DemoFilter implements GatewayFilter{

	@Override
	public Mono<Void> filter(ServerWebExchange exchange) {
		System.out.println(" ===>>> filters: demo filter ...");

		exchange.getRequest().getHeaders().toSingleValueMap()
				.forEach((k, v) -> System.out.println(k + ":" + v));

		return Mono.empty();
	}

}
