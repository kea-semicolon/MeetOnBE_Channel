package semicolon.MeetOn_Channel.domain.channel.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import semicolon.MeetOn_Channel.domain.channel.application.ChannelOtherService;

@Slf4j
@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelOtherController {

    private final ChannelOtherService channelOtherService;

    @GetMapping("/find")
    public Boolean existChannel(@RequestParam Long channelId) {
        return channelOtherService.findChannel(channelId);
    }
}
