package hack.maze.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserAlreadyFoundException extends RuntimeException {
    public UserAlreadyFoundException(String message) {
        super(message);
    }
}
