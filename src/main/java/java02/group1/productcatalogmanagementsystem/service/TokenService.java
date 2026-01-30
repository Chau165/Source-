package java02.group1.productcatalogmanagementsystem.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java02.group1.productcatalogmanagementsystem.entity.Account;
import java02.group1.productcatalogmanagementsystem.repository.AccountRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TokenService {


    private final AccountRepository accountRepository;

    private final String SECRET_KEY = "123456789999999999999999999999999999999999999999999999999999999999999999999999999999";

    public SecretKey getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //generate token
    public String generateToken(Account account){
        return Jwts.builder()
                .subject(account.getAccountId() + "")
                .claim("role",account.getRole().getRoleName().name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(getSignInKey())
                .compact();
    }

    //verify token
    public Account extractToken(String token) {
        String value = extractClaim(token, Claims::getSubject);
        long id = Long.parseLong(value);
        return accountRepository.findAccountByAccountId(id);
    }

    public Claims extractAllClaims(String token) {
        return  Jwts.parser().
                verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public <T> T extractClaim(String token, Function<Claims,T> resolver){
        Claims claims = extractAllClaims(token);
        return  resolver.apply(claims);
    }

}
