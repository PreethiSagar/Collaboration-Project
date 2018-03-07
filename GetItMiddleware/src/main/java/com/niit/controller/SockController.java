package com.niit.controller;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import com.niit.model.Chat;

@Controller
public class SockController 
{
	private static final Log logger = LogFactory.getLog(SockController.class);
	
	// To send data to client
	private final SimpMessagingTemplate messagingTemplate;
	
	// List of users who have joined the chat room
	private List<String> users = new ArrayList<String>();
	
	@Autowired
	public SockController(SimpMessagingTemplate messagingTemplate) 
	{
		this.messagingTemplate = messagingTemplate;
	}
	
	// $stompClient.subscribeMapping("app/join/"+$scope.user, function(message) {}) - client side code
	@SubscribeMapping("/join/{username}")
	public List<String> join(String username)
	{
		System.out.println("username in SockController "+username);
		if(!users.contains(username))
			users.add(username);
		System.out.println("====JOIN==== "+username);
		messagingTemplate.convertAndSend("/topic/join", username);	// to all other users in chatroom
		return users;	// newly joined user
	}
	
	// $stompClient.send("app/chat")
	@MessageMapping(value="/chat")
	public void chatReceived(Chat chat)
	{
		if("all".equals(chat.getTo()))
		{
			System.out.println("IN CHAT REVEIVED "+chat.getMessage()+" "+chat.getFrom()+" to "+chat.getTo());
			messagingTemplate.convertAndSend("/queue/chats", chat);
		}
		else
		{
			System.out.println("CHAT TO "+chat.getTo()+" From "+chat.getFrom()+" Message "+chat.getMessage());
			messagingTemplate.convertAndSend("/queue/chats/"+chat.getFrom(), chat);
			messagingTemplate.convertAndSend("/queue/chats/"+chat.getTo(), chat);
		}
	}
}