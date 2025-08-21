package com.sorsix.backend.config.notifications

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    /**
     * Registers the STOMP endpoint that clients will use to connect.
     * "/ws" is the connection endpoint.
     * withSockJS() provides a fallback for browsers that don't support WebSockets.
     */
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
    }

    /**
     * Configures the message broker.
     * "/queue" are prefixes for destinations that the broker will handle.
     * "/app" is the prefix for application-specific destinations that map to @MessageMapping methods.
     */
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        // "/app" → frontend sends to backend using @MessageMapping
        registry.setApplicationDestinationPrefixes("/app")
        // "/queue" → backend sends messages to a specific user
        registry.enableSimpleBroker("/queue")
    }
}