package whackamr.data.dto;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergeRequestDto {
	private int mergeRequestId;
	private String link;
	private UserDto owner;
	private boolean active;
	private Collection<MergeRequestDto> relatedRequests;
}
