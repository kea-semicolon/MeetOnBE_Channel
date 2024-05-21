package semicolon.MeetOn_Channel.domain.channel.dto;

import lombok.Builder;
import lombok.Getter;
import semicolon.MeetOn_Channel.domain.channel.domain.Authority;

public class ChannelMemberDto {

    @Builder
    @Getter
    public static class UpdateMemberRequest {
        private String userNickname;
        private Authority authority;
        private Long channelId;

        public static UpdateMemberRequest toUpdateMemberRequest(String userNickname,
                                                                Authority authority,
                                                                Long channelId) {
            return UpdateMemberRequest.builder()
                    .userNickname(userNickname)
                    .authority(authority)
                    .channelId(channelId)
                    .build();
        }
    }
}
