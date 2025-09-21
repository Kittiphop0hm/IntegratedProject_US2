package com.example.backend.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    //     http://intproj24.sit.kmutt.ac.th/us2/users/verify-email?token=%s
    public void sendEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Confirm your email");
        String messageBody = """
                 Thank you for registration, Please Confirm your email to Get 2000 Robux Free!
                
                 http://localhost:5173/verify-email?token=%s
                """.formatted(token);
        message.setText(messageBody);
        mailSender.send(message);
    }
}
