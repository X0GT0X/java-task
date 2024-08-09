/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.vet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.SerializationUtils;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dave Syer
 */
class VetTests {

	@Test
	void testSerialization() {
		Vet vet = new Vet();
		vet.setFirstName("Zaphod");
		vet.setLastName("Beeblebrox");
		vet.setId(123);
		@SuppressWarnings("deprecation")
		Vet other = (Vet) SerializationUtils.deserialize(SerializationUtils.serialize(vet));
		assertThat(other.getFirstName()).isEqualTo(vet.getFirstName());
		assertThat(other.getLastName()).isEqualTo(vet.getLastName());
		assertThat(other.getId()).isEqualTo(vet.getId());
	}

	@Test
	void testThat_ReturnsYearsOfExperience() {
		var careerStartDate = LocalDate.of(2010, 1, 1);

		var vet = new Vet();
		vet.setFirstName("John");
		vet.setLastName("Smith");
		vet.setCareerStartDate(careerStartDate);

		assertThat(vet.getExperienceInYears()).isEqualTo(LocalDate.now().getYear() - careerStartDate.getYear());
	}

	@ParameterizedTest
	@MethodSource("provideCareerStartDate")
	void testThat_ReturnsExperience_InProperFormatting(LocalDate careerStartDate, String expected) {
		var vet = new Vet();
		vet.setFirstName("John");
		vet.setLastName("Smith");
		vet.setCareerStartDate(careerStartDate);

		assertThat(vet.getFormattedExperienceInYears()).isEqualTo(expected);
	}

	private static Stream<Arguments> provideCareerStartDate() {
		return Stream.of(Arguments.of(LocalDate.now().minusMonths(5), "less than 1 year of experience"),
				Arguments.of(LocalDate.now().minusYears(1), "1 year of experience"),
				Arguments.of(LocalDate.now().minusYears(3), "3 years of experience"));
	}

}
