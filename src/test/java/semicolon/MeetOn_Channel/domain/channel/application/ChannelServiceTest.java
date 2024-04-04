package semicolon.MeetOn_Channel.domain.channel.application;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import semicolon.MeetOn_Channel.domain.channel.dto.ChannelDto;
import semicolon.MeetOn_Channel.domain.global.util.Aes256;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class ChannelServiceTest {

    @Autowired
    Aes256 aes256;

    @Autowired
    ChannelService channelService;

    MockHttpServletResponse response;
    MockHttpServletRequest request;

    @BeforeEach
    void 세팅(){
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
    }

    @Test
    void 방_코드_찾기() {
        String codeCompare = "1XoHayZElxAI2S3AicOHCbRPn/KPXWa4/kqMK0XwVpg=";
        MockCookie cookie = createCookie(String.valueOf(6352));
        response.addCookie(cookie);
        Cookie[] cookies = response.getCookies();
        request.setCookies(cookies);
        ChannelDto.ChannelCode code = channelService.findCode(request);

        assertThat(code.getCode()).isEqualTo(codeCompare);
    }

    private MockCookie createCookie(String value) {
        MockCookie mockCookie = new MockCookie("channelId", value);
        mockCookie.setPath("/");
        mockCookie.setHttpOnly(true);
        return mockCookie;
    }
}