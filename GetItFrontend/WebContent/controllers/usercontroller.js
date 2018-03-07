/**
 * User Controller
 */
app.controller('UserController', function($scope, UserService, $location, $rootScope, $cookieStore) 
{	
	if($rootScope.currentUser != undefined) {
		UserService.getUser().then(function(response) {
			$scope.user = response.data;    //User object
		}, function(response) {   //401, 500
			if(response.status == 401)
			{
				$location.path("/login");
			}
			if(response.status == 500)
			{
				$scope.error = response.data;
				$location.path("/editprofile");
			}
		});
	}
	
	$scope.registerUser = function() {
		UserService.registerUser($scope.user).then(function(response) {
			$location.path("/home");
		}, function(response) {
			$scope.error = response.data;
		});
	}
	
	$scope.login = function() {
		UserService.login($scope.user).then(function(response){
			$rootScope.currentUser = response.data;
			console.log(response.data);
			$cookieStore.put('currentUser', response.data);
			console.log("Cookie");
			console.log($cookieStore.get('currentUser'));
			$location.path("/home");
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
		});
	}
	
	$scope.editProfile = function() {
		UserService.editProfile($scope.user).then(function(response) {
			alert("Updated Profile Successfully.");
			$location.path("/home");
		}, function(response) {//401, 500
			if(response.status == 401)
			{
				$location.path("/login");
			}
			if(response.status == 500)
			{
				$scope.error = response.data;
				$location.path("/editprofile");
			}
		});
	}
})