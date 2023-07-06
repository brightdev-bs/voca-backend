package vanille.vocabe.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Topic extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String content;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @ToString.Exclude
    @OneToMany(mappedBy = "community")
    private List<Post> posts = new ArrayList<>();

    protected Topic() {}

    public Topic(Long id) {
        this.id = id;
    }

    @Builder
    public Topic(Long id, String content, Community community, List<Post> posts) {
        this.id = id;
        this.content = content;
        this.community = community;
        this.posts = posts == null ? new ArrayList<>() : posts;
    }


    public void setContent(String content) {
        this.content = content;
    }
}
