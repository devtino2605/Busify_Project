package com.busify.project.routePopular;

import com.busify.project.route.controller.RouteController;
import com.busify.project.route.dto.response.PopularRouteResponse;
import com.busify.project.route.dto.response.RouteResponse;
import com.busify.project.route.service.RouteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RouteController.class)
class RouteControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private RouteService routeService;

        @Test
        void getPopularRoutes_ShouldReturnApiResponse() throws Exception {
                List<PopularRouteResponse> mockRoutes = Arrays.asList(
                                new PopularRouteResponse(
                                                1L,
                                                "TP.HCM - Hà Nội",
                                                "5", // durationHours là String
                                                BigDecimal.valueOf(500000)),
                                new PopularRouteResponse(
                                                2L,
                                                "TP.HCM - Đà Lạt",
                                                "5", // durationHours là String
                                                BigDecimal.valueOf(500000)));
                when(routeService.getPopularRoutes()).thenReturn(mockRoutes);

                mockMvc.perform(get("/api/routes/popular-routes")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Popular routes fetched successfully"))
                                .andExpect(jsonPath("$.result[0].routeName").value("Hanoi - HCMC"));
        }

        @Test
        void getAllRoutes_ShouldReturnApiResponse() throws Exception {
                List<RouteResponse> mockRoutes = Arrays.asList(
                                RouteResponse.builder()
                                                .id(1L)
                                                .name("Hà Nội-Hải Phòng")
                                                .start_location("Hà Nội")
                                                .end_location("Hải Phòng")
                                                .default_price(new BigDecimal("150000"))
                                                .build(),
                                RouteResponse.builder()
                                                .id(2L)
                                                .name("Hà Nội-Đà Nẵng")
                                                .start_location("Hà Nội")
                                                .end_location("Đà nẵng")
                                                .default_price(new BigDecimal("150000"))
                                                .build()

                );
                when(routeService.getAllRoutes()).thenReturn(mockRoutes);

                mockMvc.perform(get("/api/routes")
                                .accept(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("All routes fetched successfully"))
                                .andExpect(jsonPath("$.result[1].startLocation").value("Danang"));
        }
}
