package whackamr;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.experimental.StandardException;

@StandardException
@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Resource already exists")
public class AlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = -8017602101336945832L;
}
