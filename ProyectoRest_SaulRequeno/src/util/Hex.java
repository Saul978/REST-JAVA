package util;

import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Hex {

	private static SecureRandom random = new SecureRandom();
	public static String hexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	public static byte[] hexBytes(String s) {
		int len = s.length();
		byte[] b = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			b[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return b;
	}
	
	public static String cifrarClave(String clave) {
		// Creamos un salt aleatorio de 8 bytes
		byte[] salt = new byte[8];
		random.nextBytes(salt);

		String hash = cifrarClave(clave, salt);

		String passwordCifrada = Hex.hexString(salt) + "_" + hash;
		System.out.println(passwordCifrada);
		return passwordCifrada;
	}
	
	public static String cifrarClave(String clave, byte[] salt) {
		try {
			// Creamos los parámetros del algoritmo con la contraseña y el salt, 
			// especificando 65536 iteraciones y 128 bits de tamaño de hash
			KeySpec spec = new PBEKeySpec(clave.toCharArray(), salt, 65536, 128);
	
			// Obtenemos una instancia del algoritmo PBKDF2 con HmacSHA1
			String algPBKDF2 = "PBKDF2WithHmacSHA1";
			SecretKeyFactory factory = SecretKeyFactory.getInstance(algPBKDF2);
	
			// Generamos el hash con los parámetros establecidos
			SecretKey key = factory.generateSecret(spec);
			byte[] hash = key.getEncoded();
			
			return Hex.hexString(hash);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
