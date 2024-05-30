package com.abysscat.catgateway;

import com.abysscat.catrpc.core.registry.RegistryCenter;
import com.abysscat.catrpc.core.registry.cat.CatRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
