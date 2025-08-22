package com.sorsix.backend.config.security

import com.sorsix.backend.domain.enums.UserRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JWTUtility {
    @Value("\${app.jwt.secret}")
    private lateinit var jwtSecret: String

    @Value("\${app.jwt.expiration}")
    private var jwtExpirationMs: Long = 0

    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    fun generateJwtToken(userDetails: CustomUserDetails): String {
        return generateTokenFromUsername(userDetails.username, userDetails.getRole(), jwtExpirationMs)
    }

    private fun generateTokenFromUsername(username: String, role: UserRole, expiration: Long): String {
        return Jwts.builder()
            .setSubject(username)
            .claim("role", role.name)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + expiration))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun isTokenExpired(token: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
            claims.expiration.before(Date())
        } catch (e: Exception) {
            true
        }
    }

    fun getUsernameFromJwtToken(token: String?): String {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun validateToken(jwt: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .body

            !claims.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun getClaims(jwt: String): Claims? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .body
        } catch (e: Exception) {
            null
        }
    }
}