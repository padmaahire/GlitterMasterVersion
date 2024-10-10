package com.goglitter.ui.Loan;
import java.util.Base64;

public class CrmUtils {
    public byte[] encodeToBase64(byte[] input) {
        return Base64.getEncoder().encode(input);
    }

    public byte[] decodeFromBase64(byte[] input) {
        return Base64.getDecoder().decode(input);
    }
}
