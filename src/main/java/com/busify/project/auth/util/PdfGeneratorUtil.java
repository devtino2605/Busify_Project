package com.busify.project.auth.util;

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
}