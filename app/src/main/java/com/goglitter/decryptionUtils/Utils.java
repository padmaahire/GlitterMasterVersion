package com.goglitter.decryptionUtils;

import java.util.Base64;

public class Utils {
  public byte[] encodeToBase64(byte[] input) {
    return Base64.getEncoder().encode(input);
  }
  
  public byte[] decodeFromBase64(byte[] input) {
    return Base64.getDecoder().decode(input);
  }
}
