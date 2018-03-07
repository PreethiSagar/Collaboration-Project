/**
 * Job Service
 */
app.factory('JobService', function($http) {
	var BASE_URL = "http://localhost:8081/GetItMiddleware";
	var jobService = {};
	
	jobService.addJob = function(job) {
		return $http.post(BASE_URL+"/savejob",job);
	}
	
	jobService.getAllJobs = function() {
		return $http.get(BASE_URL+"/alljobs")
	}
	
	jobService.getJob = function(jobId) {
		return $http.get(BASE_URL+"/getjob/"+jobId);
	}
	
	jobService.editJobSubmit = function(job) 
	{
		return $http.put(BASE_URL+"/editjobsubmit",job);
	}
	
	jobService.deleteJob = function(jobId) {
		return $http.get(BASE_URL+"/deleteJob/"+jobId)
	}
	
	return jobService;
});