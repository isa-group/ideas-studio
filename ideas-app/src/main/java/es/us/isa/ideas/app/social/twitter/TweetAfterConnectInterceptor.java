package es.us.isa.ideas.app.social.twitter;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.web.ConnectInterceptor;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author japarejo
 */
public class TweetAfterConnectInterceptor implements ConnectInterceptor<Twitter> {

        
    @Override
    public void preConnect(ConnectionFactory<Twitter> cf, MultiValueMap<String, String> mvm, WebRequest wr) {
        
    }

    @Override
    public void postConnect(Connection<Twitter> cnctn, WebRequest wr) {
            cnctn.updateStatus("I'm using E3 (Experiment Execution Environtment) for my empirical research! You can find more information on E3 and other components of MOSES at http://www.moses.us.es");
    }

}
