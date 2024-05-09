package com.drainshawty.lab1.security;

import com.drainshawty.lab1.services.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component public class JWTUtil {

    @Value("${jwt.key}") String KEY;
    @Value("${jwt.validity}") long VALIDITY;
    final UserDetailsService userDetails;

    @Autowired
    public JWTUtil(UserDetailsServiceImpl service) {
        this.userDetails = service;
    }

    public String generateToken(String username, List<String> roles) {
        val claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        val now = new Date();
        return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(new Date(now.getTime() + VALIDITY))
                .signWith(SignatureAlgorithm.HS256, KEY).compact();
    }

    public boolean validateToken(String token) {
        return !Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    private boolean isTokenExpired(String token) { return getExpirationDate(token).before(new Date()); }

    public String resolveToken(HttpServletRequest req) {
        val bearerToken = req.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }

    public Authentication getAuthentication(String token) {
        val details = this.userDetails.loadUserByUsername(getUsername(token));
        try {
            return new UsernamePasswordAuthenticationToken(details, "", details.getAuthorities());
        } catch (NullPointerException e) {
            return null;
        }
    }

    public String getUsername(String token) { return getClaim(token, Claims::getSubject); }

    public Date getExpirationDate(String token) { return getClaim(token, Claims::getExpiration); }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody());
    }

    public String decode(HttpServletRequest req) { return this.getUsername(this.resolveToken(req)); }
}