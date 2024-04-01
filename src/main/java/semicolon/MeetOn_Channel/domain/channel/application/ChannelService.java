package semicolon.MeetOn_Channel.domain.channel.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.MeetOn_Channel.domain.channel.dao.ChannelRepository;
import semicolon.MeetOn_Channel.domain.channel.domain.Channel;
import semicolon.MeetOn_Channel.domain.channel.dto.ChannelDto;
import semicolon.MeetOn_Channel.domain.channel.dto.ChannelMemberDto;
import semicolon.MeetOn_Channel.domain.global.exception.BusinessLogicException;
import semicolon.MeetOn_Channel.domain.global.exception.code.ExceptionCode;
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

    /**
     * 방 코드 확인
     * @param request
     * @return
     */
    public ChannelCode findCode(HttpServletRequest request) {
        Long channelId = Long.valueOf(CookieUtil.getCookieValue("channelId", request));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHANNEL_NOT_FOUND));
        return ChannelCode.toChannelCode(channel.getName());
    }


    /**
     * 채널 생성 및 유저 정보 업데이트
     * @param createRequest
     */
    @Transactional
    public void createChannel(CreateRequest createRequest, HttpServletRequest request) {
        Channel channel = Channel.builder().name(createRequest.getChannelName()).build();
        channelRepository.save(channel);
        UpdateMemberRequest updateMemberRequest =
                UpdateMemberRequest.toUpdateMemberRequest(createRequest.getUserNickname(), createRequest.getUserImage());
        channelMemberService.updateMemberInfo(updateMemberRequest, request);
    }
}
