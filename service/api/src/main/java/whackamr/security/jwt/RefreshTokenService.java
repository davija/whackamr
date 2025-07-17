package whackamr.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService
{
    @Value("${whackamr.jwt.refresh-token-expiration-ms}")
    private Long refreshTokenDurationMs;

    @Autowired
    private JwtUtils jwtUtils;

    public String createRefreshToken(String userId)
    {
        return jwtUtils.generateJwtRefreshToken(userId);
    }

    public String verifyExpiration(String token)
    {
        if (jwtUtils.isTokenExpired(token))
        {
            throw new TokenRefreshException(token, "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }
}
