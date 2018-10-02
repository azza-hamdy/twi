package com.thirdwayv.westpharma.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtils {
	public static String generateHashBySHA256(Object... params) throws NoSuchAlgorithmException {
		StringBuilder sb = new StringBuilder();
		for (Object s : params) {
			sb.append(String.valueOf(s));
		}
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encodedhash = digest.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
		return bytesToHex(encodedhash);
	}

	private static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
