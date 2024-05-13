package semicolon.MeetOn_Channel.domain.channel.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semicolon.MeetOn_Channel.domain.channel.dao.ChannelRepository;
import semicolon.MeetOn_Channel.domain.channel.domain.Channel;
import semicolon.MeetOn_Channel.domain.global.exception.BusinessLogicException;
import semicolon.MeetOn_Channel.domain.global.exception.code.ExceptionCode;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelOtherService {

    private final ChannelRepository channelRepository;

    public Boolean findChannel(Long channelId) {
        Optional<Channel> find = channelRepository.findById(channelId);
        return find.isPresent();
    }
}
