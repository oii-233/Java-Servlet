package com.example.todo.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.time.Instant;

/**
 * JWTUtil issues and verifies JWT tokens using HS256 and a secret from env.
 */
public final class JWTUtil {
    private static final String ISSUER = "todo-servlet";
    private static final Algorithm ALG = Algorithm.HMAC256(EnvUtil.getRequired("JWT_SECRET"));
    private static final JWTVerifier VERIFIER = JWT.require(ALG).withIssuer(ISSUER).build();

    private JWTUtil() {
    }

    public static String issueToken(int userId, String username) {
        Instant now = Instant.now();
        // 24h expiry
        Instant exp = now.plusSeconds(24 * 60 * 60);
        return JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(java.util.Date.from(now))
                .withExpiresAt(java.util.Date.from(exp))
                .withClaim("uid", userId)
                .withClaim("uname", username)
                .sign(ALG);
    }

    public static DecodedJWT verify(String token) {
        return VERIFIER.verify(token);
    }
}
