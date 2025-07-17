package whackamr.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {
	private int permissionId;
	private String permissionCode;
	private String description;
	private boolean active;
}
