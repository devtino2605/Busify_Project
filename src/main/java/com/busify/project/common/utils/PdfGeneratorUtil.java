package com.busify.project.common.utils;

import com.busify.project.cargo.entity.CargoBooking;
import com.busify.project.ticket.entity.Tickets;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.core.io.ClassPathResource;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;

public class PdfGeneratorUtil {

        private static PdfFont loadVietnameseFont() throws IOException {
                String fontPath = new ClassPathResource("fonts/DejaVuSans.ttf").getFile().getAbsolutePath();
                return PdfFontFactory.createFont(fontPath);
        }

        public static byte[] generateTicketPDF(String fullName, List<Tickets> tickets) throws IOException {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
                NumberFormat currencyFormatter = NumberFormat.getInstance(new Locale("vi", "VN"));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                try {
                        PdfWriter writer = new PdfWriter(baos);
                        PdfDocument pdfDoc = new PdfDocument(writer);
                        PageSize ticketSize = new PageSize(80 * 2.83f, 100 * 2.83f); // 1 mm ≈ 2.83 pt
                        Document document = new Document(pdfDoc, ticketSize);
                        document.setMargins(5, 5, 5, 5);

                        // Font tiếng Việt
                        PdfFont vnFont = loadVietnameseFont();
                        document.setFont(vnFont);
                        document.setFontSize(5);

                        // ===== HEADER =====
                        document.add(new Paragraph("VÉ XE KHÁCH BUSIFY")
                                        .setFontSize(7)
                                        .setBold()
                                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

                        document.add(new Paragraph("Xin chào " + fullName)
                                        .setFontSize(5)
                                        .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                                        .setMarginBottom(3));

                        // ===== THÔNG TIN HÀNH TRÌNH =====
                        Tickets firstTicket = tickets.get(0);
                        String departureTime = formatter.format(firstTicket.getBooking().getTrip().getDepartureTime());
                        String arrivalTime = formatter
                                        .format(firstTicket.getBooking().getTrip().getEstimatedArrivalTime());
                        String formattedPrice = currencyFormatter.format(firstTicket.getPrice());

                        Table tripTable = new Table(new float[] { 2, 4 });
                        tripTable.setWidth(UnitValue.createPercentValue(100));
                        tripTable.setFontSize(5);

                        tripTable.addCell(new Cell().add(new Paragraph("Tuyến đi")).setBold());
                        tripTable.addCell(new Cell().add(new Paragraph(
                                        firstTicket.getBooking().getTrip().getRoute().getStartLocation().getName()
                                                        + " → " +
                                                        firstTicket.getBooking().getTrip().getRoute().getEndLocation()
                                                                        .getName())));

                        tripTable.addCell(new Cell().add(new Paragraph("Ngày đi")).setBold());
                        tripTable.addCell(new Cell().add(new Paragraph(departureTime)));

                        tripTable.addCell(new Cell().add(new Paragraph("Dự kiến đến")).setBold());
                        tripTable.addCell(new Cell().add(new Paragraph(arrivalTime)));

                        tripTable.addCell(new Cell().add(new Paragraph("Xe/ Biển số")).setBold());
                        tripTable.addCell(
                                        new Cell().add(new Paragraph(firstTicket.getBooking().getTrip().getBus()
                                                        .getLicensePlate())));

                        tripTable.addCell(new Cell().add(new Paragraph("Giá vé")).setBold());
                        tripTable.addCell(new Cell().add(new Paragraph(formattedPrice + " VND")));

                        // Thêm hành khách (tên + sdt) lên bảng này
                        tripTable.addCell(new Cell().add(new Paragraph("Hành khách")).setBold());
                        tripTable.addCell(new Cell().add(new Paragraph(fullName)));
                        tripTable.addCell(new Cell().add(new Paragraph("Số điện thoại")).setBold());
                        tripTable.addCell(new Cell().add(new Paragraph(firstTicket.getPassengerPhone())));

                        document.add(tripTable.setMarginBottom(3));

                        // ===== QR CODE (chung cho cả booking) =====
                        String bookingCode = firstTicket.getBooking().getBookingCode();
                        String qrContent = "Mã đặt chỗ: " + bookingCode + "\nHành khách: " + fullName;

                        byte[] qrCodeBytes = generateQRCode(qrContent, 60, 60);
                        Image qrImage = new Image(ImageDataFactory.create(qrCodeBytes))
                                        .setWidth(60)
                                        .setHeight(60);

                        // ===== DANH SÁCH VÉ + QR =====
                        Table mainTable = new Table(UnitValue.createPercentArray(new float[] { 2, 1 }))
                                        .useAllAvailableWidth().setBorder(Border.NO_BORDER);

                        // Bên trái: bảng vé
                        Table ticketTable = new Table(new float[] { 2, 2 });
                        ticketTable.setWidth(UnitValue.createPercentValue(100));
                        ticketTable.setFontSize(5);
                        ticketTable.setBorder(Border.NO_BORDER);

                        ticketTable.addHeaderCell(new Cell().add(new Paragraph("Mã vé").setBold()));
                        ticketTable.addHeaderCell(new Cell().add(new Paragraph("Ghế").setBold()));

                        for (Tickets ticket : tickets) {
                                ticketTable.addCell(new Cell().add(new Paragraph(ticket.getTicketCode())));
                                ticketTable.addCell(new Cell().add(new Paragraph(ticket.getSeatNumber())));
                        }

                        mainTable.addCell(new Cell()
                                        .add(ticketTable)
                                        .setBorder(Border.NO_BORDER));

                        // Bên phải: QR + mã đặt chỗ
                        Cell rightCell = new Cell()
                                        .add(new Paragraph("Mã đặt chỗ: " + bookingCode)
                                                        .setBold()
                                                        .setFontSize(5)
                                                        .setTextAlignment(TextAlignment.CENTER)
                                                        .setMarginBottom(2)) // chỉ cách QR 2pt
                                        .add(qrImage.setAutoScale(true))
                                        .setBorder(Border.NO_BORDER)
                                        .setTextAlignment(TextAlignment.CENTER)
                                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                                        .setPadding(0); // bỏ padding dư

                        mainTable.addCell(rightCell);

                        document.add(mainTable.setMarginBottom(3));

                        // ===== FOOTER =====
                        document.add(new Paragraph("Lưu ý:")
                                        .setBold()
                                        .setFontSize(5)
                                        .setMarginTop(2) // chỉ 2pt so với phần trên
                                        .setMarginBottom(1));

                        document.add(new Paragraph("- Vui lòng mang theo giấy tờ tùy thân khi lên xe")
                                        .setFontSize(5)
                                        .setMargin(0));
                        document.add(new Paragraph("- Có mặt tại điểm đón trước giờ khởi hành 15 phút")
                                        .setFontSize(5)
                                        .setMargin(0));
                        document.add(new Paragraph("- Liên hệ tổng đài nếu cần hỗ trợ")
                                        .setFontSize(5)
                                        .setMargin(0));

                        document.close();
                } catch (Exception e) {
                        throw new IOException("Error generating PDF", e);
                }

                return baos.toByteArray();
        }

        private static byte[] generateQRCode(String content, int width, int height) throws IOException {
                try {
                        QRCodeWriter qrCodeWriter = new QRCodeWriter();
                        Map<EncodeHintType, Object> hints = new HashMap<>();
                        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // quan trọng

                        BitMatrix bitMatrix = qrCodeWriter.encode(
                                        content,
                                        BarcodeFormat.QR_CODE,
                                        width,
                                        height,
                                        hints);

                        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(bufferedImage, "PNG", baos);

                        return baos.toByteArray();

                } catch (WriterException e) {
                        throw new IOException("Error generating QR code", e);
                }
        }

        /**
         * Generate PDF for cargo booking
         * 
         * @param cargo CargoBooking entity
         * @return PDF as byte array
         * @throws IOException if PDF generation fails
         */
        public static byte[] generateCargoPDF(CargoBooking cargo) throws IOException {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
                NumberFormat currencyFormatter = NumberFormat.getInstance(new Locale("vi", "VN"));

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                try {
                        PdfWriter writer = new PdfWriter(baos);
                        PdfDocument pdfDoc = new PdfDocument(writer);
                        PageSize cargoSize = new PageSize(100 * 2.83f, 140 * 2.83f); // 100x140mm
                        Document document = new Document(pdfDoc, cargoSize);
                        document.setMargins(5, 5, 5, 5); // Giảm margin từ 8 xuống 5

                        // Font tiếng Việt
                        PdfFont vnFont = loadVietnameseFont();
                        document.setFont(vnFont);
                        document.setFontSize(5); // Giảm font size mặc định từ 6 xuống 5

                        // ===== HEADER =====
                        document.add(new Paragraph("PHIẾU GỬI HÀNG BUSIFY")
                                        .setFontSize(7) // Giảm từ 9 xuống 7
                                        .setBold()
                                        .setTextAlignment(TextAlignment.CENTER)
                                        .setMarginBottom(2)); // Giảm từ 4 xuống 2

                        document.add(new Paragraph("Mã vận đơn: " + cargo.getCargoCode())
                                        .setFontSize(6) // Giảm từ 7 xuống 6
                                        .setBold()
                                        .setTextAlignment(TextAlignment.CENTER)
                                        .setMarginBottom(2)); // Giảm từ 4 xuống 2

                        // ===== THÔNG TIN NGƯỜI GỬI =====
                        document.add(new Paragraph("NGƯỜI GỬI")
                                        .setFontSize(6) // Giảm từ 7 xuống 6
                                        .setBold()
                                        .setMarginBottom(1)); // Giảm từ 2 xuống 1

                        Table senderTable = new Table(new float[] { 2, 4 });
                        senderTable.setWidth(UnitValue.createPercentValue(100));
                        senderTable.setFontSize(5); // Giảm từ 6 xuống 5

                        senderTable.addCell(
                                        new Cell().add(new Paragraph("Họ tên")).setBold().setBorder(Border.NO_BORDER));
                        senderTable.addCell(new Cell().add(new Paragraph(cargo.getSenderName()))
                                        .setBorder(Border.NO_BORDER));

                        senderTable.addCell(new Cell().add(new Paragraph("SĐT")).setBold().setBorder(Border.NO_BORDER));
                        senderTable.addCell(new Cell().add(new Paragraph(cargo.getSenderPhone()))
                                        .setBorder(Border.NO_BORDER));

                        if (cargo.getSenderEmail() != null) {
                                senderTable.addCell(new Cell().add(new Paragraph("Email")).setBold()
                                                .setBorder(Border.NO_BORDER));
                                senderTable.addCell(new Cell().add(new Paragraph(cargo.getSenderEmail()))
                                                .setBorder(Border.NO_BORDER));
                        }

                        document.add(senderTable.setMarginBottom(2)); // Giảm từ 4 xuống 2

                        // ===== THÔNG TIN NGƯỜI NHẬN =====
                        document.add(new Paragraph("NGƯỜI NHẬN")
                                        .setFontSize(6) // Giảm từ 7 xuống 6
                                        .setBold()
                                        .setMarginBottom(1)); // Giảm từ 2 xuống 1

                        Table receiverTable = new Table(new float[] { 2, 4 });
                        receiverTable.setWidth(UnitValue.createPercentValue(100));
                        receiverTable.setFontSize(5); // Giảm từ 6 xuống 5

                        receiverTable.addCell(
                                        new Cell().add(new Paragraph("Họ tên")).setBold().setBorder(Border.NO_BORDER));
                        receiverTable.addCell(new Cell().add(new Paragraph(cargo.getReceiverName()))
                                        .setBorder(Border.NO_BORDER));

                        receiverTable.addCell(
                                        new Cell().add(new Paragraph("SĐT")).setBold().setBorder(Border.NO_BORDER));
                        receiverTable.addCell(new Cell().add(new Paragraph(cargo.getReceiverPhone()))
                                        .setBorder(Border.NO_BORDER));

                        if (cargo.getReceiverEmail() != null) {
                                receiverTable.addCell(new Cell().add(new Paragraph("Email")).setBold()
                                                .setBorder(Border.NO_BORDER));
                                receiverTable.addCell(new Cell().add(new Paragraph(cargo.getReceiverEmail()))
                                                .setBorder(Border.NO_BORDER));
                        }

                        document.add(receiverTable.setMarginBottom(2)); // Giảm từ 4 xuống 2

                        // ===== THÔNG TIN HÀNH TRÌNH =====
                        document.add(new Paragraph("THÔNG TIN VẬN CHUYỂN")
                                        .setFontSize(4) // Giảm từ 7 xuống 6
                                        .setBold()
                                        .setMarginBottom(1)); // Giảm từ 2 xuống 1

                        Table tripTable = new Table(new float[] { 2, 4 });
                        tripTable.setWidth(UnitValue.createPercentValue(100));
                        tripTable.setFontSize(5); // Giảm từ 6 xuống 5

                        // Bus operator (company name)
                        String busOperatorName = cargo.getTrip().getBus().getOperator() != null
                                        ? cargo.getTrip().getBus().getOperator().getName()
                                        : "Chưa xác định";
                        tripTable.addCell(
                                        new Cell().add(new Paragraph("Nhà xe")).setBold().setBorder(Border.NO_BORDER));
                        tripTable.addCell(new Cell().add(new Paragraph(busOperatorName)).setBorder(Border.NO_BORDER));

                        String routeName = cargo.getTrip().getRoute().getStartLocation().getName() + " → " +
                                        cargo.getTrip().getRoute().getEndLocation().getName();
                        tripTable.addCell(new Cell().add(new Paragraph("Tuyến")).setBold().setBorder(Border.NO_BORDER));
                        tripTable.addCell(new Cell().add(new Paragraph(routeName)).setBorder(Border.NO_BORDER));

                        String departureTime = formatter.format(cargo.getTrip().getDepartureTime());
                        tripTable.addCell(
                                        new Cell().add(new Paragraph("Ngày đi")).setBold().setBorder(Border.NO_BORDER));
                        tripTable.addCell(new Cell().add(new Paragraph(departureTime)).setBorder(Border.NO_BORDER));

                        // Pickup location with city
                        String pickupLocationName = cargo.getPickupLocation().getName();
                        if (cargo.getPickupLocation().getCity() != null) {
                                pickupLocationName += " - " + cargo.getPickupLocation().getCity();
                        }
                        tripTable.addCell(new Cell().add(new Paragraph("Điểm lấy")).setBold()
                                        .setBorder(Border.NO_BORDER));
                        tripTable.addCell(new Cell().add(new Paragraph(pickupLocationName))
                                        .setBorder(Border.NO_BORDER));

                        // Dropoff location with city
                        String dropoffLocationName = cargo.getDropoffLocation().getName();
                        if (cargo.getDropoffLocation().getCity() != null) {
                                dropoffLocationName += " - " + cargo.getDropoffLocation().getCity();
                        }
                        tripTable.addCell(new Cell().add(new Paragraph("Điểm trả")).setBold()
                                        .setBorder(Border.NO_BORDER));
                        tripTable.addCell(new Cell().add(new Paragraph(dropoffLocationName))
                                        .setBorder(Border.NO_BORDER));

                        tripTable.addCell(new Cell().add(new Paragraph("Xe")).setBold().setBorder(Border.NO_BORDER));
                        tripTable.addCell(new Cell().add(new Paragraph(cargo.getTrip().getBus().getLicensePlate()))
                                        .setBorder(Border.NO_BORDER));

                        // Driver info (name and phone if available)
                        if (cargo.getTrip().getDriver() != null) {
                                String driverName = cargo.getTrip().getDriver().getFullName() != null
                                                ? cargo.getTrip().getDriver().getFullName()
                                                : "Chưa xác định";
                                String driverPhone = cargo.getTrip().getDriver().getPhoneNumber() != null
                                                ? " - " + cargo.getTrip().getDriver().getPhoneNumber()
                                                : "";
                                tripTable.addCell(new Cell().add(new Paragraph("Tài xế")).setBold()
                                                .setBorder(Border.NO_BORDER));
                                tripTable.addCell(new Cell().add(new Paragraph(driverName + driverPhone))
                                                .setBorder(Border.NO_BORDER));
                        }

                        document.add(tripTable.setMarginBottom(2)); // Giảm từ 4 xuống 2

                        // ===== THÔNG TIN HÀNG HÓA =====
                        document.add(new Paragraph("THÔNG TIN HÀNG HÓA")
                                        .setFontSize(6) // Giảm từ 7 xuống 6
                                        .setBold()
                                        .setMarginBottom(1)); // Giảm từ 2 xuống 1

                        Table cargoInfoTable = new Table(new float[] { 2, 4 });
                        cargoInfoTable.setWidth(UnitValue.createPercentValue(100));
                        cargoInfoTable.setFontSize(5); // Giảm từ 6 xuống 5

                        cargoInfoTable.addCell(
                                        new Cell().add(new Paragraph("Loại")).setBold().setBorder(Border.NO_BORDER));
                        cargoInfoTable.addCell(new Cell().add(new Paragraph(cargo.getCargoType().getDisplayName()))
                                        .setBorder(Border.NO_BORDER));

                        cargoInfoTable.addCell(new Cell().add(new Paragraph("Trọng lượng")).setBold()
                                        .setBorder(Border.NO_BORDER));
                        cargoInfoTable.addCell(new Cell().add(new Paragraph(cargo.getWeight() + " kg"))
                                        .setBorder(Border.NO_BORDER));

                        if (cargo.getDimensions() != null) {
                                cargoInfoTable.addCell(new Cell().add(new Paragraph("Kích thước")).setBold()
                                                .setBorder(Border.NO_BORDER));
                                cargoInfoTable.addCell(new Cell().add(new Paragraph(cargo.getDimensions()))
                                                .setBorder(Border.NO_BORDER));
                        }

                        if (cargo.getDeclaredValue() != null) {
                                String declaredValue = currencyFormatter.format(cargo.getDeclaredValue());
                                cargoInfoTable.addCell(new Cell().add(new Paragraph("Giá trị")).setBold()
                                                .setBorder(Border.NO_BORDER));
                                cargoInfoTable.addCell(new Cell().add(new Paragraph(declaredValue + " VND"))
                                                .setBorder(Border.NO_BORDER));
                        }

                        String totalAmount = currencyFormatter.format(cargo.getTotalAmount());
                        cargoInfoTable.addCell(new Cell().add(new Paragraph("Phí vận chuyển")).setBold()
                                        .setBorder(Border.NO_BORDER));
                        cargoInfoTable.addCell(new Cell().add(new Paragraph(totalAmount + " VND")).setBold()
                                        .setBorder(Border.NO_BORDER));

                        document.add(cargoInfoTable.setMarginBottom(2)); // Giảm từ 4 xuống 2

                        // ===== QR CODE + LƯU Ý (CÙNG HÀNG) =====
                        String qrContent = "Mã vận đơn: " + cargo.getCargoCode() + "\n" +
                                        "Người gửi: " + cargo.getSenderName() + "\n" +
                                        "Người nhận: " + cargo.getReceiverName();

                        byte[] qrCodeBytes = generateQRCode(qrContent, 60, 60);
                        Image qrImage = new Image(ImageDataFactory.create(qrCodeBytes))
                                        .setWidth(60)
                                        .setHeight(60);

                        // Table 2 cột: QR bên trái, Lưu ý bên phải
                        Table qrAndNotesTable = new Table(new float[] { 1, 2 }); // QR nhỏ hơn, Notes lớn hơn
                        qrAndNotesTable.setWidth(UnitValue.createPercentValue(100));

                        // Cột 1: QR Code
                        qrAndNotesTable.addCell(new Cell()
                                        .add(qrImage)
                                        .setBorder(Border.NO_BORDER)
                                        .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.TOP)
                                        .setTextAlignment(TextAlignment.CENTER));

                        // Cột 2: Lưu ý
                        Cell notesCell = new Cell()
                                        .setBorder(Border.NO_BORDER)
                                        .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.TOP)
                                        .setPaddingLeft(5);

                        notesCell.add(new Paragraph("LƯU Ý:")
                                        .setBold()
                                        .setFontSize(5)
                                        .setMarginBottom(1));

                        notesCell.add(new Paragraph("• Kiểm tra hàng hóa trước khi nhận")
                                        .setFontSize(4)
                                        .setMargin(0));

                        notesCell.add(new Paragraph("• Liên hệ hotline nếu có vấn đề")
                                        .setFontSize(4)
                                        .setMargin(0));

                        notesCell.add(new Paragraph("• Giữ phiếu để đối chiếu khi nhận")
                                        .setFontSize(4)
                                        .setMargin(0));

                        qrAndNotesTable.addCell(notesCell);

                        document.add(qrAndNotesTable);

                        document.close();
                } catch (Exception e) {
                        throw new IOException("Error generating cargo PDF", e);
                }

                return baos.toByteArray();
        }
}