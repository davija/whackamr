package whackamr.security.password;

/**
 * Validates passwords match rules for passwords.
 * 
 * @author James Davis
 */
public interface PasswordValidator
{
    /**
     * Validates that the password meets requirements. If any violations are found, an exception will be thrown.
     * 
     * @param password
     */
    void validate(String password);
}