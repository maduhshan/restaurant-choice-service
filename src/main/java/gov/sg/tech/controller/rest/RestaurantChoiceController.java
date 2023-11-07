package gov.sg.tech.controller.rest;

import gov.sg.tech.domain.SubmitRestaurantChoiceRequest;
import gov.sg.tech.domain.UserResponse;
import gov.sg.tech.service.RestaurantChoiceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/restaurantChoices")
public class RestaurantChoiceController {

    private final RestaurantChoiceService restaurantChoiceService;

    @PostMapping("/sessions/{id}/submit")
    public UserResponse submitRestaurantChoice(@PathVariable String id,
                                               @RequestBody @Valid SubmitRestaurantChoiceRequest choice) {
        return restaurantChoiceService.submitRestaurantChoice(id, choice);
    }
}
