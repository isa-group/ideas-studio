package es.us.isa.ideas.app.social;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.social.connect.jpa.JpaTemplate;
import org.springframework.social.connect.jpa.RemoteUser;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author japarejo
 */
@Repository
@Transactional
public class CustomConnectionRepository implements ConnectionRepository {
        
    private JpaTemplate jpaTemplate;
    private String userId;
    private ConnectionFactoryLocator connectionFactoryLocator;
    private TextEncryptor textEncryptor;        
    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        return findAllConnections(getUserId());
    }
    
    public MultiValueMap<String, Connection<?>> findAllConnections(String customUserId) {
        List<Connection<?>> resultList = getConnectionMapper().mapEntities(getJpaTemplate().getAll(customUserId));

        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        Set<String> registeredProviderIds = getConnectionFactoryLocator().registeredProviderIds();
        for (String registeredProviderId : registeredProviderIds) {
            connections.put(registeredProviderId, Collections.<Connection<?>>emptyList());
        }
        for (Connection<?> connection : resultList) {
            String providerId = connection.getKey().getProviderId();
            if (connections.get(providerId).size() == 0) {
                connections.put(providerId, new LinkedList<Connection<?>>());
            }
            connections.add(providerId, connection);
        }
        return connections;
    }

    @Override
    public List<Connection<?>> findConnections(String providerId) {
        return getConnectionMapper().mapEntities(getJpaTemplate().getAll(getUserId(), providerId));
    }

    @SuppressWarnings("unchecked")
    public <A> List<Connection<A>> findConnections(Class< A> apiType) {
        List<?> connections = findConnections(getProviderId(apiType));
        return (List<Connection<A>>) connections;
    }

    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUsers) {
        if (providerUsers.isEmpty()) {
            throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
        }

        List<Connection<?>> resultList = getConnectionMapper().mapEntities(getJpaTemplate().getAll(getUserId(), providerUsers));

        MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
        for (Connection<?> connection : resultList) {
            String providerId = connection.getKey().getProviderId();
            List<String> userIds = providerUsers.get(providerId);
            List<Connection<?>> connections = connectionsForUsers.get(providerId);
            if (connections == null) {
                connections = new ArrayList<Connection<?>>(userIds.size());
                for (int i = 0; i < userIds.size(); i++) {
                    connections.add(null);
                }
                connectionsForUsers.put(providerId, connections);
            }
            String providerUserId = connection.getKey().getProviderUserId();
            int connectionIndex = userIds.indexOf(providerUserId);
            connections.set(connectionIndex, connection);
        }
        return connectionsForUsers;
    }

    public Connection<?> getConnection(ConnectionKey connectionKey) {
        try {
            return getConnectionMapper().mapEntity(getJpaTemplate().get(getUserId(), connectionKey.getProviderId(), connectionKey.getProviderUserId()));
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchConnectionException(connectionKey);
        }
    }

    @SuppressWarnings("unchecked")
    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
    }

    @SuppressWarnings("unchecked")
    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
        if (connection == null) {
            throw new NotConnectedException(providerId);
        }
        return connection;
    }

    @SuppressWarnings("unchecked")
    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) findPrimaryConnection(providerId);
    }

    @Transactional
    public void addConnection(Connection<?> connection) {
        try {
            ConnectionData data = connection.createData();
            int rank = getJpaTemplate().getRank(getUserId(), data.getProviderId());

            getJpaTemplate().createRemoteUser(getUserId(), data.getProviderId(), data.getProviderUserId(), rank, data.getDisplayName(), data.getProfileUrl(), data.getImageUrl(), encrypt(data.getAccessToken()), encrypt(data.getSecret()), encrypt(data.getRefreshToken()), data.getExpireTime());
        } catch (DuplicateKeyException e) {
            throw new DuplicateConnectionException(connection.getKey());
        }
    }

    public void updateConnection(Connection<?> connection) {
        ConnectionData data = connection.createData();

        RemoteUser su = getJpaTemplate().get(getUserId(), data.getProviderId(), data.getProviderUserId());
        if (su != null) {
            su.setDisplayName(data.getDisplayName());
            su.setProfileUrl(data.getProfileUrl());
            su.setImageUrl(data.getImageUrl());
            su.setAccessToken(encrypt(data.getAccessToken()));
            su.setSecret(encrypt(data.getSecret()));
            su.setRefreshToken(encrypt(data.getRefreshToken()));
            su.setExpireTime(data.getExpireTime());

            su = getJpaTemplate().save(su);
        }
    }

    public void removeConnections(String providerId) {
        getJpaTemplate().remove(getUserId(), providerId);
    }

    public void removeConnection(ConnectionKey connectionKey) {
        getJpaTemplate().remove(getUserId(), connectionKey.getProviderId(), connectionKey.getProviderUserId());
    }

    private Connection<?> findPrimaryConnection(String providerId) {
        List<Connection<?>> connections = getConnectionMapper().mapEntities(getJpaTemplate().getPrimary(getUserId(), providerId));
        if (connections.size() > 0) {
            return connections.get(0);
        } else {
            return null;
        }
    }
    
    private ServiceProviderConnectionMapper connectionMapper = new ServiceProviderConnectionMapper();

    

    /**
     * @return the userId
     */
    public String getUserId() {                    
        return userId;
    }
    
    public void setUserId(String value)
    {
        userId=value;
    }

    /**
     * @return the jpaTemplate
     */
    public JpaTemplate getJpaTemplate() {
        return jpaTemplate;
    }

    /**
     * @param jpaTemplate the jpaTemplate to set
     */
    public void setJpaTemplate(JpaTemplate jpaTemplate) {
        this.jpaTemplate = jpaTemplate;
    }

    /**
     * @return the connectionFactoryLocator
     */
    public ConnectionFactoryLocator getConnectionFactoryLocator() {
        return connectionFactoryLocator;
    }

    /**
     * @param connectionFactoryLocator the connectionFactoryLocator to set
     */
    public void setConnectionFactoryLocator(ConnectionFactoryLocator connectionFactoryLocator) {
        this.connectionFactoryLocator = connectionFactoryLocator;
    }

    /**
     * @return the textEncryptor
     */
    public TextEncryptor getTextEncryptor() {
        return textEncryptor;
    }

    /**
     * @param textEncryptor the textEncryptor to set
     */
    public void setTextEncryptor(TextEncryptor textEncryptor) {
        this.textEncryptor = textEncryptor;
    }

    /**
     * @return the connectionMapper
     */
    public ServiceProviderConnectionMapper getConnectionMapper() {
        return connectionMapper;
    }

    /**
     * @param connectionMapper the connectionMapper to set
     */
    public void setConnectionMapper(ServiceProviderConnectionMapper connectionMapper) {
        this.connectionMapper = connectionMapper;
    }

    private final class ServiceProviderConnectionMapper {

        public List<Connection<?>> mapEntities(List<RemoteUser> socialUsers) {
            List<Connection<?>> result = new ArrayList<Connection<?>>();
            for (RemoteUser su : socialUsers) {
                result.add(mapEntity(su));
            }
            return result;
        }

        public Connection<?> mapEntity(RemoteUser socialUser) {
            ConnectionData connectionData = mapConnectionData(socialUser);
            ConnectionFactory<?> connectionFactory = getConnectionFactoryLocator().getConnectionFactory(connectionData.getProviderId());
            return connectionFactory.createConnection(connectionData);
        }

        private ConnectionData mapConnectionData(RemoteUser socialUser) {
            return new ConnectionData(socialUser.getProviderId(), socialUser.getProviderUserId(), socialUser.getDisplayName(), socialUser.getProfileUrl(), socialUser.getImageUrl(),
                    decrypt(socialUser.getAccessToken()), decrypt(socialUser.getSecret()), decrypt(socialUser.getRefreshToken()), expireTime(socialUser.getExpireTime()));
        }

        private String decrypt(String encryptedText) {
            return encryptedText != null ? getTextEncryptor().decrypt(encryptedText) : encryptedText;
        }

        private Long expireTime(Long expireTime) {
            return expireTime == null || expireTime == 0 ? null : expireTime;
        }
    }

    private <A> String getProviderId(Class<A> apiType) {
        return getConnectionFactoryLocator().getConnectionFactory(apiType).getProviderId();
    }

    private String encrypt(String text) {
        return text != null ? getTextEncryptor().encrypt(text) : text;
    }

}
