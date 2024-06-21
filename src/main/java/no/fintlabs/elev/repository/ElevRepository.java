package no.fintlabs.elev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElevRepository extends JpaRepository<ElevEntity, String> {

    @Query("SELECT e.birthNumber FROM ElevEntity e")
    List<String> findAllBirthNumbers();

    @Query("SELECT e.email FROM ElevEntity e")
    List<String> findAllEmails();

    @Query("SELECT e.mobileNumber FROM ElevEntity e")
    List<String> findAllMobileNumbers();

}
