/**
 * Blog Service
 */
app.factory('BlogService', function($http) {
	var BASE_URL = "http://localhost:8081/GetItMiddleware";
	var blogService = {};
	
	blogService.addBlog = function(blog) {
		return $http.post(BASE_URL+"/saveblog",blog);
	}
	
	blogService.getBlogsApproved = function() {
		return $http.get(BASE_URL+"/getblogs/"+1);
	}
	
	blogService.getBlogsWaitingApproval = function() {
		return $http.get(BASE_URL+"/getblogs/"+0);
	}
	
	blogService.getBlogDetails = function(id) {
		return $http.get(BASE_URL+"/getblogdetail/"+id);
	}
	
	blogService.updateApprovalStatus = function(blogDetails,rejectionReason) {
		if(rejectionReason == undefined)
			return $http.put(BASE_URL+"/updateapprovalstatus?rejectionReason="+'Not Mentioned by Admin', blogDetails);			
		else
			return $http.put(BASE_URL+"/updateapprovalstatus?rejectionReason="+rejectionReason, blogDetails)
	}
	
	blogService.editBlogSubmit = function(blog) 
	{
		return $http.put(BASE_URL+"/editblogsubmit",blog);
	}
	
	blogService.userLiked = function(id) {
		return $http.get(BASE_URL+"/userliked/"+id);
	}
	
	blogService.updateBlogLikes = function(blogDetails) {
		return $http.put(BASE_URL+"/updatebloglikes",blogDetails);
	}
	
	blogService.addBlogComment = function(commentText, id) {
		return $http.post(BASE_URL+"/addblogcomments?commentText="+commentText+"&id="+id);
	}
	
	return blogService;
});