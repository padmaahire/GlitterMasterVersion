package com.goglitter.ui.Loan;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Symmetric {
    public byte[] decrypt(byte[] encryptedContent, byte[] iv, byte[] key, String decryptionMode, String decryptionAlgo) throws GeneralSecurityException {
      try {
          DecryptionHelper helper = new DecryptionHelper();
          SecretKeySpec secretKey = helper.getSecretKey(key, decryptionMode);
          return helper.decrypt(encryptedContent, secretKey, iv, decryptionAlgo);
      }catch (Exception e){
          e.printStackTrace();
      }

      return null;

    }

    public byte[] encrypt(byte[] content, byte[] iv, byte[] key, String encryptionMode, String encryptionAlgo) throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        EncryptionHelper helper = new EncryptionHelper();
        SecretKeySpec secretKey = helper.getSecretKey(key, encryptionMode);
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        return helper.encrypt(content, secretKey, ivParams, encryptionAlgo);
    }

    public byte[] encrypt(byte[] content, byte[] key, String encryptionMode, String encryptionAlgo) throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        EncryptionHelper helper = new EncryptionHelper();
        SecretKeySpec secretKey = helper.getSecretKey(key, encryptionMode);
        return helper.encrypt(content, secretKey, encryptionAlgo);
    }


    public static byte[] encryptGCM_test(byte[] content, SecretKey key, byte[] IV) throws Exception
    {
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, IV);
        // Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

        // Perform Encryption
        byte[] cipherText = cipher.doFinal(content);
        return cipherText;
    }

    public static String decryptGCM_test(byte[] cipherText, SecretKey key, byte[] IV) throws Exception
    {
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");

        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, IV);

        // Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

        // Perform Decryption
        byte[] decryptedText = cipher.doFinal(cipherText);

        return new String(decryptedText);
    }



    //For Apigee
    public byte[] encryptGCM(byte[] content, byte[] iv, byte[] key, String encryptionMode, String encryptionAlgo) throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        EncryptionHelper helper = new EncryptionHelper();
        SecretKeySpec secretKey = helper.getSecretKey(key, encryptionMode);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, iv);
        byte[] encContent =helper.encryptGCM(content, secretKey, gcmParameterSpec, encryptionAlgo);;
        return encContent;
    }
    public byte[] decryptGCM(byte[] encryptedContent, byte[] iv, byte[] key, String decryptionMode, String decryptionAlgo) throws GeneralSecurityException {
        DecryptionHelper helper = new DecryptionHelper();
        SecretKeySpec secretKey = helper.getSecretKey(key, decryptionMode);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * 8, iv);
        return helper.decryptGCM(encryptedContent, secretKey, gcmParameterSpec, decryptionAlgo);
    }
}