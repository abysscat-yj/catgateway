package com.abysscat.catgateway;

import com.abysscat.catrpc.core.api.LoadBalancer;
import com.abysscat.catrpc.core.cluster.RoundRobinBalancer;
import com.abysscat.catrpc.core.meta.InstanceMeta;
import com.abysscat.catrpc.core.meta.ServiceMeta;
import com.abysscat.catrpc.core.registry.RegistryCenter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * gateway handler.
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/31 1:05
 */
@Component
public class GatewayHandler {

	@Autowired
	RegistryCenter rc;

	LoadBalancer<InstanceMeta> loadBalancer = new RoundRobinBalancer<>();

	Mono<ServerResponse> handle(ServerRequest request) {
		// 通过请求路径或者服务名
		String service = request.path().substring(4);
		ServiceMeta serviceMeta = ServiceMeta.builder().name(service)
				.app("app1").env("dev").namespace("public").version("v1").build();

		// 通过rc拿到所有活着的服务实例
		List<InstanceMeta> instanceMetas = rc.fetchAll(serviceMeta);

		InstanceMeta instanceMeta = loadBalancer.choose(instanceMetas);
		System.out.println(" inst size=" + instanceMetas.size() +  ", inst  " + instanceMeta);
		String url = instanceMeta.toUrl();

		// 拿到请求的报文
		Mono<String> requestMono = request.bodyToMono(String.class);
		return requestMono.flatMap( x -> invokeFromRegistry(x, url));
	}

	private static @NotNull Mono<ServerResponse> invokeFromRegistry(String x, String url) {
		// 通过webclient发送post请求
		WebClient client = WebClient.create(url);
		Mono<ResponseEntity<String>> entity = client.post()
				.header("Content-Type", "application/json")
				.bodyValue(x).retrieve().toEntity(String.class);

		// 通过entity获取响应报文
		Mono<String> body = entity.map(ResponseEntity::getBody);
		body.subscribe(resp -> System.out.println("response:" + resp));

		// 组装响应报文
		return ServerResponse.ok()
				.header("Content-Type", "application/json")
				.header("cat.gw.version", "v1.0.0")
				.body(body, String.class);
	}

}
