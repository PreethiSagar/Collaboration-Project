/**
 * Home Service
 */
app.factory('HomeService', function($http) {
	var BASE_URL = "http://localhost:8081/GetItMiddleware";
	var homeService = {};
	
	homeService.getViewedNotification = function() {
		return $http.get(BASE_URL+"/getnotification/"+1);
	}
	
	homeService.getNotViewedNotification = function() {
		return $http.get(BASE_URL+"/getnotification/"+0);
	}
	
	homeService.updateNotificationViewed = function(notificationId) {
		return $http.put(BASE_URL+"/updatenotificationviewed/"+notificationId);
	}
	
	homeService.latestJobs = function() {
		return $http.get(BASE_URL+"/latestjobs");
	}
	
	homeService.submitFeedback = function(feedback) {
		return $http.post(BASE_URL+"/submitfeedback",feedback);
	}
	
	homeService.customerFeedbacks = function() {
		return $http.get(BASE_URL+"/customerfeedbacks");
	}
	
	return homeService;
});