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

import com.niit.dao.JobDao;
import com.niit.dao.UserDao;
import com.niit.model.ErrorClazz;
import com.niit.model.Job;
import com.niit.model.User;

@Controller
public class JobController
{
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private JobDao jobDao;
	
	public JobController()
	{
		System.out.println("JobController is Instantiated.");
	}
	
	@RequestMapping(value="/savejob", method=RequestMethod.POST)
	public ResponseEntity<?> saveJob(@RequestBody Job job, HttpSession session)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Acces.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		//User is Authenticated, so now check for role - authorization
		User user = userDao.getUserByUsername(username);
		if(!user.getRole().equals("ADMIN"))
		{
			ErrorClazz error = new ErrorClazz(7, "Access Denied.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		job.setPostedOn(new Date());
		try
		{
			jobDao.saveJob(job);
		}
		catch(Exception e)
		{
			ErrorClazz error = new ErrorClazz(8, "Unable to insert the job details "+e.getMessage());
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Job>(job,HttpStatus.OK);
	}
	
	@RequestMapping(value="/alljobs", method=RequestMethod.GET)
	public ResponseEntity<?> getAllJobs(HttpSession session)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Access.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		List<Job> jobsList = jobDao.getAllJobs();
		return new ResponseEntity<List<Job>>(jobsList,HttpStatus.OK);
	}
	
	@RequestMapping(value="/getjob/{jobId}", method=RequestMethod.GET)
	public ResponseEntity<?> getJob(@PathVariable int jobId, HttpSession session)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Access.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		Job job = jobDao.getJob(jobId);
		return new ResponseEntity<Job>(job,HttpStatus.OK);
	}
	
	@RequestMapping(value="/editjobsubmit", method=RequestMethod.PUT)
	public ResponseEntity<?> editJobSubmit(@RequestBody Job job, HttpSession session)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Acces.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		//User is Authenticated, so now check for role - authorization
		User user = userDao.getUserByUsername(username);
		if(!user.getRole().equals("ADMIN"))
		{
			ErrorClazz error = new ErrorClazz(7, "Access Denied.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		try
		{
			jobDao.updateJob(job);
		}
		catch(Exception e)
		{
			ErrorClazz error = new ErrorClazz(6, e.getMessage());
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Job>(job,HttpStatus.OK);
	}
	
	@RequestMapping(value="/deleteJob/{jobId}", method=RequestMethod.GET)
	public ResponseEntity<?> deleteJob(@PathVariable int jobId, HttpSession session)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Acces.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		//User is Authenticated, so now check for role - authorization
		User user = userDao.getUserByUsername(username);
		if(!user.getRole().equals("ADMIN"))
		{
			ErrorClazz error = new ErrorClazz(7, "Access Denied.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		Job job = jobDao.getJob(jobId);
		try
		{
			jobDao.deleteJob(job);
		}
		catch(Exception e)
		{
			ErrorClazz error = new ErrorClazz(6, e.getMessage());
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Job>(job,HttpStatus.OK);
	}
}
