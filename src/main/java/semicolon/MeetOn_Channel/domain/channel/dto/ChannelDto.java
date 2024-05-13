package semicolon.MeetOn_Channel.domain.channel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
        private String userImage;
        private String userNickname;
        private Authority authority;
        private String channelName;
    }

    @Builder
    @Getter
    public static class JoinRequest {
        private String userImage;
        private String userNickname;
        private Authority authority;
        private String channelCode;
    }
}
