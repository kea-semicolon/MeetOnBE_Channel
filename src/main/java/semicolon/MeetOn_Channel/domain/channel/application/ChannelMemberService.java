package semicolon.MeetOn_Channel.domain.channel.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import semicolon.MeetOn_Channel.domain.global.util.CookieUtil;

import static semicolon.MeetOn_Channel.domain.channel.dto.ChannelMemberDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelMemberService {

    private final WebClient webClient;
    private final CookieUtil cookieUtil;
    @Value("${app.gateway.url}")
    private String gateway;
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
                .fromUriString(gateway + "/member/update")
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

    public void deleteChannelMember(Long channelId, HttpServletRequest request) {
        log.info("Channel에 속해있는 멤버 default Channel로 변경");
        String accessToken = request.getHeader("Authorization");
        String uri = UriComponentsBuilder
                .fromUriString(gateway + "/member/delete/channel")
                .queryParam("channelId", channelId)
                .toUriString();
        log.info("uri={}", uri);
        webClient.patch()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

//    public void deleteMemberInChannel(Long memberId, HttpServletRequest request) {
//        log.info("Channel에 속해있는 특정 멤버 default Channel로 변경");
//        String accessToken = request.getHeader("Authorization");
//        String uri = UriComponentsBuilder
//                .fromUriString("http://localhost:8000/member/delelte/member")
//                .queryParam("memberId", memberId)
//                .toUriString();
//
//        webClient.patch()
//                .uri(uri)
//                .header(HttpHeaders.AUTHORIZATION, accessToken)
//                .retrieve()
//                .toBodilessEntity()
//                .block();
//    }
}
