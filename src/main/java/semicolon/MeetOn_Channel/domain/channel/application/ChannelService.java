package semicolon.MeetOn_Channel.domain.channel.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.scheduler.Schedulers;
import semicolon.MeetOn_Channel.domain.channel.dao.ChannelRepository;
import semicolon.MeetOn_Channel.domain.channel.domain.Authority;
import semicolon.MeetOn_Channel.domain.channel.domain.Channel;
import semicolon.MeetOn_Channel.domain.global.exception.BusinessLogicException;
import semicolon.MeetOn_Channel.domain.global.exception.code.ExceptionCode;
import semicolon.MeetOn_Channel.domain.global.util.Aes256;
import semicolon.MeetOn_Channel.domain.global.util.CookieUtil;
import semicolon.MeetOn_Channel.domain.global.util.UniqueIdMapper;


import static semicolon.MeetOn_Channel.domain.channel.dto.ChannelDto.*;
import static semicolon.MeetOn_Channel.domain.channel.dto.ChannelDto.ChannelCode.*;
import static semicolon.MeetOn_Channel.domain.channel.dto.ChannelMemberDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMemberService channelMemberService;
    private final CookieUtil cookieUtil;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final static String CHANNEL_MEMBER_KICK_TOPIC = "channel_member_kick_topic";
    private final static String CHANNEL_DELETED_TOPIC = "channel_deleted_topic";
    private final UniqueIdMapper uniqueIdMapper;


    /**
     * 방 코드 확인
     * 채널 ID + " " + 채널 이름 -> 가장 처음 나온 " " 기준으로 id 찾음
     * @param request
     * @return
     */
    public ChannelCode findCode(HttpServletRequest request) {
        Long channelId = Long.valueOf(cookieUtil.getCookieValue("channelId", request));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHANNEL_NOT_FOUND));
        String channelCode = null;
        try {
            channelCode = uniqueIdMapper.generateAndStore(channelId + " " + channel.getName());
            log.info("decrypt_code={} encrypt_code={}", uniqueIdMapper.getOriginal(channelCode), channelCode);
//            channelCode = aes256.encrypt( channelId + " " + channel.getName());
//            log.info("decrypt_code={}", aes256.decrypt(channelCode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return toChannelCode(channelCode);
    }

    /**
     * 채널 생성 및 유저 정보 업데이트
     * @param createRequest
     */
    @Transactional
    public Long createChannel(CreateRequest createRequest, HttpServletRequest request, HttpServletResponse response) {
        Channel channel = Channel.builder().name(createRequest.getChannelName()).build();
        Channel save = channelRepository.save(channel);
        UpdateMemberRequest updateMemberRequest =
                updateMember(createRequest.getUserNickname(), Authority.ROLE_HOST, save.getId());
        channelMemberService.updateMemberInfo(updateMemberRequest, request);
        cookieUtil.createCookie("channelId", save.getId().toString(), response);
        return save.getId();
    }

    /**
     * 채널 코드를 디코딩 후 채널을 찾아 유저 주입 및 업데이트
     * @param joinRequest
     * @param request
     * @param response
     */
    @Transactional
    public Long joinChannel(JoinRequest joinRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            String channelCode = uniqueIdMapper.getOriginal(joinRequest.getChannelCode());
            int i = channelCode.indexOf(" ");
            Long channelId = Long.valueOf(channelCode.substring(0, i));
            Channel channel = channelRepository.findById(channelId)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHANNEL_NOT_FOUND));
            UpdateMemberRequest updateMemberRequest =
                    updateMember(joinRequest.getUserNickname(), Authority.ROLE_CLIENT, channel.getId());
            channelMemberService.updateMemberInfo(updateMemberRequest, request);
            cookieUtil.createCookie("channelId", channel.getId().toString(), response);
            return channel.getId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 해당 채널 삭제
     * 다른 값도 같이 지워야 함(DB에서 해결)
     * @param response
     */
    @Transactional
    public Long deleteChannel(HttpServletRequest request, HttpServletResponse response) {
        Channel channel = findChannel(request);
//        channelMemberService.deleteChannelMember(channel.getId(), request);
        channelRepository.delete(channel);
        kafkaTemplate.send(CHANNEL_DELETED_TOPIC, channel.getId().toString());
        //cookieUtil.createCookie("channelId", "1", response);
        return 1L;
    }

    /**
     * 해당 채널의 특정 유저 추방
     * 여기가 맞는지 잘 모름
     * @param memberId
     */
    public void deleteUser(Long memberId) {
        //channelMemberService.deleteMemberInChannel(memberId, request);
        kafkaTemplate.send(CHANNEL_MEMBER_KICK_TOPIC, memberId.toString());
    }

    private Channel findChannel(HttpServletRequest request) {
        Long channelId = Long.valueOf(cookieUtil.getCookieValue("channelId", request));
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHANNEL_NOT_FOUND));
    }

    private UpdateMemberRequest updateMember(String name, Authority authority, Long id) {
        return UpdateMemberRequest
                .toUpdateMemberRequest(name, authority, id);
    }
}
