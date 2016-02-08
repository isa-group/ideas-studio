package es.us.isa.ideas.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import es.us.isa.ideas.app.entities.Researcher;
import es.us.isa.ideas.app.security.UserAccount;

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
        
        @Query(value="SELECT * FROM Researcher  WHERE userAccount_id = ?",nativeQuery = true)
        Researcher findByUserAccountId(int id);
        
        Researcher findByUserAccount(UserAccount ua);
        
}
