package com.niit.dao;

import java.util.List;
import com.niit.model.Job;
import com.niit.model.User;

public interface JobNotificationDao 
{
	void addNotifierUser(User user, Job job);
	List<Job> jobNotification(User user);
}
