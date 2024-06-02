package com.abysscat.catgateway.web.handler;

import com.abysscat.catgateway.plugin.DefaultGatewayPluginChain;
import com.abysscat.catgateway.plugin.GatewayPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * gateway web handler.
 *
 * @Author: abysscat-yj
 * @Create: 2024/6/2 0:47
 */
@Component("gatewayWebHandler")
public class GatewayWebHandler implements WebHandler {


	@Autowired
	List<GatewayPlugin> plugins;

	@Override
	public Mono<Void> handle(ServerWebExchange exchange) {
		System.out.println(" ====> cat gateway web handler ... ");

		if (plugins == null || plugins.isEmpty()) {
			String mock = """
					{"result":"no plugin"}
					""";
			return exchange.getResponse()
					.writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(mock.getBytes())));
		}

		// 执行插件链
		return new DefaultGatewayPluginChain(plugins).handle(exchange);

	}
}
