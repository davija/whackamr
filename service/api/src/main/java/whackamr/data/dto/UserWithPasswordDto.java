package whackamr.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithPasswordDto
{
    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private boolean active;
}
