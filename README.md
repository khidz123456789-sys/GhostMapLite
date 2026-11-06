# GhostMap Lite

Ứng dụng Android độc lập, giao diện Light, viết mới bằng Kotlin + Jetpack Compose.

## Tính năng bản khởi đầu
- Trang tổng quan.
- Mở OpenStreetMap trong trình duyệt.
- Công cụ ghi chú trong phiên.
- Chọn cấu hình giao diện/chế độ.
- Không đăng nhập, không thanh toán, không yêu cầu quyền vị trí.

## Build APK bằng GitHub trên điện thoại
1. Tải và giải nén `GhostMapLite.zip`.
2. Đăng nhập GitHub bằng trình duyệt trên điện thoại.
3. Tạo repository mới tên `GhostMapLite`.
4. Chọn **Add file → Upload files**, tải toàn bộ nội dung đã giải nén lên repository (giữ nguyên thư mục `.github/workflows`).
5. Chọn **Commit changes**.
6. Mở tab **Actions** → workflow **Build GhostMap Lite APK**.
7. Nhấn **Run workflow** (hoặc đợi workflow chạy sau khi push).
8. Khi chạy thành công, mở lần chạy mới nhất ở cuối trang.
9. Trong **Artifacts**, tải `GhostMapLite-debug` và giải nén để lấy `app-debug.apk`.
10. Mở APK trên điện thoại để cài. Nếu Android hỏi, chỉ cho phép cài ứng dụng từ trình duyệt/tệp bạn vừa dùng.

## Ghi chú kỹ thuật
- GitHub Actions cài JDK 17 và dùng Gradle có sẵn trên runner.
- Dự án dùng Android Gradle Plugin 8.7.3, Kotlin 2.0.21, compileSdk 35.
- Đây là bản prototype. Ghi chú hiện chỉ lưu trong bộ nhớ phiên chạy; chưa có lưu trữ lâu dài.
- Không chứa mã, tài nguyên, API key hoặc cơ chế Premium của GhostMap gốc.
