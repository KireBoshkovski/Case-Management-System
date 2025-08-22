package com.sorsix.backend.config.notifications

import com.sorsix.backend.config.security.JWTUtility
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
    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(object : ChannelInterceptor {
            override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
                val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)

                if (StompCommand.CONNECT == accessor?.command) {
                    val authHeader = accessor.getFirstNativeHeader("Authorization")

                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        val jwt = authHeader.substring(7)

                        if (jwtUtility.validateToken(jwt)) {
                            val username = jwtUtility.getUsernameFromJwtToken(jwt)
                            val userDetails = userDetailsService.loadUserByUsername(username)

                            val authentication = UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.authorities
                            )
                            accessor.user = authentication
                            SecurityContextHolder.getContext().authentication = authentication
                        }
                    }
                }
                return message
            }
        })
    }
}