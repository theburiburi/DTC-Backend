package hanium.dtc.global;

import hanium.dtc.exception.ErrorCode;

public record ExceptionDto(
        Integer code,
        String message
) {
    public ExceptionDto(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    public static ExceptionDto of(ErrorCode errorCode) {
        return new ExceptionDto(errorCode);
    }
}