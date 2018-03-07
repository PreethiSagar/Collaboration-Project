package com.niit.daoImpl;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.niit.dao.JobNotificationDao;
import com.niit.model.Job;
import com.niit.model.User;

@Repository
@Transactional
public class JobNotificationDaoImpl implements JobNotificationDao 
{
	@Autowired
	private SessionFactory sessionFactory;
	
	public void addNotifierUser(User user, Job job) 
	{
		Session session = sessionFactory.getCurrentSession();
		List<User> notifiedUsers = job.getNotifiedUsers();
		if(notifiedUsers == null)
			notifiedUsers = new ArrayList<User>();
		notifiedUsers.add(user);
		session.update(job);
	}

	public List<Job> jobNotification(User user) 
	{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Job");
		List<Job> jobs = query.list();
		List<Job> jobsToBeNotified = new ArrayList<Job>();
		for(Job job:jobs)
		{
			List<User> notifiedUsers = job.getNotifiedUsers();
			if(!notifiedUsers.contains(user))
				jobsToBeNotified.add(job);
		}
		System.out.println(jobsToBeNotified.size());
		for(Job j:jobsToBeNotified)
			System.out.println(j.getId()+" - "+j.getJobTitle());
		return jobsToBeNotified;
	}
}
