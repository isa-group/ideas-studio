package es.us.isa.ideas.app;

import es.us.isa.ideas.app.configuration.StudioConfiguration;
import java.sql.SQLException;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("es.us.isa.ideas.app") 
@EntityScan("es.us.isa.ideas.app")
@SpringBootApplication
public class IdeasStudioApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdeasStudioApplication.class, args);
	}
        
        /**
     * Start internal H2 server so we can query the DB from IDE
     *
     * @return H2 Server instance
     * @throws SQLException
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }        

}
