package com.cornelisdemooij.springsecuritylesson.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    // IRL this shouldn't be hardcoded but set securely at deploy time:
    private String SECRET_KEY = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIJRAIBADANBgkqhkiG9w0BAQEFAASCCS4wggkqAgEAAoICAQC6t8JEI/trqVFH\n" +
            "NDf3F4JgO5d5Za5W1NP/PsHPak5YdN6pAAndGAZX/th9vv8QtIOuKL5xM6Ot8cO7\n" +
            "h6c1dEtgzvXUZTIvt0UEFBHHoBRgb1KH0Q5pSXeKNaccjO1a6tOk86r0hcYAncvP\n" +
            "iv4B+cUqv2s9KmiVirkNcbg/zrHuz0BTZFhx1a4+EFsjU7gwURp9TUe54fMVqVmH\n" +
            "dxpGHa4nW0fBbhn8jOcwevFOAmrzn7fkY3/Mm1l5v6O79ulP0NV7GCeSzjUxgVcB\n" +
            "8m1UvYRcc0sa6AZ4osHpxOFXc5UwvKgmNI/HXDD+cePXQDF7oZ7gdgOimbYjGGNa\n" +
            "UJskErFQI1RGl23QFkRoMMkVeMtB33KuiMPhqHl3EjDL6g+InXb5yIOa4L/CqG1/\n" +
            "050p4CoteixZUIHAqFeqwDmNz5ohhZfjqFFE80aeRvffBBtqbfBjNe/WiWanL3lt\n" +
            "ddLDU4rSsvGYGc1VYglVEsBScTRWCXK13cIULu2unApQC7nnBto2Cx+dV1TjGaU0\n" +
            "Qr8xVdIJ62HQU1uA02+ieFnT7xOLz/JJfP3PYne6TXtQbL9g+NtHTDQyCdPs5ukq\n" +
            "GL7jKxgYmB6TLLBQCbZqsRj4FbRHaNj0ZeyFgNG04CmyRwPvYMzx3gn82Y+fIpVV\n" +
            "CUb9QzDkbiJdBo9BATujd/JwMUhKEwIDAQABAoICAH6/jxhCdMG4xnvXbsZFJzO8\n" +
            "QC8gXyCCiQaTy2zJdTWG4D9Iz2lOjb/eN4fkx7DHWTw4XE7sBwhlPcFnbJJUx3aI\n" +
            "B0iPCWpqtmb2WLtyZ2KvLw8TUv0V6hfgI/i1kcpN7i3jWwFybd6tP1U7vQIKU1WZ\n" +
            "a2qyBxy8drIxqCloXlEGT1xMmd1seEOZRsTMaBQkeh7k/vOn9iOWOjhDGYrPSxFc\n" +
            "XXFpFCrkW2wFiRize3a1zVc0U+TI8Aw9kyBdlDOiR8ivtO1rHaDlc/LWZnFH1ToM\n" +
            "6cbyCrKJiajvBSkgRP44ksyckZ17b6bKGShdYznRlNA6TG1ser+IJqmAXlA2gDYF\n" +
            "b+o8FjV8mKd5vNBM1V2kkxMsSSF31DX99KdQqXL1m9W/EYH/qrBbZ+j8m8fMQeFp\n" +
            "7p28NjUbO4yGxhlZAw0APfv1iWasq6yfq9SnT5S6HwFOqupxbfSPhXP1auvG1SP6\n" +
            "ABHZ0SCfh1PcLOkHbNDxNaqkwzYEuZIdsuUnEmGZGy9v0VPQsYkVYzB7GKeMVngo\n" +
            "tEEmk7GvhSDOvh1KN44pgJW0DZsZxriN903ZcHDU9Lo2hHniskbfbSKMS6nauYMa\n" +
            "Vf7y7SX5hTZMSGEKTZj7IQE4VIwpmOgty4cWzcZQge0sr7gLGt2AgRAJjUu3tSbw\n" +
            "NW8OmwzbObNrnPCA3+fBAoIBAQDcALeEBUAIGOnpOSPCwPofvdiI6XjCZRLvSbtQ\n" +
            "2K6cfsvrBhSothmfQ08mbrAwGtcw+OkytdBPUSjxpLpxs4yu6Z6tw6imP2BJzXaU\n" +
            "B8wqMCF5G0uJe+eZSC9oanPy8Clqb6GejHNLxWQoM3irtUELD/Zsin1UwnGW6W28\n" +
            "/1IAk4IaG/TMxksIKQ511M+JpdZKkphp4Qau5DY3wbx+/Epst9SSJLdPuRC9R8e5\n" +
            "SfP2hChx+qDZAftXvfukThdyN4tGnUbKRxh1z+TYpBuQvvnGL5U6LLsDOfaMcpLl\n" +
            "SozoozH44VzKvT1lLBGml1jZXS0md4q0jnnLwGd4u4EwFuKnAoIBAQDZRNRhnoGt\n" +
            "I5kO3mA7O4X6ExIf6iqncIT+Jsr2kZDEiFdTb52ZDWOUgsgyhGWpjzB4KAXxlJjr\n" +
            "Ug07twdkLqMuunHL45RZE28Wwij70G4aNMBY0XPF6SYTKlz4pvToocfPLrWnSqyu\n" +
            "x31BpM4NvNALd7exxTj8yhyGCxfVtK+RCzkC000DIzr98QrhQaYhcNBV7mtc3Clh\n" +
            "CpG/nE22CTZs21prrj2MlrDPUOHdxkvJdIqlwK1eaq2l4wiPVdR6Y3ETrISKqv7q\n" +
            "nKNMnDDLNx0/oQ96ET8zIArj96Ko6YH3lhVAdrgeLkORUWn+ZDTvI5ghKC3Etbir\n" +
            "+8WEwnzZyOa1AoIBAQCrZCOTxkNMC5yvBMTHRG+Xqt62Sqy5ROSrZlV3dlcRs/ZI\n" +
            "8kPF65eqa1WOK003LLHYhRGnX90Y7bFoD1zwl2BSI85qsLMCUP1Cgb6PNEPp2fU5\n" +
            "0/nU8aJL1+CXB9esu0yIjs7qJaDstqqAZxjrLBmv9I+Vn45kKdc0KkZDpuTLbh89\n" +
            "F+puXUy23sVVJqVIJtn63pm+YoBCX6edaGDgzMtuRwhjkhb9FL/ltbXys5pLwkmA\n" +
            "AjCs3s5gXLFeHzhE0uC98mjjzN9RaNRvsemHqkAMFaW73H0U1mMsp1WpK5r64ahc\n" +
            "q7UOfL8Hwn09u0qMi/pQrXBxmU1pXks901WhkTKhAoIBAQDAcqk0drQian+unebb\n" +
            "UJQRuEa7oi159r62aCIrWTDEBIhlTV5pLWThLR6rMH7kPAMQy1LIc3koJ5u1IyAW\n" +
            "6wB76YXWtZS3efFSVVT8B9keuU8O3SgRS70ACOwlC62vSW205Ijez9Q5VFZsiNBD\n" +
            "dtVI8xfxDOK3d0bgrH7GOq+waCJtkECM4O3l9VZPxFilEkpXc0ICFLlKTZRgBtEG\n" +
            "xw5ASePDsaivGOMwH1KRoI2Muf/9ZberBA8f+wOgAsScS+zjIlKciW4W3fv/ZfIK\n" +
            "bp0nuT/+PbyfSmibGMQpnfwTFAtq+D2J7izHUTfzr0r776lW5UH1FYmoPPDt8V+p\n" +
            "gLxpAoIBAQChlL1EVQL9kAgWe6OkunHOKuGNXvkQBrw/AP5d5FTKJQOHHrlkOlvH\n" +
            "PxXRMxZV7aN2GJQ8gvAzzSerwmD9zKiJrBID2oJumEY5QtPcFmoRTBzZo+y2gO4m\n" +
            "oVP3p/erRg7Ko/Xp/88GQYr4aZpb3YqaBUYeFd/WEDbwSGLBIEVQ/kU9ufAQl2fv\n" +
            "ud/fTZuNj5CfLWpPVEcM040JnsPusHtw1fvOZ3Ym6ngDkyC/8+iHBemjMrnHUgP0\n" +
            "QIugJ0xMb+abn34yeo8OC4iI6YDE/k+0ZqYJcWn6W+vLAxi9bBII2EFkps74BEmM\n" +
            "QxFDo/o4ZwqK5+bLc+vHy5pWnEAj0WjN\n" +
            "-----END PRIVATE KEY-----\n";
    private PrivateKey keyStringToPrivateKeyObject(String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        keyString = keyString
                .replaceAll("\\n", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "");
        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyString));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpecPKCS8);
    }
    private PrivateKey privateKey;
    {
        try {
            privateKey = keyStringToPrivateKeyObject(SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.RS512, privateKey).compact();
    }

    private Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
