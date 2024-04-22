package semicolon.MeetOn_Channel.domain.channel.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import semicolon.MeetOn_Channel.domain.channel.application.ChannelService;

import static semicolon.MeetOn_Channel.domain.channel.dto.ChannelDto.*;

@Slf4j
@RestController
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;
    private final ObjectMapper objectMapper;

    /**
     * 방 코드 확인
     * 채널 코드는 (채널 이름 + 채널 id)를 AES256 알고리즘으로 암호화
     * @param request
     * @return
     */
    @Operation(summary = "방 코드 확인  ", description = "방 코드 확인")
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
    @Operation(summary = "방 생성 ", description = "방 생성")
    @PostMapping("")
    public ResponseEntity<String> createChannel(@RequestPart("userImage") MultipartFile userImage,
                                                @RequestPart("userInfo") String userInfoJson,
                                                HttpServletRequest request,
                                                HttpServletResponse response) throws JsonProcessingException {
        CreateRequest createRequest = objectMapper.readValue(userInfoJson, CreateRequest.class);
        channelService.createChannel(userImage, createRequest, request, response);
        return ResponseEntity.ok("Ok");
    }
//    @Operation(summary = "방 생성 ", description = "방 생성")
//    @PostMapping("")
//    public ResponseEntity<String> createChannel(@RequestBody CreateRequest createRequest,
//                                                HttpServletRequest request,
//                                                HttpServletResponse response) {
//        channelService.createChannel(createRequest, request, response);
//        return ResponseEntity.ok("Ok");
//    }

    /**
     * 방 참가
     * @param userImage
     * @param userInfoJson
     * @param request
     * @param response
     * @return
     * @throws JsonProcessingException
     */
    @Operation(summary = "방 참가", description = "방 참가")
    @PutMapping("")
    public ResponseEntity<String> joinChannel(@RequestPart("userImage") MultipartFile userImage,
                                              @RequestPart("userInfo") String userInfoJson,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws JsonProcessingException {
        JoinRequest joinRequest = objectMapper.readValue(userInfoJson, JoinRequest.class);
        log.info("test={}", joinRequest.getChannelCode());
        channelService.joinChannel(userImage, joinRequest, request, response);
        return ResponseEntity.ok("Ok");
    }

    /**
     * 방 삭제
     * @param request
     * @param response
     * @return
     */
    @Operation(summary = "방 삭제", description = "방 삭제")
    @DeleteMapping
    public ResponseEntity<String> deleteChannel(HttpServletRequest request, HttpServletResponse response) {
        channelService.deleteChannel(request, response);
        return ResponseEntity.ok("Ok");
    }

    /**
     * 특정 유저 방 퇴출
     * @param memberId
     * @return
     */
    @Operation(summary = "유저 추방", description = "특정 유저 방 추방")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<String> kickUser(@PathVariable Long memberId) {
        channelService.deleteUser(memberId);
        return ResponseEntity.ok("Ok");
    }
}
