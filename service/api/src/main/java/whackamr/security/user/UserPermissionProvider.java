package whackamr.security.user;

import java.util.Set;

import whackamr.data.entity.Permission;
import whackamr.data.entity.User;

public interface UserPermissionProvider
{
    Set<Permission> getPermissionsForUser(User user);
}
