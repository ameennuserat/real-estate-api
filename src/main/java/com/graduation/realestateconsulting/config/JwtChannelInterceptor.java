package com.graduation.realestateconsulting.config;

import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                String username = jwtService.extractUsername(token);
                User user = userRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("user with email " + username + " is not found"));

                if (!jwtService.isTokenValid(token,user)) {
                    throw new JwtException("Invalid JWT token");
                }

                accessor.setUser(() -> username);
            } else {
                throw new JwtException("No JWT token found");
            }
        }
        return message;
    }
}
