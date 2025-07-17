package whackamr.security.jwt;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.experimental.StandardException;

/**
 * Exception thrown to indicate that a token provided is invalid.
 * 
 * @author James Davis
 */
@StandardException
@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidTokenException extends RuntimeException
{
    private static final long serialVersionUID = -1883814788189920370L;
}
