package whackamr.api.controller;

import java.util.List;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import whackamr.NoSuchEntityException;
import whackamr.data.dto.RoleDto;
import whackamr.data.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Transactional
@RestController
@AllArgsConstructor
@RequestMapping("/api/roles")
public class RolesController
{

    private ModelMapper mapper;
    private RoleRepository roleRepository;

    @GetMapping
    public List<RoleDto> getAllRoles()
    {
        var entities = roleRepository.findAll();

        return StreamSupport.stream(entities.spliterator(), true).map(e -> mapper.map(e, RoleDto.class)).toList();
    }

    @GetMapping("/{roleId}")
    public RoleDto getRoleById(@PathVariable int roleId)
    {
        var role = roleRepository.findById(roleId);

        if (role.isEmpty())
        {
            throw new NoSuchEntityException(String.format("A role with id %d could not be found", roleId));
        }

        return mapper.map(role.get(), RoleDto.class);
    }
}
