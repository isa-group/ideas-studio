/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.us.isa.ideas.app.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.us.isa.ideas.app.security.LoginService;
/**
 *
 * @author japarejo
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
    @Autowired
    LoginService loginService;
    
    @Autowired
    DataSource datasource;
    /*
    @Autowired
    DigestAuthenticationEntryPoint digestEntryPoint;
            
    @Bean
    public DigestAuthenticationFilter digestAuthenticationService(){
        DigestAuthenticationFilter result=new DigestAuthenticationFilter();
        result.setUserDetailsService(loginService);
        result.setAuthenticationEntryPoint(digestEntryPoint);
        return result;
        
        
    }
   
    @Bean
    public DigestAuthenticationEntryPoint digestEntryPoint(@Value("${security.digestReamlName}") String realmName,@Value("${security.digestKey}") String key){
        digestEntryPoint=new DigestAuthenticationEntryPoint();
        digestEntryPoint.setRealmName(realmName);
        digestEntryPoint.setKey(key);
        return digestEntryPoint;
    }                
    */
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
      throws Exception {
        auth.userDetailsService(loginService)
            .passwordEncoder(encoder())
            .and()
            .authenticationProvider(authenticationProvider())
            .jdbcAuthentication()
            .dataSource(datasource);
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(loginService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new MessageDigestPasswordEncoder("MD5");
    }

    
    /*
    <!--===============================-->
    <!--  ACCESS CONTROL CONFIGURATION -->
    <!--===============================-->    
<bean id="ajaxTimeoutRedirectFilter" class="es.us.isa.ideas.app.security.IdeasTimeoutRedirectFilter">
    <property name="customSessionExpiredErrorCode" value="901"/>
</bean>
<!--     <security:http auto-config="true" use-expressions="true" entry-point-ref="authenticationEntryPoint">    -->
        <security:http auto-config="true" use-expressions="true">          

        
        <!-- Enables Spring Security CSRF protection -->
        <!--<security:csrf/>-->
        <!--================-->
        <!-- Access control -->
        <!--================-->
        <security:intercept-url pattern="/" access="permitAll" />
        <security:intercept-url pattern="/j_spring_security_check" access="permitAll"/>
        <security:intercept-url pattern="/welcome/**" access="permitAll" />
        <!-- Basic Resources access -->
        <security:intercept-url pattern="/views/help/**" access="permitAll" />
        <security:intercept-url pattern="/#{studioConfiguration.helpURI}" access="permitAll"  requires-channel="https" />
        <security:intercept-url pattern="/favicon.ico" access="permitAll"  requires-channel="https" />
        <security:intercept-url pattern="/img/**" access="permitAll"  requires-channel="https" />
        <security:intercept-url pattern="/js/**" access="permitAll"  requires-channel="https" />
        <security:intercept-url pattern="/fonts/**" access="permitAll"  requires-channel="https" />
        <security:intercept-url pattern="/css/**" access="permitAll"  requires-channel="https" />
        <!-- Required by he Sprint social connection controller -->
        <security:intercept-url pattern="/connect/**" access="permitAll"  requires-channel="https"/>
        <security:intercept-url pattern="/disconnect/**" access="permitAll"  requires-channel="https" />
        
        <security:intercept-url pattern="/resources/**" access="hasRole('RESEARCHER')" requires-channel="https" />                                            
<!--         <security:intercept-url pattern="/resources/**" access="permitAll" requires-channel="https" /> -->
<!--         <security:intercept-url pattern="/errorpages/**" access="permitAll" requires-channel="https"/> -->
        <security:intercept-url pattern="/resources/**" access="permitAll" />
        <security:intercept-url pattern="/errorpages/**" access="permitAll"/>
        <security:intercept-url pattern="/signin/**" access="permitAll"/>
        <security:intercept-url pattern="/social/signin/**" access="permitAll"/>
        <security:intercept-url pattern="/signup/**" access="permitAll"/>
        <security:intercept-url pattern="/social/signup/**" access="permitAll"/>
        <security:intercept-url pattern="/security/login" access="permitAll" requires-channel="https"/>
        <security:intercept-url pattern="/security/loginFailure" access="permitAll" requires-channel="https"/>
        <security:intercept-url pattern="/security/useraccount/list" access="hasRole('ADMIN')"  requires-channel="https"/>
        <security:intercept-url pattern="/security/useraccount/edit" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/security/useraccount/general" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/security/useraccount/resetPassword" access="permitAll" requires-channel="https"/>                
        <security:intercept-url pattern="/researcher/create" access="permitAll" requires-channel="https"/>
        <security:intercept-url pattern="/researcher/list" access="hasRole('ADMIN') || hasRole('RESEARCHER')" requires-channel="https"/>
        <security:intercept-url pattern="/researcher/edit" access="hasRole('ADMIN') || hasRole('RESEARCHER')" requires-channel="https"/>        
        <security:intercept-url pattern="/researcher/social" access="hasRole('ADMIN') || hasRole('RESEARCHER')" requires-channel="https"/>
        <security:intercept-url pattern="/researcher/principaluser" access="hasRole('ADMIN') || hasRole('RESEARCHER')" requires-channel="https"/>          
        <security:intercept-url pattern="/researcher/delete" access="hasRole('ADMIN') || hasRole('RESEARCHER')" />             
        <security:intercept-url pattern="/confirm/**" access="permitAll"  requires-channel="https"/>
        <security:intercept-url pattern="/app/wsm" access="permitAll"  requires-channel="https"/>
        <security:intercept-url pattern="/app/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/app_content/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/app_content/app_administration_content" access="hasRole('ADMIN')"  requires-channel="https"/>
        <security:intercept-url pattern="/files/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/tags/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/views/workspace/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/workspaces/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
<!--        <security:intercept-url pattern="/wsm" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>-->
        <security:intercept-url pattern="/analyse/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/command/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/convert/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/settings/user" access="permitAll" requires-channel="https"/>
        <security:intercept-url pattern="/settings/useraccount" access="hasRole('ADMIN') || hasRole('RESEARCHER')" requires-channel="https"/>
        <security:intercept-url pattern="/settings/admin" access="hasRole('ADMIN')" requires-channel="https"/>
        <security:intercept-url pattern="/settings/detail" access="hasRole('ADMIN')" requires-channel="https"/>
        <security:intercept-url pattern="/module/**" access="permitAll"/>
        <security:intercept-url pattern="/demo/**" access="permitAll"/>
        
        <security:intercept-url pattern="/**" access="hasRole('ADMIN')" requires-channel="https"/>
        
         <security:custom-filter ref="ajaxTimeoutRedirectFilter" after="EXCEPTION_TRANSLATION_FILTER"/>
//        <security:form-login login-page="/security/login"
                                 password-parameter="password" username-parameter="username"
                                 default-target-url="/app/wsm"
                                 authentication-failure-url="/security/loginFailure"
                                   />

        <security:logout logout-success-url="/welcome" invalidate-session="true" delete-cookies="JESSIONID"/>
        
        <!-- Adds social authentication filter to the Spring Security filter chain.
        <security:custom-filter ref="socialAuthenticationFilter" before="PRE_AUTH_FILTER" />  -->
        
        <!-- Authentication models are basic and digest for th restfull APIs -->
        <!-- <security:http-basic />
        <security:custom-filter ref="digestFilter" after="BASIC_AUTH_FILTER" /> -->
        <security:port-mappings>
            <security:port-mapping http="8888" https="8881" />
        </security:port-mappings>
    </security:http>
    
    <!--===============================-->
    <!-- BASIC SECURITY INFRASTRUCTURE -->
    <!--===============================-->

    <!--
        Configures the authentication manager bean which processes authentication
        requests.
    -->
    <security:authentication-manager alias="authenticationManager">
        <security:authentication-provider user-service-ref="loginService">
            <security:password-encoder ref="passwordEncoder"/>
        </security:authentication-provider>

       <!-- <security:authentication-provider ref="socialAuthenticationProvider"/>-->
    </security:authentication-manager>

    <bean id="loginService" class="es.us.isa.ideas.app.security.LoginService" />

    <bean id="passwordEncoder"
          class="org.springframework.security.authentication.encoding.Md5PasswordEncoder" />               
         
    <!-- Digest authentication -->                
    <bean id="digestFilter" class="org.springframework.security.web.authentication.www.DigestAuthenticationFilter">
        <property name="userDetailsService" ref="loginService" />
        <property name="authenticationEntryPoint" ref="digestEntryPoint" />
    </bean>
   
    <bean id="digestEntryPoint" class="org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint">
        <property name="realmName" value="${security.digestReamlName}"/>
        <property name="key" value="${security.digestKey}" />
    </bean>
    
    
    
    
    
    */
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
            .authorizeRequests()
                .antMatchers("/","/h2-console/*", "/home","/welcome/**",
                            "/views/help/**","/welcome/**",
                            // Static resources:
                            "/favicon.ico","/img/**", "/js/**","/fonts/**","/css/**",
                            // Spring Social controllers:
                            "/connect/**","/disconnect/**", "/signin/**",
                            "/social/signin/**","/signup/**","/social/signup/**",
                            // Security controllers:
                            "/security/login","/security/loginFailure",
                            "/security/useraccount/resetPassword","/confirm/**",
                            "/settings/user",
                            // Public demos and modules:
                           "/module/**","/demo/**",
                           // Error pages and other resources:
                           "/resources/**","/errorpages/**",
                           // Soma additiona domain-specific public pages:
                           "/researcher/create","/app/wsm")
                            .permitAll()                                
                .anyRequest().authenticated()
            .and()
                .formLogin()
                    .loginPage("/security/login")
                    .loginProcessingUrl("/security/login")
                    //.defaultSuccessUrl("/app/editor")
                    /*.passwordParameter("password")
                    .usernameParameter("username")
                    .failureUrl("/security/loginFailure")
                    .defaultSuccessUrl("/app/wsm")*/
            .and()
                .logout()
                    .logoutSuccessUrl("/welcome");
                                   
                
        
        /*
        <security:intercept-url pattern="/security/useraccount/list" access="hasRole('ADMIN')"  requires-channel="https"/>                               
        <security:intercept-url pattern="/researcher/list" access="hasRole('ADMIN') || hasRole('RESEARCHER')" requires-channel="https"/>
        <security:intercept-url pattern="/researcher/edit" access="hasRole('ADMIN') || hasRole('RESEARCHER')" requires-channel="https"/>        
        <security:intercept-url pattern="/researcher/social" access="hasRole('ADMIN') || hasRole('RESEARCHER')" requires-channel="https"/>
        <security:intercept-url pattern="/researcher/principaluser" access="hasRole('ADMIN') || hasRole('RESEARCHER')" requires-channel="https"/>          
        <security:intercept-url pattern="/researcher/delete" access="hasRole('ADMIN') || hasRole('RESEARCHER')" />                             
        <security:intercept-url pattern="/app/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/app_content/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/app_content/app_administration_content" access="hasRole('ADMIN')"  requires-channel="https"/>
        <security:intercept-url pattern="/files/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/tags/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/views/workspace/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/workspaces/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
<!--        <security:intercept-url pattern="/wsm" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>-->
        <security:intercept-url pattern="/analyse/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/command/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>
        <security:intercept-url pattern="/convert/**" access="hasRole('ADMIN') || hasRole('RESEARCHER')"  requires-channel="https"/>        
        <security:intercept-url pattern="/settings/useraccount" access="hasRole('ADMIN') || hasRole('RESEARCHER')" requires-channel="https"/>
        <security:intercept-url pattern="/settings/admin" access="hasRole('ADMIN')" requires-channel="https"/>
        <security:intercept-url pattern="/settings/detail" access="hasRole('ADMIN')" requires-channel="https"/>*/        
        
       
       http.authorizeRequests().antMatchers("/").permitAll().and()
                .authorizeRequests().antMatchers("/console/**").permitAll();
 
        http.csrf().disable();
        http.headers().frameOptions().disable();
       
    }
}
