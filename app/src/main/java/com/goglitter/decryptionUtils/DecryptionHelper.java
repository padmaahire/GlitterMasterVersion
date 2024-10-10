package com.goglitter.decryptionUtils;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;




public class DecryptionHelper {
  public SecretKeySpec getSecretKey(byte[] keyBytes, String algorithm) {
    return new SecretKeySpec(keyBytes, algorithm);
  }
  
  public PrivateKey getPrivate(byte[] privateKey, String mode) throws NoSuchAlgorithmException, InvalidKeySpecException {
    Utils utilities = new Utils();
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(utilities.decodeFromBase64(privateKey));
    KeyFactory kf = KeyFactory.getInstance(mode);
    return kf.generatePrivate(spec);
  }
  
  public byte[] decrypt(byte[] input, PrivateKey key, String algorithm) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(2, key);
    return cipher.doFinal(input);
  }
  
  public byte[] decrypt(byte[] input, SecretKeySpec key, byte[] iv, String algorithm) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(algorithm);
    IvParameterSpec ivParams = new IvParameterSpec(iv);
    cipher.init(2, key, ivParams);
    return cipher.doFinal(input);
  }
  
  public byte[] decryptGCM(byte[] input, SecretKeySpec key, GCMParameterSpec gcmParameterSpec, String algorithm) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(algorithm);
    cipher.init(2, key, gcmParameterSpec);
    return cipher.doFinal(input);
  }
  
  public byte[] extractBytes(byte[] input, int startIndex, int endIndex) {
    return Arrays.copyOfRange(input, startIndex, endIndex);
  }
  
}