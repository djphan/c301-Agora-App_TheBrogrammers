package com.brogrammers.agora;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class md5 {
	static public Long hash(String s) {
		try {
			// make hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			
			return ByteBuffer.wrap(digest.digest()).getLong();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return 0L;
	}
}