package tech.investbuddy.notificationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.investbuddy.notificationservice.properties.MailgunProperties;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final MailgunProperties mailgunProperties;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("key: " + mailgunProperties.getApiKey() +
                "domain: "+ mailgunProperties.getDomain() +
                "url: " + mailgunProperties.getBaseUrl());
    }
}
