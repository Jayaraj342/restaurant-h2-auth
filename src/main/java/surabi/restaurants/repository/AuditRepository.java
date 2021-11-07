package surabi.restaurants.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import surabi.restaurants.entity.Audit;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Integer> {
}

