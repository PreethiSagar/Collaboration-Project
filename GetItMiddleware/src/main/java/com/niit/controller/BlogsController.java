package com.niit.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.niit.dao.BlogsDao;
import com.niit.dao.UserDao;
import com.niit.model.BlogComments;
import com.niit.model.BlogLikes;
import com.niit.model.Blogs;
import com.niit.model.ErrorClazz;
import com.niit.model.User;

@Controller
public class BlogsController 
{
	@Autowired
	private BlogsDao blogsDao;
	
	@Autowired
	private UserDao userDao;
	
	public BlogsController()
	{
		System.out.println("BlogsController is Instantiated.");
	}
	
	@RequestMapping(value="/saveblog", method=RequestMethod.POST)
	public ResponseEntity<?> saveBlog(@RequestBody Blogs blogs, HttpSession session)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Access.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		User user = userDao.getUserByUsername(username);
		blogs.setPostedOn(new Date());
		blogs.setPostedBy(user);
		try
		{
			blogsDao.saveBlogs(blogs);
		}
		catch(Exception e)
		{
			ErrorClazz error = new ErrorClazz(8, "Unable to insert the blog details "+e.getMessage());
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Blogs>(blogs,HttpStatus.OK);
	}
	
	@RequestMapping(value="/getblogs/{approved}", method=RequestMethod.GET)
	public ResponseEntity<?> getBlogs(HttpSession session, @PathVariable int approved)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Access.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		if(approved == 0)
		{
			User user = userDao.getUserByUsername(username);
			if(!user.getRole().equals("ADMIN"))
			{
				ErrorClazz error = new ErrorClazz(7, "Access Denied.");
				return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
			}
		}
		List<Blogs> blogs = blogsDao.getBlogs(approved);
		return new ResponseEntity<List<Blogs>>(blogs, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getblogdetail/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> getBlogDetail(@PathVariable int id,  HttpSession session)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Access.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		Blogs blogDetail = blogsDao.getBlogById(id);
		return new ResponseEntity<Blogs>(blogDetail, HttpStatus.OK);
	}
	
	@RequestMapping(value="/updateapprovalstatus", method=RequestMethod.PUT)
	public ResponseEntity<?> updateApprovalStatus(HttpSession session, @RequestBody Blogs blogs, @RequestParam(required=false) String rejectionReason)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Access.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		blogsDao.updateApprovalStatus(blogs, rejectionReason);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/editblogsubmit", method=RequestMethod.PUT)
	public ResponseEntity<?> editBlogSubmit(@RequestBody Blogs blog, HttpSession session)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Acces.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		try
		{
			blogsDao.updateBlog(blog);
		}
		catch(Exception e)
		{
			ErrorClazz error = new ErrorClazz(6, e.getMessage());
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Blogs>(blog,HttpStatus.OK);
	}
	
	@RequestMapping(value="/userliked/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> userLikedPost(HttpSession session, @PathVariable int id)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Acces.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		Blogs blog = blogsDao.getBlogById(id);
		User user = userDao.getUserByUsername(username);
		BlogLikes userLiked = blogsDao.userLikedPost(blog, user);
		return new ResponseEntity<BlogLikes>(userLiked, HttpStatus.OK);
	}
	
	@RequestMapping(value="/updatebloglikes",method=RequestMethod.PUT)
	public ResponseEntity<?> updateBlogLikes(HttpSession session, @RequestBody Blogs blog)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Acces.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		User user = userDao.getUserByUsername(username);
		Blogs updatedBlogLikes = blogsDao.updateLikes(blog, user);
		return new ResponseEntity<Blogs>(updatedBlogLikes, HttpStatus.OK);
	}
	
	@RequestMapping(value="/addblogcomments", method=RequestMethod.POST)
	public ResponseEntity<?> addBlogComments(HttpSession session, @RequestParam String commentText, @RequestParam int id)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Acces.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		BlogComments blogComment = new BlogComments();
		blogComment.setCommentText(commentText);
		Blogs blog = blogsDao.getBlogById(id);
		blogComment.setBlog(blog);
		User user = userDao.getUserByUsername(username);
		blogComment.setUser(user);
		blogComment.setCommentedOn(new Date());
		try
		{
			blogsDao.addBlogComment(blogComment);
		}
		catch(Exception e)
		{
			ErrorClazz error = new ErrorClazz(1, "Unable to post comments "+e.getMessage());
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Blogs updatedBlog = blogsDao.getBlogById(id);
		return new ResponseEntity<Blogs>(updatedBlog, HttpStatus.OK);
	}
}
