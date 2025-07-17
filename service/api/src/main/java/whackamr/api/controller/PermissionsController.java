package whackamr.api.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import whackamr.NoSuchEntityException;
import whackamr.data.dto.PermissionDto;
import whackamr.data.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Transactional
@RestController
@AllArgsConstructor
@RequestMapping("/api/permissions")
public class PermissionsController {
    private static final Logger logger = LoggerFactory.getLogger(PermissionsController.class);
	private ModelMapper mapper;
	private PermissionRepository permissionRepository;
	
	@GetMapping
	public List<PermissionDto> getAllPermissions() {
		var entities = permissionRepository.findAll();

		return entities.stream().map(e -> mapper.map(e, PermissionDto.class)).toList();
	}

	
	@GetMapping("{permissionId}")
	public PermissionDto getPermissionById(@PathVariable int permissionId) {
	    logger.info("here in permission method");
	    logger.info(String.format("Id passed in is: %d", permissionId));
		var permission = permissionRepository.findById(permissionId);
	
		if (permission.isEmpty()) {
			throw new NoSuchEntityException(String.format("A permission with id %d could not be found", permissionId));
		}
		
		return mapper.map(permission.get(), PermissionDto.class);
	}	
}
