package semicolon.MeetOn_Channel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableDiscoveryClient
@EnableJpaAuditing
@SpringBootApplication
public class MeetOnChannelApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetOnChannelApplication.class, args);
	}

}
