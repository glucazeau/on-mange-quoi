package com.sasagui.onmangequoi.dish;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/dishes", produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class DishController {

    private final DishService dishService;

    @Operation(summary = "List dishes", description = "Returns the list of dishes")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
            })
    @GetMapping
    public List<Dish> listDishes(@RequestBody(required = false) DishSearchCriteria searchCiteria) {
        return dishService.listDishes(searchCiteria);
    }

    @Operation(summary = "Create dish", description = "Adds a new dish")
    @ApiResponse(responseCode = "201", description = "Dish successfully created")
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    public void addDish(@RequestBody NewDish newDish) {
        log.info("New dish request received for {}", newDish);
        dishService.addDish(newDish);
    }
}
