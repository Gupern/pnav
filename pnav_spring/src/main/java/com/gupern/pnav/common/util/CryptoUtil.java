package com.gupern.pnav.common.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Objects;
import java.util.Random;

/**
 * @author: Gupern
 * @date: 2022/3/5 16:54
 * @description: 密码学相关工具 some crypto tools
 * 参考链接： https://www.cnblogs.com/zfding/p/9268245.html
 */
public class CryptoUtil {
    private static Logger log = LoggerFactory.getLogger(CryptoUtil.class);

    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String HmacMD5 = "HmacMD5";
    public static final String HmacSHA1 = "HmacSHA1";
    public static final String DES = "DES";
    public static final String AES = "AES";
    // 以下内容引用微信官方加解密包，主要为了改写解密时兼容json格式
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final org.apache.commons.codec.binary.Base64 base64 = new org.apache.commons.codec.binary.Base64();

    /**
     * 编码格式，如null 或者 uft-8
     */
    public String charset;
    /**
     * DES
     */
    public int keysizeDES = 0;
    /**
     * AES
     */
    public int keysizeAES = 128;

    public static CryptoUtil me;

    private CryptoUtil(String charSet) {
        //单例
        charset = charSet;
    }

    //双重锁
    public static CryptoUtil getInstance(String charSet) {
        if (me == null) {
            synchronized (CryptoUtil.class) {
                if (me == null) {
                    me = new CryptoUtil(charSet);
                }
            }
        }
        return me;
    }

    /**
     * 使用MessageDigest进行单向加密（无密码）
     *
     * @param res       被加密的文本
     * @param algorithm 加密算法名称
     * @return
     */
    private String messageDigest(String res, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] resBytes = charset == null ? res.getBytes() : res.getBytes(charset);
            // 如果是SHA1，则返回Hex
            if (algorithm.equals(SHA1)) {
                return byteToStr(md.digest(resBytes));
            } else {
                return base64(md.digest(resBytes));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * @author: Gupern
     * @date: 2022/3/5 22:18
     * @description:  将字节数组转换为十六进制字符串
     */
    public static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /*
     * @author: Gupern
     * @date: 2022/3/5 22:21
     * @description: 将字节转换为十六进制字符串
     */
    public static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    /**
     * 使用KeyGenerator进行单向/双向加密（可设密码）
     *
     * @param res       被加密的原文
     * @param algorithm 加密使用的算法名称
     * @param key       加密使用的秘钥
     * @return
     */
    private String keyGeneratorMac(String res, String algorithm, String key) {
        try {
            SecretKey sk = null;
            if (key == null) {
                KeyGenerator kg = KeyGenerator.getInstance(algorithm);
                sk = kg.generateKey();
            } else {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                sk = new SecretKeySpec(keyBytes, algorithm);
            }
            Mac mac = Mac.getInstance(algorithm);
            mac.init(sk);
            byte[] result = mac.doFinal(res.getBytes());
            return base64(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用KeyGenerator双向加密，DES/AES，注意这里转化为字符串的时候是将2进制转为16进制格式的字符串，不是直接转，因为会出错
     *
     * @param res       加密的原文
     * @param algorithm 加密使用的算法名称
     * @param key       加密的秘钥
     * @param keysize
     * @param isEncode
     * @return
     */
    private String keyGeneratorES(String res, String algorithm, String key, int keysize, boolean isEncode) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(algorithm);
            if (keysize == 0) {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                kg.init(new SecureRandom(keyBytes));
            } else if (key == null) {
                kg.init(keysize);
            } else {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                kg.init(keysize, new SecureRandom(keyBytes));
            }
            SecretKey sk = kg.generateKey();
            SecretKeySpec sks = new SecretKeySpec(sk.getEncoded(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            if (isEncode) {
                cipher.init(Cipher.ENCRYPT_MODE, sks);
                byte[] resBytes = charset == null ? res.getBytes() : res.getBytes(charset);
                return parseByte2HexStr(cipher.doFinal(resBytes));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, sks);
                return new String(cipher.doFinal(Objects.requireNonNull(parseHexStr2Byte(res))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String base64(byte[] res) {
        return Base64.getEncoder().encodeToString(res);
    }

    /**
     * 将二进制转换成16进制
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * md5加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     * @return
     */
    public String MD5(String res) {
        return messageDigest(res, MD5);
    }

    /**
     * md5加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     * @param key 秘钥
     * @return
     */
    public String MD5(String res, String key) {
        return keyGeneratorMac(res, HmacMD5, key);
    }

    /**
     * 使用SHA1加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     * @return
     */
    public String SHA1(String res) {
        return messageDigest(res, SHA1);
    }

    /**
     * 使用SHA1加密算法进行加密（不可逆）
     *
     * @param res 需要加密的原文
     * @param key 秘钥
     * @return
     */
    public String SHA1(String res, String key) {
        return keyGeneratorMac(res, HmacSHA1, key);
    }

    /**
     * 使用DES加密算法进行加密（可逆）
     *
     * @param res 需要加密的原文
     * @param key 秘钥
     * @return
     */
    public String DESencode(String res, String key) {
        return keyGeneratorES(res, DES, key, keysizeDES, true);
    }

    /**
     * 对使用DES加密算法的密文进行解密（可逆）
     *
     * @param res 需要解密的密文
     * @param key 秘钥
     * @return
     */
    public String DESdecode(String res, String key) {
        return keyGeneratorES(res, DES, key, keysizeDES, false);
    }

    /**
     * 使用AES加密算法进行加密（可逆）
     *
     * @param res 需要加密的密文
     * @param key 秘钥
     * @return
     * @deprecated TODO 此算法在换了BASE64的包后有问题，暂时弃用，后续需修复
     */
    public String AESencode(String res, String key) {
        return keyGeneratorES(res, AES, key, keysizeAES, true);
    }

    /**
     * 对使用AES加密算法的密文进行解密
     *
     * @param res 需要解密的密文
     * @param key 秘钥
     * @return
     * @deprecated TODO 此算法在换了BASE64的包后有问题，暂时弃用，后续需修复
     */
    public String AESdecode(String res, String key) {
        return keyGeneratorES(res, AES, key, keysizeAES, false);
    }

    /**
     * 使用异或进行加密
     *
     * @param res 需要加密的密文
     * @param key 秘钥
     * @return
     */
    public String XORencode(String res, String key) {
        byte[] bs = res.getBytes();
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) ((bs[i]) ^ key.hashCode());
        }
        return parseByte2HexStr(bs);
    }

    /**
     * 使用异或进行解密
     *
     * @param res 需要解密的密文
     * @param key 秘钥
     * @return
     */
    public String XORdecode(String res, String key) {
        byte[] bs = parseHexStr2Byte(res);
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) ((bs[i]) ^ key.hashCode());
        }
        return new String(bs);
    }

    /**
     * 直接使用异或（第一调用加密，第二次调用解密）
     *
     * @param res 密文
     * @param key 秘钥
     * @return
     */
    public int XOR(int res, String key) {
        return res ^ key.hashCode();
    }

    /**
     * 使用Base64进行加密
     *
     * @param res 密文
     * @return
     */
    public String Base64Encode(String res) {
        return Base64.getEncoder().encodeToString(res.getBytes());
    }

    /**
     * 使用Base64进行解密
     *
     * @param res
     * @return
     */
    public String Base64Decode(String res) throws IOException {
        return new String(Base64.getDecoder().decode(res.getBytes()));
    }


    /**
     * 对明文进行加密.
     *
     * @param text   需要加密的明文
     * @param aesKey
     * @return 加密后base64编码的字符串
     * @throws AesException aes加密失败
     */
    public String aesEncrypt(String randomStr, String text, byte[] aesKey) throws AesException {
        ByteGroup byteCollector = new ByteGroup();
        byte[] randomStrBytes = randomStr.getBytes(CHARSET);
        byte[] textBytes = text.getBytes(CHARSET);
        byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);

        // randomStr + networkBytesOrder + text + appid
        byteCollector.addBytes(randomStrBytes);
        byteCollector.addBytes(networkBytesOrder);
        byteCollector.addBytes(textBytes);

        // ... + pad: 使用自定义的填充方式对明文进行补位填充
        byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
        byteCollector.addBytes(padBytes);

        // 获得最终的字节流, 未加密
        byte[] unencrypted = byteCollector.toBytes();

        try {
            // 设置加密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            // 加密
            byte[] encrypted = cipher.doFinal(unencrypted);

            // 使用BASE64对加密后的字符串进行编码
            String base64Encrypted = base64.encodeToString(encrypted);

            return base64Encrypted;
        } catch (Exception e) {
            e.printStackTrace();
            throw new AesException(AesException.EncryptAESError);
        }
    }

    /**
     * 对密文进行解密.
     *
     * @param text   需要解密的密文
     * @param aesKey
     * @return 解密得到的明文
     * @throws AesException aes解密失败
     */
    public String aesDecrypt(String text, byte[] aesKey) throws AesException {
        byte[] original;
        try {
            // 设置解密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

            // 使用BASE64对密文进行解码
            byte[] encrypted = org.apache.commons.codec.binary.Base64.decodeBase64(text);

            // 解密
            original = cipher.doFinal(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AesException(AesException.DecryptAESError);
        }

        String xmlContent, from_appid;
        try {
            // 去除补位字符
            byte[] bytes = PKCS7Encoder.decode(original);

            // 分离16位随机字符串,网络字节序和AppId
            byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

            int xmlLength = recoverNetworkBytesOrder(networkOrder);

            xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), CHARSET);
            from_appid = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length),
                    CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AesException(AesException.IllegalBuffer);
        }

        return xmlContent;

    }

    // 生成4个字节的网络字节序
    public static byte[] getNetworkBytesOrder(int sourceNumber) {
        byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (sourceNumber & 0xFF);
        orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
        orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
        orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
        return orderBytes;
    }

    // 还原4个字节的网络字节序
    public static int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }

    // 随机生成16位字符串
    String getRandomStr() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
