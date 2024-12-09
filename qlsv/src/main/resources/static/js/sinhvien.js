const autocompleteList = [];
//Init load data screen
document.addEventListener("DOMContentLoaded", function() {
	// Get token login success
	var token = localStorage.getItem('jwtToken');

	if (!token) {
		console.error('No JWT token found');
		window.location.href = "http://localhost:8080/login";
	}

	// Function to fetch and render data
	const fetchData = async () => {
		try {
			const response = await fetch('/api/sinhviens', {
				method: 'GET',
				headers: {
					'Authorization': 'Bearer ' + token,
					'Content-Type': 'application/json'
				}
			});
			// If token is exper
			if (response.status === 401) {
				// Token hết hạn, gửi lại refresh token
				const refreshToken = localStorage.getItem("jwtToken");

				// Gọi API refresh token
				return fetch('/api/auth/refresh-token', {
					method: 'POST',
					headers: {
						'Authorization': `Bearer ${refreshToken}`
					}
				})
					.then(res => res.json())
					.then(data => {
						if (data.accessToken) {
							// Lưu lại Access Token mới
							localStorage.setItem("jwtToken", data.accessToken);
							// Gửi lại request ban đầu với Access Token mới
							options.headers['Authorization'] = `Bearer ${data.accessToken}`;
							return fetchData(url, options); // Gửi lại yêu cầu với token mới
						} else {
							// Nếu không thể lấy lại access token, logout
							window.location.href = "/login";
						}
					});
			}
			
			const res = await response.json();
			setDataAutoSearchBox(res.data);
			renderTable(res.data);
		} catch (error) {
			console.error('Error fetching data:', error);
		}
	};

	fetchData();

	//Autocomplete
	autocompleteSearchBox(autocompleteList);
});

// Function set data for autocomplete search box
const setDataAutoSearchBox = (data) => {
	data.forEach(item => {
		autocompleteList.push(item.tenSinhVien, item.email, item.phone);
	});
	console.log("data autocomplete: " + autocompleteList);
}

// Function to render the table
const renderTable = (data) => {
	const listHeader = document.getElementById('sinhvien-list-header');
	listHeader.innerHTML = '';

	const headers = [
		'STT', 'Tên Sinh Viên', 'Tuổi', 'Số Điện Thoại', 'Email', 'Ngày Sinh', 'Giới Tính', 'Địa Chỉ', 'Trạng Thái'
	];

	const headerRow = document.createElement('tr');
	headers.forEach(header => {
		const th = document.createElement('th');
		th.textContent = header;
		headerRow.appendChild(th);
	});

	const thCheckbox = document.createElement('th');
	thCheckbox.innerHTML = `<input type="checkbox" id="checkbox-all" onclick="checkedAllCheckboxs()" />`;
	headerRow.appendChild(thCheckbox);

	listHeader.appendChild(headerRow);

	const listBody = document.getElementById('sinhvien-list-body');
	listBody.innerHTML = '';

	data.forEach((item, index) => {
		const row = document.createElement('tr');
		row.innerHTML = `
                <td>${index + 1}</td>
                <td>${item.tenSinhVien}</td>
                <td>${item.tuoi}</td>
                <td>${item.phone}</td>
                <td>${item.email}</td>
                <td>${item.ngaySinh}</td>
                <td>${item.gioiTinh}</td>
                <td>${item.diaChi}</td>
                <td>${item.trangThai}</td>
                <td><input type="checkbox" onclick="removeAllCheckbox()" name="ids" value="${item.maSV}" /></td>
            `;
		listBody.appendChild(row);
	});
};

$(document).ready(function() {

	// Call API export Excel
	$('#export-excel').click(function(e) {
		e.preventDefault();
		const values = getSelectedCheckbox();
		$.ajax({
			url: '/api/sinhviens/export/excel',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(values),
			success: function(response) {
				unCheckedCheckbox();
				alert('Export successful: ' + '\n' + response);
			},
			error: function(xhr, status, error) {
				$('#response').text('Error: ' + error);
			}
		});
	});

	// Call API export PDF
	$('#export-pdf').click(function(e) {
		e.preventDefault();
		const values = getSelectedCheckbox();
		$.ajax({
			url: '/api/sinhviens/export/pdf',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(values),
			success: function(response) {
				unCheckedCheckbox();
				alert('PDF successful: ' + '\n' + response);
			},
			error: function(xhr, status, error) {
				$('#response').text('Error: ' + error);
			}
		});
	});

	// Call API search
	$('#btn-search').click(function(e) {
		e.preventDefault();
		const input = $("#search").val().trim();
		if (input === "") {
			$('#input-check').text('Nhập Thông Tin Tìm Kiếm');
			setTimeout(() => {
				$('#search').focus();
			}, "1000");
			return;
		}
		obj = {
			search: input
		}
		$.ajax({
			url: '/api/sinhvien/search',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(obj),
			success: function(response) {
				$('#input-check').text('');
				if (response.length === 0) {
					$('#empty-data').text('Không Tồn Tại Sinh Viên!');
				} else {
					$('#empty-data').text('');
				}
				renderTable(response);
			},
			error: function(xhr, status, error) {
				$('#response').text('Error: ' + error);
			}
		});
	});

	$('#cursor-pointer').click(function() {
		location.reload();
	});
});

// Get all checkbox is checked
function getSelectedCheckbox() {
	const checkboxes = document.querySelectorAll('input[name="ids"]:checked');
	if (checkboxes.length === 0) {
		alert('Chọn ít nhất 1 sinh viên!');
		return;
	}
	return Array.from(checkboxes).map(checkbox => checkbox.value);
}

// uncheck checkbox
function unCheckedCheckbox() {
	// Uncheck all checkboxes
	document.querySelectorAll('input[name="ids"]').forEach(checkbox => checkbox.checked = false);
	document.getElementById("checkbox-all").checked = false;
}

$('#checked-all').click(function() {
	document.querySelectorAll('input[name="ids"]').forEach(checkbox => checkbox.checked = true);
	document.getElementById("checkbox-all").checked = true;
})

$('#allcheckbox').click(function() {
	document.querySelectorAll('input[name="ids"]').forEach(checkbox => checkbox.checked = true);
});

// Set all checkbox is checked
function checkedAllCheckboxs() {
	const allCheckbox = document.querySelectorAll('input[name="ids"]');
	const checkboxes = document.querySelectorAll('input[name="ids"]:checked');

	if (allCheckbox.length !== checkboxes.length) {
		allCheckbox.forEach(checkbox => checkbox.checked = true);
	} else {
		allCheckbox.forEach(checkbox => checkbox.checked = false);
	}
}

// Set checkbox-all flow by checkbox
// if checkbox all is checked then item checkbox-all is checked
// else checkbox all is not checked then item checkbox-all not checked
function removeAllCheckbox() {
	const allCheckbox = document.querySelectorAll('input[name="ids"]');
	const checkboxes = document.querySelectorAll('input[name="ids"]:checked');
	const checkboxHeader = document.getElementById("checkbox-all");
	if (allCheckbox.length === checkboxes.length) {
		// Check
		checkboxHeader.checked = true;
	} else {
		// Uncheck
		checkboxHeader.checked = false;
	}
}

// Autocomplete
function autocompleteSearchBox(data) {
	const searchInput = document.getElementById('search');
	const suggestionsContainer = document.getElementById('suggestions');

	searchInput.addEventListener('input', function() {
		const query = searchInput.value.toLowerCase();
		suggestionsContainer.innerHTML = '';

		if (query) {
			const filteredData = data.filter(item => item.toLowerCase().includes(query));
			filteredData.forEach(item => {
				const suggestionItem = document.createElement('div');
				suggestionItem.textContent = item;
				suggestionItem.classList.add('suggestion-item');
				suggestionItem.addEventListener('click', function() {
					searchInput.value = item;
					suggestionsContainer.innerHTML = '';
				});
				suggestionsContainer.appendChild(suggestionItem);
			});
		}
	});

	document.addEventListener('click', function(event) {
		if (!searchInput.contains(event.target) && !suggestionsContainer.contains(event.target)) {
			suggestionsContainer.innerHTML = '';
		}
	});
}