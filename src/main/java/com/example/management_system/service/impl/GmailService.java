package com.example.management_system.service.impl;

import com.example.management_system.config.GmailConfig;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.Session;
import jakarta.mail.Multipart;
import jakarta.mail.internet.*;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.Properties;

@Service
public class GmailService {

    public void sendInvoiceEmail(String to, String subject, String transactionId) throws Exception {
        Gmail service = GmailConfig.getGmailService();

        MimeMessage email = createEmailWithPdf(to, "me", subject, transactionId);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        String encoded = Base64.getUrlEncoder().encodeToString(buffer.toByteArray());

        Message message = new Message().setRaw(encoded);
        service.users().messages().send("me", message).execute();
        System.out.println("✅ Email with PDF sent to: " + to);
    }

    private MimeMessage createEmailWithPdf(String to, String from, String subject, String transactionId) throws Exception {
        Session session = Session.getDefaultInstance(new Properties(), null);
        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);

        // Email body
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(
                "🧾 Mandela Factory Outlet\n\n" +
                        "Dear Customer,\n\n" +
                        "Your invoice #" + transactionId + " is attached as a PDF.\n\n" +
                        "Thank you for shopping with us!\n\n" +
                        "Best regards,\nMandela Factory Outlet Team",
                "utf-8"
        );

        // PDF attachment
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(generateInvoicePdf(transactionId));

        // Combine parts
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);

        email.setContent(multipart);
        return email;
    }

    private File generateInvoicePdf(String transactionId) throws Exception {
        File file = new File("invoice_" + transactionId + ".pdf");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            Document doc = new Document();
            PdfWriter.getInstance(doc, fos);
            doc.open();

            doc.add(new Paragraph("Mandela Factory Outlet", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            doc.add(new Paragraph("Official Invoice", FontFactory.getFont(FontFactory.HELVETICA, 14)));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Invoice ID: " + transactionId));
            doc.add(new Paragraph("Thank you for your purchase!"));
            doc.close();
        }
        return file;
    }
}
