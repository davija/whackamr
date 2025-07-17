package whackamr.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.experimental.StandardException;

@StandardException
@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAuthorizedException extends RuntimeException
{
    private static final long serialVersionUID = 3794051274450319259L;
}
