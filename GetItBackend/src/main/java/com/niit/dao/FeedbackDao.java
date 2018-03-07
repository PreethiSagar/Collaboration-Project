package com.niit.dao;

import java.util.List;
import com.niit.model.Feedback;

public interface FeedbackDao 
{
	void submitFeedback(Feedback feedback);
	List<Feedback> getCustomerFeedbacks();
}
