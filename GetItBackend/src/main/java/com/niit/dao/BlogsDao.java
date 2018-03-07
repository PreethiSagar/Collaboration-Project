package com.niit.dao;

import java.util.List;

import com.niit.model.BlogComments;
import com.niit.model.BlogLikes;
import com.niit.model.Blogs;
import com.niit.model.User;

public interface BlogsDao 
{
	void saveBlogs(Blogs blogs);
	//return list of blogs waiting for approval(0) / list of blogs approved(1)
	//getBlogs(0) -> list of blogs waiting for approval
	//getBlogs(10 -> list of blogs approved
	List<Blogs> getBlogs(int approved);   //value of approved = 0 or 1
	Blogs getBlogById(int id);
	void updateApprovalStatus(Blogs blogs, String rejectionReason);
	void updateBlog(Blogs blog);
	
	// select * from tbl_bloglikes where blogs_id=? and user_username=?
	// if user already liked the post, 1 object
	// if user has not yet liked the post, null object
	BlogLikes userLikedPost(Blogs blog, User user);
	// increment / decrement the number of likes
	// insert into tbl_bloglikes / delete from tbl_bloglikes
	Blogs updateLikes(Blogs blog, User user);
	void addBlogComment(BlogComments blogComments);
}
