package vanille.vocabe.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@ToString(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Topic {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    @OneToOne
    @JoinColumn(name = "community_id")
    private Community community;

    public Topic() {}

    @Builder
    public Topic(Long id, String content, Community community) {
        this.id = id;
        this.content = content;
        this.community = community;
    }
}
