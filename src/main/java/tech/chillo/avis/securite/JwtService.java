package tech.chillo.avis.securite;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.chillo.avis.entite.Utilisateur;
import tech.chillo.avis.service.UtilisateurService;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class JwtService {
    private UtilisateurService utilisateurService;
    private final String ENCRIPTION_KEY = "608f36e92dc66d97d5933f0e6371493cb4fc05b1aa8f8de64014732472303a7c";




    public Map<String,String> generer(String email) {
        Utilisateur utilisateur = (Utilisateur) this.utilisateurService.loadUserByUsername(email);
        return this.generateJWT(utilisateur);
    }

    private Map<String, String> generateJWT(Utilisateur utilisateur) {

        final long currentTime = System.currentTimeMillis();
        final long expiration = currentTime+ 10 * 60 * 1000;

        final Map<String, Object> claims = Map.of(
                "nom", utilisateur.getNom(),
                Claims.EXPIRATION, new Date(expiration),
                Claims.SUBJECT, utilisateur.getEmail()

        );

        final String bearer = Jwts.builder()
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(expiration))
                .setSubject(utilisateur.getEmail())
                .setClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();


        return Map.of("bearer", bearer);
    }

    private Key getKey() {
        final byte[] decoder = Decoders.BASE64.decode(ENCRIPTION_KEY);
        return Keys.hmacShaKeyFor(decoder);
    }

    public String extractUsername(String token) {
        return  this.getClaim(token, Claims::getSubject);

    }
    public  boolean isTokenExpirede(String token) {
        Date ExpirationDate=this.getClaim(token, Claims::getExpiration);
        return ExpirationDate.before(new Date());
    }



    private   <T> T  getClaim(String token, Function<Claims, T> function) {

        final Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return function.apply(claims);

    }
}
