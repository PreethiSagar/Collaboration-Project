/**
 * Job Controller
 */
app.controller('JobController', function($scope, JobService, $location, $rootScope, $routeParams) 
{	
	var jobId = $routeParams.id;
	
	$scope.addJob = function() {
		JobService.addJob($scope.job).then(function(response) {
			console.log(response.data);
			alert("Job added successfully.")
			$location.path("/alljobs");
		}, function(response) {
			console.log(response.data);
			if(response.status == 401)
			{
				if(response.data.code == 7)
				{
					alert("Access denied.");
					$location.path("/home");
				}
				else
				{
					$scope.error = response.data;
					$location.path("/login");
				}
			}
			else
			{
				$scope.error = response.data;
				$location.path("/addjob");
			}			
		});
	}
	
	function getAllJobs()
	{
		JobService.getAllJobs().then(function(response) {
			$scope.jobs = response.data; //List<job> in JSON format
		}, function(response) {
			console.log(response.data);
			$scope.error = response.data;
			$location.path("/login");
		})
	}
	getAllJobs();   //function call
	
	if(jobId != undefined)
	{
		JobService.getJob(jobId).then(function(response) {
			$scope.job = response.data
		}, function(response) {
			$scope.error = response.data;
			$location.path("/login");
		});
	}
	
	$scope.editJob = function(jobId) {
		JobService.getJob(jobId).then(function(response) {
			if($rootScope.currentUser.role != 'ADMIN')
			{
				alert("Access denied.");
				$location.path("/home");
			}
			else
			{
				$rootScope.job = response.data;
				console.log(response.data);
				$location.path("/editJob");
			}			
		}, function(response) {
			$scope.error = response.data;
			$location.path("/login");
		});
	}
	
	$scope.editJobSubmit = function() {
		JobService.editJobSubmit($scope.job).then(function(response) {
			alert("Updated Job Successfully.");
			$location.path("/home");
		}, function(response) {//401, 500
			if(response.status == 401)
			{
				$location.path("/login");
			}
			if(response.status == 500)
			{
				$scope.error = response.data;
				$location.path("/editJob");
			}
		});
	}
	
	$scope.deleteJob = function(jobId) {
		JobService.deleteJob(jobId).then(function(response){
			alert("Deleted Job Successfully.");
			$location.path("/home");
		}, function(response) {
			if(response.status == 401)
			{
				$location.path("/login");
			}
			if(response.status == 500)
			{
				$scope.error = response.data;
				$location.path("/editJob");
			}
		});
	}
});