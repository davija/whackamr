package whackamr.security.password;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import whackamr.NoSuchEntityException;
import whackamr.data.dto.request.PasswordChangeRequest;
import whackamr.data.entity.User;
import whackamr.data.entity.UserPassword;
import whackamr.data.repository.UserPasswordRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class WhackAMrPasswordManager implements PasswordManager
{
    private UserPasswordRepository userPasswordRepository;
    private PasswordValidator validator;
    private PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePassword(int userId, PasswordChangeRequest request)
    {
        var userPasswordContainer = userPasswordRepository.findById(userId);

        if (userPasswordContainer.isPresent())
        {
            var userPassword = userPasswordContainer.get();
            
            // Validate that old password matches and new password meets password rules.
            validateOldPasswordMatches(userPassword, request.getOldPassword());
            validatePasswordStrength(request.getNewPassword());

            // Password validation succeeded, encode and update the users password.
            userPassword.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userPasswordRepository.save(userPassword);
        }
        else
        {
            throw new NoSuchEntityException();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPassword(User user, String password)
    {
        var userPassword = new UserPassword();

        userPassword.setUser(user);
        userPassword.setPassword(passwordEncoder.encode(password));
        
        userPasswordRepository.save(userPassword);
    }
    
    /**
     * Validates that the old password matches what the user provided.
     * 
     * @param userPassword
     * @param oldPassword
     */
    private void validateOldPasswordMatches(UserPassword userPassword, String oldPassword)
    {
        if (!passwordEncoder.matches(oldPassword, userPassword.getPassword()))
        {
            throw new PasswordMismatchException();
        }
    }

    /**
     * Validates that password meets the proper rules.
     * 
     * @param password
     */
    private void validatePasswordStrength(String password)
    {
        validator.validate(password);
    }    
}
