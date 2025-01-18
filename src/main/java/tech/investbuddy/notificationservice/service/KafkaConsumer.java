package tech.investbuddy.notificationservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final MailgunEmailService mailgunEmailService;
    private final ObjectMapper objectMapper; // Jackson ObjectMapper pour parser JSON

    @KafkaListener(topics = "user-created", groupId = "notification-group")
    public void handleUserEmailVerification(String message) {
        System.out.println("Message reçu : " + message);

        try {
            // Parsez le message JSON
            JsonNode jsonNode = objectMapper.readTree(message);
            String email = jsonNode.get("email").asText();
            String verificationToken = jsonNode.get("verificationToken").asText();

            // Construire le lien de vérification
            String verificationLink = "http://localhost:8080/api/v1/users/auth/verify-email?token=" + verificationToken;

            // Construire le contenu HTML enrichi
            String emailContent = buildHtmlEmailContent(verificationLink);

            // Envoyer l'email
            mailgunEmailService.sendVerificationEmail(
                    email,
                    "Verify Your Email Address",
                    emailContent
            );
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement du message Kafka : " + e.getMessage());
            //e.printStackTrace();
        }
    }

    private String buildHtmlEmailContent(String verificationLink) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 0;
                            padding: 0;
                            background-color: #f9f9f9;
                            color: #333;
                        }
                        .email-container {
                            max-width: 600px;
                            margin: 20px auto;
                            padding: 20px;
                            background-color: #ffffff;
                            border: 1px solid #dddddd;
                            border-radius: 8px;
                            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                        }
                        .email-header {
                            text-align: center;
                            padding-bottom: 20px;
                            border-bottom: 1px solid #eeeeee;
                        }
                        .email-header img {
                            max-width: 120px;
                        }
                        .email-body {
                            margin: 20px 0;
                            line-height: 1.6;
                        }
                        .email-footer {
                            text-align: center;
                            margin-top: 20px;
                            font-size: 12px;
                            color: #888;
                        }
                        .button {
                            display: inline-block;
                            padding: 10px 20px;
                            font-size: 16px;
                            color: #ffffff;
                            background-color: #007bff;
                            text-decoration: none;
                            border-radius: 5px;
                            margin-top: 10px;
                        }
                        .button:hover {
                            background-color: #0056b3;
                        }
                    </style>
                </head>
                <body>
                    <div class="email-container">
                        <div class="email-header">
                            <img src="https://your-logo-url.com/logo.png" alt="InvestBuddy Logo" />
                            <h2>Welcome to InvestBuddy</h2>
                        </div>
                        <div class="email-body">
                            <p>Hello,</p>
                            <p>Thank you for signing up at <strong>InvestBuddy</strong>! To get started, please verify your email address by clicking the button below:</p>
                            <p>
                                <a href="%s" class="button">Verify My Email</a>
                            </p>
                            <p>If the button above doesn’t work, you can also verify your email by copying and pasting the following link into your browser:</p>
                            <p><a href="%s">%s</a></p>
                            <p>Thank you,<br>The InvestBuddy Team</p>
                        </div>
                        <div class="email-footer">
                            <p>© 2025 InvestBuddy. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(verificationLink, verificationLink, verificationLink);
    }
}
