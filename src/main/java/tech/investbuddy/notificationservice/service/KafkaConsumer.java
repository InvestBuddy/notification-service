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

            // Envoyer l'email
            mailgunEmailService.sendVerificationEmail(
                    email,
                    "Verify Your Email Address",
                    "Please click the following link to verify your email: " + verificationLink
            );
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement du message Kafka : " + e.getMessage());
            //e.printStackTrace();
        }
    }
}
