package com.sorsix.backend.config.security

import com.sorsix.backend.service.UserService
import io.jsonwebtoken.Claims
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class JwtAuthenticationFilter(
    private val jwtUtils: JWTUtility,
    private val userDetailsService: UserService
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.servletPath
        val shouldSkip = path.startsWith("/api/auth/")

        if (shouldSkip) {
            logger.debug("Skipping JWT authentication for path: $path")
        }

        return shouldSkip
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestURI = request.requestURI
        logger.debug("Processing JWT authentication for request: ${request.method} $requestURI")

        try {
            val jwt = parseJwt(request)

            if (!jwt.isNullOrBlank()) {
                logger.debug("JWT token found in request")

                val claims: Claims? = jwtUtils.getClaims(jwt)

                if (claims != null && claims.expiration.after(Date())) {
                    val username = claims.subject
                    if (!username.isNullOrBlank()) {
                        logger.debug("Valid JWT token found for user: $username")

                        val userDetails = userDetailsService.loadUserByUsername(username)
                        val authentication = UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.authorities
                        )
                        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authentication

                        logger.debug("User $username successfully authenticated via JWT")
                    } else {
                        logger.warn("JWT token has no subject (username)")
                    }
                } else {
                    if (claims == null) {
                        logger.warn("Invalid JWT token - unable to parse claims")
                    } else {
                        logger.warn("JWT token is expired. Expiration: ${claims.expiration}")
                    }
                }
            } else {
                logger.debug("No JWT token found in Authorization header")
            }
        } catch (e: Exception) {
            logger.error("JWT authentication failed for request: ${request.method} $requestURI", e)
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")
        return if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            logger.debug("Bearer token found in Authorization header")
            headerAuth.substring(7)
        } else {
            logger.debug("No Bearer token found in Authorization header")
            null
        }
    }
}