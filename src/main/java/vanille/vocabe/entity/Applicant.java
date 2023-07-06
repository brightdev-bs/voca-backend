package vanille.vocabe.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Applicant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToOne
    private Community community;

    private String Motive;
    private boolean accepted = false;
    private boolean checked = false;

    protected Applicant() {}

    @Builder
    public Applicant(Long id, User user, Community community, String motive, boolean accepted, boolean checked) {
        this.id = id;
        this.user = user;
        this.community = community;
        Motive = motive;
        this.accepted = false;
        this.checked = false;
    }

    public void acceptMember() {
        this.accepted = true;
        this.checked = true;
    }

    public void rejectMember() {
        this.accepted = false;
        this.checked = true;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
