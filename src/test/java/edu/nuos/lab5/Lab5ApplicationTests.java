package edu.nuos.lab5;

import edu.nuos.lab5.repository.CarRepository;
import edu.nuos.lab5.repository.OwnerRepository;
import edu.nuos.lab5.repository.ServiceItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class Lab5ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ServiceItemRepository serviceItemRepository;

    @Test
    void contextLoadsAndRendersPages() throws Exception {
        assertThat(ownerRepository.count()).isGreaterThanOrEqualTo(3);
        assertThat(serviceItemRepository.count()).isGreaterThanOrEqualTo(4);
        assertThat(carRepository.count()).isGreaterThanOrEqualTo(3);

        mockMvc.perform(get("/cars")).andExpect(status().isOk());
        mockMvc.perform(get("/owners")).andExpect(status().isOk());
        mockMvc.perform(get("/services")).andExpect(status().isOk());

        Long firstCarId = carRepository.findAllByOrderByIdAsc().get(0).getId();
        mockMvc.perform(get("/cars/" + firstCarId + "/services")).andExpect(status().isOk());
    }
}
