package ies.projeto.watchful_care;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface healthDataRepository extends JpaRepository<healthData, Long>{

}