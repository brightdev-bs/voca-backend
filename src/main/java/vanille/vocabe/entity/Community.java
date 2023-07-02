package vanille.vocabe.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@ToString(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Community extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;
    private int totalMember;
    private boolean open;
    @ToString.Exclude
    @OneToMany(mappedBy = "community")
    private List<CommunityUser> communityUsers = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "community")
    private List<Post> posts = new ArrayList<>();

    protected Community() {}

    @Builder
    public Community(Long id, String name, String description, int totalMember, boolean open, List<CommunityUser> communityUsers, List<Post> posts) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalMember = totalMember;
        this.open = open;
        this.communityUsers = communityUsers;
        this.posts = posts;
    }
}
