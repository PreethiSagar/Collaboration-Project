package com.niit.daoImpl;

import java.util.List;

import javax.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.niit.dao.FeedbackDao;
import com.niit.model.Feedback;

@Repository
@Transactional
public class FeedbackDaoImpl implements FeedbackDao
{
	@Autowired
	private SessionFactory sessionFactory;
	
	public void submitFeedback(Feedback feedback) 
	{
		Session session = sessionFactory.getCurrentSession();
		session.save(feedback);
	}

	public List<Feedback> getCustomerFeedbacks() 
	{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Feedback");
		List<Feedback> customerFeedbacks = query.list();
		return customerFeedbacks;
	}
}
