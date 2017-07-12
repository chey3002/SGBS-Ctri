package encriptacion;

import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * This example program shows how AES encryption and decryption can be done in Java.
 * Please note that secret key and encrypted text is unreadable binary and hence 
 * in the following program we display it in hexadecimal format of the underlying bytes.
 * @author Jayson
 */
public class AESEncryption {
 
    /**
     * 1. Generate a plain text for encryption
     * 2. Get a secret key (printed in hexadecimal form). In actual use this must 
     * by encrypted and kept safe. The same key is required for decryption.
     * 3. 
     */
 //   public static void main(String[] args) throws Exception {
 //       String plainText = "Hello World";
 //       SecretKey secKey = getSecretEncryptionKey();
 //       byte[] cipherText = encryptText(plainText, secKey);
 //       String decryptedText = decryptText(cipherText, secKey);
        
 //       System.out.println("Original Text:" + plainText);
 //       System.out.println("AES Key (Hex Form):"+bytesToHex(secKey.getEncoded()));
 //       System.out.println("Encrypted Text (Hex Form):"+bytesToHex(cipherText));
 //       System.out.println("Descrypted Text:"+decryptedText);
        
 //   }
    
    /**
     * gets the AES encryption key. In your actual programs, this should be safely
     * stored.
     * @return
     * @throws Exception 
     */
    public  SecretKey getSecretEncryptionKey() throws Exception{
        SecretKey secKey;
        byte[] key={-116, -15, 3, 91, -122, 96, -94, 102, -54, -66, 8, -62, 98, -101, -85, -39};
        secKey = new SecretKeySpec(key, "AES");
        return secKey;
    }
    
    /**
     * Encrypts plainText in AES using the secret key
     * @param plainText
     * @param secKey
     * @return
     * @throws Exception 
     */
    public  byte[] encryptText(String plainText,SecretKey secKey) throws Exception{
		// AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return byteCipherText;
    }
    
    /**
     * Decrypts encrypted byte array using the key used for encryption.
     * @param byteCipherText
     * @param secKey
     * @return
     * @throws Exception 
     */
    public  String decryptText(String hex, SecretKey secKey) throws Exception {
		// AES defaults to AES/ECB/PKCS5Padding in Java 7
        int len = hex.length();
          byte[] byteCipherText = new byte[len / 2];
         for (int i = 0; i < len; i += 2) {
        byteCipherText[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                             + Character.digit(hex.charAt(i+1), 16));
    }
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }
    
    /**
     * Convert a binary byte array into readable hex form
     * @param hash
     * @return 
     */
    public  String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }
}