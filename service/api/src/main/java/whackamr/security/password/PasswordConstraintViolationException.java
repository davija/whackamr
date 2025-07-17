package whackamr.security.password;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.StandardException;

/**
 * Exception thrown when password does not meet password rules.
 * 
 * @author James Davis
 */
@StandardException
@AllArgsConstructor
public class PasswordConstraintViolationException extends RuntimeException
{
    private static final long serialVersionUID = -8681229893842803124L;

    @Getter
    private List<String> errorMessages;
}