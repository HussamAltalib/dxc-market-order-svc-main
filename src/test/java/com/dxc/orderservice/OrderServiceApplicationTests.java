package com.dxc.orderservice;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class OrderServiceApplicationTests {

	Calculator underTest = new Calculator();

	@Test
	void itShouldAddNumbers() {
		// given
		int numberOne = 20;
		int numberTwo = 30;
		// when
		int result = underTest.add(numberOne, numberTwo);
		// then
		assertThat(result).isEqualTo(50);
	}

	class Calculator {
		int add(int a, int b) {
			return a + b;
		}
	}

}
