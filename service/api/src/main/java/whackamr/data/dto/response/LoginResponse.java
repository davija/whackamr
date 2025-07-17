package whackamr.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse
{
    private String accessToken;
    private String refreshToken;
    private String username;
}
