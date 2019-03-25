package es.us.isa.ideas.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("es.us.isa.ideas.app")
@EntityScan("es.us.isa.ideas.app")
@SpringBootApplication
public class IdeasStudioApplication /*extends SpringBootServletInitializer*/ {

    public static void main(String[] args) {
        SpringApplication.run(IdeasStudioApplication.class, args);
    }

    /* The extends and this overrided method are needed for using the app with tomcat 
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(IdeasStudioApplication.class);
    }*/

    /**
     * Start internal H2 server so we can query the DB from IDE
     *
     * @return H2 Server instance
     * @throws SQLException
     *
     * @Bean(initMethod = "start", destroyMethod = "stop") public Server
     * h2Server() throws SQLException { return Server.createTcpServer("-tcp",
     * "-tcpAllowOthers", "-tcpPort", "9092"); }      
     *
     */
}
