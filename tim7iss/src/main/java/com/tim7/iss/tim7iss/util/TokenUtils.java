package com.tim7.iss.tim7iss.util;

import com.tim7.iss.tim7iss.models.Role;
import com.tim7.iss.tim7iss.models.User;
import com.tim7.iss.tim7iss.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenUtils {

    private static final String AUDIENCE_WEB = "web";
    private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    @Value("secret")
    public String SECRET;
    @Value("spring-security-example")
    private String APP_NAME;

    // 30min
    @Value("1800000")
    private int EXPIRES_IN;
    @Value("Authorization")
    private String AUTH_HEADER;

    @Autowired
    private UserRepository userRepository;

    // Metoda vraca informacije o uredjaju za koji je namenjen token
    private String generateAudience() {
        return AUDIENCE_WEB;
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + EXPIRES_IN);
    }

    // Validaciona metoda
    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }


    // Metoda vraca kredencijale tokena(atribute)
    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            claims = null;
        }
        return claims;
        // Preuzimanje proizvoljnih podataka je moguce pozivom funkcije claims.get(key)
    }

    public int getExpiredIn() {
        return EXPIRES_IN;
    }

    // Generisemo token
    public String generateToken(String email) {
        User user = userRepository.findByEmailAddress(email);
        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(email)
                .setAudience(generateAudience())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .claim("roles", user.getRoles().stream().map(Role::getAuthority).toList())
                .claim("id", user.getId())
                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();

        // moguce je postavljanje proizvoljnih podataka u telo JWT tokena.claim("key", value), npr. .claim("role", user.getRole())
    }

    // Metoda vraca sadrzaj auth hedera u kom se nalazi JWT token sa Brave
    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }


    // Metoda vraca samo token bez Barer
    // asdas13sdf3xadxSDAsdafsfdsf3216
    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }


    public String getEmailFromToken(String token) {
        String email;

        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            email = claims.getSubject();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            email = null;
        }

        return email;
    }


    public Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }


    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            audience = claims.getAudience();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }


    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            expiration = null;
        }

        return expiration;
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        final String email = getEmailFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);

        // Token je validan kada:
        // email nije null
        // email iz tokena se podudara sa korisnickom imenom koje pise u bazi
        // nakon kreiranja tokena korisnik nije menjao svoju lozinku
        return (email != null
                && email.equals(((User) userDetails).getEmailAddress())
                && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
    }


}