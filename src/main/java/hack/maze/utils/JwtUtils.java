package hack.maze.utils;

import hack.maze.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import static hack.maze.constant.ApplicationConstant.APP;
import static hack.maze.constant.ApplicationConstant.APP_ADMINISTRATION;
import static hack.maze.constant.SecurityConstant.EXPIRATION_PERIOD;

@Component
public class JwtUtils {


    @Value("${jwt.secret.key}")
    private String SECRET_KEY;


    public String generateToken(Map<String, Object> claims, AppUser appUser) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuer(APP)
                .setAudience(APP_ADMINISTRATION)
                .setSubject(appUser.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_PERIOD))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, AppUser appUser) {
        final long id = (long) (int) extractClaims(token).get("userId");
        return (id == appUser.getId()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

}
