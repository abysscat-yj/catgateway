package com.abysscat.catgateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * gateway router.
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/31 0:55
 */
@Component
public class GatewayRouter {

	@Autowired
	GatewayHandler gatewayHandler;

	@Bean
	public RouterFunction<?> helloRouterFunction() {
		return route(GET("/hello"),
//				request -> ok().body(Mono.just("hello catgateway!"), String.class));
				HelloHandler::handle);
	}

	@Bean
	public RouterFunction<?> gatewayRouterFunction() {
		return route(GET("/gw").or(POST("/gw/**")), gatewayHandler::handle);
	}

}
