package com.busify.project.routePopular;

import com.busify.project.route.dto.response.PopularRouteResponse;

import com.busify.project.route.repository.RouteRepository;
import com.busify.project.route.service.RouteService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteService routeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPopularRoutes_ShouldReturnList() {
        // Giả lập dữ liệu trả về từ repository
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
        
        when(routeRepository.findPopularRoutes()).thenReturn(mockRoutes);

        // Gọi service
        List<PopularRouteResponse> result = routeService.getPopularRoutes();

        // Kiểm tra
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getRouteName()).isEqualTo("Hanoi - HCMC");
    }

    // @Test
    // void getAllRoutes_ShouldReturnMappedList() {
    //     // Giả lập dữ liệu Route entity
    //     List<Route> mockEntities = Arrays.asList(
    //             new Route(1L, "Hanoi", "HCMC"),
    //             new Route(2L, "Danang", "HCMC")
    //     );
    //     when(routeRepository.findAll()).thenReturn(mockEntities);

    //     // Gọi service
    //     List<RouteResponse> result = routeService.getAllRoutes();

    //     // Kiểm tra
    //     assertThat(result).hasSize(2);
    //     assertThat(result.get(0).getStartLocation()).isEqualTo("Hanoi");
    //     assertThat(result.get(1).getEndLocation()).isEqualTo("HCMC");
    // }
}
