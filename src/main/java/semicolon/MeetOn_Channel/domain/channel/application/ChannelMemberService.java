package semicolon.MeetOn_Channel.domain.channel.application;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import semicolon.MeetOn_Channel.domain.channel.domain.Channel;
import semicolon.MeetOn_Channel.domain.channel.dto.ChannelMemberDto;
import semicolon.MeetOn_Channel.domain.global.util.CookieUtil;

import static semicolon.MeetOn_Channel.domain.channel.dto.ChannelMemberDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMemberService {

    private final WebClient webClient;
    private final CookieUtil cookieUtil;

    /**
     * 서버 간 통신 Service
     * Member 서버에게 유저 업데이트 정보와 함께 업데이트 요청
     * @param updateMemberRequest
     * @param request
     */
    public void updateMemberInfo(UpdateMemberRequest updateMemberRequest, HttpServletRequest request){
        log.info("Member에 Patch 보내기");
        String memberId = cookieUtil.getCookieValue("memberId", request);
        Long channelId = updateMemberRequest.getChannelId();
        String accessToken = request.getHeader("Authorization");
        log.info("memberId={}, channelId={}, accessToken={}, ", memberId, channelId, accessToken);
        String uri = UriComponentsBuilder
                .fromUriString("http://localhost:8000/member/create")
                .queryParam("memberId", memberId)
                .queryParam("channelId", channelId)
                .toUriString();
        log.info("uri={}", uri);
        webClient.patch()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .bodyValue(updateMemberRequest)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
