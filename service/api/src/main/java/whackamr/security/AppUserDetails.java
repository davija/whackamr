package whackamr.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import whackamr.data.entity.Permission;
import whackamr.data.entity.User;
import whackamr.data.entity.UserPassword;

public class AppUserDetails implements UserDetails
{
    private static final long serialVersionUID = -3649598668410334136L;

    @JsonIgnore
    private User user;

    @JsonIgnore
    private UserPassword userPassword;

    @JsonIgnore
    private Collection<GrantedAuthority> grantedAuthorities;

    public AppUserDetails(User user, UserPassword userPassword, Collection<Permission> permissions)
    {
        this.user = user;
        this.userPassword = userPassword;
        this.grantedAuthorities = permissions.stream().map(perm -> (GrantedAuthority) new SimpleGrantedAuthority(perm.getPermissionCode())).toList();
    }

    public static AppUserDetails build(User user, UserPassword userPassword, Collection<Permission> permissions)
    {
        return new AppUserDetails(user, userPassword, permissions);
    }

    public int getUserId()
    {
        return user.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return grantedAuthorities;
    }

    @Override
    public String getPassword()
    {
        return userPassword.getPassword();
    }

    @Override
    public String getUsername()
    {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return user != null ? user.getActive() : false;
    }
}
