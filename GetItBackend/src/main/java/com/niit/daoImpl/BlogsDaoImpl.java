package com.niit.daoImpl;

import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.niit.dao.BlogsDao;
import com.niit.model.BlogComments;
import com.niit.model.BlogLikes;
import com.niit.model.Blogs;
import com.niit.model.Notification;
import com.niit.model.User;

@Repository
@Transactional
public class BlogsDaoImpl implements BlogsDao 
{
	@Autowired
	private SessionFactory sessionFactory;
	
	public void saveBlogs(Blogs blogs) 
	{
		Session session = sessionFactory.getCurrentSession();
		session.save(blogs);
	}

	public List<Blogs> getBlogs(int approved) 
	{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Blogs where approved = "+approved);
		return query.list();
	}

	public Blogs getBlogById(int id) 
	{
		Session session = sessionFactory.getCurrentSession();
		Blogs blogDetail = (Blogs)session.get(Blogs.class, id);
		return blogDetail;
	}

	public void updateApprovalStatus(Blogs blogs, String rejectionReason) 
	{
		Session session = sessionFactory.getCurrentSession();
		Notification notification = new Notification();
		notification.setBlogTitle(blogs.getBlogTitle());
		notification.setUserName(blogs.getPostedBy().getUserName());
		if(blogs.isApproved())
		{
			session.update(blogs);
			notification.setApprovalStatus("Approved");
			session.save(notification);
		}
		else
		{
			if(rejectionReason == null)
				notification.setRejectionReason("Not Mentioned by Admin");
			else
				notification.setRejectionReason(rejectionReason);
			notification.setApprovalStatus("Rejected");
			session.save(notification);
			session.delete(blogs);
		}
	}

	public void updateBlog(Blogs blog) 
	{
		Session session = sessionFactory.getCurrentSession();
		session.update(blog);		
	}

	public BlogLikes userLikedPost(Blogs blog, User user) 
	{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from BlogLikes where blog.id=? and user.userName=?");
		query.setInteger(0, blog.getId());
		query.setString(1, user.getUserName());
		BlogLikes blogLikes = (BlogLikes)query.uniqueResult();
		return blogLikes;
	}

	public Blogs updateLikes(Blogs blog, User user) 
	{
		Session session = sessionFactory.getCurrentSession();
		BlogLikes blogLikes = userLikedPost(blog, user);
		if(blogLikes == null)
		{
			//Like Section
			BlogLikes insertUserLikes = new BlogLikes();
			insertUserLikes.setBlog(blog);
			insertUserLikes.setUser(user);
			session.save(insertUserLikes);
			blog.setLikes(blog.getLikes() + 1);
			session.update(blog);
		}
		else
		{
			//Unlike Section
			session.delete(blogLikes);
			blog.setLikes(blog.getLikes() - 1);
			session.merge(blog);
		}
		return blog;
	}

	public void addBlogComment(BlogComments blogComments) 
	{
		Session session = sessionFactory.getCurrentSession();
		session.save(blogComments);
	}
}
