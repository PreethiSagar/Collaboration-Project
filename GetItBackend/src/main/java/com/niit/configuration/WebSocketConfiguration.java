package com.niit.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
// Enable stomp bases messaging
// Auto-detecting annotation @MessageMapping & @SubscribeMapping
@EnableWebSocketMessageBroker
@ComponentScan(basePackages="com.niit")
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer 
{
	public void registerStompEndpoints(StompEndpointRegistry registry) 
	{
		System.out.println("REGISTER STOMP ENDPOINTS...");
		// JS Stomp.over("../chatmodule")
		registry.addEndpoint("/chatmodule").withSockJS();
	}
	
	public void configureClientInboundChannel(ChannelRegistration registration) 
	{
		// TODO Auto-generated method stub		
	}

	public void configureClientOutboundChannel(ChannelRegistration registration) 
	{
		// TODO Auto-generated method stub		
	}

	public void configureMessageBroker(MessageBrokerRegistry configurer) 
	{
		System.out.println("CONFIGURE MESSAGE BROKER REGISTRY");
		// enableSimpleBroker destination prefixes - by Spring Controller to send messages to client
		// to send data from server to client
		// topic - to notify the newly joined username
		// queue - to send chat message
		// communication direction - from server to client
		// server users destination /queue, /topic to send messages to the client
		// client will receive the message by subscribing $scope.subscribe("/topic/join",..)
		configurer.enableSimpleBroker("/queue/", "/topic");	//server side
		// client to server - destination prefix as /app
		// in client side ..send("/app/join", ..)
		configurer.setApplicationDestinationPrefixes("/app");	//client side
	}
}