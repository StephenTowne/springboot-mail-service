package stephen.springboot.mailservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stephen.springboot.mailservice.entity.Email;

@Repository
public interface EmailRepository extends JpaRepository<Email,Long> {
}
