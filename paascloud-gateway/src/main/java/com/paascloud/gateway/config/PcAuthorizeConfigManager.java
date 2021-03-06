/*
 * Copyright (c) 2018. paascloud.net All Rights Reserved.
 * 项目名称：paascloud快速搭建企业级分布式微服务平台
 *  类名称：PcAuthorizeConfigManager.java
 *  创建人：刘兆明
 *  联系方式：paascloud.net@gmail.com
 *  开源地址: https://github.com/paascloud
 *  博客地址: http://blog.paascloud.net
 *  项目官网: http://paascloud.net
 */

package com.paascloud.gateway.config;

import com.paascloud.security.core.AuthorizeConfigManager;
import com.paascloud.security.core.AuthorizeConfigProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 默认的授权配置管理器
 *
 * @author paascloud.net @gmail.com
 */
@Component
public class PcAuthorizeConfigManager implements AuthorizeConfigManager {

	private final List<AuthorizeConfigProvider> authorizeConfigProviders;

	/**
	 * Instantiates a new Pc authorize config manager.
	 *
	 * @param authorizeConfigProviders the authorize config providers
	 */
	@Autowired
	public PcAuthorizeConfigManager(List<AuthorizeConfigProvider> authorizeConfigProviders) {
		this.authorizeConfigProviders = authorizeConfigProviders;
	}

	/**
	 * Config.
	 *
	 * @param config the config
	 */
	@Override
	public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		boolean existAnyRequestConfig = false;
		String existAnyRequestConfigName = null;

		for (AuthorizeConfigProvider authorizeConfigProvider : authorizeConfigProviders) {
			boolean currentIsAnyRequestConfig = authorizeConfigProvider.config(config);
			if (existAnyRequestConfig && currentIsAnyRequestConfig) {
				throw new RuntimeException("重复的anyRequest配置:" + existAnyRequestConfigName + "," + authorizeConfigProvider.getClass().getSimpleName());
			} else if (currentIsAnyRequestConfig) {
				existAnyRequestConfig = true;
				existAnyRequestConfigName = authorizeConfigProvider.getClass().getSimpleName();
			}
		}

		if (!existAnyRequestConfig) {
			config.anyRequest().authenticated();
		}
	}

}
