package com.tim7.iss.tim7iss;

import com.tim7.iss.tim7iss.services.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class Tim7issApplicationTests {

    @Autowired
    MailService mailService;

    void contextLoads() throws IOException {
    }

}
