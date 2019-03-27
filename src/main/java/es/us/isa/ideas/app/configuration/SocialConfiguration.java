/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.configuration;

import es.us.isa.ideas.app.social.SocialConnectionSignup;
import es.us.isa.ideas.app.social.SocialSignInAdapter;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;


@Configuration
@EnableSocial
public class SocialConfiguration implements SocialConfigurer {
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private SocialSignInAdapter signInAdapter;
    
    @Autowired 
    private SocialConnectionSignup socialConnectionSignup;

    @Resource
    public Environment env;
   
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {        
        JdbcUsersConnectionRepository usersConnectionRepository=new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        usersConnectionRepository.setConnectionSignUp(socialConnectionSignup);
        return usersConnectionRepository;
    }
    
    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer cfConfig, Environment env) {
        cfConfig.addConnectionFactory(new TwitterConnectionFactory(
            env.getProperty("twitter.consumerKey"),
            env.getProperty("twitter.consumerSecret")));
        cfConfig.addConnectionFactory(new FacebookConnectionFactory(
            env.getProperty("facebook.clientId"),
            env.getProperty("facebook.clientSecret")));
        cfConfig.addConnectionFactory(new GoogleConnectionFactory(
            env.getProperty("google.consumerKey"),
            env.getProperty("google.consumerSecret")));
    }
    
    
    /**
     * Crea las conexiones sociales para los usuarios cuando se le llame. Ojo!, no dejará
     * a los usuarios autenticados, sino que sencillamente creará la conexión asociada a los usuarios 
     * en el servicio que corresponda y la persistirá. Concretamente este 
     * controlador se encarga de gestionar los callbacks de los distintos proveedores para crear la conexión.
     * 
     * @param connectionFactoryLocator
     * @param connectionRepository
     * @return 
     */
    @Bean
    public ConnectController connectController(
                ConnectionFactoryLocator connectionFactoryLocator,
                ConnectionRepository connectionRepository) {
        ConnectController controller =  new ConnectController(connectionFactoryLocator, connectionRepository);
        controller.setApplicationUrl(env.getProperty("application.url"));

        return controller;
    }
    
    @Bean
    public ProviderSignInController providerSignInController(
            ConnectionFactoryLocator connectionFactoryLocator,
            UsersConnectionRepository usersConnectionRepository) {                
        
        return new ProviderSignInController(
            connectionFactoryLocator,
            usersConnectionRepository,
            signInAdapter);
}

    
}
