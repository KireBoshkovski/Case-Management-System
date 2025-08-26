package com.sorsix.backend.config.notifications

import com.sorsix.backend.config.security.JWTUtility
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
class WebSocketSecurityConfig(
    val jwtUtility: JWTUtility,
    val userDetailsService: UserDetailsService
) : WebSocketMessageBrokerConfigurer {
    private val logger = LoggerFactory.getLogger(WebSocketSecurityConfig::class.java)

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        logger.info("Configuring WebSocket client inbound channel interceptor")

        registration.interceptors(object : ChannelInterceptor {
            override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
                val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)

                logger.debug("Processing WebSocket message with command: {}", accessor?.command)

                if (StompCommand.CONNECT == accessor?.command) {
                    logger.debug("Processing STOMP CONNECT command")
                    val authHeader = accessor.getFirstNativeHeader("Authorization")

                    try {
                        if (authHeader != null && authHeader.startsWith("Bearer ")) {
                            logger.debug("Authorization header found, extracting JWT token")
                            val jwt = authHeader.substring(7)

                            if (jwtUtility.validateToken(jwt)) {
                                logger.debug("JWT token is valid, extracting username")
                                val username = jwtUtility.getUsernameFromJwtToken(jwt)
                                logger.info("Authenticating WebSocket connection for user: {}", username)
                                logger.debug("Loading user details for username: {}", username)
                                val userDetails = userDetailsService.loadUserByUsername(username)
                                logger.debug("User details loaded successfully for user: {}", username)

                                val authentication = UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.authorities
                                )
                                accessor.user = authentication
                                SecurityContextHolder.getContext().authentication = authentication
                                logger.info("WebSocket authentication successful for user: {}", username)
                            } else {
                                logger.warn("Invalid JWT token provided for WebSocket connection")
                            }
                        }
                    } catch (e: Exception) {
                        logger.error("Error during WebSocket authentication", e)
                    }
                } else {
                    logger.trace("Non-CONNECT STOMP command, skipping authentication: {}", accessor?.command)
                }
                return message
            }
        })
        logger.info("WebSocket security configuration completed")
    }
}