package semicolon.MeetOn_Channel.domain.channel.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import semicolon.MeetOn_Channel.domain.channel.domain.Channel;
import semicolon.MeetOn_Channel.domain.channel.dto.ChannelMemberDto;
import semicolon.MeetOn_Channel.domain.global.util.CookieUtil;

import static semicolon.MeetOn_Channel.domain.channel.dto.ChannelMemberDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMemberService {

    private final WebClient webClient;

    /**
     * Member 서버에게 유저 업데이트 정보와 함께 업데이트 요청
     * @param updateMemberRequest
     * @param request
     */
    public void updateMemberInfo(UpdateMemberRequest updateMemberRequest, HttpServletRequest request){
        log.info("Member에 Patch 보내기");
        String memberId = CookieUtil.getCookieValue("memberId", request);
        String accessToken = request.getHeader("Authorization");
        log.info("accessToken={}, memberId={}", accessToken, memberId);
        webClient.patch()
                .uri("http://localhost:8000/member/create/" + memberId)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .bodyValue(updateMemberRequest)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
