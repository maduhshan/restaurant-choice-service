package gov.sg.tech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("application.activemq.host")
    private String hostName;

    @Value("application.activemq.user")
    private String username;

    @Value("application.activemq.port")
    private Integer port;

    @Value("application.activemq.password")
    private String password;

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry config) {
        // needs to enable the activemq server to broker the messages via given hostname and port
        config.enableStompBrokerRelay("/topic")
                .setRelayHost(hostName)
                .setRelayPort(port)
                .setClientLogin(username)
                .setClientPasscode(password);
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").withSockJS();
    }
}
