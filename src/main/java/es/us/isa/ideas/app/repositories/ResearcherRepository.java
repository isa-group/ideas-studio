package es.us.isa.ideas.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.us.isa.ideas.app.entities.Researcher;

/**
 *
 * @author japarejo
 */
@Repository
public interface ResearcherRepository  extends JpaRepository<Researcher, Integer> {
        @Query("SELECT r FROM Researcher r WHERE r.userAccount.username=?")
        Researcher findByUsername(String username);
        
        Researcher findByName(String name);        
        Researcher findByEmail(String email);
        
        @Query("SELECT r FROM Researcher r WHERE r.userAccount.id = ?")
        Researcher findByUserAccountId(int id);
}
