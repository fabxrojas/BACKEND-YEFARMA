package com.yefarma.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys; 
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    // Generamos una llave segura de forma automática para HS256
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generarToken(String nombreUser) {
        return Jwts.builder()
                .setSubject(nombreUser)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SECRET_KEY) 
                .compact();
    }
}