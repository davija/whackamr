package whackamr.api.controller;

import java.util.Collection;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import whackamr.NoSuchEntityException;
import whackamr.data.dto.PermissionDto;
import whackamr.data.dto.UserDto;
import whackamr.data.entity.User;
import whackamr.data.repository.UserRepository;
import whackamr.security.user.UserPermissionProvider;

@Transactional
@RestController
@AllArgsConstructor
@RequestMapping("/api/me")
public class MeController
{
    private ModelMapper mapper;
    private UserRepository userRepository;
    private UserPermissionProvider userPermissionProvider;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getMe(@Parameter(hidden = true) Authentication authentication)
    {
        var user = getMeFromAuth(authentication);

        return ResponseEntity.ok(mapper.map(user, UserDto.class));
    }

    @GetMapping("/permissions")
    @PreAuthorize("isAuthenticated()")
    public Collection<PermissionDto> getMyPermissions(@Parameter(hidden = true) Authentication authentication)
    {
        var user = getMeFromAuth(authentication);

        return userPermissionProvider.getPermissionsForUser(user).stream()
                .map(perm -> mapper.map(perm, PermissionDto.class)).toList();
    }

    private User getMeFromAuth(Authentication authentication)
    {
        var user = userRepository.findByUsername(authentication.getName());

        if (user != null)
        {
            return user;
        }

        throw new NoSuchEntityException();
    }
}
