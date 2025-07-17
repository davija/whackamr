package whackamr.security.user;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import whackamr.data.entity.Permission;
import whackamr.data.entity.User;

@Component
public class UserPermissionProviderImpl implements UserPermissionProvider
{

    @Override
    public Set<Permission> getPermissionsForUser(User user)
    {
        // Collect all permissions from all roles and return a distinct list of
        // permissions.
        return user.getRoles().stream().flatMap(role -> role.getPermissions().stream()).collect(Collectors.toSet());
    }
}
