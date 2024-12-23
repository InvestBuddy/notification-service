package tech.investbuddy.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tech.investbuddy.notificationservice.properties.MailgunProperties;

@Service
@RequiredArgsConstructor
public class MailgunEmailService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final MailgunProperties mailgunProperties;

    public void sendVerificationEmail(String to, String subject, String text) {
        String url = String.format("%s/%s/messages", mailgunProperties.getBaseUrl(), mailgunProperties.getDomain());

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("api", mailgunProperties.getApiKey());

        String requestBody = String.format(
                "from=%s&to=%s&subject=%s&text=%s",
                "no-reply@" + mailgunProperties.getDomain(), to, subject, text
        );

        headers.set("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send email: " + response.getBody());
        }
    }
}
