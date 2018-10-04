package com.thirdwayv.westpharma.util;

public class BlockConcurrancyManager {

	private static boolean isLocked = false;

	public static synchronized boolean lock() {
		if (isLocked) {
			return false;
		}
		isLocked = true;
		return true;
	}

	public static synchronized boolean unlock() {
		if (!isLocked) {
			return false;
		}
		isLocked = false;
		return true;
	}

}
