/**
 * Home Controller
 */
app.controller('HomeController', function(HomeService, $location, $rootScope, $scope) 
{
	function getNotification()
	{
		HomeService.getViewedNotification().then(function(response) {
			//Notifications from tbl_notification with field viewed=1
			$rootScope.viewedNotification = response.data;
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
		});
		
		HomeService.getNotViewedNotification().then(function(response) {
			//Notifications from tbl_notification with field viewed=0
			$rootScope.notViewedNotification = response.data;
			$rootScope.notificationLength = $rootScope.notViewedNotification.length;
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
		});
	}
	if($rootScope.currentUser != undefined)
	{
		getNotification();
	}
	
	$rootScope.updateLength = function() {
		$rootScope.notificationLength = 0;
	}
	
	$rootScope.updateNotificationViewed = function(notificationId) {
		HomeService.updateNotificationViewed(notificationId).then(function(response) {
			getNotification();
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
		});
	}
	
	function latestJobs()
	{
		HomeService.latestJobs().then(function(response) {
			$rootScope.latestJobs = response.data;
		}, function(response) {
			$location.path("/home");
		});
	}
	latestJobs();
	
	$scope.submitFeedback = function() {
		HomeService.submitFeedback($scope.feedback).then(function(response) {
			$scope.feedbackDetails = response.data;
			$scope.feedbackSuccess = "Feedback Submitted Successfully.";
			$scope.feedback = "";
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
		});
	}
	
	function getCustomerFeedbacks()
	{
		HomeService.customerFeedbacks().then(function(response) {
			$scope.CustomerFeedbacks = response.data;
			console.log($scope.CustomerFeedbacks);
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
		});
	}
	if($rootScope.currentUser != undefined)
	{
		getCustomerFeedbacks();
	}	
	
});