package semicolon.MeetOn_Channel.domain.channel.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.MeetOn_Channel.domain.channel.dao.ChannelRepository;
import semicolon.MeetOn_Channel.domain.channel.domain.Authority;
import semicolon.MeetOn_Channel.domain.channel.domain.Channel;
import semicolon.MeetOn_Channel.domain.channel.dto.ChannelDto;
import semicolon.MeetOn_Channel.domain.channel.dto.ChannelMemberDto;
import semicolon.MeetOn_Channel.domain.global.exception.BusinessLogicException;
import semicolon.MeetOn_Channel.domain.global.exception.code.ExceptionCode;
import semicolon.MeetOn_Channel.domain.global.util.Aes256;
import semicolon.MeetOn_Channel.domain.global.util.CookieUtil;


import java.util.List;

import static semicolon.MeetOn_Channel.domain.channel.dto.ChannelDto.*;
import static semicolon.MeetOn_Channel.domain.channel.dto.ChannelMemberDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMemberService channelMemberService;
    private final Aes256 aes256;

    /**
     * 방 코드 확인
     * 채널 ID + " " + 채널 이름 -> 가장 처음 나온 " " 기준으로 id 찾음
     * @param request
     * @return
     */
    public ChannelCode findCode(HttpServletRequest request) {
        Long channelId = Long.valueOf(CookieUtil.getCookieValue("channelId", request));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHANNEL_NOT_FOUND));
        String channelCode = null;
        try {
            channelCode = aes256.encrypt( channelId + " " + channel.getName());
            log.info("decrypt_code={}", aes256.decrypt(channelCode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ChannelCode.toChannelCode(channelCode);
    }


    /**
     * 채널 생성 및 유저 정보 업데이트
     * @param createRequest
     */
    @Transactional
    public void createChannel(CreateRequest createRequest, HttpServletRequest request, HttpServletResponse response) {
        Channel channel = Channel.builder().name(createRequest.getChannelName()).build();
        Channel save = channelRepository.save(channel);
        UpdateMemberRequest updateMemberRequest =
                updateUser(createRequest.getChannelName(), createRequest.getUserImage(), Authority.ROLE_HOST, save.getId());
        channelMemberService.updateMemberInfo(updateMemberRequest, request);
        CookieUtil.createCookie("channelId", save.getId().toString(), response);
    }

    @Transactional
    public void joinChannel(JoinRequest joinRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            String channelCode = aes256.decrypt(joinRequest.getChannelCode());
            int i = channelCode.indexOf(" ");
            Long channelId = Long.valueOf(channelCode.substring(0, i));
            log.info("channelId={}", channelId);
            Channel channel = channelRepository.findById(channelId)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHANNEL_NOT_FOUND));
            UpdateMemberRequest updateMemberRequest =
                    updateUser(joinRequest.getUserNickname(), joinRequest.getUserImage(), Authority.ROLE_CLIENT, channel.getId());
            channelMemberService.updateMemberInfo(updateMemberRequest, request);
            CookieUtil.createCookie("channelId", channel.getId().toString(), response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private UpdateMemberRequest updateUser(String name, String image, Authority authority, Long id) {
        return UpdateMemberRequest
                .toUpdateMemberRequest(name, image, authority, id);
    }
}
