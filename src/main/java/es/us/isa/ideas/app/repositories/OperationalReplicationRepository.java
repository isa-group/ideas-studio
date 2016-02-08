package es.us.isa.ideas.app.repositories;

import es.us.isa.ideas.app.entities.OperationalReplication;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationalReplicationRepository extends JpaRepository<OperationalReplication, Integer>{
    
    @Query("SELECT r FROM OperationalReplication r WHERE r.id = ?")
    OperationalReplication findById(Integer id);
       
    @Query("SELECT r FROM OperationalReplication r WHERE r.workspace = ?")
    Collection<OperationalReplication> findByWorkspace(String id);
    
    @Query("SELECT r FROM OperationalReplication r "
                + " JOIN r.workspace w"
                + " JOIN w.owner o"
                + " JOIN o.userAccount u"
                + " WHERE u.username = ?")
    Collection<OperationalReplication> findByOwner(String userAccount);
}