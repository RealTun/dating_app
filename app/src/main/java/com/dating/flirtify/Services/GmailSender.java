package com.dating.flirtify.Services;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GmailSender {
    private static final String EMAIL = "dungsatoru.dev@gmail.com";
    private static final String PASSWORD = "qitt aoci zxmd hxcu";

    public static boolean sendEmail(String toEmail, String subject, String otp) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            String htmlContent = ""+
                "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">" +
                    "<div style=\"margin:50px auto;width:95%;padding:20px 0\">" +
                        "<div style=\"border-bottom:1px solid #eee\">" +
                            "<a href=\"\" style=\"font-size:1.4em;color: #E32F70;text-decoration:none;font-weight:600\">Flirtify</a>" +
                        "</div>" +
                        "<p style=\"font-size:1.1em\">Thân gửi,</p>" +
                        "<p>Cảm ơn bạn đã lựa chọn chúng tôi. Sử dụng mã xác thực OTP dưới đây để hoàn tất đăng ký. Mã OTP có hiệu lực trong vòng 1 phút 30 giây</p>" +
                        "<h2 style=\"background: #EE8585;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">" + otp + "</h2>" +
                        "<p style=\"font-size:0.9em;\">Trân trọng,<br />Flirtify</p>" +
                        "<hr style=\"border:none;border-top:1px solid #eee\" />" +
                        "<div style=\"float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300\">" +
                            "<p>Hạ Quang Dũng</p>" +
                            "<p>Đỗ Hữu Tuấn</p>" +
                            "<p>Lê Đình Tú</p>" +
                        "</div>" +
                    "</div>" +
                "</div>";

            // Sử dụng setContent thay vì setText để gửi email định dạng HTML
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            return true; // Email sent successfully
        } catch (Exception e) {
            // Sử dụng logging framework thay vì in stack trace
            // e.g., Logger.getLogger(GmailSender.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
            return false; // Failed to send email
        }
    }
}