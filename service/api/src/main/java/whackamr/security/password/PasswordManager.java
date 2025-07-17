package whackamr.security.password;

import whackamr.data.dto.request.PasswordChangeRequest;
import whackamr.data.entity.User;

/**
 * Performs various operations on passwords.
 * 
 * @author James Davis
 */
public interface PasswordManager
{
    /**
     * Updates a users password using the provided request.
     * 
     * @param userId
     * @param request
     */
    void updatePassword(int userId, PasswordChangeRequest request);

    /**
     * Sets the password for the specified user.
     * 
     * @param user
     * @param password
     */
    void setPassword(User user, String password);
}
