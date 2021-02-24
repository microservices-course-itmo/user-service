package com.wine.to.up.user.service.configuration.firebase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Base64;

@Component
@Order(2)
public class FirebaseConfigDecryptReadStrategy implements FirebaseConfigReadStrategy {
    @Value("${firebase.encryptedConfigFileName}")
    private String path;

    @Value("${firebase.decryptPassword}")
    private String secretKey;

    @Override
    public boolean validate() {
        return !secretKey.equals("unset") && this.getClass().getClassLoader().getResourceAsStream(path) != null;
    }

    @Override
    @SuppressWarnings("ConstantConditions") // for npe inspection, because it is checked before
    public InputStream read() throws GeneralSecurityException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] key = digest.digest(secretKey.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");

        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(path);
        byte[] bytes = stream.readAllBytes();

        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(cipher.doFinal(bytes));
        return new ByteArrayInputStream(decodedBytes);
    }
}
