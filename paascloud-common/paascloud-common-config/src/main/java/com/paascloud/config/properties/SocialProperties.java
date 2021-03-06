package com.paascloud.config.properties;

import lombok.Data;

/**
 * 社交登录配置项
 *
 * @author paascloud.net @gmail.com
 */
@Data
public class SocialProperties {

	/**
	 * 社交登录功能拦截的url
	 */
	private String filterProcessesUrl = "/socialLogin";

	private QQProperties qq = new QQProperties();

	private WeixinProperties weixin = new WeixinProperties();

}
