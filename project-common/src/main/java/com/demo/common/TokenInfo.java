/**
 * 
 */
package com.demo.common;

import lombok.Data;

import java.util.Map;

@Data
public class TokenInfo {

	private String access_token;

	private String token_type;

	private String refresh_token;
	
	private String scope;
	
	private Map<String,String> additionalInfo;
 	
}
