package whackamr.data.dto;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
	private int roleId;
	private String roleCode;
	private String description;
	private boolean active;
	private Collection<PermissionDto> permissions;
}
