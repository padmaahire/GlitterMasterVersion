import com.goglitter.decryptionUtils.Asymmetric
import com.goglitter.decryptionUtils.EncryptionHelper
import com.goglitter.decryptionUtils.Symmetric
import com.goglitter.decryptionUtils.Utils
import com.goglitter.ui.Loan.EncryptedData
import javax.crypto.spec.SecretKeySpec

/*
@Author-Padma Ahire
@date-20/01/25
 */
class CRMEncryption(crmDataJson: String) {
    val publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAsIwVStQi6aSMLBZu3vhafOR5NTMNp+TXPwyk/6VoaSQfDnZaSQPYhdt4a8X215KwXwpIL1eBJOH2NW8jp5AO4WauHWEwEggJvPaC8FgzZtDhjYexOk+/yaDbY7U9BofJSU76VIBxRoN7YmAknAKrpfn0ukXPPuUx5Ny/cy85nunqo5M8Acf2VVwSGZQMBZFSm3yxYOdS4laDlM+s1w+5wLDMjYSgIMm76rpVdO3hs2n2dSAYM6XMOaqNDwHdZk6n8lPgivYVXjTz7KU9eqkFnecWvn2ugRI7hgrplZxS020k0QBeYd0AH7zJZKS3Xo5VycL01UO/WYOQvB7v8lge7TiQZ3CCrnuykqcJ/r5DMLO/cKQAeZi+LQ95FQg39joO8G7bfO7+a3Gs8Re3mRW7AA8x1aEn7XZMOUu4l4IfNvwh20V4cz3xvGXdr9ZLFvgX5593MxCDBjkiaynzG8gmLVTIoaItPy+khwO/vjfWka0L3yvT3l55R4H/KRKxlHaY58HVdLbuWrUoH/4gbkYFYFC+rejBW5wbE0FJmWIkEXLKsTlXcsn6eAzi4BRxidQ/4rIEf8qWpSFzJobivBnWe4bpBA19g3N47PDpD5xS6uj7ODSBhEn22UnsiDaGV+RhsXYA/xqaJCjB6+W7CN00Lowr87sUoT4VAK8wrOk4D5sCAwEAAQ=="
    val utilities = Utils()
    val asymmetric = Asymmetric()
    val helper = EncryptionHelper()
     val skey = com.goglitter.ui.Loan.EncryptionHelper.randomString(16)
    //val skey = "1111222233334444"
    val key = skey.toByteArray()
    val secretKey = SecretKeySpec(key, "AES")
    val plaintextKey = secretKey.encoded
    val encryptedKey = asymmetric.encrypt(plaintextKey, publicKey.toByteArray(), "RSA", "RSA/ECB/PKCS1Padding")
    val encodedEncryptedKey = utilities.encodeToBase64(encryptedKey)
    val sIV = com.goglitter.ui.Loan.EncryptionHelper.randomString(16)
    //val sIV = "1111222233334444"
    val iv = sIV.toByteArray()
    val symmetric = Symmetric()
    val encryptedContent = symmetric.encrypt(crmDataJson.toByteArray(), iv, plaintextKey, "AES", "AES/CBC/PKCS5Padding")
    val ivAndEncryptedContent = helper.mergeTwoByteArrays(iv, encryptedContent)
    val encodedIvAndEncryptedContent = utilities.encodeToBase64(ivAndEncryptedContent)

    fun getEncryptedValues(): EncryptedData {
        return EncryptedData(
            encryptedKey = String(encodedEncryptedKey),
            encryptedData = String(encodedIvAndEncryptedContent)
        )
    }
}
