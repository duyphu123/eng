package com.smartviet.base.salary.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Checks if the current authenticated user has the specified role.
     *
     * @param role the role to check for
     * @return true if the user has the specified role, false otherwise
     */
    public static boolean checkRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().anyMatch(grantedAuthority -> Objects.equals(grantedAuthority.getAuthority(), role));
    }

    /**
     * Encrypts the specified data using RSA algorithm.
     *
     * @param data the data to encrypt
     * @return the encrypted data
     */
    public static String encrypt(String data, String publicSecretKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicSecretKey.getBytes())));
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            log.error("Error during encryption");
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Decrypts the specified encrypted data using RSA algorithm.
     *
     * @param encryptedData the encrypted data to decrypt
     * @return the decrypted data
     */
    public static String decrypt(String encryptedData, String privateSecretKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateSecretKey.getBytes())));
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | InvalidKeyException |
                 IllegalBlockSizeException | BadPaddingException e) {
            log.error("Error during decryption");
            throw new IllegalArgumentException(e);
        }
    }

    public static List<String> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

}
