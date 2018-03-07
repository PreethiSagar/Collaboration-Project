/**
 * Friend Service
 */
app.factory('FriendService', function($http) {
	var BASE_URL = "http://localhost:8081/GetItMiddleware";
	var friendService = {};
	
	friendService.getSuggestedUsers = function() {
		return $http.get(BASE_URL+"/suggestedusers");
	}
	
	friendService.getFriendRequests = function() {
		return $http.get(BASE_URL+"/pendingrequests");
	}
	
	friendService.addFriendRequest = function(toId) {
		return $http.post(BASE_URL+"/addfriendrequest/"+toId);
	}
	
	friendService.updateFriendRequest = function(friendRequest) {
		return $http.put(BASE_URL+"/updatefriendrequest",friendRequest);
	}
	
	friendService.getFriendsList = function() {
		return $http.get(BASE_URL+"/getfriendslist");
	}
	
	return friendService;
});