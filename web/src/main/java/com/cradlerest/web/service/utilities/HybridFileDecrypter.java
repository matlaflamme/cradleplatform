package com.cradlerest.web.service.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;

public class HybridFileDecrypter {
    private static final String TAG = "HybridFileEncrypter";

    private static final int NUM_BITS_AES_KEY = 256;

    private static final String LINEFEED = "\r\n";

    /* Linux command for creating new public key:
    openssl rsa -in private.pem -outform PEM -pubout -out public.pem
    */

    // TODO : pull private key from file so it's not just sitting here in the open
    private static final String PRIVATE_KEY = "    -----BEGIN RSA PRIVATE KEY-----                             " + LINEFEED +
    "MIIEpQIBAAKCAQEAqC4A0onri/58QMcMNtmPnmt7quCOYswhsFIwReSzdRzbqxhH" + LINEFEED +
    "DowsWfah8lEmF7l2X2znx0vFJ0d9bL/aixtJjSjm63c2h0o6P2G6Jk3cLc5eQ+fW" + LINEFEED +
    "+u8IELg4I9VsLNso9hbhWVcwuU47oWL7FCdCgtV6Gr0tTPmnJfygkUHggK2mDkGg" + LINEFEED +
    "j0mKfOU5TdNoUharvQMmu+d4o29gn+8thiZ5G70Jlgsmq0PjvXuKj+skfRcXcKHB" + LINEFEED +
    "JhgU/0K9H1Y5itGYHSAnW05vJfSK2/uuXFjJ0zCO1noM3hACOFA+AIbiwTzDVlPa" + LINEFEED +
    "J/HBIaxZn0Vdfr1A864+CC7RYXFF4OdE+hJbPQIDAQABAoIBAHDZraOY+H2P3kB5" + LINEFEED +
    "UDDd/AkJhoo69FBHObPe8pVzyRekraI5QDT6yifz3ueCnFT/FfciVMTeiwS4a+k1" + LINEFEED +
    "M6h0Pv6Bm7IU5HoTzNIK+aYKSNxTxd07DH6FNpxBgSpKY7QVu36mC8CvSPqPDGRW" + LINEFEED +
    "zrPPREoWhUC8AFHOk+JhijzuP38X72E6PmRSQkkyA/Ye1E++Tzxo6Zs8gYwcMIxk" + LINEFEED +
    "Pv6GDyV5FU3+fQmdiAyTulcwi6MobIJgwo65sXd683sEkqXMwJogUI+5V8x61bC+" + LINEFEED +
    "8YukYFkVJKh02Vmbe+NGRu7TRkkRYIzy1ARl+lD25WJFMaK2Cppc5iD52FxE0NQ3" + LINEFEED +
    "RljvEuECgYEA3haYF5+X4TrOmg1SsxMQMJo60PUfDRZ3I3jKz95INlzWEvjRFQlf" + LINEFEED +
    "CyOZgatiZFmDklC1fKumDLzIkPzKfT+k4kZ/lFARlXOZixZPFuoV+zt+3Ghu65S8" + LINEFEED +
    "TjKvlHzk/zuf8chaMl2VJ4wwCVAmZ/7MEtGJaacsNJkK1ehyGq6p+7MCgYEAwdwh" + LINEFEED +
    "IY6u4AFgXDxXeodVqEs0h9zSZ66DjUBbBTOZEes8f3HY54ggExTynsmNh+gI+UGQ" + LINEFEED +
    "HyGoGZPDpNvCARCjeazk+KlJwNctfJuX/nIuD0tVIJV676agd3DGR1AoopOtzrk4" + LINEFEED +
    "SaDy2FIvBATrJkKQSCoEadZShS6vqeYPw/ooFU8CgYEAytmwScOHoj1of+UzaEU9" + LINEFEED +
    "QGde2YVKu1WNtGScOok5RA4/qkyCDMjmevIlP/8ee7IXLwlw+1J89J1qRKPg/82l" + LINEFEED +
    "+NeTqBAKP4u67wONccyWD0ckdIUe6yaLaJF7NhFtyILZcKojWGWJ2vl81sTSj6J3" + LINEFEED +
    "G6kv7cS358Lx1rfdoOlwcWkCgYEAul7DZzyB+I66MdI9E2naOpIabWcozE6k3/33" + LINEFEED +
    "w1domCZ7odY0fdqLY2znFyqTqw2y2tZiFNvJfNVm6C3xJA736nkCI+C0K/VyIHKB" + LINEFEED +
    "PDhO+Zslus6aQp0BdfJwXIy+lBW/qZa9e2OFM9xGoOmNm9mwVgA2//zwlmNcs8ye" + LINEFEED +
    "aX0sXdcCgYEAudjeQ2QawPz4b11whITH/emVkAjWTv8XmE7YLgxdZJ4Sg9plkyZA" + LINEFEED +
    "Hv6vzVr4cI44GQ5Ns6C2yUlll0NZRwMiE6pTIPOCgl0c0YwZOQp93AIQxYuLBuLk" + LINEFEED +
    "mN0KpMn5cBeDjWNn+DVU821y4PRmjTC51DlH3atqQxtUTv6wYo9pcHk=        " + LINEFEED +
    "-----END RSA PRIVATE KEY-----                                   " + LINEFEED;

    public static void hybridDecryptFile(File inputFile) {
        // 1. Decrypt AES Key to Base64 string
        // 2. Decrypt Initialization Vector (IV) to Base64 string
        // 3. Convert both AES key and IV to binary format
        // 4. Convert both binaries to hexdumps format '16/1 "%02x"'
        // 5. Decrypt file using hexdumps


    }

    private static String decryptFileWithAES(SecretKey secretAesKey, byte[] initializationVector, File sourceDataFile, File encryptedDataFile) throws GeneralSecurityException, IOException {
        // Get Cipher instance for AES algorithm (public key)
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Initialize cipher
        SecretKeySpec sKeySpec = new SecretKeySpec(secretAesKey.getEncoded(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, new IvParameterSpec(initializationVector));

        // Encrypt the byte data
        try (FileOutputStream fosData = new FileOutputStream(encryptedDataFile);
             FileInputStream fisData = new FileInputStream(sourceDataFile))
        {
            final int BUFF_SIZE = 1024 * 1024;
            byte[] buffer = new byte[BUFF_SIZE];

            while (fisData.available() > 0) {
                // get data
                int bytesRead = fisData.read(buffer);
                // encrypt
                byte[] encryptedBytes = cipher.doFinal(buffer, 0, bytesRead);
                // write to output file
                fosData.write(encryptedBytes);
            }
        }

        return "";
    }

    private static String decryptRSA(PrivateKey privateKey, byte[] data) throws GeneralSecurityException {
        Cipher decrypt = Cipher.getInstance("RSA/ECB/OAEPPadding");
        decrypt.init(Cipher.DECRYPT_MODE, privateKey, new OAEPParameterSpec("SHA-1", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT));
        String decryptedMessage = new String(decrypt.doFinal(data), StandardCharsets.UTF_8);
        return decryptedMessage;
    }

}

