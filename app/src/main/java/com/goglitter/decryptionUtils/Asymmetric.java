package com.goglitter.decryptionUtils;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Asymmetric {
  public byte[] decrypt(byte[] encryptedContent, byte[] privateKey, String decryptionMode, String decryptionAlgo) throws GeneralSecurityException {
    DecryptionHelper helper = new DecryptionHelper();
    PrivateKey pkcs8PrivateKey = helper.getPrivate(privateKey, decryptionMode);
    return helper.decrypt(encryptedContent, pkcs8PrivateKey, decryptionAlgo);
  }
  
  public byte[] encrypt(byte[] content, byte[] publicKey, String encryptionMode, String encryptionAlgo) throws GeneralSecurityException {
    EncryptionHelper helper = new EncryptionHelper();
    PublicKey x509Key = helper.getPublic(publicKey, encryptionMode);
    return helper.encrypt(content, x509Key, encryptionAlgo);
  }
  
  public PublicKey GetPubKey(byte[] publicKey, String encryptionMode) throws GeneralSecurityException {
	    EncryptionHelper helper = new EncryptionHelper();
	    PublicKey x509Key = helper.getPublic(publicKey, encryptionMode);
	    return x509Key;
}
  
}
