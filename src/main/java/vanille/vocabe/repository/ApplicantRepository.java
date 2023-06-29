package vanille.vocabe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vanille.vocabe.entity.Applicant;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
}
