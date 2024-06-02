package com.abysscat.catgateway.plugin;

import com.abysscat.catrpc.core.api.LoadBalancer;
import com.abysscat.catrpc.core.cluster.RoundRobinBalancer;
import com.abysscat.catrpc.core.meta.InstanceMeta;
import com.abysscat.catrpc.core.meta.ServiceMeta;
import com.abysscat.catrpc.core.registry.RegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * catrpc plugin.
 *
 * @Author: abysscat-yj
 * @Create: 2024/6/2 19:51
 */
@Component("catrpc")
public class CatrpcPlugin extends AbstractGatewayPlugin {

	public static final String NAME = "catrpc";
	private final String PREFIX = GATEWAY_PREFIX + "/" + NAME + "/";

	@Autowired
	RegistryCenter rc;

	LoadBalancer<InstanceMeta> loadBalancer = new RoundRobinBalancer<>();

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Mono<Void> doHandle(ServerWebExchange exchange, GatewayPluginChain chain) {
		System.out.println("===>>>> CatrpcPlugin doHandle ...");

		// 通过请求path拿到服务名
		String service = exchange.getRequest().getPath().value().substring(PREFIX.length());
		ServiceMeta serviceMeta = ServiceMeta.builder().name(service)
				.app("app1").env("dev").namespace("public").version("v1").build();

		// 通过rc拿到所有活着的服务实例
		List<InstanceMeta> instanceMetas = rc.fetchAll(serviceMeta);

		// 负载均衡，选择一个实例
		InstanceMeta instanceMeta = loadBalancer.choose(instanceMetas);
		System.out.println(" inst size=" + instanceMetas.size() + ", inst  " + instanceMeta);
		String url = instanceMeta.toUrl();

		// 拿到请求的报文
		Flux<DataBuffer> requestBody = exchange.getRequest().getBody();

		// 通过webclient发送post请求
		WebClient client = WebClient.create(url);
		Mono<ResponseEntity<String>> entity = client.post()
				.header("Content-Type", "application/json")
				.body(requestBody, DataBuffer.class).retrieve().toEntity(String.class);

		// 通过entity获取响应报文
		Mono<String> body = entity.map(ResponseEntity::getBody);

		// 组装响应报文
		exchange.getResponse().getHeaders().add("Content-Type", "application/json");
		exchange.getResponse().getHeaders().add("cat.gw.version", "v1.0.0");
		exchange.getResponse().getHeaders().add("cat.gw.plugin", getName());

		return body.flatMap(x -> exchange.getResponse()
						.writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(x.getBytes()))))
				.then(chain.handle(exchange));
	}

	@Override
	public boolean doSupport(ServerWebExchange exchange) {
		return exchange.getRequest().getPath().value().startsWith(PREFIX);
	}
}
