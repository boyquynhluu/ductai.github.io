var values = [];
$(document).ready(function() {
	$('.openStandings').on('click', function(e) {
		e.preventDefault();
		values = getSelectedCheckbox();
		if (values === undefined) {
			return;
		}
		$('#myModalPositions').modal('show');
	});

	$('#send-mail').click(function(e) {
		// Get token login success
		var accessToken = localStorage.getItem('jwtToken');
		e.preventDefault();
		let formData = getDataSendMail();
		if (formData === null) {
			return; // Exit if validation failed
		}
		$.ajax({
			url: '/api/sendMail/sendMailWithAttachment',
			type: 'POST',
			headers: {
				'Authorization': `Bearer ${accessToken}`
			},
			processData: false, // Important for FormData
			contentType: false, // Important for FormData
			data: formData,
			success: function(response) {
				alert('Gửi Mail Thành Công: ' + '\n' + response);
				setTimeout(() => {
					$('#myModalPositions').modal('hide');
					unCheckedCheckbox();
				}, "1000");
			},
			error: function(xhr, status, error) {
				$('#response').text('Error: ' + error);
			}
		});
	});

	$('.close').click(function(e) {
		e.preventDefault();
		$('#myModalPositions').modal('hide');
	});
});

// Get data send mail
function getDataSendMail() {
	let formData = new FormData();
	var subject = $("#subject").val();
	var body = $("#msgBody").val();
	if (subject === "" || body === "") {
		alert('Subject và Body mail không được trống');
		return null;
	}

	/*formData.append("ids", JSON.stringify(values));*/
	formData.append("ids", values);
	formData.append("recipient", "");
	formData.append("msgBody", body);
	formData.append("subject", subject);

	var attach = document.getElementById('attachment').files[0];
	if (attach) {
		formData.append("attachment", attach);
	}

	return formData;
}