package whackamr.security.password;

import lombok.experimental.StandardException;

/**
 * Exception that indicates passwords provided do not match.
 * 
 * @author James Davis
 */
@StandardException
public class PasswordMismatchException extends RuntimeException
{
    private static final long serialVersionUID = 6926377314804595183L;
}