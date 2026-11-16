package prep.core.service.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/test/ping")
public class TestController {

    @Value("${server.port:8080}")
    private int port;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @GetMapping
    public ResponseEntity<Map<String,Object>> pingTest(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", 200);
        response.put("message", "Core Service is Running");
        response.put("activeProfile", activeProfile);
        response.put("port", port);
        response.put("time", LocalDateTime.now().format(FORMATTER));
        response.put("host", request.getHeader("Host"));
        response.put("platform", detectPlatform(userAgent));
        response.put("clientIp", getClientIp(request));
        response.put("endpoint", request.getMethod() + " " + request.getRequestURI());
        response.put("userAgent", userAgent);

        return ResponseEntity.ok(response);
    }

    private String detectPlatform(String userAgent) {
        if (userAgent == null) return "Unknown";
        String ua = userAgent.toLowerCase();
        if (ua.contains("postman")) return "Postman";
        if (ua.contains("insomnia")) return "Insomnia";
        if (ua.contains("curl")) return "cURL";
        if (ua.contains("android")) return "Android";
        if (ua.contains("iphone") || ua.contains("ipad")) return "iOS";
        if (ua.contains("windows")) return "Windows Browser";
        if (ua.contains("mac")) return "Mac Browser";
        if (ua.contains("linux")) return "Linux Client";
        return "Other";
    }

    private String getClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}