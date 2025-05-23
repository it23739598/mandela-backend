package com.example.management_system.controller;

import com.example.management_system.service.impl.GmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "*")
public class GmailEmailController {

    @Autowired
    private GmailService gmailService;

    @PostMapping("/send")
    public String sendEmail(@RequestParam String to, @RequestParam String transactionId) {
        try {
            gmailService.sendInvoiceEmail(to, "Your Invoice #" + transactionId, transactionId);
            return "✅ Email sent!";
        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Failed to send: " + e.getMessage();
        }
    }
}
