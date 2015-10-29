/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.us.isa.ideas.app.entities.Confirmation;

/**
 *
 * @author japarejo
 */
@Repository
public interface ConfirmationRepository extends JpaRepository<Confirmation, Integer>{
    @Query("SELECT c FROM Confirmation c WHERE c.researcher.id=?")
    Confirmation getByResearcherId(int researcherId);
    
    @Query("SELECT c FROM Confirmation c WHERE c.researcher.userAccount.id=?")
    Confirmation getByUserAccountId(int userAccountId);
    
    Confirmation getByConfirmationCode(String code);
}
