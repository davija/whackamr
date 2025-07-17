package whackamr.security;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.SneakyThrows;

@Component
public class WhackAMrAuthenticationProvider implements AuthenticationProvider
{
    private static final Logger logger = LoggerFactory.getLogger(WhackAMrAuthenticationProvider.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsPasswordService userDetailsPasswordService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        logger.debug("Attempting to authenticate");
        AppUserDetails userDetails = (AppUserDetails) authentication.getDetails();

        if (userDetails != null)
        {
            logger.trace(String.format("Authenticating user %s", userDetails.getUsername()));

            if (isDefaultPassword(userDetails.getPassword()))
            {
                return processDefaultPassword(userDetails, authentication);
            } else
            {
                logger.debug("using new password");
                String password = (String) authentication.getCredentials();
                var matches = passwordEncoder.matches(password, userDetails.getPassword());

                logger.debug(String.format("password match? %s", matches));
                return matches ? new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), new ArrayList<>()) :
                        null;
            }
        }

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authenticationClass)
    {
        return WhackAMrAuthenticationToken.class.isAssignableFrom(authenticationClass);
    }

    @SneakyThrows
    private Authentication processDefaultPassword(AppUserDetails userDetails, Authentication authentication)
    {
        logger.debug("default password found");

        if (isOldPasswordMatch((String) authentication.getCredentials(), userDetails.getPassword()))
        {
            logger.debug("default password matches, updating");
            userDetailsPasswordService.updatePassword(userDetails, (String) authentication.getCredentials());

            return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), new ArrayList<>());
        } else
        {
            logger.debug("default password does not match...");

            return null;
        }
    }

    private boolean isDefaultPassword(String password)
    {
        return password.startsWith("==>");
    }

    private boolean isOldPasswordMatch(String password, String defaultPassword)
    {
        return defaultPassword.equals(String.format("==>%s", password));
    }
}
