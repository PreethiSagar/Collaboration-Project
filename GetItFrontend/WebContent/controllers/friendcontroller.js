/**
 * Friend Controller
 */
app.controller('FriendController', function($scope, FriendService, $location) 
{
	function friendRequestData()
	{
		FriendService.getSuggestedUsers().then(function(response) {
			$scope.suggestedUsers = response.data;
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
		});
		
		FriendService.getFriendRequests().then(function(response) {
			$scope.friendRequests = response.data;
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
		});
	}
	
	$scope.addFriendRequest = function(toId) {
		FriendService.addFriendRequest(toId).then(function(response) {
			alert("Friend request sent successfully");
			getSuggestedUsers();
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
		});
	}
	
	$scope.updateFriendRequest = function(friendRequest, updatedStatus) {
		friendRequest.status = updatedStatus;
		FriendService.updateFriendRequest(friendRequest).then(function(response) {
			friendRequestData();
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
		});
	}
	
	function friendsList()
	{
		FriendService.getFriendsList().then(function(response) {
			$scope.friendsList = response.data;
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
		});
	}
	
	friendRequestData();
	friendsList();
	
});