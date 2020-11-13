package com.lzy.safecheck.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignCheckUtil {
    private Context context;
    private String cer = null;
    private String realCer = null;
    private static final String TAG = "SignCheck";

    public SignCheckUtil(Context context) {
        this.context = context;
        this.cer = getCertificateSHA1Fingerprint();
    }

    public SignCheckUtil(Context context, String realCer) {
        this.context = context;
        this.realCer = realCer;
        this.cer = getCertificateSHA1Fingerprint();
    }


    public String getRealCer() {
        return realCer;
    }

    /**
     * 设置正确的签名
     *
     * @param realCer
     */
    public void setRealCer(String realCer) {
        this.realCer = realCer;
    }

    //当前应用的签名sha1
    public String getCer() {
        return cer;
    }

    /**
     * 获取应用的签名
     *
     * @return
     */
    public String getCertificateSHA1Fingerprint() {
        return getAppSignatureSHA1();
    }

    /**
     * 检测签名是否正确
     *
     * @return true 签名正常 false 签名不正常
     */
    public boolean check() {
        if (!TextUtils.isEmpty(realCer)) {
            realCer = realCer.trim();
            cer = cer.trim();
            if (this.realCer.equals(this.cer)) {
                return true;
            }
        } else {
            Utils.log("未给定正版的签名 SHA-1 值");
        }
        return false;
    }

    public  String getAppSignatureSHA1() {
        return getAppSignatureSHA1(context.getPackageName());
    }

    /**
     * Return the application's signature for SHA1 value.
     *
     * @param packageName The name of the package.
     * @return the application's signature for SHA1 value
     */
    public  String getAppSignatureSHA1(final String packageName) {
        return getAppSignatureHash(packageName, "SHA1");
    }

    private  String getAppSignatureHash(final String packageName, final String algorithm) {
        if (TextUtils.isEmpty(packageName)) return "";
        Signature[] signature = getAppSignature(packageName);
        if (signature == null || signature.length <= 0) return "";
        return bytes2HexString(hashTemplate(signature[0].toByteArray(), algorithm))
                .replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
    }

    public  Signature[] getAppSignature(final String packageName) {
        if (TextUtils.isEmpty(packageName)) return null;
        try {
            PackageManager pm =context .getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return pi == null ? null : pi.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return "";
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = HEX_DIGITS[bytes[i] >> 4 & 0x0f];
            ret[j++] = HEX_DIGITS[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    private static byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length <= 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
