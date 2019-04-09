package es.us.isa.ideas.app.repositories;


import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import es.us.isa.ideas.app.entities.SocialNetworkConfiguration;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author japarejo
 */
@Repository
public interface SocialNetworkConfigurationRepository  extends JpaRepository<SocialNetworkConfiguration, Integer> {
    @Query("SELECT snc FROM SocialNetworkConfiguration snc WHERE snc.actor.id=:id")
    Collection<SocialNetworkConfiguration> findByActorId(@Param("id")int researcherId);
}
