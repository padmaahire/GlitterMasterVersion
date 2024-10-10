package com.goglitter.decryptionUtils;

import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



public class EncryptionHelper {
  public PublicKey getPublic(byte[] publicKey, String mode) throws NoSuchAlgorithmException, InvalidKeySpecException {
    Utils utilities = new Utils();
    X509EncodedKeySpec spec = new X509EncodedKeySpec(utilities.decodeFromBase64(publicKey));
    KeyFactory kf = KeyFactory.getInstance(mode);
    return kf.generatePublic(spec);
  }
  
  public PublicKey getPublicKeyFromPrivateKey(PrivateKey pkcs8PrivateKey, String mode) throws NoSuchAlgorithmException, InvalidKeySpecException {
    RSAPrivateCrtKey privk = (RSAPrivateCrtKey)pkcs8PrivateKey;
    RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(privk.getModulus(), privk.getPublicExponent());
    KeyFactory keyFactory = KeyFactory.getInstance(mode);
    PublicKey myPublicKey = keyFactory.generatePublic(publicKeySpec);
    return myPublicKey;
  }
  
  public SecretKeySpec generateSecretKey(int length, String algorithm) {
    SecureRandom rnd = new SecureRandom();
    byte[] key = new byte[length];
    rnd.nextBytes(key);
    SecretKeySpec secretKey = new SecretKeySpec(key, algorithm);
    return secretKey;
  }
  
  public SecretKeySpec getSecretKey(byte[] keyBytes, String algorithm) {
    return new SecretKeySpec(keyBytes, algorithm);
  }
  
  public byte[] generateIv(String cipherAlgorithm) throws NoSuchAlgorithmException, NoSuchPaddingException {
    Cipher cipher = Cipher.getInstance(cipherAlgorithm);
    SecureRandom random = SecureRandom.getInstanceStrong();
    byte[] iv = new byte[cipher.getBlockSize()];
    random.nextBytes(iv);
    return iv;
  }
  
  public byte[] encrypt(byte[] input, PublicKey key, String cipherAlgorithm) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance(cipherAlgorithm);
    cipher.init(1, key);
    return cipher.doFinal(input);
  }
  
  public byte[] encrypt(byte[] input, SecretKeySpec key, IvParameterSpec ivSpec, String cipherAlgorithm) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException {
    Cipher cipher = Cipher.getInstance(cipherAlgorithm);
    cipher.init(1, key, ivSpec);
    return cipher.doFinal(input);
  }
  
  public byte[] encrypt(byte[] input, SecretKeySpec key, String cipherAlgorithm) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException {
	    Cipher cipher = Cipher.getInstance(cipherAlgorithm);
	    cipher.init(1, key);
	    return cipher.doFinal(input);
	  }
  
  public byte[] encryptGCM(byte[] input, SecretKeySpec key, GCMParameterSpec gcmParameterSpec, String cipherAlgorithm) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException {
	    Cipher cipher = Cipher.getInstance(cipherAlgorithm);
	    cipher.init(1, key, gcmParameterSpec);
	    return cipher.doFinal(input);
	  }
  
  public byte[] mergeTwoByteArrays(byte[] arrayOne, byte[] arrayTwo) {
    byte[] mergedArray = new byte[arrayOne.length + arrayTwo.length];
    System.arraycopy(arrayOne, 0, mergedArray, 0, arrayOne.length);
    System.arraycopy(arrayTwo, 0, mergedArray, arrayOne.length, arrayTwo.length);
    return mergedArray;
  }
  
  
}
