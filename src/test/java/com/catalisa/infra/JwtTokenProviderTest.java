package com.catalisa.infra;



import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import com.catalisa.taxes_api.infra.jwt.JwtTokenProvider;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenProvider = new JwtTokenProvider();
    }

    @SuppressWarnings("deprecation")
    @Test
    void testGenerateToken() {

        when(authentication.getName()).thenReturn("testuser");

        
        String token = jwtTokenProvider.generateToken(authentication);


        assertNotNull(token);
        String username = Jwts.parser()
                .setSigningKey(jwtTokenProvider.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        assertEquals("testuser", username);
    }

    @Test
    void testGetUsername() {

        when(authentication.getName()).thenReturn("testuser");
        String token = jwtTokenProvider.generateToken(authentication);

        
        String username = jwtTokenProvider.getUsername(token);


        assertEquals("testuser", username);
    }

    @Test
    void testValidateToken_ValidToken() {

        when(authentication.getName()).thenReturn("testuser");
        String token = jwtTokenProvider.generateToken(authentication);

        
        boolean isValid = jwtTokenProvider.validateToken(token);


        assertTrue(isValid);
    }

    @Test
    void testValidateToken_ExpiredToken() throws InterruptedException {

        jwtTokenProvider = new JwtTokenProvider() {
            @Override
            public String generateToken(Authentication authentication) {
                String username = authentication.getName();
                Date currentDate = new Date();
                Date expireDate = new Date(currentDate.getTime() + 1000);

                return Jwts.builder()
                        .subject(username)
                        .issuedAt(currentDate)
                        .expiration(expireDate)
                        .signWith(getSigningKey())
                        .compact();
            }
        };
        when(authentication.getName()).thenReturn("testuser");
        String token = jwtTokenProvider.generateToken(authentication);


        Thread.sleep(2000);


        assertThrows(ExpiredJwtException.class, () -> jwtTokenProvider.validateToken(token));
    }

    @Test
    void testValidateToken_InvalidToken() {

        String invalidToken = "invalid.token.value";


        assertThrows(Exception.class, () -> jwtTokenProvider.validateToken(invalidToken));
    }
}