package com.graduation.realestateconsulting;

import com.graduation.realestateconsulting.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@RequiredArgsConstructor
class RealEstateConsultingApplicationTests {

	private final AuthService authService;
	@Test
	void contextLoads() {
	}

	@Test
	void testCheckExpired_ShouldPass_WhenTimeIsExpired() {
		// Time older than 5 seconds ago → should NOT throw
		LocalDateTime fiveSecondsAgo = LocalDateTime.now().minusSeconds(6);
		assertDoesNotThrow(() -> authService.checkExpired(fiveSecondsAgo));
	}

}
