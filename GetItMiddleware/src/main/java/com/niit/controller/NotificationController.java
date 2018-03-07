package com.niit.controller;

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

import com.niit.dao.FeedbackDao;
import com.niit.dao.JobDao;
import com.niit.dao.NotificationDao;
import com.niit.model.ErrorClazz;
import com.niit.model.Feedback;
import com.niit.model.Job;
import com.niit.model.Notification;

@Controller
public class NotificationController 
{
	@Autowired
	private NotificationDao notificationDao;
	
	@Autowired
	private JobDao jobDao;
	
	@Autowired
	private FeedbackDao feedbackDao;
	
	@RequestMapping(value="/getnotification/{viewed}", method=RequestMethod.GET)
	public ResponseEntity<?> getNotification(HttpSession session, @PathVariable int viewed)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Access.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		List<Notification> allNotifications = notificationDao.getNotification(username, viewed);
		return new ResponseEntity<List<Notification>>(allNotifications, HttpStatus.OK);
	}
	
	@RequestMapping(value="/updatenotificationviewed/{notificationId}", method=RequestMethod.PUT)
	public ResponseEntity<?> updateNotificationViewed(HttpSession session, @PathVariable int notificationId)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Access.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		Notification updateNotificationViewed = notificationDao.updateNotificationViewed(notificationId);
		return new ResponseEntity<Notification>(updateNotificationViewed, HttpStatus.OK);
	}
	
	@RequestMapping(value="/latestjobs", method=RequestMethod.GET)
	public ResponseEntity<?> getLatestJobs()
	{
		List<Job> latestJobs = jobDao.latestJobs();
		return new ResponseEntity<List<Job>>(latestJobs, HttpStatus.OK);
	}
	
	@RequestMapping(value="/submitfeedback", method=RequestMethod.POST)
	public ResponseEntity<?> submitFeedback(@RequestBody Feedback feedback)
	{
		try
		{
			feedbackDao.submitFeedback(feedback);
		}
		catch(Exception e)
		{
			ErrorClazz error = new ErrorClazz(8, "Unable to insert the feedback details "+e.getMessage());
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Feedback>(feedback,HttpStatus.OK);
	}
	
	@RequestMapping(value="/customerfeedbacks", method=RequestMethod.GET)
	public ResponseEntity<?> customerFeedbacks(HttpSession session)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Access.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		List<Feedback> customerFeedbacks = feedbackDao.getCustomerFeedbacks();
		return new ResponseEntity<List<Feedback>>(customerFeedbacks,HttpStatus.OK);
	}
}
