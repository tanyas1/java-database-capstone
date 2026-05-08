package com.project.back_end;

import com.project.back_end.repo.PrescriptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class BackEndApplicationTests {

	@MockBean
	private PrescriptionRepository prescriptionRepository;

	@Test
	void contextLoads() {
	}

}
