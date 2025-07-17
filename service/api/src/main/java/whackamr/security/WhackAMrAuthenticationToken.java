package whackamr.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class WhackAMrAuthenticationToken extends UsernamePasswordAuthenticationToken
{
    private static final long serialVersionUID = -1677683268994222052L;

    public WhackAMrAuthenticationToken(Object principal, Object credentials, UserDetails userDetails)
    {
        super(principal, credentials);

        this.setDetails(userDetails);
    }

    public WhackAMrAuthenticationToken(Object principal,
            Object credentials,
            UserDetails userDetails,
            Collection<? extends GrantedAuthority> authorities)
    {
        super(principal, credentials, authorities);

        this.setDetails(userDetails);
    }
}
