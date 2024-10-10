package com.goglitter.ui.Loan
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import java.io.File
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAPublicKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

fun generateKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048) // You can adjust the key size as needed
        return keyPairGenerator.generateKeyPair()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encryptWithRSA(data: String, publicKey: ByteArray): String {

        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
       // val keyBytes = java.util.Base64.getDecoder().decode(publicKey.replace(Regex("[\\r\\n]"), ""))
        val keySpec = X509EncodedKeySpec(publicKey)

        val keyFactory = KeyFactory.getInstance("RSA")
        val key = keyFactory.generatePublic(keySpec)

        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedBytes = cipher.doFinal(data.toByteArray())

        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }
fun loadPublicKey(publicKeyString: String): PublicKey {
    try {
        // Remove newline characters and whitespaces
        val cleanedKeyString = publicKeyString.replace(Regex("[\\r\\n\\s]"), "")

        // Check if the length is a multiple of 4 and add padding if necessary
        val padding = cleanedKeyString.length % 4
        val paddedKeyString = if (padding > 0) cleanedKeyString + "=".repeat(4 - padding) else cleanedKeyString

        // Decode the Base64-encoded key using android.util.Base64
        val keyBytes = Base64.decode(paddedKeyString, Base64.DEFAULT)

        // Create the public key
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    } catch (e: Exception) {
        throw RuntimeException("Error loading public key: ${e.message}", e)
    }
}
fun extractModulusAndExponent(publicKeyString: String): Pair<String, String>? {
    try {
        // Remove header and footer lines, and clean up whitespace
        val cleanedKeyString = publicKeyString
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace(Regex("[\\r\\n\\s]"), "")

        // Decode the Base64-encoded public key
        val keyBytes = Base64.decode(cleanedKeyString, Base64.DEFAULT)

        // Create a public key from the key bytes
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKeySpec = X509EncodedKeySpec(keyBytes)
        val publicKey: PublicKey = keyFactory.generatePublic(publicKeySpec)

        // Extract modulus and exponent
        val rsaPublicKey = publicKey as java.security.interfaces.RSAPublicKey
        val modulus = rsaPublicKey.modulus.toString(16)
        val exponent = rsaPublicKey.publicExponent.toString(16)

        return Pair(modulus, exponent)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}
fun loadRSAPublicKey(modulus: String, exponent: String): PublicKey {
   /* val modulusBytes = Base64.getDecoder().decode(modulus)
    val exponentBytes = Base64.getDecoder().decode(exponent)*/
    val modulusBytes = Base64.decode(modulus, Base64.DEFAULT)
    val exponentBytes = Base64.decode(exponent, Base64.DEFAULT)

    val keyFactory = KeyFactory.getInstance("RSA")
    // Convert hexadecimal strings to BigIntegers
    val modulusBigInteger = BigInteger(modulusBytes.toString(), 16)
    val exponentBigInteger = BigInteger(exponentBytes.toString(), 16)
    val publicKeySpec = RSAPublicKeySpec(modulusBigInteger, exponentBigInteger)

    return keyFactory.generatePublic(publicKeySpec)
}
fun createRSAPublicKeyFromPEM(filePath: String): PublicKey {
    val fileContent = File(filePath).readText(StandardCharsets.UTF_8)

    // Remove PEM header and footer if present
    val cleanedKeyString = fileContent
        .replace("-----BEGIN PUBLIC KEY-----", "")
        .replace("-----END PUBLIC KEY-----", "")
        .replace(Regex("[\\r\\n\\s]"), "")

    // Decode the Base64-encoded public key
   // val keyBytes = Base64.getDecoder().decode(cleanedKeyString)
    val keyBytes = Base64.decode(cleanedKeyString, Base64.DEFAULT)

    // Parse the key bytes to get modulus and exponent
    val keyFactory = KeyFactory.getInstance("RSA")
    val publicKeySpec = PKCS8EncodedKeySpec(keyBytes)
    val publicKey: PublicKey = keyFactory.generatePublic(publicKeySpec)

    return publicKey
}

fun getFirst16Bytes(base64EncodedData: String): ByteArray {
    // Decode the Base64-encoded string
    val decodedBytes = Base64.decode(base64EncodedData, Base64.DEFAULT)

    // Extract the first 16 bytes
    val first16Bytes = decodedBytes.copyOfRange(0, 16)

    return first16Bytes
}
fun extractIVFromEncryptedData(encryptedData: ByteArray): ByteArray {
    // Assuming the first 16 bytes of the encrypted data represent the IV
    return encryptedData.copyOfRange(0, 16)
}


/*fun decryptAESCBC(encryptedData: String, key: String): String {
    try {
      //  val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC")

        val iv = getIVFromEncryptedData(encryptedData)

        if (iv != null) {
            // Print the IV bytes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                println("NEWIV: ${java.util.Base64.getEncoder().encodeToString(iv)}")
            }
        } else {
            // Handle the error or provide feedback
            println("Failed to get the IV.")
        }

        val keyBytes = Base64.decode(key, Base64.DEFAULT)
        val ivBytes = Base64.decode(iv, Base64.DEFAULT)

        // Create SecretKeySpec and IvParameterSpec
        val secretKeySpec = SecretKeySpec(keyBytes, "AES")
        val ivParameterSpec = IvParameterSpec(ivBytes)

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)


        // Decode Base64 and decrypt
       // val encryptedBytes = java.util.Base64.getDecoder().decode(encryptedData)
        val encryptedBytes =  Base64.decode(encryptedData, Base64.DEFAULT)

        val decryptedBytes = cipher.doFinal(encryptedBytes)

        return String(decryptedBytes, Charsets.UTF_8)
    } catch (e: Exception) {
        // Handle exceptions, e.g., InvalidKeyException, BadPaddingException, IllegalBlockSizeException
        e.printStackTrace()
        return "Decryption failed"
    }
}*/

fun getIVFromEncryptedData(encryptedData: String): ByteArray? {
    try {
        // Decode the Base64-encoded encrypted data
        val decodedBytes =org.bouncycastle.util.encoders.Base64.decode(encryptedData)

        // Ensure that the decoded bytes contain at least 16 bytes
        if (decodedBytes.size < 16) {
            println("Error: Decoded data does not contain a valid IV (less than 16 bytes).")
            return null
        }

        // Get the IV (first 16 bytes)
        return decodedBytes.copyOfRange(0, 16)
    } catch (e: IllegalArgumentException) {
        println("Error: Illegal base64 character. Check the input string.")
        return null
    }
}


