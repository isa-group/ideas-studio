/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.us.isa.ideas.app.entities.Confirmation;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author japarejo
 */
@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Integer>{
    @Query("SELECT c FROM Confirmation c WHERE c.researcher.id=:id")
    Confirmation getByResearcherId(@Param("id")int researcherId);
    
    @Query("SELECT c FROM Confirmation c WHERE c.researcher.userAccount.id=:id")
    Confirmation getByUserAccountId(@Param("id")int userAccountId);
    
    Confirmation getByConfirmationCode(String code);
}
