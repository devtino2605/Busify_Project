### **Câu chuyện người dùng (User Stories) của Nhà xe**

### 🧩 **1. Đăng nhập & Xác thực**

*   **User Story:**
    Là một **Nhà xe**,
    Tôi muốn **đăng nhập vào hệ thống một cách an toàn**,
    Để **tôi có thể truy cập vào bảng điều khiển quản lý và thực hiện các tác vụ vận hành**.

*   **Tiêu chí Chấp nhận (Acceptance Criteria):**
    *   [ ] Giao diện đăng nhập phải có các trường "Email" và "Mật khẩu".
    *   [ ] Hệ thống phải xác thực định dạng email khi người dùng nhập.
    *   [ ] Khi đăng nhập thành công, hệ thống phải chuyển hướng nhà xe đến trang quản trị chính (Dashboard).
    *   [ ] Khi đăng nhập thất bại (sai email hoặc mật khẩu), hệ thống phải hiển thị thông báo lỗi chung: "Email hoặc mật khẩu không chính xác."
    *   [ ] Phải có liên kết "Quên mật khẩu?" để dẫn đến trang yêu cầu đặt lại mật khẩu.

*   **Mức độ Ưu tiên:** Cao
*   **Story Points:** 2

---

### 🧩 **2. Quản lý Hồ sơ Công ty & Hợp đồng**

*   **User Story:**
    Là một **Nhà xe**,
    Tôi muốn **quản lý hồ sơ công ty và hợp đồng của mình**,
    Để **thông tin của tôi luôn chính xác và mối quan hệ với nền tảng được minh bạch**.

*   **Tiêu chí Chấp nhận:**
    *   [ ] Trong trang quản trị, phải có mục "Thông tin nhà xe" hoặc "Hồ sơ công ty".
    *   [ ] Tôi có thể xem và chỉnh sửa các thông tin: Tên nhà xe, Địa chỉ, Hotline, Email liên hệ, Mô tả ngắn.
    *   [ ] Nút "Lưu thay đổi" chỉ được kích hoạt khi có sự thay đổi thông tin.
    *   [ ] Tôi có thể xem trạng thái hợp đồng hiện tại (ví dụ: Đang hoạt động, Chờ duyệt, Đã chấm dứt).
    *   [ ] Phải có chức năng yêu cầu chấm dứt hợp đồng, yêu cầu này cần một bước xác nhận (ví dụ: modal pop-up) trước khi gửi đi.

*   **Mức độ Ưu tiên:** Cao
*   **Story Points:** 3

---

### 🧩 **3. Quản lý Tài xế**

*   **User Story:**
    Là một **Nhà xe**,
    Tôi muốn **quản lý danh sách tài xế một cách đầy đủ (thêm, xem, sửa, đổi trạng thái)**,
    Để **tôi có thể duy trì hồ sơ tài xế chính xác và phân công họ cho các chuyến đi**.

*   **Tiêu chí Chấp nhận:**
    *   [ ] Có một trang "Quản lý tài xế" hiển thị danh sách tất cả tài xế với các cột: Họ tên, Số điện thoại, Trạng thái (Hoạt động/Ngừng hoạt động).
    *   [ ] Có nút "Thêm tài xế mới" để mở form nhập thông tin: Họ tên, email, số điện thoại, số giấy phép lái xe.
    *   [ ] Hệ thống phải kiểm tra số điện thoại và email không được trùng với tài xế khác trong cùng nhà xe.
    *   [ ] Tôi có thể nhấp vào một tài xế để xem/chỉnh sửa thông tin chi tiết của họ.
    *   [ ] Tôi có thể thay đổi trạng thái của tài xế từ "Hoạt động" sang "Ngừng hoạt động" và ngược lại. Tài xế "Ngừng hoạt động" sẽ không xuất hiện trong danh sách lựa chọn khi tạo chuyến đi mới.

*   **Mức độ Ưu tiên:** Cao
*   **Story Points:** 5 (Cho toàn bộ chức năng CRUD - Create, Read, Update, Delete)

---

### 🧩 **4. Quản lý Xe (Phương tiện)**

*   **User Story:**
    Là một **Nhà xe**,
    Tôi muốn **quản lý đội xe của mình một cách hiệu quả**,
    Để **đảm bảo tình trạng sẵn sàng của xe là chính xác cho việc lên lịch chuyến đi**.

*   **Tiêu chí Chấp nhận:**
    *   [ ] Trang "Quản lý xe" hiển thị danh sách xe với các cột: Biển số xe, Loại xe, Tổng số ghế, Trạng thái.
    *   [ ] Có nút "Thêm xe mới" để mở form nhập: Biển số, Hãng sản xuất, Mẫu xe, Tổng số ghế, Lựa chọn sơ đồ ghế, Tiện ích (wifi, nước uống, v.v.).
    *   [ ] Biển số xe phải là duy nhất trên toàn hệ thống và được xác thực.
    *   [ ] Tôi có thể chỉnh sửa thông tin của một chiếc xe hiện có.
    *   [ ] Tôi có thể thay đổi trạng thái của xe: "Đang hoạt động", "Đang bảo trì", "Ngừng hoạt động". Xe không ở trạng thái "Đang hoạt động" sẽ không thể được chọn để tạo chuyến đi.
    *   [ ] Có chức năng "Xóa xe", nhưng chỉ cho phép xóa những xe chưa từng thực hiện chuyến đi nào.

*   **Mức độ Ưu tiên:** Cao
*   **Story Points:** 5

---

### 🧩 **5. Quản lý Tuyến đường**

*   **User Story:**
    Là một **Nhà xe**,
    Tôi muốn **tạo và quản lý các tuyến đường dịch vụ của mình**,
    Để **thiết lập các lộ trình và điểm dừng cho những chuyến đi mà tôi cung cấp**.

*   **Tiêu chí Chấp nhận:**
    *   [ ] Trang "Quản lý tuyến đường" cho phép tôi xem tất cả các tuyến đã tạo.
    *   [ ] Khi tạo tuyến mới, tôi phải chọn Điểm đi, Điểm đến từ danh sách địa điểm có sẵn.
    *   [ ] Tôi có thể thêm các điểm dừng (trạm đón/trả khách) vào tuyến đường theo đúng thứ tự.
    *   [ ] Tôi phải nhập thời gian di chuyển mặc định (tính bằng phút) và giá vé mặc định cho tuyến đường.
    *   [ ] Tôi có thể chỉnh sửa (thêm/bớt điểm dừng, thay đổi giá, thời gian) hoặc xóa một tuyến đường.
    *   [ ] Hệ thống không cho phép xóa tuyến đường nếu nó đang được sử dụng bởi một chuyến đi sắp diễn ra.

*   **Mức độ Ưu tiên:** Cao
*   **Story Points:** 8 (Phức tạp hơn do quản lý các điểm dừng theo thứ tự và logic liên quan)

---

### 🧩 **6. Quản lý Lịch trình Chuyến đi**

*   **User Story:**
    Là một **Nhà xe**,
    Tôi muốn **tạo và quản lý lịch trình các chuyến đi**,
    Để **khách hàng có thể tìm kiếm và đặt vé cho các chuyến đi này**.

*   **Tiêu chí Chấp nhận:**
    *   [ ] Tại trang "Quản lý chuyến đi", tôi có thể xem các chuyến đi theo bộ lọc (ngày, tuyến đường, trạng thái).
    *   [ ] Nút "Tạo chuyến đi mới" sẽ mở một form yêu cầu: chọn Tuyến đường, chọn Xe, chọn Tài xế, nhập Ngày giờ khởi hành, giá vé (mặc định lấy từ tuyến đường nhưng có thể ghi đè).
    *   [ ] Danh sách "chọn Xe" và "chọn Tài xế" chỉ hiển thị những xe/tài xế đang "Hoạt động" và chưa bị trùng lịch vào thời điểm đó.
    *   [ ] Thời gian đến dự kiến được hệ thống tự động tính toán.
    *   [ ] Tôi có thể cập nhật thông tin chuyến đi (ví dụ: đổi xe/tài xế vì lý do đột xuất) trước giờ khởi hành.
    *   [ ] Tôi có thể "Hủy chuyến". Khi hủy, hệ thống phải có cơ chế thông báo cho các hành khách đã đặt vé.

*   **Mức độ Ưu tiên:** Cao
*   **Story Points:** 8

---

### 🧩 **7. Quản lý Vé**

*   **User Story:**
    Là một **Nhà xe**,
    Tôi muốn **xem thông tin chi tiết về vé và lượt đặt chỗ**,
    Để **tôi có thể theo dõi doanh số, quản lý danh sách hành khách và tình trạng ghế ngồi**.

*   **Tiêu chí Chấp nhận:**
    *   [ ] Từ danh sách chuyến đi, tôi có thể nhấp vào một chuyến để xem chi tiết.
    *   [ ] Trang chi tiết chuyến đi hiển thị danh sách hành khách đã đặt vé (Tên, SĐT, số ghế, trạng thái thanh toán).
    *   [ ] Phải có một sơ đồ ghế trực quan của xe, hiển thị rõ các ghế: Còn trống, Đã đặt, Đang được giữ chỗ.
    *   [ ] Tôi có thể xuất danh sách hành khách của chuyến đi ra file (PDF/Excel) để giao cho tài xế.

*   **Mức độ Ưu tiên:** Trung bình
*   **Story Points:** 5 (Phần sơ đồ ghế trực quan làm tăng độ phức tạp)

---

### 🧩 **8. Theo dõi Thống kê Kinh doanh**

*   **User Story:**
    Là một **Nhà xe**,
    Tôi muốn **xem các báo cáo và thống kê về hiệu suất kinh doanh**,
    Để **tôi có thể theo dõi doanh thu và đưa ra các quyết định chiến lược**.

*   **Tiêu chí Chấp nhận:**
    *   [ ] Có một trang "Báo cáo & Thống kê" (Dashboard).
    *   [ ] Hiển thị các chỉ số tổng quan: Tổng doanh thu, Tổng số vé bán ra, Tỷ lệ lấp đầy trung bình.
    *   [ ] Tôi có thể lọc các báo cáo theo khoảng thời gian (Hôm nay, 7 ngày qua, Tháng này, Tùy chọn).
    *   [ ] Phải có biểu đồ thể hiện doanh thu theo thời gian (ví dụ: biểu đồ đường).
    *   [ ] Phải có bảng thống kê hiệu suất của từng tuyến đường (tuyến nào doanh thu cao nhất/thấp nhất).
    *   [ ] Có chức năng xuất dữ liệu báo cáo ra file CSV.

*   **Mức độ Ưu tiên:** Trung bình
*   **Story Points:** 8 (Yêu cầu xử lý dữ liệu phức tạp ở backend và hiển thị biểu đồ ở frontend)

Chắc chắn rồi. Câu hỏi của bạn rất chính xác và quan trọng.

**Câu trả lời ngắn gọn: Có, bạn nên thêm User Story để quản lý nhân viên nhà xe.**

**Câu trả lời chi tiết và lý do:**

Việc thêm một User Story cho chức năng "Quản lý nhân viên" là một bước cải tiến nền tảng và cực kỳ cần thiết cho hệ thống. Nó không chỉ là một tính năng "nên có" mà là một yêu cầu cốt lõi để nhà xe có thể vận hành như một doanh nghiệp thực thụ.

### **Tại sao chức năng này lại quan trọng?**

1.  **Phân quyền và Bảo mật:** Chủ nhà xe (người quản lý cấp cao nhất) không muốn nhân viên bán vé hay nhân viên soát vé có quyền xem báo cáo doanh thu, thay đổi thông tin nhà xe, hoặc xóa các tuyến đường chiến lược. Mỗi nhân viên chỉ nên có quyền truy cập vào những chức năng cần thiết cho công việc của họ.
2.  **Khả năng mở rộng (Scalability):** Khi nhà xe phát triển, họ sẽ có nhiều nhân viên ở các vai trò khác nhau. Hệ thống cần phải đáp ứng được nhu cầu này, cho phép tạo và quản lý nhiều tài khoản nhân viên một cách dễ dàng.
3.  **Trách nhiệm và Truy vết (Accountability):** Khi có sự cố hoặc sai sót (ví dụ: một vé được xác nhận không hợp lệ), hệ thống cần ghi nhận được **nhân viên nào** đã thực hiện hành động đó. Điều này là không thể nếu tất cả mọi người đều dùng chung một tài khoản của chủ nhà xe.
4.  **Hiệu quả vận hành:** Nhân viên có thể chủ động thực hiện công việc của mình (bán vé, xác nhận vé, xem danh sách khách) mà không cần phải thông qua hoặc sử dụng tài khoản của người quản lý, giúp quy trình làm việc trôi chảy hơn.

---

### **User Story chi tiết cho chức năng Quản lý Nhân viên**

Dưới đây là cách bạn có thể định nghĩa User Story này một cách đầy đủ.

### 🧩 **9. Quản lý Nhân viên và Phân quyền**

*   **User Story:**
    Là một **Quản lý nhà xe** (chủ xe),
    Tôi muốn **tạo và quản lý các tài khoản cho nhân viên của mình (ví dụ: nhân viên bán vé, nhân viên điều hành)**,
    Để **tôi có thể phân công công việc và kiểm soát quyền truy cập của họ vào các chức năng trong hệ thống một cách an toàn**.

*   **Tiêu chí Chấp nhận (Acceptance Criteria):**
    *   [ ] Trong trang quản trị, phải có mục "Quản lý nhân viên".
    *   [ ] Tôi có thể xem danh sách tất cả nhân viên đang có với các thông tin: Tên, Email, Vai trò, Trạng thái.
    *   [ ] Khi tạo tài khoản nhân viên mới, tôi phải nhập các thông tin cơ bản (Họ tên, Email, Mật khẩu tạm thời) và **chọn vai trò** cho họ từ một danh sách định sẵn.
    *   [ ] Hệ thống phải có ít nhất các vai trò sau:
        *   `Quản lý`: Có toàn bộ quyền hạn như chủ xe.
        *   `Nhân viên điều hành`: Có quyền tạo/quản lý chuyến đi, xem danh sách vé, nhưng không xem được báo cáo doanh thu.
        *   `Nhân viên bán vé / CSKH`: Chỉ có quyền truy cập các chức năng liên quan đến đặt và xử lý vé.
    *   [ ] Tôi có thể thay đổi vai trò hoặc vô hiệu hóa (`deactivate`) tài khoản của một nhân viên khi họ nghỉ việc.
    *   [ ] Nhân viên bị vô hiệu hóa sẽ không thể đăng nhập vào hệ thống.
    *   [ ] Hệ thống phải thực thi việc giới hạn quyền truy cập một cách nghiêm ngặt. Ví dụ: tài khoản có vai trò `Nhân viên bán vé` sẽ không thấy được mục "Báo cáo & Thống kê" trên menu.

*   **Mức độ Ưu tiên:** Cao
*   **Story Points:** **8** (Đây là một chức năng có độ phức tạp cao vì nó không chỉ là CRUD đơn giản mà còn liên quan đến việc xây dựng và áp dụng một hệ thống phân quyền (Role-Based Access Control - RBAC) trên toàn bộ ứng dụng).