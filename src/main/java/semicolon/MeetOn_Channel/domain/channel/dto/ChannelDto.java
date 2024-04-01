package semicolon.MeetOn_Channel.domain.channel.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
}
