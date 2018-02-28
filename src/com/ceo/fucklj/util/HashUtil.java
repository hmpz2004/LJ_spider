package com.ceo.fucklj.util;

import com.ceo.fucklj.Env;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 封装的计算 MD5, SHA1 等的工具
 *
 * @author zhangxu
 *
 */
public class HashUtil {

    private static final String TAG = "HashUtil";

    public static final String HASH_MD5 = "MD5";

    public static final String HASH_SHA1 = "SHA1";

    public static byte[] getHash(String hashName, byte[] buf) {
        MessageDigest m;
        try {
            m = MessageDigest.getInstance(hashName);
            m.update(buf);
            return m.digest();
        } catch (Exception e) {
            if (Env.DEBUG) {
                Log.e(TAG, "", e);
            }
        }

        return null;
    }

    public static byte[] getFileHash(String hashName, File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            return getInputStreamHash(hashName, fis);
        } catch (Exception e) {
            if (Env.DEBUG) {
                Log.e(TAG, "", e);
            }
        }
        return null;
    }

    /** 计算 InputStream 的 Hash，并完成之后关闭 InputStream */
    public static byte[] getInputStreamHash(String hashName, InputStream is) {
        byte[] buffer = new byte[1024];
        MessageDigest m;
        try {
            m = MessageDigest.getInstance(hashName);

            int numRead;

            do {
                numRead = is.read(buffer);
                if (numRead > 0) {
                    m.update(buffer, 0, numRead);
                }
            } while (numRead != -1);

            return m.digest();
        } catch (Exception e) {
            if (Env.DEBUG) {
                Log.e(TAG, "", e);
            }
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                //ignore
            }
        }

        return null;
    }
}
