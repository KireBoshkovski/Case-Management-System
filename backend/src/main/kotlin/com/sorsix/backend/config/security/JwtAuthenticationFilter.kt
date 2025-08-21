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
        return path.startsWith("/api/auth/")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt = parseJwt(request)

            if (!jwt.isNullOrBlank()) {
                val claims: Claims? = jwtUtils.getClaims(jwt)

                if (claims != null && claims.expiration.after(Date())) {
                    val username = claims.subject
                    if (!username.isNullOrBlank()) {
                        val userDetails = userDetailsService.loadUserByUsername(username)
                        val authentication = UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.authorities
                        )
                        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authentication
                    }
                }
            }
        } catch (e: Exception) {
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")
        return if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            headerAuth.substring(7)
        } else null
    }
}