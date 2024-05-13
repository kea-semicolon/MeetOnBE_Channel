package semicolon.MeetOn_Channel.domain.global.exception.code;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    CHANNEL_NOT_FOUND(404, "Channel Not Found"),
    MEMBER_NOT_FOUND(404, "Member Not Found"),
    LOGOUT_MEMBER(404, "Member Already Logout"),
    ADMIN_DUPLICATED(404, "Admin Duplicated"),
    NO_SUCH_ALGORITHM(404, "No_Such_Algorithm"),
    UNABLE_TO_SEND_EMAIL(404, "Email Not Send"),
    INVALID_REQUEST(400, "No Cookie Info"),
    INVALID_REFRESHTOKEN(410, "Invalid RefreshToken");

    private final int status;

    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
