package whackamr.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import whackamr.data.repository.UserPasswordRepository;
import whackamr.data.repository.UserRepository;
import whackamr.security.user.UserPermissionProvider;
import io.swagger.v3.core.util.Json;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DbUserDetailsService implements UserDetailsService
{
    private static final Logger logger = LoggerFactory.getLogger(DbUserDetailsService.class);

    private UserRepository userRepository;
    private UserPasswordRepository userPasswordRepository;
    private UserPermissionProvider userPermissionProvider;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        var user = userRepository.findByUsername(username);

        if (user == null)
        {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        var userPassword = userPasswordRepository.findByUserId(user.getUserId());

        if (!user.getActive())
        {
            throw new NotAuthorizedException("user account is not active");
        }

        var details = new AppUserDetails(user, userPassword, userPermissionProvider.getPermissionsForUser(user));

        logger.debug("Loaded user");
        logger.debug(Json.pretty(details));

        return details;
    }
}
