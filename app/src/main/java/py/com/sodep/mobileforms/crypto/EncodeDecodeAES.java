package py.com.sodep.mobileforms.crypto;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import py.com.sodep.mobileforms.dataservices.sql.SQLProjectsDAO;

public class EncodeDecodeAES {

    private static final String LOG_TAG = SQLProjectsDAO.class.getSimpleName();
    private static char[] HEX_CHARS = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    private static final String PREF_KEY_BYTES = "Key_Bytes";
    private static final String PREF_IV_BYTES = "IV_Bytes";

    private static IvParameterSpec ivspec;
    private static SecretKeySpec keyspec;
    private static Cipher cipher;

    private EncodeDecodeAES()
    {
        /*Constructor privado*/
    }

    private static void init(Context context) {
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
            keyspec = new SecretKeySpec(generateKeyBytes(context), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keyspec);
            ivspec = new IvParameterSpec(generateIVBytes(context));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static byte[] generateKeyBytes(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String savedKeyBytes = sharedPreferences.getString(PREF_KEY_BYTES, null);

        if (savedKeyBytes != null) {
            return Base64.getDecoder().decode(savedKeyBytes);
        } else {
            byte[] newKeyBytes = new byte[16];
            new Random().nextBytes(newKeyBytes);

            String byteToString = Base64.getEncoder().encodeToString(newKeyBytes);
            sharedPreferences.edit().putString(PREF_KEY_BYTES, byteToString).apply();
            return newKeyBytes;
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static byte[] generateIVBytes(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String savedIVBytes = sharedPreferences.getString(PREF_IV_BYTES, null);

        if (savedIVBytes != null) {
            return Base64.getDecoder().decode(savedIVBytes);
        } else {
            byte[] newIVBytes = cipher.getIV();

            String byteToString = Base64.getEncoder().encodeToString(newIVBytes);
            sharedPreferences.edit().putString(PREF_IV_BYTES, byteToString).apply();
            return newIVBytes;
        }
    }

    public static byte[] encrypt(Context context, String text) throws Exception
    {
        if(text == null || text.length() == 0)
            throw new Exception("Empty string");

        if (cipher == null )
            init(context);

        byte[] encrypted;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

            encrypted = cipher.doFinal(padString(text).getBytes());
        } catch (Exception e)
        {
            throw new Exception("[encrypt] " + e.getMessage());
        }

        return encrypted;
    }

    public static byte[] decrypt(Context context, String code) throws Exception
    {
        if(code == null || code.length() == 0)
            throw new Exception("Empty string");

        if (cipher == null )
            init(context);

        byte[] decrypted;

        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            decrypted = cipher.doFinal(hexToBytes(code));
            //Remove trailing zeroes
            if( decrypted.length > 0)
            {
                int trim = 0;
                for( int i = decrypted.length - 1; i >= 0; i-- ) if( decrypted[i] == 0 ) trim++;

                if( trim > 0 )
                {
                    byte[] newArray = new byte[decrypted.length - trim];
                    System.arraycopy(decrypted, 0, newArray, 0, decrypted.length - trim);
                    decrypted = newArray;
                }
            }
        } catch (Exception e)
        {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }

    public static String bytesToHex(byte[] buf)
    {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i)
        {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }

    private static byte[] hexToBytes(String str) {
        if (str==null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i=0; i<len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
            }
            return buffer;
        }
    }

    private static String padString(String source)
    {
        char paddingChar = 0;
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        StringBuilder sourceBuilder = new StringBuilder(source);
        for (int i = 0; i < padLength; i++)
        {
            sourceBuilder.append(paddingChar);
        }
        source = sourceBuilder.toString();

        return source;
    }

}
