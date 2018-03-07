package com.niit.dao;

import java.util.List;
import com.niit.model.Job;

public interface JobDao 
{
	void saveJob(Job job);
	List<Job> getAllJobs();
	Job getJob(int jobId);
	void updateJob(Job job);
	void deleteJob(Job job);
	List<Job> latestJobs();
}
