package org.bootstrap.member.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping
    public String healthCheck() {
        return "Goooood!";
    }
}
