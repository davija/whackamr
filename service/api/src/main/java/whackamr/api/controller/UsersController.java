package whackamr.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import whackamr.NoSuchEntityException;
import whackamr.data.dto.UserDto;
import whackamr.data.dto.UserWithPasswordDto;
import whackamr.data.entity.User;
import whackamr.data.repository.RoleRepository;
import whackamr.data.repository.UserPasswordRepository;
import whackamr.data.repository.UserRepository;
import whackamr.security.AuthorityAllowed;
import whackamr.security.Permissions;
import whackamr.security.password.PasswordManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Transactional
@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
@AuthorityAllowed(Permissions.UPDATE_ROLE)
@AuthorityAllowed(Permissions.ADD_ROLE)
public class UsersController
{
    private ModelMapper mapper;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private UserPasswordRepository userPasswordRepository;
    private PasswordManager passwordManager;

    @GetMapping
//    @RolesAllowed(Permissions.Fields.ADD_ROLE)
//    @PreAuthorize("hasAuthority('ADD_ROLE')")    
    public List<UserDto> getAllUsers()
    {
        var users = new ArrayList<UserDto>();
        var entities = userRepository.findAll();

        entities.forEach(user -> users.add(mapper.map(user, UserDto.class)));

        return users;
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable int userId)
    {
        var user = userRepository.findById(userId);

        if (user.isEmpty())
        {
            throw new NoSuchEntityException(String.format("User with id %d could not be found", userId));
        }

        return mapper.map(user, UserDto.class);
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserWithPasswordDto dto)
    {
        var entity = mapper.map(dto, User.class);

        entity.setActive(true);

        var savedEntity = userRepository.save(entity);

        passwordManager.setPassword(savedEntity, dto.getPassword());

        return mapper.map(savedEntity, UserDto.class);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int userId)
    {
        var userContainer = userRepository.findById(userId);

        if (userContainer.isPresent())
        {
            var user = userContainer.get();

            // Remove user from all teams before deleting.
            user.getTeams().clear();

            // Remove password entry
            userPasswordRepository.deleteByUserId(userId);

            // remove user
            userRepository.delete(user);
        }
    }

    @PostMapping("/{userId}/roles/{roleId}")
    public UserDto addRoleToUser(@PathVariable int userId, @PathVariable int roleId)
    {
        var roleContainer = roleRepository.findById(roleId);
        var userContainer = userRepository.findById(userId);

        if (roleContainer.isEmpty() || userContainer.isEmpty())
        {
            throw new IllegalStateException("Could not find specified role or user");
        }

        var role = roleContainer.get();
        var user = userContainer.get();

        role.getUsers().add(userContainer.get());

        role = roleRepository.save(role);

        userRepository.refresh(user);

        return mapper.map(user, UserDto.class);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public UserDto removeRoleFromUser(@PathVariable int userId, @PathVariable int roleId)
    {
        var roleContainer = roleRepository.findById(roleId);
        var userContainer = userRepository.findById(userId);

        if (roleContainer.isEmpty() || userContainer.isEmpty())
        {
            throw new IllegalStateException("Could not find specified role or user");
        }

        var role = roleContainer.get();
        var user = userContainer.get();

        role.getUsers().remove(userContainer.get());

        role = roleRepository.save(role);

        userRepository.refresh(user);

        return mapper.map(user, UserDto.class);
    }
}
