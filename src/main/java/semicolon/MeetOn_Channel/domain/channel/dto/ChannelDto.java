package semicolon.MeetOn_Channel.domain.channel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import semicolon.MeetOn_Channel.domain.channel.domain.Authority;
import semicolon.MeetOn_Channel.domain.channel.domain.Channel;

public class ChannelDto {

    @Builder
    @Getter
    public static class ChannelCode {
        private String code;

        public static ChannelCode toChannelCode(String code) {
            return ChannelCode
                    .builder()
                    .code(code)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class CreateRequest {
        private MultipartFile userImage;
        private String userNickname;
        private Authority authority;
        private String channelName;
    }

    @Builder
    @Getter
    public static class JoinRequest {
        private MultipartFile userImage;
        private String userNickname;
        private Authority authority;
        private String channelCode;
    }
}
