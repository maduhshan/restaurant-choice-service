package gov.sg.tech.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");

        /* needs to enable the activemq server to broker the messages via given hostname and port
        This code is disabled at this scope.
        config.enableStompBrokerRelay("/topic")
                .setRelayHost(hostName)
                .setRelayPort(port)
                .setClientLogin(username)
                .setClientPasscode(password);
         */
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/restaurantPicker").withSockJS();
    }
}