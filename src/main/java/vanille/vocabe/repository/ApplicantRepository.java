package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.Applicant;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.User;

import java.util.Optional;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    Optional<Applicant> findApplicantByUserAndCommunity(User user, Community community);
}
