package com.example.umbrella;

import com.example.umbrella.model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGetEmail() {
        User user = new User();
        user.setEmail("test@sch.ac.kr");
        assertEquals("test@sch.ac.kr", user.getEmail());  // 이게 컴파일되면 Lombok 잘 작동 중
    }
}
