package es.us.isa.ideas.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import es.us.isa.ideas.app.entities.Researcher;
import es.us.isa.ideas.app.security.UserAccount;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author japarejo
 */
@Repository
public interface ResearcherRepository  extends JpaRepository<Researcher, Integer> {
        @Query("SELECT r FROM Researcher r WHERE r.userAccount.username=:username")
        Researcher findByUsername(@Param("username")String username);
        
        Researcher findByName(String name);  
        
        Researcher findByEmail(String email);
        
        @Query(value="SELECT * FROM Researcher  WHERE userAccount_id = :id",nativeQuery = true)
        Researcher findByUserAccountId(@Param("id")int id);
        
        Researcher findByUserAccount(UserAccount ua);
        
}
