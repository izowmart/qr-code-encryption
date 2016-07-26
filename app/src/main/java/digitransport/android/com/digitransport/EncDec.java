package digitransport.android.com.digitransport;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.content.Context;
import android.util.Base64;

/**
 * Created by Srikant Bhushan on 04-04-2016.
 */
public class EncDec {
    private  Context context;
    public static String CLASS_VALUE;

  public EncDec(Context context){
      this.context=context;
  }
    public  String encDec(String input, String password) {

        byte[] in = input.getBytes();
        byte[] key = password.getBytes();
        byte[] result = new byte[in.length];
        int k = 0;
        for (int i = 0; i < in.length; i++) {
            result[i] = (byte)(in[i] ^ key[k]);
            k++;
            if (k == key.length)
                k=0;
        }

        return new String(result);
    }


    public   PublicKey readPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        byte[] keyBytes=null;
        String pubKey = "MEwwDQYJKoZIhvcNAQEBBQADOwAwOAIxAthOnE+ypkBGfT+v6aAA5igH1nthwoac\n" +
                "wDhbWzxRe0Rd/3vI3SsesLGpNq+2uKdutQIDAQAB";
        keyBytes = Base64.decode(pubKey.getBytes("utf-8"), Base64.DEFAULT);
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(publicSpec);
    }

    public  byte[] decryptWithPublic(PublicKey key, byte[] ciphertext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
        byte[] fullDecryptedData = null;
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        ciphertext = Base64.decode(ciphertext, Base64.NO_PADDING | Base64.NO_WRAP);

        for(int index = 0; index < (ciphertext.length / 49) ; index++)
        {
            byte [] buffer = new byte[49];
            for(int index2 = 0; index2 < 49; index2++)
            {
                buffer[index2] = ciphertext[(index * 49) + index2];
            }
            byte [] decryptedData = cipher.doFinal(buffer);
            int fullDecryptedDataLength = 0;
            if(null != fullDecryptedData)
            {
                fullDecryptedDataLength = fullDecryptedData.length;
            }
            byte [] tempDecrypted = new byte[fullDecryptedDataLength + decryptedData.length];
            int index2 = 0;
            for(index2 = 0; index2 < fullDecryptedDataLength; index2++)
            {
                tempDecrypted[index2] = fullDecryptedData[index2];
            }

            for(int index3 = index2; index3 < (fullDecryptedDataLength + decryptedData.length); index3++)
            {
                tempDecrypted[index3] = decryptedData[index3 - index2];
            }

            fullDecryptedData = tempDecrypted;
        }


        return fullDecryptedData;
    }

}
