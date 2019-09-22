package com.cradlerest.web.service.utilities;


import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;

public class HybridFileDecrypter {
    private static final String TAG = "HybridFileDecrypter";

    private static final int NUM_BITS_AES_KEY = 256;

    private static final String LINEFEED = "\r\n";

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


        // 3. Convert both AES key and IV to binary format
        // 4. Convert both binaries to hexdumps format '16/1 "%02x"'
        // 5. Decrypt file using hexdumps

        // Unzip uploaded file
        HashMap<String, byte[]> encryptedFiles = Zipper.unZip(inputFile.getInputStream(), filePath);

        PrivateKey privateKey = convertRsaPemToPrivateKey(PRIVATE_KEY);

        // Decrypt Initialization Vector (IV) to Base64 string
        byte[] aesIvEncrypted = encryptedFiles.get("aes_iv.rsa");
        String aesIv64 = decryptRSA(privateKey, aesIvEncrypted);
        // Convert Base64 String to Hex
        byte[] aesIvHex = Base64.getDecoder().decode(aesIv64);

        // Decrypt AES Key to Base64 string
        byte[] aesKeyBytes = encryptedFiles.get("aes_key.rsa");
        String aesKey = decryptRSA(privateKey, aesKeyBytes);
        byte[] aesKeyHex = Base64.getDecoder().decode(aesKey);


        ByteArrayInputStream decryptedZip = decryptFileWithAES(aesKeyHex, aesIvHex, encryptedFiles.get("data.zip.aes"));
        System.out.println(decryptedZip);

        HashMap<String, byte[]> decryptedFiles = Zipper.unZip(decryptedZip, filePath);
        System.out.println(decryptedFiles);
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
//        privateKeyPEM = privateKeyPEM.replaceAll("\\n", "");
        privateKeyPEM = privateKeyPEM.trim();

//        System.out.print(TAG + ": " + privateKeyPEM);
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

