package com.niit.daoImpl;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.niit.dao.FriendDao;
import com.niit.model.Friend;
import com.niit.model.User;

@Repository
@Transactional
public class FriendDaoImpl implements FriendDao 
{
	@Autowired
	private SessionFactory sessionFactory;
	
	public List<User> suggestedUsersList(String username) 
	{
		Session session = sessionFactory.getCurrentSession();
		SQLQuery query = session.createSQLQuery("SELECT * FROM tbl_user WHERE userName IN "
				+ " (SELECT userName FROM tbl_user WHERE userName != ? "
				+ " minus "
				+ " (SELECT fromId FROM tbl_friend WHERE toId = ? "
				+ " UNION "
				+ " SELECT toId FROM tbl_friend WHERE fromId = ?))");
		query.setString(0, username);
		query.setString(1, username);
		query.setString(2, username);
		query.addEntity(User.class);
		List<User> suggestedUsers = query.list();
		return suggestedUsers;
	}

	public void addFriendRequest(Friend friend) 
	{
		Session session = sessionFactory.getCurrentSession();
		session.save(friend);
	}

	public List<Friend> pendingRequests(String username) 
	{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Friend where toId=? and status=?");
		query.setString(0, username);
		query.setCharacter(1, 'P');
		List<Friend> pendingRequests = query.list();
		return pendingRequests;
	}

	//Friend -> friend.status='A' for Accept & friend.status='R' for Reject
	public void updatePendingRequest(Friend friend) 
	{
		Session session = sessionFactory.getCurrentSession();
		if(friend.getStatus() == 'A')
			session.update(friend);
		else
			session.delete(friend);
	}

	public List<User> getFriendsList(String username) 
	{
		Session session = sessionFactory.getCurrentSession();

		/*SQLQuery query1 = session.createSQLQuery("SELECT * FROM tbl_user WHERE userName IN "
				+ "(SELECT toId FROM tbl_friend WHERE fromId = ? AND status = 'A')");

		SQLQuery query2 = session.createSQLQuery("SELECT * FROM tbl_user WHERE userName IN "
				+ "(SELECT fromId FROM tbl_friend WHERE toId = ?AND status = 'A')");
		query1.setString(0, username);
		query2.setString(0, username);
		query1.addEntity(User.class);
		query2.addEntity(User.class);		
		List<User> list1 = query1.list();	
		List<User> list2 = query2.list();
		list1.addAll(list2);
		return list1;*/
		
		SQLQuery query = session.createSQLQuery("SELECT * FROM tbl_user WHERE userName IN "
				+ "(SELECT toId FROM tbl_friend WHERE fromId = ? AND status = 'A' "
				+ " UNION "
				+ " SELECT fromId FROM tbl_friend WHERE toId = ? AND status = 'A')");
		query.setString(0, username);
		query.setString(1, username);
		query.addEntity(User.class);
		List<User> friendsList = query.list();
		return friendsList;
	}

	public List<User> mutualFriendsList(String loginId, String suggestedUser) 
	{
		List<User> list1 = getFriendsList(loginId);
		List<User> list2 = getFriendsList(suggestedUser);
		List<User> mutualFriends = new ArrayList<User>();
		for(User user1:list1)
		{
			for(User user2:list2)
			{
				if(user1.getUserName().equals(user2.getUserName()))
					mutualFriends.add(user1);
			}
		}
		return mutualFriends;
	}
}
