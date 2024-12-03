$(document).ready(function() {
	$("#loginForm").submit(function(event) {
		event.preventDefault();

		$.ajax({
			url: '/api/auth/login',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({
				usernameOrEmail: $("#usernameOrEmail").val(),
				password: $("#password").val()
			}),
			success: function(response) {
				localStorage.setItem("jwtToken", response.accessToken);
				console.log("localStorage", response.accessToken);
			},
			error: function(error) {
				alert("Login failed!");
			}
		});
	});
});