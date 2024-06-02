package com.abysscat.catgateway.plugin;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * direct plugin.
 *
 * @Author: abysscat-yj
 * @Create: 2024/6/2 19:51
 */
@Component("direct")
public class DirectPlugin extends AbstractGatewayPlugin {

	public static final String NAME = "direct";
	private final String PREFIX = GATEWAY_PREFIX + "/" + NAME + "/";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain) {
		System.out.println("===>>>> DirectPlugin doHandle ...");

		String backend = exchange.getRequest().getQueryParams().getFirst("backend");
		Flux<DataBuffer> requestBody = exchange.getRequest().getBody();

		exchange.getResponse().getHeaders().add("Content-Type", "application/json");
		exchange.getResponse().getHeaders().add("cat.gw.version", "v1.0.0");

		if (backend == null || backend.isEmpty()) {
			return requestBody.flatMap(x -> exchange.getResponse().writeWith(Mono.just(x)))
					.then(chain.handle(exchange));
		}

		WebClient client = WebClient.create(backend);
		Mono<ResponseEntity<String>> entity = client.post()
				.header("Content-Type", "application/json")
				.body(requestBody, DataBuffer.class).retrieve().toEntity(String.class);
		Mono<String> body = entity.map(ResponseEntity::getBody);

		return body.flatMap(x -> exchange.getResponse()
						.writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))))
				.then(chain.handle(exchange));
	}

	@Override
	public boolean doSupport(ServerWebExchange exchange) {
		return exchange.getRequest().getPath().value().startsWith(PREFIX);
	}
}
