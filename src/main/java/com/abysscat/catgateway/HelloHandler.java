package com.abysscat.catgateway;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * hello handler.
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/31 1:05
 */
public class HelloHandler {

	static Mono<ServerResponse> handle(ServerRequest request) {
		return ServerResponse.ok().bodyValue("hello catgateway!");
	}

}
