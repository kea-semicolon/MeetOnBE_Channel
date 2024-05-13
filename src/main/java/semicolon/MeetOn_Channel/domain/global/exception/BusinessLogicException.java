package semicolon.MeetOn_Channel.domain.global.exception;

import lombok.Getter;
import semicolon.MeetOn_Channel.domain.global.exception.code.ExceptionCode;

@Getter
public class BusinessLogicException extends RuntimeException{

    private final ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
