package com.goglitter.ui.Loan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.goglitter.R;

import java.security.GeneralSecurityException;

import javax.crypto.spec.SecretKeySpec;

public class SampleEncryptedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_encrypted);
       /* CrmUtils utilities = new CrmUtils();
        Asymmetric asymmetric = new Asymmetric();
        EncryptionHelper helper = new EncryptionHelper();
        String skey =  EncryptionHelper.randomString(16);
        byte[] key1 = skey.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(key1, "AES");
        byte[] plaintextKey = secretKey.getEncoded();

        String publicKey = "";

        byte[] encryptedKey = new byte[0];
        try {
            encryptedKey = asymmetric.encrypt(plaintextKey, publicKey.getBytes(), "RSA", "RSA/ECB/PKCS1Padding");
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        byte[] encodedEncryptedKey = utilities.encodeToBase64(encryptedKey);

        String sIV =  EncryptionHelper.randomString(16);
        byte[] iv = sIV.getBytes();
        Symmetric symmetric = new Symmetric();

        byte[] encryptedContent = symmetric.encrypt(content.getBytes(), iv, plaintextKey, "AES", "AES/CBC/PKCS5Padding");

        byte[] ivAndEncryptedContent = helper.mergeTwoByteArrays(iv, encryptedContent);

        byte[] encodedIvAndEncryptedContent = utilities.encodeToBase64(ivAndEncryptedContent);

        contentLogger.log("ENCODED_ENCRYPTED_KEY", new String(encodedEncryptedKey));
        contentLogger.log("ENCODED_ENCRYPTED_CONTENT", new String(encodedIvAndEncryptedContent));*/
    }
}