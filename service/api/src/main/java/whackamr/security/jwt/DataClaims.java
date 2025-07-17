package whackamr.security.jwt;

import io.jsonwebtoken.Claims;

public interface DataClaims extends Claims
{
    boolean valid();
}
