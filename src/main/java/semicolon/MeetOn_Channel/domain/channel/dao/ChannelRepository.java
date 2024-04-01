package semicolon.MeetOn_Channel.domain.channel.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import semicolon.MeetOn_Channel.domain.channel.domain.Channel;

public interface ChannelRepository extends JpaRepository<Channel, Long> {


}
