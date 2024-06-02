package com.abysscat.catgateway.web.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * gateway web filter.
 *
 * @Author: abysscat-yj
 * @Create: 2024/6/2 0:53
 */
@Component
public class GatewayWebFilter implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		System.out.println("===>>> cat Gateway web filter ...");

		// 测试前置mock返回结果
		if(exchange.getRequest().getQueryParams().getFirst("mock")==null) {
			return chain.filter(exchange);
		}

		String mock = """
                {"result": "mock"}
                """;
		return exchange.getResponse()
				.writeWith(Mono.just(exchange.getResponse()
						.bufferFactory().wrap(mock.getBytes())));
	}
}
