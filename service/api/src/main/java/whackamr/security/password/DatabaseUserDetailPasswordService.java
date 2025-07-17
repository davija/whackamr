package whackamr.security.password;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import whackamr.data.repository.UserPasswordRepository;
import whackamr.security.AppUserDetails;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DatabaseUserDetailPasswordService implements UserDetailsPasswordService
{
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUserDetailPasswordService.class);

    PasswordEncoder passwordEncoder;

    private UserPasswordRepository userPasswordRepository;

    private UserDetailsService userDetailsService;

    @Override
    public UserDetails updatePassword(UserDetails userDetails, String s)
    {
        logger.debug("updating password for " + userDetails.getUsername());

        if (userDetails.getClass().isAssignableFrom(AppUserDetails.class))
        {
            logger.debug("we were provided with an AppUserDetails");

            var userPasswordContainer = userPasswordRepository.findById(((AppUserDetails) userDetails).getUserId());

            if (userPasswordContainer.isPresent())
            {
                var userPassword = userPasswordContainer.get();

                userPassword.setPassword(passwordEncoder.encode(s));
                userPasswordRepository.save(userPassword);
            }
        }

        return userDetailsService.loadUserByUsername(userDetails.getUsername());
    }
}
