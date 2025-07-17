package whackamr;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.experimental.StandardException;

@StandardException
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Resource not found")
public class NoSuchEntityException extends RuntimeException {
	private static final long serialVersionUID = 2125287822247852086L;
}
