package org.springframework.samples.petclinic.vet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

@Component
public class SpecialtyFormatter implements Formatter<Specialty> {

	private final VetRepository vetRepository;

	@Autowired
	public SpecialtyFormatter(VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	@Override
	public Specialty parse(String text, Locale locale) throws ParseException {
		Collection<Specialty> specialties = this.vetRepository.findVetSpecialties();

		for (Specialty specialty : specialties) {
			if (specialty.getName().equals(text)) {
				return specialty;
			}
		}
		throw new ParseException("Specialty not found: " + text, 0);
	}

	@Override
	public String print(Specialty specialty, Locale locale) {
		return specialty.getName();
	}

}
