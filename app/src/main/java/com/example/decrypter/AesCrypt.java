package com.example.decrypter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCrypt {
    private final Cipher cipher;

    private final SecretKeySpec key;

    private AlgorithmParameterSpec spec;

    public AesCrypt(String paramString) throws NoSuchAlgorithmException, UnsupportedEncodingException, NoSuchPaddingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] arrayOfByte1 = paramString.getBytes("UTF-8");
        messageDigest.update(arrayOfByte1);
        byte[] arrayOfByte2 = new byte[32];
        arrayOfByte1 = messageDigest.digest();
        int i = arrayOfByte2.length;
        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        this.cipher = cipher;
        SecretKeySpec secretKeySpec = new SecretKeySpec(arrayOfByte2, "AES");
        this.key = secretKeySpec;
        AlgorithmParameterSpec algorithmParameterSpec = getIV();
        this.spec = algorithmParameterSpec;
    }

    public void decrypt(String paramString1, String paramString2) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException {
        FileInputStream fileInputStream = new FileInputStream(paramString1);
        FileOutputStream fileOutputStream = new FileOutputStream(paramString2);
        Cipher cipher = this.cipher;
        byte b = 2;
        SecretKeySpec secretKeySpec = this.key;
        AlgorithmParameterSpec algorithmParameterSpec = this.spec;
        cipher.init(b, secretKeySpec, algorithmParameterSpec);
        CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, this.cipher);
        byte b1 = 8;
        byte[] arrayOfByte = new byte[b1];
        while (true) {
            int i = cipherInputStream.read(arrayOfByte);
            b1 = -1;
            if (i == b1) {
                fileOutputStream.flush();
                fileOutputStream.close();
                cipherInputStream.close();
                return;
            }
            b1 = 0;
            cipher = null;
            fileOutputStream.write(arrayOfByte, 0, i);
        }
    }

    public AlgorithmParameterSpec getIV() {
        byte[] arrayOfByte = new byte[16];
        IvParameterSpec ivParameterSpec = new IvParameterSpec(arrayOfByte);
        return ivParameterSpec;
    }
}