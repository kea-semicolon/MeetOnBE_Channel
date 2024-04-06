package semicolon.MeetOn_Channel.domain.channel.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import semicolon.MeetOn_Channel.domain.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor
public class Channel extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

//    @ElementCollection
//    private Set<Long> memberList = new HashSet<>();

    @Builder
    public Channel(Long id, String name) {
        this.id = id;
        this.name = name;
//        this.memberList = memberList;
    }
}
