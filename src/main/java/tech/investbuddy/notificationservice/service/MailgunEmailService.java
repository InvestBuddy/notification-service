/*package tech.investbuddy.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tech.investbuddy.notificationservice.properties.MailgunProperties;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MailgunEmailService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final MailgunProperties mailgunProperties;

    public void sendVerificationEmail(String to, String subject, String htmlContent) {
        try {
            // Construire l'URI de la requête Mailgun avec UriComponentsBuilder.fromUriString
            URI uri = UriComponentsBuilder.fromUriString(mailgunProperties.getBaseUrl())
                    .pathSegment(mailgunProperties.getDomain(), "messages")
                    .build()
                    .toUri();

            // Préparer les headers avec authentification
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth("api", mailgunProperties.getApiKey());
            headers.set("Content-Type", "application/x-www-form-urlencoded");

            // Construire le corps de la requête pour HTML
            String requestBody = buildRequestBody(
                    "no-reply@" + mailgunProperties.getDomain(),
                    to,
                    subject,
                    htmlContent
            );

            // Préparer et envoyer la requête POST
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

            // Vérifier la réponse
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to send email: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while sending email: " + e.getMessage(), e);
        }
    }


    private String buildRequestBody(String from, String to, String subject, String htmlContent) {
        return "from=" + encode(from) +
                "&to=" + encode(to) +
                "&subject=" + encode(subject) +
                "&html=" + encode(htmlContent); // Utilisation de "html" pour les e-mails enrichi
    }


    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}*/

package tech.investbuddy.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tech.investbuddy.notificationservice.properties.MailgunProperties;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MailgunEmailService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final MailgunProperties mailgunProperties;

    /**
     * Envoie un email utilisant un template Mailgun.
     *
     * @param to              L'adresse email du destinataire.
     * @param subject         Le sujet de l'email.
     * @param verificationLink Le lien de vérification.
     */
    public void sendVerificationEmail(String to, String subject, String verificationLink) {
        try {
            // Construire l'URI de la requête Mailgun
            URI uri = UriComponentsBuilder.fromUriString(mailgunProperties.getBaseUrl())
                    .pathSegment(mailgunProperties.getDomain(), "messages")
                    .build()
                    .toUri();

            // Préparer les headers avec authentification
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth("api", mailgunProperties.getApiKey());
            headers.set("Content-Type", "application/x-www-form-urlencoded");

            // Construire le corps de la requête
            String requestBody = buildRequestBody(
                    "no-reply@" + mailgunProperties.getDomain(),
                    to,
                    subject,
                    verificationLink
            );

            // Préparer et envoyer la requête POST
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(uri, request, String.class);

            // Vérifier la réponse
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to send email: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while sending email: " + e.getMessage(), e);
        }
    }

    /**
     * Construit le corps de la requête pour envoyer un email via un template Mailgun.
     */
    private String buildRequestBody(String from, String to, String subject, String verificationLink) {
        return "from=" + encode(from) +
                "&to=" + encode(to) +
                "&subject=" + encode(subject) +
                "&template=verificationtemplate" + // Nom du template Mailgun
                "&h:X-Mailgun-Variables=" + encode("{\"verificationLink\": \"" + verificationLink + "\"}");
    }

    /**
     * Encode une valeur pour "application/x-www-form-urlencoded".
     */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}

