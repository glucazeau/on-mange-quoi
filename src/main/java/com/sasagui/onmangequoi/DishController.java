package com.sasagui.onmangequoi;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class DishController {

    private final DishService dishService;

    @Operation(summary = "List dishes", description = "Returns the list of dishes")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            })
    @PostMapping("dishes")
    public List<Dish> listDishes(@RequestBody(required = false) DishSearchCriteria searchCiteria) {
        return dishService.listDishes(searchCiteria);
    }
}
