package com.abysscat.catgateway.config;

import com.abysscat.catrpc.core.registry.RegistryCenter;
import com.abysscat.catrpc.core.registry.cat.CatRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

/**
 * gateway config.
 *
 * @Author: abysscat-yj
 * @Create: 2024/5/31 1:56
 */
@Configuration
public class GatewayConfig {

	@Bean
	public RegistryCenter rc() {
		return new CatRegistryCenter();
	}

	@Bean
	ApplicationRunner runner(@Autowired ApplicationContext context) {
		return args -> {
			// 通过 SimpleUrlHandlerMapping 将请求路径 /ga/** 映射到 gatewayWebHandler
			SimpleUrlHandlerMapping handlerMapping = context.getBean(SimpleUrlHandlerMapping.class);

			Properties mappings = new Properties();
			mappings.put("/ga/**", "gatewayWebHandler");
			handlerMapping.setMappings(mappings);
			handlerMapping.initApplicationContext();

			System.out.println("catrpc gateway start");
		};
	}
}
