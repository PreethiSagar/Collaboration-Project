/**
 * User Service
 */
app.factory('UserService', function($http) {
	var BASE_URL = "http://localhost:8081/GetItMiddleware";
	var userService = {};
	
	userService.registerUser = function(user) {
		return $http.post(BASE_URL+"/registeruser",user);
	}
	
	userService.login = function(user) 
	{
		return $http.post(BASE_URL+"/login",user);
	}
	
	userService.logout = function()
	{
		return $http.get(BASE_URL+"/logout");
	}
	
	userService.getUser = function() 
	{
		return $http.get(BASE_URL+"/getuser");
	}
	
	userService.editProfile = function(user) 
	{
		return $http.put(BASE_URL+"/editprofile",user);
	}
	
	return userService;
})