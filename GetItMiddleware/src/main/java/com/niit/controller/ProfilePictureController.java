package com.niit.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.niit.dao.ProfilePictureDao;
import com.niit.model.ErrorClazz;
import com.niit.model.ProfilePicture;

@Controller
public class ProfilePictureController 
{
	@Autowired
	private ProfilePictureDao profilePictureDao;
	
	@RequestMapping(value="/uploadprofilepicture", method=RequestMethod.POST)
	public ResponseEntity<?> uploadProfilePicture(HttpSession session, @RequestParam CommonsMultipartFile image)
	{
		String username = (String)session.getAttribute("username");
		if(username == null)
		{
			ErrorClazz error = new ErrorClazz(5, "Unauthorized Access.");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED);
		}
		ProfilePicture profilePicture = new ProfilePicture();
		profilePicture.setImage(image.getBytes());
		profilePicture.setUserName(username);
		profilePictureDao.saveOrUpdateProfilePicture(profilePicture);
		return new ResponseEntity<ProfilePicture>(profilePicture, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getimage/{username}", method=RequestMethod.GET)
	public @ResponseBody byte[] getProfilePicture(HttpSession session, @PathVariable String username)
	{
		System.out.println(username);
		String loginId = (String)session.getAttribute("username");
		if(loginId == null)
		{
			return null;
		}
		System.out.println(username);
		System.out.println(loginId);
		ProfilePicture profilePicture = profilePictureDao.getProfilePicture(username);
		if(profilePicture == null)
		{
			String defaultImageName = "no_image";
			ProfilePicture defaultImage = profilePictureDao.getProfilePicture(defaultImageName);
			return defaultImage.getImage();
		}
		return profilePicture.getImage();
	}
}
