package es.us.isa.ideas.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.us.isa.ideas.app.entities.Experiment;


/**
 *
 * @author japarejo
 */
@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, Integer> {
    /*@Query("SELECT e FROM Experiment e WHERE e.owner.id=?1")
    Collection<Experiment> findByOwnerId(int researcherId);
    Collection<Experiment> findByName(String name);*/
    /*@Query("SELECT e FROM Experiment e WHERE e.created>?")
    Collection<Experiment> findByCreatedAfter(Date date);*/
}
