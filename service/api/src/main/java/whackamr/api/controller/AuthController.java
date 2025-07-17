package whackamr.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import whackamr.data.dto.request.LoginRequest;
import whackamr.data.dto.request.TokenRefreshRequest;
import whackamr.data.dto.response.LoginResponse;
import whackamr.data.dto.response.TokenRefreshResponse;
import whackamr.data.repository.UserPasswordRepository;
import whackamr.data.repository.UserRepository;
import whackamr.security.DbUserDetailsService;
import whackamr.security.NotAuthorizedException;
import whackamr.security.WhackAMrAuthenticationToken;
import whackamr.security.jwt.JwtUtils;
import whackamr.security.jwt.RefreshTokenService;
import whackamr.security.jwt.TokenRefreshException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * Controller that handles user authentication tasks.
 */
@Transactional
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController
{
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private AuthenticationManager authenticationManager;

    private DbUserDetailsService userDetailsService;

    private JwtUtils jwtUtils;

    private RefreshTokenService refreshTokenService;

    private UserRepository userRepository;

    private UserPasswordRepository userPasswordRepository;

    /**
     * Service method that gets the name of the currently logged in user.
     *
     * @param authentication
     *
     * @return Returns the name of the currently logged in user or a message if no
     *         user is logged in
     */
    @GetMapping("/me")
    public String getLoggedInUser(Authentication authentication)
    {
        logger.trace("Checking for authenticated user info");

        if (authentication != null)
        {
            return authentication.getName();
        } else
        {
            return "Not logged in";
        }
    }

    /**
     * Service method that accepts a login request and returns a response.
     *
     * @param loginRequest
     *
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid
    @RequestBody LoginRequest loginRequest)
    {
        var userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        var authToken = new WhackAMrAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), userDetails);
        var user = userRepository.findByUsername(loginRequest.getUsername());

        if (user != null)
        {
            var userPasswordContainer = userPasswordRepository.findById(user.getUserId());

            if (userPasswordContainer.isPresent())
            {
                authenticationManager.authenticate(authToken);
//
//                SecurityContextHolder.getContext().setAuthentication(authentication);
                String accessToken = jwtUtils.generateJwtTokenFromUsername(userDetails.getUsername());
                String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

                return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, userDetails.getUsername()));
            }
        }

        throw new NotAuthorizedException();
    }

    /**
     * Service method that accepts a refresh token and returns a response with new
     * tokens.
     *
     * @param request
     *
     * @return
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid
    @RequestBody TokenRefreshRequest request, HttpServletRequest httpRequest)
    {
        String requestRefreshToken = request.getRefreshToken();

        if (refreshTokenService.verifyExpiration(requestRefreshToken).equals(requestRefreshToken))
        {
            String username = jwtUtils.getUserNameFromJwtToken(requestRefreshToken);
            String token = jwtUtils.generateJwtTokenFromUsername(username);
            String newRefreshToken = jwtUtils.generateJwtRefreshToken(username);

            return ResponseEntity.ok(new TokenRefreshResponse(token, newRefreshToken));
        }

        throw new TokenRefreshException(requestRefreshToken, "Refresh token is expired!");
    }
}
