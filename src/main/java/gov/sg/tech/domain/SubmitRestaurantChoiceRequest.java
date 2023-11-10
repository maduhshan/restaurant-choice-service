package gov.sg.tech.domain;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubmitRestaurantChoiceRequest {

    @Nonnull
    private Long userId;

    @NotBlank
    private String restaurantChoiceName;
}
