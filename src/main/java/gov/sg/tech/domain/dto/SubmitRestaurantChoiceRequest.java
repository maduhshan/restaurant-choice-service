package gov.sg.tech.domain.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SubmitRestaurantChoiceRequest {

    @Nonnull
    private Long userId;

    @NotBlank
    private String restaurantChoiceName;
}
