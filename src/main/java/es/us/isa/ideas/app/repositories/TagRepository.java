/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import es.us.isa.ideas.app.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository  extends JpaRepository<Tag, Integer> {
    
    @Query("SELECT t FROM Tag AS t WHERE t.id = :id")
    Optional<Tag> findById(@Param("id")Integer id);

    @Query("SELECT t FROM Tag AS t WHERE t.name = :name")
    Optional<Tag> findByName(@Param("name")String name);
    
    @Query("SELECT t FROM Tag t "
                + "JOIN t.workspaces w "
                + "JOIN w.owner o"
                + " JOIN o.userAccount u"
                + " WHERE w.name = :workspacename AND u.username = :username")
    Collection<Tag> findByWorkspaceName(@Param("workspacename")String workspaceName, @Param("username")String username);

    @Query("SELECT t FROM Tag t "
                + "JOIN t.workspaces w "
                + "WHERE w.name != NULL")
    Collection<Tag> findTagsInUse();
    
}
