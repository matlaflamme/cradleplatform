package com.cradlerest.web.service.utilities;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;

public class HybridFileDecrypter {
	private static final String TAG = "HybridFileDecrypter";
	/* Encryption Design:
     - AES is symmetric, requiring a single private key plus an initialization vector (IV)
     - RSA is asymmetric, requiring a public key for encryption, but limits on data length (~60-117 bytes)
     - Hybrid Cryptosystem:
        * AES encrypt data with a random key
        * RSA encrypt the AES key & IV
    */

    /* Linux commands to process the generated file:
    unzip encrypted_reading_*.zip
    openssl rsautl -decrypt -inkey ./private.pem -in aes_key.rsa -out aes_key.base64 -oaep
    openssl rsautl -decrypt -inkey ./private.pem -in aes_iv.rsa -out iv.base64 -oaep

    base64 -d aes_key.base64 > aes_key.bin
    base64 -d iv.base64 > iv.bin
    hexdump -e '16/1 "%02x"' aes_key.bin > aes_key.hex
    hexdump -e '16/1 "%02x"' iv.bin > iv.hex

    openssl enc -d -aes-256-cbc -in data.zip.aes -out data_decoded.zip -K `cat aes_key.hex` -iv `cat iv.hex`
    unzip data_decoded.zip
    */

    /* Linux command for creating new public key:
    openssl rsa -in private.pem -outform PEM -pubout -out public.pem
    */

	// TODO : pull private key from file so it's not just sitting here in the open

	private static final String PRIVATE_KEY =
			"-----BEGIN RSA PRIVATE KEY-----" +
			"MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCoLgDSieuL/nxA" +
			"xww22Y+ea3uq4I5izCGwUjBF5LN1HNurGEcOjCxZ9qHyUSYXuXZfbOfHS8UnR31s" +
			"v9qLG0mNKObrdzaHSjo/YbomTdwtzl5D59b67wgQuDgj1Wws2yj2FuFZVzC5Tjuh" +
			"YvsUJ0KC1XoavS1M+acl/KCRQeCAraYOQaCPSYp85TlN02hSFqu9Aya753ijb2Cf" +
			"7y2GJnkbvQmWCyarQ+O9e4qP6yR9FxdwocEmGBT/Qr0fVjmK0ZgdICdbTm8l9Irb" +
			"+65cWMnTMI7WegzeEAI4UD4AhuLBPMNWU9on8cEhrFmfRV1+vUDzrj4ILtFhcUXg" +
			"50T6Els9AgMBAAECggEAcNmto5j4fY/eQHlQMN38CQmGijr0UEc5s97ylXPJF6St" +
			"ojlANPrKJ/Pe54KcVP8V9yJUxN6LBLhr6TUzqHQ+/oGbshTkehPM0gr5pgpI3FPF" +
			"3TsMfoU2nEGBKkpjtBW7fqYLwK9I+o8MZFbOs89EShaFQLwAUc6T4mGKPO4/fxfv"+
			"YTo+ZFJCSTID9h7UT75PPGjpmzyBjBwwjGQ+/oYPJXkVTf59CZ2IDJO6VzCLoyhs"+
			"gmDCjrmxd3rzewSSpczAmiBQj7lXzHrVsL7xi6RgWRUkqHTZWZt740ZG7tNGSRFg"+
			"jPLUBGX6UPblYkUxorYKmlzmIPnYXETQ1DdGWO8S4QKBgQDeFpgXn5fhOs6aDVKz"+
			"ExAwmjrQ9R8NFncjeMrP3kg2XNYS+NEVCV8LI5mBq2JkWYOSULV8q6YMvMiQ/Mp9"+
			"P6TiRn+UUBGVc5mLFk8W6hX7O37caG7rlLxOMq+UfOT/O5/xyFoyXZUnjDAJUCZn"+
			"/swS0Ylppyw0mQrV6HIarqn7swKBgQDB3CEhjq7gAWBcPFd6h1WoSzSH3NJnroON"+
			"QFsFM5kR6zx/cdjniCATFPKeyY2H6Aj5QZAfIagZk8Ok28IBEKN5rOT4qUnA1y18"+
			"m5f+ci4PS1UglXrvpqB3cMZHUCiik63OuThJoPLYUi8EBOsmQpBIKgRp1lKFLq+p"+
			"5g/D+igVTwKBgQDK2bBJw4eiPWh/5TNoRT1AZ17ZhUq7VY20ZJw6iTlEDj+qTIIM"+
			"yOZ68iU//x57shcvCXD7Unz0nWpEo+D/zaX415OoEAo/i7rvA41xzJYPRyR0hR7r"+
			"JotokXs2EW3IgtlwqiNYZYna+XzWxNKPoncbqS/txLfnwvHWt92g6XBxaQKBgQC6"+
			"XsNnPIH4jrox0j0Tado6khptZyjMTqTf/ffDV2iYJnuh1jR92otjbOcXKpOrDbLa"+
			"1mIU28l81WboLfEkDvfqeQIj4LQr9XIgcoE8OE75myW6zppCnQF18nBcjL6UFb+p"+
			"lr17Y4Uz3Eag6Y2b2bBWADb//PCWY1yzzJ5pfSxd1wKBgQC52N5DZBrA/PhvXXCE"+
			"hMf96ZWQCNZO/xeYTtguDF1knhKD2mWTJkAe/q/NWvhwjjgZDk2zoLbJSWWXQ1lH"+
			"AyITqlMg84KCXRzRjBk5Cn3cAhDFi4sG4uSY3QqkyflwF4ONY2f4NVTzbXLg9GaN"+
			"MLnUOUfdq2pDG1RO/rBij2lweQ=="+
			"-----END RSA PRIVATE KEY-----";



	public static void hybridDecryptFile(MultipartFile inputFile, String filePath) throws GeneralSecurityException, IOException {

		// Unzip uploaded file
		HashMap<String, byte[]> encryptedFiles = Zipper.unZip(inputFile.getInputStream(), filePath);

		PrivateKey privateKey = convertRsaPemToPrivateKey(PRIVATE_KEY);

		// Decrypt Initialization Vector (IV) to Base64 string with Private Key
		byte[] aesIvEncrypted = encryptedFiles.get("aes_iv.rsa");
		String aesIv64 = decryptRSA(privateKey, aesIvEncrypted);
		// Convert Base64 String to Hex
		byte[] aesIvHex = Base64.getDecoder().decode(aesIv64);

		// Decrypt AES Key to Base64 string with Private Key
		byte[] aesKeyBytes = encryptedFiles.get("aes_key.rsa");
		String aesKey = decryptRSA(privateKey, aesKeyBytes);
		// Convert Base64 String to Hex
		byte[] aesKeyHex = Base64.getDecoder().decode(aesKey);

		// Use the IV and AES key to decrypt the data
		ByteArrayInputStream decryptedZip = decryptFileWithAES(aesKeyHex, aesIvHex, encryptedFiles.get("data.zip.aes"));

		// Unzip the decrypted data
		HashMap<String, byte[]> decryptedFiles = Zipper.unZip(decryptedZip, filePath);

		// TODO : figure out what to do with the unencrypted files...
		for (HashMap.Entry<String, byte[]> readingFile : decryptedFiles.entrySet()) {
			System.out.println(readingFile.getKey());
			// byte[] rFile = readingFile.getValue();
		}
	}

	public static PrivateKey convertRsaPemToPrivateKey(String pkcsKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// source: https://stackoverflow.com/questions/7216969/getting-rsa-private-key-from-pem-base64-encoded-private-key-file#7221381
		// Remove the first and last lines
		final String HEADER = "-----BEGIN RSA PRIVATE KEY-----";
		final String FOOTER = "-----END RSA PRIVATE KEY-----";

		String privateKeyPEM = pkcsKey.trim();
		if (!pkcsKey.startsWith(HEADER)) {
			throw new InvalidKeySpecException("Private key must start with: " + HEADER);
		}
		if (!pkcsKey.endsWith(FOOTER)) {
			throw new InvalidKeySpecException("Private key must end with: " + FOOTER);
		}

		privateKeyPEM = privateKeyPEM.replace(HEADER, "");
		privateKeyPEM = privateKeyPEM.replace(FOOTER, "");
		privateKeyPEM = privateKeyPEM.trim();

		// Base64 decode the data
		byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
		System.out.println(TAG + ": Decoded key bytes from base64 length: " + keyBytes.length);

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");

		return kf.generatePrivate(spec);
	}

	private static ByteArrayInputStream decryptFileWithAES(byte[] secretAesKey, byte[] initializationVector, byte[] sourceData) throws GeneralSecurityException, IOException {
		// Get Cipher instance for AES algorithm (public key)
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		// Initialize cipher
		SecretKeySpec sKeySpec = new SecretKeySpec(secretAesKey, "AES");
		cipher.init(Cipher.DECRYPT_MODE, sKeySpec, new IvParameterSpec(initializationVector));

		// Encrypt the byte data

		final int BUFF_SIZE = 1024 * 1024;
		byte[] buffer = new byte[BUFF_SIZE];

		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		try (ByteArrayInputStream byteArray = new ByteArrayInputStream(sourceData)) {
			while (byteArray.available() > 0) {
				// get data
				int bytesRead = byteArray.read(buffer);
				// encrypt
				byte[] encryptedBytes = cipher.doFinal(buffer, 0, bytesRead);
				// write to output file
				byteOutput.write(encryptedBytes);
			}
		}
		ByteArrayInputStream decryptedBytes = new ByteArrayInputStream(byteOutput.toByteArray());
		return decryptedBytes;
	}

	private static String decryptRSA(PrivateKey privateKey, byte[] data) throws GeneralSecurityException {
		Cipher decrypt = Cipher.getInstance("RSA/ECB/OAEPPadding");
		decrypt.init(Cipher.DECRYPT_MODE, privateKey, new OAEPParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT));
		String decryptedMessage = new String(decrypt.doFinal(data), StandardCharsets.UTF_8);
		return decryptedMessage;
	}

}

