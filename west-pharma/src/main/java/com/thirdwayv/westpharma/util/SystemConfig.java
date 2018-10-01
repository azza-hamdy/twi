package com.thirdwayv.westpharma.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="sysconfig")
public class SystemConfig {
	
	private int transctionNumInBlock;

	public int getTransctionNumInBlock() {
		return transctionNumInBlock;
	}

	public void setTransctionNumInBlock(int transctionNumInBlock) {
		this.transctionNumInBlock = transctionNumInBlock;
	}
	
	
}
