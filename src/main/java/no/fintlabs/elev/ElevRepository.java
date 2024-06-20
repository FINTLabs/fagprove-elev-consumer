package no.fintlabs.elev;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElevRepository extends JpaRepository<ElevEntity, String> {
}
