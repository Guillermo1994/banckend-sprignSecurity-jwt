package gramirez.demojwt.jwt;

import gramirez.demojwt.User.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    public String getToken(User user) {
        return getToken(new HashMap<>(), user);
    }

    private <K, V> String getToken(Map<String, Object> extraClaims, User user) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .claim("userId", user.getId())
                .claim("firstName", user.getFirstname())
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);

        return username.equals(userDetails.getUsername()) && !isTokenExpirrado(token);
    }

    private Claims getAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
            final Claims claims = getAllClaims(token);
            return claimsResolver.apply(claims);
    }

    private Date getExpiracion(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpirrado(String token) {
        return getExpiracion(token).before(new Date());
    }
}