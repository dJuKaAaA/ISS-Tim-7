package com.tim7.iss.tim7iss.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/api/socket")
				.setAllowedOrigins("http://localhost:4200")
				.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/socket-subscriber")
				.enableSimpleBroker(
						"/socket-schedule-ride",
						"/socket-send-message",
						"/socket-driver-movement",
						"/socket-scheduled-ride",  // RideDto
						"/socket-ride-evaluation",   // RideDto
						"/socket-notify-start-ride",  // RideDto
						"/socket-notify-arrived-at-departure"  // RideDto
				);
	}

}
