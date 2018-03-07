/**
 * Blog Controller
 */
app.controller('BlogController', function($scope, BlogService, $location, $rootScope, $routeParams) 
{
	var id = $routeParams.id;
	$scope.isRejected = false;
	$scope.showRejectionText = function(val) {
		$scope.isRejected = val;
	}
	$scope.showComment = false;
	
	$scope.addBlog = function() {
		BlogService.addBlog($scope.blog).then(function(response) {
			console.log(response.data);
			alert("Blog added successfully.")
			$location.path("/getBlogs");
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
				$location.path("/addblog");
			}			
		});
	}
	
	
	//Two Variables: blogsApproved & blogsWaitingApproval
	//Statement to initialize  variable blogsApproved
	BlogService.getBlogsApproved().then(function(response) {
		console.log(response.data);
		$scope.blogsApproved = response.data;
	}, function(response) {
		if(response.status == 401)
		{
			$scope.error = response.data;
			$location.path("/login");
		}
	});
	
	//Statement to initialize variable blogsWaitingApproval
	if($rootScope.currentUser.role == 'ADMIN')
	{
		BlogService.getBlogsWaitingApproval().then(function(response) {
			console.log(response.data);
			$scope.blogsWaitingApproval = response.data;
		}, function(response) {
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
		});
	}
	
	if(id != undefined)
	{
		BlogService.getBlogDetails(id).then(function(response){
			$scope.blogDetails = response.data;
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
		});
	}
	
	$scope.updateApprovalStatus = function() {
		BlogService.updateApprovalStatus($scope.blogDetails, $scope.rejectionReason).then(function(response){
			$location.path("/getBlogs");
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
			if(response.status == 500)
			{
				alert(response.data);
				$scope.error = response.data;
			}
		});
	}
	
	$scope.editBlog = function(blogId) {
		BlogService.getBlogDetails(blogId).then(function(response) {
			$rootScope.blog = response.data;
			console.log(response.data);
			$location.path("/editBlog");
		}, function(response) {
			$scope.error = response.data;
			$location.path("/login");
		});
	}
	
	$scope.editBlogSubmit = function() {
		BlogService.editBlogSubmit($scope.blog).then(function(response) {
			alert("Updated Blog Successfully.");
			$location.path("/getBlogs");
		}, function(response) {//401, 500
			if(response.status == 401)
			{
				$location.path("/login");
			}
			if(response.status == 500)
			{
				$scope.error = response.data;
				$location.path("/editBlog");
			}
		});
	}
	
	if(id != undefined)
	{
		BlogService.userLiked(id).then(function(response) {
			if(response.data == '')
				$scope.liked = false;
			else
				$scope.liked = true;
			//alert($scope.liked);
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
		});
	}
	
	$scope.updateBlogLikes = function() {
		BlogService.updateBlogLikes($scope.blogDetails).then(function(response) {
			//update likes & alter the glyphicon thumbsup color
			$scope.blogDetails = response.data;
			$scope.liked = !$scope.liked;
		}, function(response) {
			if(response.status == 401)
			{
				$scope.error = response.data;
				$location.path("/login");
			}
		});
	}
	
	$scope.addBlogComment = function() {
		if($scope.blogComments.commentText == undefined)
		{
			alert("Please enter comment.");
		}
		else
		{
			BlogService.addBlogComment($scope.blogComments.commentText,id).then(function(response) {
				$scope.blogComments .commentText = '';
				$scope.blogDetails = response.data;	//List of all comments of that particular blog
			}, function(response){
				if(response.status == 401)
				{
					$scope.error = response.data;
					$location.path("/login");
				}
				else
				{
					$scope.error = response.data;
				}
			});
		}
	}
	
	$scope.showComments = function() {
		$scope.showComment = !$scope.showComment;
	}
	
});