package org.springframework.samples.petclinic.vet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisabledInNativeImage
public class SpecialtyFormatterTests {

	@Mock
	private VetRepository vetRepository;

	private SpecialtyFormatter specialtyFormatter;

	@BeforeEach
	void setup() {
		this.specialtyFormatter = new SpecialtyFormatter(vetRepository);
	}

	@Test
	void testThat_Print_ReturnsSpecialtyName() {
		Specialty specialty = new Specialty();
		specialty.setName("Surgery");

		String specialtyName = this.specialtyFormatter.print(specialty, Locale.ENGLISH);

		assertThat(specialtyName).isEqualTo("Surgery");
	}

	@Test
	void testThat_Parse_ConvertName_IntoSpecialty() throws ParseException {
		Specialty specialty = new Specialty();
		specialty.setId(1);
		specialty.setName("Surgery");

		given(vetRepository.findVetSpecialties()).willReturn(List.of(specialty));

		var parsedSpecialty = specialtyFormatter.parse("Surgery", Locale.ENGLISH);

		assertThat(parsedSpecialty.getId()).isEqualTo(1);
		assertThat(parsedSpecialty.getName()).isEqualTo("Surgery");
	}

	@Test
	void testThat_Parse_ThrowsParseException_WhenSpecialtyNotFound() throws ParseException {
		given(vetRepository.findVetSpecialties()).willReturn(new ArrayList<>());

		Assertions.assertThrows(ParseException.class, () -> specialtyFormatter.parse("Surgery", Locale.ENGLISH));
	}

}
