package com.mercer.mettl.assessment.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTestInvite(String toEmail, String inviteLink) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("You're invited to take a test");
        message.setText(
                "Hello,\n\n" +
                        "You have been invited to take a test.\n\n" +
                        "Click the link below to start:\n" +
                        inviteLink + "\n\n" +
                        "This link will expire in 1 hour.\n\n" +
                        "Best regards"
        );

        mailSender.send(message);
    }
}
//
//@Service
//public class EmailService {
//
//    public void sendTestLink(String email, Long testId, String token) {
//
//        String link = "http://localhost:3000/attempt/" + testId + "?token=" + token;
//
//        System.out.println("Sending email to: " + email);
//        System.out.println("Test Link: " + link);
//    }
//}
//
