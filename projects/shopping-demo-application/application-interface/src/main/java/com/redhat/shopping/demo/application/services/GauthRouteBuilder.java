package com.redhat.shopping.demo.application.services;

import java.net.URLEncoder;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;

/**
 * Builds the OAuth-specific routes (implements the OAuth integration layer) of the demo application.
 */
public class GauthRouteBuilder extends RouteBuilder {

	@Value(value="${gauth.callback.url}")
	private String callBackUrl;
    
	
	@Override
    public void configure() throws Exception {
    	getContext().setTracing(true);
		String encodedCallback = URLEncoder
				.encode(getCallBackUrl(),
						"UTF-8");
		String encodedScope = URLEncoder.encode(
				"https://www.google.com/m8/feeds/", "UTF-8");
        from("ghttp:///authorize")
            .to("gauth:authorize?callback=" + encodedCallback + "&scope=" + encodedScope);
        from("ghttp:///handler")
           .log("Inside Upgradation Process")
				.to("gauth:upgrade")
				.log("Upgraded tokens")
            .process(new TokenProcessor());
    }


	public String getCallBackUrl() {
		return callBackUrl;
	}


	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

}