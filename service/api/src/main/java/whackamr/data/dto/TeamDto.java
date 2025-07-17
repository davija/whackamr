package whackamr.data.dto;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {
	private int teamId;
	private String teamName;
	private Collection<UserDto> members;
}
