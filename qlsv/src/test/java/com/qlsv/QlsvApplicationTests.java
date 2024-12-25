package com.qlsv;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.qlsv.controller.AuthController;
import com.qlsv.controller.EmailRestController;
import com.qlsv.controller.SinhVienRestController;

@SpringBootTest
class QlsvApplicationTests {

    @InjectMocks
    private AuthController authController;

    @InjectMocks
    private SinhVienRestController sinhVienController;

    @InjectMocks
    private EmailRestController emController;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(authController);
        Assertions.assertNotNull(sinhVienController);
        Assertions.assertNotNull(emController);
    }
}
