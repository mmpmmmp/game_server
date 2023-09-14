package com.wre.game.api.util;

import com.wre.game.api.entity.KSRechargeParam;
import com.wre.game.api.message.ProductSDKMessage;
import org.apache.commons.codec.binary.Base64;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;


/**
 *  * 数字签名
 *  * 1：SHA512withRSA，：将正文通过SHA512数字摘要后，将密文 再次通过生成的RSA密钥加密，生成数字签名，
 *  * 将明文与密文以及公钥发送给对方，对方拿到私钥/公钥对数字签名进行解密，然后解密后的，与明文经过MD5加密进行比较
 *  * 如果一致则通过
 *  * 2：使用Signature的API来实现SHA512withRSA
 *  *
 *  
 */
public class SHA512withRSA {
 
 
    /**
     * 使用RSA生成一对钥匙
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        //生成返回带有公钥和私钥的对象
        KeyPair generateKeyPair = keyPairGenerator.generateKeyPair();
        return generateKeyPair;
    }
 
    /**
     * 生成私钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @return 
     */
    public static PrivateKey getPrivateKey(KeyPair key) {
        PrivateKey generatePrivate = null;
        try {
            PrivateKey private1 = key.getPrivate();
            byte[] encoded = private1.getEncoded();
            byte[] bytes = Base64.encodeBase64(encoded);
            String string = new String(bytes, "UTF-8");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            generatePrivate = factory.generatePrivate(keySpec);
        } catch (Exception e) {
// TODO: handle exception
        }
        return generatePrivate;
    }

    /**
     * 生成私钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) {
        PrivateKey generatePrivate = null;
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(RasKeyPairGenerator.decryptBASE64(privateKey));

            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            return priKey;
        } catch (Exception e) {
// TODO: handle exception
        }
        return generatePrivate;
    }
 
    /**
     * 私钥加密
     *
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     */
    public static byte[] encrtpyByPrivateKey(byte[] bb, PrivateKey key) throws IllegalBlockSizeException, BadPaddingException {
        byte[] doFinal = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            doFinal = cipher.doFinal(bb);
        } catch (Exception e) {
// TODO: handle exception
        }
        return doFinal;
    }
 
    /**
     * 获取公钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @return 
     */
    public static PublicKey getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = null;
        try {
            PublicKey public1 = keyPair.getPublic();
            byte[] encoded = public1.getEncoded();
            byte[] bytes = Base64.encodeBase64(encoded);
            String string = new String(bytes, "UTF-8");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            publicKey = factory.generatePublic(keySpec);
        } catch (Exception e) {
        // TODO: handle exception
        }
        return publicKey;
    }



    /**
     * 获取公钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @return
     */
    public static PublicKey getPublicKey(String privateKey) {
        PublicKey publicKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = RasKeyPairGenerator.decryptBASE64(privateKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            return pubKey;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return publicKey;
    }
 
    /**
     * 使用公钥解密
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @return 
     */
    public static byte[] decodePublicKey(byte[] b, PublicKey key) {
        byte[] doFinal = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            doFinal = cipher.doFinal(b);
        } catch (Exception e) {
// TODO: handle exception
        }
        return doFinal;
    }
 
    //通过SHA1加密
    public static byte[] encryptMD5(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA3-512");
        byte[] digest2 = digest.digest(str.getBytes());
        return digest2;
    }
 
    //sign签名
    public static byte[] sign(String str, PrivateKey key) throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptMD5 = encryptMD5(str);
        byte[] encrtpyByPrivateKey = encrtpyByPrivateKey(encryptMD5, key);
        return encrtpyByPrivateKey;
    }
 
    //校验
    public static boolean verify(String str, byte[] sign, PublicKey key) throws NoSuchAlgorithmException {
        byte[] encryptMD5 = encryptMD5(str);
        byte[] decodePublicKey = decodePublicKey(sign, key);
        String a = new String(encryptMD5);
        String b = new String(decodePublicKey);
        if (a.equals(b)) {
            return true;
        } else {
            return false;
        }
    }
 
    /**
     * Signature的用法
     * 数字签名
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException 
     * @throws SignatureException 
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] signMethod(String str, PrivateKey key) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    //初始化 MD5withRSA
        Signature signature = Signature.getInstance("SHA512withRSA");
    //使用私钥
        signature.initSign(key);
    //需要签名或校验的数据
        signature.update(str.getBytes());
        return signature.sign();//进行数字签名
    }


    /**
     * Signature的用法
     * 数字签名
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String signMethodToString(String str, PrivateKey key) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //初始化 MD5withRSA
        Signature signature = Signature.getInstance("SHA512withRSA");
        //使用私钥
        signature.initSign(key);
        //需要签名或校验的数据
        signature.update(str.getBytes());

        return com.wre.game.api.util.Base64.encode(signature.sign());
    }



    /**
     * Signature的用法
     * 数字签名
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String signMethodToString(String str, String privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //获取私钥
        PrivateKey key = getPrivateKey(privateKey);
        //初始化 MD5withRSA
        Signature signature = Signature.getInstance("SHA512withRSA");
        //使用私钥
        signature.initSign(key);
        //需要签名或校验的数据
        signature.update(str.getBytes());

        return com.wre.game.api.util.Base64.encode(signature.sign());
    }
    /**
     * 数字校验
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException 
     * @throws SignatureException 
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static boolean verifyMethod(String str, byte[] sign, PublicKey key) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA512withRSA");
        signature.initVerify(key);
        signature.update(str.getBytes());
        return signature.verify(sign);
    }


    /**
     * 数字校验
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static boolean verifyMethodToString(String str, String sign, PublicKey key) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA512withRSA");
        signature.initVerify(key);
        signature.update(str.getBytes());
        byte[] signByte= com.wre.game.api.util.Base64.decode(sign);
        return signature.verify(signByte);
    }


    /**
     * 数字校验
     *
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static boolean verifyMethodToString(String str, String sign, String key) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        //获取公钥
        PublicKey publicKey = getPublicKey(key);

        Signature signature = Signature.getInstance("SHA512withRSA");
        signature.initVerify(publicKey);
        signature.update(str.getBytes());
        byte[] signByte= com.wre.game.api.util.Base64.decode(sign);
        return signature.verify(signByte);
    }
 
    public static void main(String[] args) throws NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, SignatureException {
        //获取钥匙对
        KeyPair keyPair = getKeyPair();
        //获取公钥
        PublicKey publicKey = getPublicKey(ProductSDKMessage.AssemblyLine.publicKey);
        //获取私钥
        PrivateKey privateKey = getPrivateKey(ProductSDKMessage.AssemblyLine.appPublicKey);
 
        /********************基于SignatureAPI签名*************************************/
        KSRechargeParam body=new KSRechargeParam();

        String signStr ="aa";
       System.out.println(SHA512withRSA.signMethodToString(signStr,ProductSDKMessage.AssemblyLine.appPublicKey));
        String signMethod = "aa";

        System.out.println( signMethod );
        boolean verifyMethod = verifyMethodToString(signStr, signMethod, publicKey);
        System.out.println("使用SignatureAPI 数字签名是否一致：" + verifyMethod);
    }
}