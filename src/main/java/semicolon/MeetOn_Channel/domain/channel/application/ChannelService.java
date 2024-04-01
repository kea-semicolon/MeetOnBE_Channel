package semicolon.MeetOn_Channel.domain.channel.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import semicolon.MeetOn_Channel.domain.channel.dao.ChannelRepository;
import semicolon.MeetOn_Channel.domain.channel.domain.Channel;
import semicolon.MeetOn_Channel.domain.channel.dto.ChannelDto;
import semicolon.MeetOn_Channel.domain.global.exception.BusinessLogicException;
import semicolon.MeetOn_Channel.domain.global.exception.code.ExceptionCode;
import semicolon.MeetOn_Channel.domain.global.util.CookieUtil;


import java.util.List;

import static semicolon.MeetOn_Channel.domain.channel.dto.ChannelDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;

    public ChannelCode findCode(HttpServletRequest request) {
        Long channelId = Long.valueOf(CookieUtil.getCookieValue("channelId", request));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CHANNEL_NOT_FOUND));
        return ChannelCode.toChannelCode(channel.getName());
    }
}
