package fr.beungoud.util;

import java.io.UnsupportedEncodingException;

public class Utils {
	public static String Hash(String input) {
		String lowerInput = input.toLowerCase();
		String retour = "";

		int crc = 0xffffffff;
		try {
			byte[] bytes = lowerInput.getBytes("UTF-8");

			for (byte myByte : bytes) {

				crc ^= ((myByte) << 24);
				for (int i = 0; i < 8; i++) {
					if ((crc & 0x80000000) == 0x80000000) {
						crc = (crc << 1) ^ 0x04C11DB7;
					} else {
						crc <<= 1;
					}
				}
				crc = crc & 0xFFFFFFFF;
			}
			retour = Integer.toHexString(crc);
			retour = "000000000"  +retour;
			retour = (retour).substring(retour.length()-8);
		} catch (UnsupportedEncodingException e) {
		}
		
		return retour;
	}
}
