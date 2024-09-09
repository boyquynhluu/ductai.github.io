document.addEventListener("DOMContentLoaded", function() {
	// Using Fetch API
	fetch('/api/diems')
		.then(response => {
			if (!response.ok) {
				throw new Error('Network response was not ok');
			}
			return response.json();
		})
		.then(data => {
			console.log('Fetched Data:', data);

			// Define the table body and header elements
			const listHeader = document.getElementById('data-list-header');

			// Clear any existing content
			listHeader.innerHTML = '';

			// Append table headers
			const headers = [
				'STT', 'Tên Sinh Viên', 'Ngày Sinh', 'Giới Tính', 'Địa Chỉ',
				'Điểm Lần 1', 'Điểm Lần 2', 'Điểm Trung Bình', 'Điểm Tổng Kết',
				'Hạnh Kiểm', 'Học Kì', 'Ghi Chú', 'Môn Học', 'Số DVHT', '#'
			];

			const headerRow = document.createElement('tr');
			headers.forEach(header => {
				const th = document.createElement('th');
				th.textContent = header;
				headerRow.appendChild(th);
			});
			listHeader.appendChild(headerRow);

			// Define the table body elements
			const listBody = document.getElementById('data-list-body');

			// Clear any existing content
			listBody.innerHTML = '';

			// Append data rows
			data.forEach((item, index) => {
				const row = document.createElement('tr');

				row.innerHTML = `
					<td>${index + 1}</td>
					<td>${item.tenSinhVien}</td>
					<td>${item.ngaySinh}</td>
					<td>${item.gioiTinh}</td>
					<td>${item.diaChi}</td>
					<td>${item.diemLan1}</td>
					<td>${item.diemLan2}</td>
					<td>${item.diemTrungBinh}</td>
					<td>${item.diemTongKet}</td>
					<td>${item.hanhKiem}</td>
					<td>${item.hocKi}</td>
					<td>${item.ghiChu}</td>
					<td style="text-align: left">${item.tenMon}</td>
					<td>${item.soDVHT}</td>
					<td><input type="checkbox" name="ids" value="${item.maSV},${item.maKQ},${item.maMon}" /></td>
					`;

				listBody.appendChild(row);
			});
		})
		.catch(error => console.error('Error fetching data:', error));
});