package es.us.isa.ideas.app.repositories;

import es.us.isa.ideas.app.entities.Workspace;
import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository  extends JpaRepository<Workspace, Integer> {
          
    @Query("SELECT w FROM Workspace w WHERE w.id = :id")
    Optional<Workspace> findById(@Param("id")Integer id);

    @Query("SELECT w FROM Workspace w WHERE w.owner.id = :owner")
    Collection<Workspace> findByOwner(@Param("owner")int owner);
    
    @Query("SELECT w FROM Workspace w "
                + " JOIN w.owner o"
                + " JOIN o.userAccount u"
                + " WHERE u.username = :username")
    Collection<Workspace> findByOwner(@Param("username")String userAccount);
    
    @Query("SELECT w FROM Workspace w WHERE w.name = :name AND w.owner = :owner")
    Workspace findByName(@Param("name")String name, @Param("owner")int owner);
    
    @Query("SELECT w FROM Workspace w"
                + " JOIN w.owner o"
                + " JOIN o.userAccount u"
                + " WHERE w.name = :name AND u.username = :username")
    Workspace findByName(@Param("name")String name,@Param("username")String userAccount);
    
    @Query("SELECT w FROM Workspace w "
    		+ "JOIN w.workspaceTags t WHERE t.id= :id")
    Collection<Workspace> findByTag(@Param("id")String tagId);
    
    @Query("SELECT w FROM Workspace w "
                    + "JOIN w.workspaceTags t WHERE t.name=:name")
    Collection<Workspace> findByTagName(@Param("name")String tagName);
    
    @Query("SELECT w FROM Workspace w WHERE w.origin = :id")
    Collection<Workspace> findByOrigin(@Param("id")String id);
}