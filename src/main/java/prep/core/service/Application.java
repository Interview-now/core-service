package prep.core.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static int port;
    public static String activeProfile;

    @Value("${server.port:8080}")
    public void setPort(int port) {
        Application.port = port;
    }

    @Value("${spring.profiles.active:default}")
    public void setActiveProfile(String activeProfile) {
        Application.activeProfile = activeProfile;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("Core Service running on Port: " + port + " | Active Profile: " + activeProfile);
    }

}