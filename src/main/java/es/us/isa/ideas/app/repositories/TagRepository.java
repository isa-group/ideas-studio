/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import es.us.isa.ideas.app.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository  extends JpaRepository<Tag, Integer> {
    
    @Query("SELECT t FROM Tag AS t WHERE t.id = ?")
    Tag findById(Integer id);

    @Query("SELECT t FROM Tag AS t WHERE t.name = ?")
    Tag findByName(String name);
    
    @Query("SELECT t FROM Tag t "
                + "JOIN t.workspaces w "
                + "JOIN w.owner o"
                + " JOIN o.userAccount u"
                + " WHERE w.name = ? AND u.username = ?")
    Collection<Tag> findByWorkspaceName(String workspaceName, String username);

    @Query("SELECT t FROM Tag t "
                + "JOIN t.workspaces w "
                + "WHERE w.name != NULL")
    Collection<Tag> findTagsInUse();
    
}
