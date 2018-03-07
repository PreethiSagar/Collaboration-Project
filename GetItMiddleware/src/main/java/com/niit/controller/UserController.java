package com.niit.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.niit.dao.UserDao;
import com.niit.model.ErrorClazz;
import com.niit.model.User;

@Controller
public class UserController 
{
	@Autowired
	private UserDao userDao;
	
	public UserController()
	{
		System.out.println("UserController is Instantiated.");
	}
	
	//Client - Angular JS Client & User in JSON
	//Convert JSON to Java Object
	//? any type, for success type is User, for error type is ErrorClazz
	@RequestMapping(value="/registeruser", method=RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody User user)
	{
		try
		{
			if(!userDao.isUsernameValid(user.getUserName()))
			{
				//Duplicate Username
				ErrorClazz error = new ErrorClazz(3,"Username already exist. Please use different username.");
				return new ResponseEntity<ErrorClazz>(error, HttpStatus.CONFLICT);
			}
			if(!userDao.isEmailValid(user.getEmail()))
			{
				//Duplicate Email
				ErrorClazz error = new ErrorClazz(2,"Email already exist. Please use different email.");
				return new ResponseEntity<ErrorClazz>(error, HttpStatus.CONFLICT);
			}
			userDao.registerUser(user);
		}
		catch(Exception e)
		{
			ErrorClazz error = new ErrorClazz(1,"Unable to register user details."+e.getMessage());
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody User user, HttpSession session)
	{
		User validUser = userDao.login(user);
		if(validUser == null)
		{
			ErrorClazz error = new ErrorClazz(4, "Invalid Username/Password.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		else
		{
			session.setAttribute("username", validUser.getUserName());
			validUser.setOnlineStatus(true);
			userDao.updateUser(validUser);    //Update the user's onlineStatus to true
			return new ResponseEntity<User>(validUser,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public ResponseEntity<?> logout(HttpSession session)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			//trying logout without login
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Acces.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		User user = userDao.getUserByUsername(username);
		user.setOnlineStatus(false);
		userDao.updateUser(user);      //Update the user's onlineStatus to false
		session.removeAttribute("username");
		session.invalidate();
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/getuser", method=RequestMethod.GET)
	public ResponseEntity<?> getUser(HttpSession session)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Acces.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		User user = userDao.getUserByUsername(username);
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	
	@RequestMapping(value="/editprofile", method=RequestMethod.PUT)
	public ResponseEntity<?> editProfile(@RequestBody User user, HttpSession session)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Acces.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		try
		{
			userDao.updateUser(user);
		}
		catch(Exception e)
		{
			ErrorClazz error = new ErrorClazz(6, e.getMessage());
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
}
