package es.us.isa.ideas.app.repositories;

import es.us.isa.ideas.app.entities.Workspace;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository  extends JpaRepository<Workspace, Integer> {
          
    @Query("SELECT w FROM Workspace w WHERE w.id = ?")
    Workspace findById(Integer id);

    @Query("SELECT w FROM Workspace w WHERE w.owner.id = ?")
    Collection<Workspace> findByOwner(int owner);
    
    @Query("SELECT w FROM Workspace w "
                + " JOIN w.owner o"
                + " JOIN o.userAccount u"
                + " WHERE u.username = ?")
    Collection<Workspace> findByOwner(String userAccount);
    
    @Query("SELECT w FROM Workspace w WHERE w.name = ? AND w.owner = ?")
    Workspace findByName(String name, int owner);
    
    @Query("SELECT w FROM Workspace w"
                + " JOIN w.owner o"
                + " JOIN o.userAccount u"
                + " WHERE w.name = ? AND u.username = ?")
    Workspace findByName(String name,String userAccount);
    
    @Query("SELECT w FROM Workspace w "
    		+ "JOIN w.workspaceTags t WHERE t.id= ?")
    Collection<Workspace> findByTag(String tagId);
    
    @Query("SELECT w FROM Workspace w "
                    + "JOIN w.workspaceTags t WHERE t.name=?")
    Collection<Workspace> findByTagName(String tagName);
    
    @Query("SELECT w FROM Workspace w WHERE w.origin = ?")
    Collection<Workspace> findByOrigin(String id);
}