package semicolon.MeetOn_Channel.domain.channel.dto;

import lombok.Builder;
import lombok.Getter;
import semicolon.MeetOn_Channel.domain.channel.domain.Authority;

public class ChannelMemberDto {

    @Builder
    @Getter
    public static class UpdateMemberRequest {
        private String userNickname;
        private String userImage;
        private Authority authority;

        public static UpdateMemberRequest toUpdateMemberRequest(String userNickname, String userImage) {
            return UpdateMemberRequest.builder()
                    .userNickname(userNickname)
                    .userImage(userImage)
                    .authority(Authority.ROLE_HOST)
                    .build();
        }
    }
}
