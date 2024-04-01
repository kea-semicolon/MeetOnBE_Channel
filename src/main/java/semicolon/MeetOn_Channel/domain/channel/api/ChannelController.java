package semicolon.MeetOn_Channel.domain.channel.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semicolon.MeetOn_Channel.domain.channel.application.ChannelService;
import semicolon.MeetOn_Channel.domain.channel.dto.ChannelDto;

import java.util.List;

import static semicolon.MeetOn_Channel.domain.channel.dto.ChannelDto.*;

@Slf4j
@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping("/code")
    public ResponseEntity<ChannelCode> channelCode(HttpServletRequest request) {
        return ResponseEntity.ok(channelService.findCode(request));
    }
}
