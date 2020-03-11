package util;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Cripto {
	
	private static SecureRandom random = new SecureRandom();
	
	public static String cifrarClave(String clave) {
		// Creamos un salt aleatorio de 8 bytes
		byte[] salt = new byte[8];
		random.nextBytes(salt);

		String hash = cifrarClave(clave, salt);

		String passwordCifrada = Hex.hexString(salt) + "_" + hash;

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

	
	public static boolean comprobarClave(String clave, String claveCifrada) {
		String[] parts = claveCifrada.split("_");
		byte[] salt = Hex.hexBytes(parts[0]);
		String hash = parts[1];

		String calculatedHash = cifrarClave(clave, salt);
		
		return hash.contentEquals(calculatedHash);
	}

	
	public static String calcularMac(String msg, String clave) {
		try {
			String algoritmo = "HmacSHA256";
			Mac mac = Mac.getInstance(algoritmo);
			SecretKeySpec key = new SecretKeySpec(clave.getBytes(StandardCharsets.UTF_8), algoritmo);
			mac.init(key);
			byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);
			byte[] signatureBytes = mac.doFinal(msgBytes);
			String signature = Hex.hexString(signatureBytes);
			return signature;
		}
		catch (Exception e) {
			return null;
		}
	}

	
}
