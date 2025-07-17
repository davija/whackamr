package whackamr;

import org.jboss.logging.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import whackamr.security.AuthTokenFilter;
import whackamr.security.WhackAMrAuthenticationProvider;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
public class WebSecurityConfig
{
    private static final Logger logger = Logger.getLogger(WebSecurityConfig.class);

    private WhackAMrAuthenticationProvider authenticationProvider;

    private AuthTokenFilter authTokenFilter;

    /**
     * Creates the JWT authentication token filter.
     *
     * @return Returns a new authentication token filter
     */
    @Bean
    AuthTokenFilter authenticationJwtTokenFilter(AuthTokenFilter tokenFilter)
    {
        return tokenFilter;
    }

    /**
     * Creates the password encoder instance.
     *
     * @return Returns a new password encoder instance.
     */
    @Bean
    static PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
    {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {

        http.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable());

        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
            .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

//        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/api/auth/**", "/error")
//                                                         .permitAll()
//                                                         .requestMatchers("/api/me/**")
//                                                         .authenticated()
//                                                         .anyRequest()
//                                                         .permitAll())
//            .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .authenticationProvider(authenticationProvider)
//            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    OpenAPI openAPI()
    {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                            .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                            .info(new Info().title("Whack A MR API").description("Some custom description of API.").version("1.0"));
    }

    private SecurityScheme createAPIKeyScheme()
    {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP).bearerFormat("JWT").scheme("bearer");
    }
}
