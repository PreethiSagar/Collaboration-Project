package com.niit.dao;

import java.util.List;

import com.niit.model.Friend;
import com.niit.model.User;

public interface FriendDao 
{
	List<User> suggestedUsersList(String username);
	void addFriendRequest(Friend friend);
	List<Friend> pendingRequests(String username);
	void updatePendingRequest(Friend friend);
	List<User> getFriendsList(String username);
	List<User> mutualFriendsList(String loginId, String suggestedUser);
}
