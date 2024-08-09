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

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link VetController}
 */

@WebMvcTest(value = VetController.class,
		includeFilters = @ComponentScan.Filter(value = SpecialtyFormatter.class, type = FilterType.ASSIGNABLE_TYPE))
@DisabledInNativeImage
@DisabledInAotMode
class VetControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VetRepository vetRepository;

	@MockBean
	private CacheManager cacheManager;

	private Vet james() {
		Vet james = new Vet();
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setCareerStartDate(LocalDate.of(2010, 1, 1));
		james.setId(1);
		return james;
	}

	private Vet helen() {
		Vet helen = new Vet();
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setCareerStartDate(LocalDate.of(2015, 1, 1));
		helen.setId(2);
		Specialty radiology = new Specialty();
		radiology.setId(1);
		radiology.setName("radiology");
		helen.addSpecialty(radiology);
		return helen;
	}

	private Specialty radiology() {
		Specialty radiology = new Specialty();
		radiology.setId(1);
		radiology.setName("radiology");

		return radiology;
	}

	@BeforeEach
	void setup() {
		given(this.vetRepository.findAll()).willReturn(Lists.newArrayList(james(), helen()));
		given(this.vetRepository.findAll(any(Pageable.class)))
			.willReturn(new PageImpl<>(Lists.newArrayList(james(), helen())));

	}

	@Test
	void testShowVetListHtml() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/vets.html?page=1"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("listVets"))
			.andExpect(view().name("vets/vetList"));

	}

	@Test
	void testShowResourcesVetList() throws Exception {
		ResultActions actions = mockMvc.perform(get("/vets").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
		actions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.vetList[0].id").value(1));
	}

	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/vets/new"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("vet", "specialties"))
			.andExpect(view().name("vets/createVetForm"));
	}

	@Test
	void testProcessCreationFormSuccess() throws Exception {
		given(vetRepository.findVetSpecialties()).willReturn(List.of(radiology()));

		mockMvc
			.perform(post("/vets/new")
				.param("firstName", "John")
				.param("lastName", "Smith")
				.param("specialties", "radiology")
				.param("careerStartDate", "2010-01-01"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/vets.html"));
	}

	@Test
	void testThatCacheWasClearedWhenExistedAndNewVetWasAdded() throws Exception {
		var cache = Mockito.mock(Cache.class);

		given(vetRepository.findVetSpecialties()).willReturn(List.of(radiology()));
		given(cacheManager.getCache(VetController.VETS_CACHE_KEY)).willReturn(cache);

		mockMvc.perform(post("/vets/new")
			.param("firstName", "John")
			.param("lastName", "Smith")
			.param("specialties", "radiology")
			.param("careerStartDate", "2010-01-01"));

		verify(cache).clear();
	}

}
