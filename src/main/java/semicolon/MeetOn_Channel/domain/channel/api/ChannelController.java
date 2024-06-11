package semicolon.MeetOn_Channel.domain.channel.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import semicolon.MeetOn_Channel.domain.channel.application.ChannelService;

import static semicolon.MeetOn_Channel.domain.channel.dto.ChannelDto.*;

@Slf4j
@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    /**
     * 방 코드 확인
     * 채널 코드는 (채널 이름 + 채널 id)를 AES256 알고리즘으로 암호화
     * @param request
     * @return
     */
    @GetMapping("/code")
    public ResponseEntity<ChannelCode> getChannelCode(HttpServletRequest request) {
        return ResponseEntity.ok(channelService.findCode(request));
    }

    /**
     * 방 생성
     * @param createRequest
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseEntity<Long> createChannel(@RequestBody CreateRequest createRequest,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        return ResponseEntity.ok(channelService.createChannel(createRequest, request, response));
        //return ResponseEntity.ok("Ok");
    }

    /**
     * 방 참가
     * @param joinRequest
     * @param request
     * @param response
     * @return
     */
    @PatchMapping("")
    public ResponseEntity<Long> joinChannel(@RequestBody JoinRequest joinRequest,
                                              HttpServletRequest request,
                                              HttpServletResponse response) {
        return ResponseEntity.ok(channelService.joinChannel(joinRequest, request, response));
        //return ResponseEntity.ok("Ok");
    }

    /**
     * 방 삭제
     * @param request
     * @param response
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Long> deleteChannel(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(channelService.deleteChannel(request, response));
        //return ResponseEntity.ok("Ok");
    }

    /**
     * 특정 유저 방 퇴출
     * @param memberId
     * @return
     */
    @DeleteMapping("/kick")
    public ResponseEntity<String> kickUser(@RequestParam Long memberId) {
        channelService.deleteUser(memberId);
        return ResponseEntity.ok("Ok");
    }
}
