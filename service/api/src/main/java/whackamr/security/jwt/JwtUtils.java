package whackamr.security.jwt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import whackamr.security.AppUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils
{
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${whackamr.jwt.secret}")
    private String jwtSecret;

    @Value("${whackamr.jwt.access-token-expiration-ms}")
    private int jwtAccessTokenExpirationMs;

    @Value("${whackamr.jwt.refresh-token-expiration-ms}")
    private int jwtRefreshTokenExpirationMs;

    public String generateJwtAccessToken(Authentication authentication)
    {
        AppUserDetails userDetails = (AppUserDetails) authentication.getDetails();
        String username = userDetails.getUsername();

        return generateJwtTokenFromUsername(username);
    }

    public String generateJwtTokenFromUsername(String username)
    {
        var claims = new HashMap<String, Object>();
        var expiration = new Date((new Date()).getTime() + jwtAccessTokenExpirationMs);
        var formatter = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss");

        claims.put(Claims.SUBJECT, username);

        logger.debug("Creating new token with expiration in " + jwtAccessTokenExpirationMs + "ms");
        logger.debug(String.format("Token expires on: %s", formatter.format(expiration)));

        return Jwts.builder().claims(claims).issuedAt(new Date()).expiration(expiration).signWith(getSigningKey()).compact();
    }

    public String generateJwtRefreshToken(String username)
    {
        return Jwts.builder()
                   .subject(username)
                   .issuedAt(new Date())
                   .expiration(new Date((new Date()).getTime() + jwtRefreshTokenExpirationMs))
                   .signWith(getSigningKey())
                   .compact();
    }

    public String generateJwtDataToken(String username, DataClaims claims, Date expiration)
    {
        return Jwts.builder().subject(username).issuedAt(new Date()).expiration(expiration).claims(claims).signWith(getSigningKey()).compact();
    }

    public DataClaims getJwtDataClaims(String token)
    {
        var claims = getClaimsFromToken(token);

        return new DefaultDataClaims(claims);
    }

    public String getUserNameFromJwtToken(String token)
    {
        return getClaimsFromToken(token).getSubject();
    }

    public boolean isTokenExpired(String token)
    {
        try
        {
            getClaimsFromToken(token);

            return false;
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e)
        {
            logger.error("Invalid JWT state: {}", e.getMessage());

            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        } catch (ExpiredJwtException e)
        {
            return true;
        }
    }

    public boolean validateJwtToken(String authToken)
    {
        try
        {
            getClaimsFromToken(authToken);

            return true;
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e)
        {
            logger.error("Invalid JWT state: {}", e.getMessage());

            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        } catch (ExpiredJwtException e)
        {
            logger.error("JWT token is expired: {}", e.getMessage());

            throw e;
        }
    }

    public boolean validateJwtRefreshToken(String refreshToken)
    {
        try
        {
            getClaimsFromToken(refreshToken);

            return true;
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e)
        {
            logger.error("Invalid JWT state: {}", e.getMessage());

            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        } catch (ExpiredJwtException e)
        {
            logger.error("JWT refresh token is expired: {}", e.getMessage());

            throw e;
        }
    }

    private Claims getClaimsFromToken(String token) throws MalformedJwtException, UnsupportedJwtException, IllegalArgumentException
    {
        return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getSigningKey()
    {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
}
