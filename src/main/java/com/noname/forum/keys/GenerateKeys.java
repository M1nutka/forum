package com.noname.forum.keys;

import java.util.Base64;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class GenerateKeys {
    public static void main(String[] args) {
        SecretKey accessKey = Jwts.SIG.HS512.key().build();
        SecretKey refreshKey = Jwts.SIG.HS512.key().build();

        String accessBase64 = Base64.getEncoder().encodeToString(accessKey.getEncoded());
        String refreshBase64 = Base64.getEncoder().encodeToString(refreshKey.getEncoded());

        System.out.println("jwt.secret.access=" + accessBase64);
        System.out.println("jwt.secret.refresh=" + refreshBase64);
    }
}
