package com.niit.daoImpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.niit.dao.JobDao;
import com.niit.model.Job;

@Repository
@Transactional
public class JobDaoImpl implements JobDao
{
	@Autowired
	private SessionFactory sessionFactory;
	
	public void saveJob(Job job) 
	{
		Session session = sessionFactory.getCurrentSession();	
		session.save(job);
	}

	public List<Job> getAllJobs() 
	{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Job");
		return query.list();
	}

	public Job getJob(int jobId) 
	{
		Session session = sessionFactory.getCurrentSession();
		Job job = (Job)session.get(Job.class, jobId);
		return job;
	}

	public void updateJob(Job job) 
	{
		Session session = sessionFactory.getCurrentSession();
		session.update(job);   //Update the job details
		
	}

	public void deleteJob(Job job) 
	{
		Session session = sessionFactory.getCurrentSession();
		session.delete(job);
	}

	public List<Job> latestJobs() 
	{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("FROM Job ORDER BY id DESC");
		query.setMaxResults(6);
		List<Job> latestJobs = query.list();
		return latestJobs;
	}
}
