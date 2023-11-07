package gov.sg.tech.service;

import gov.sg.tech.domain.SubmitRestaurantChoiceRequest;
import gov.sg.tech.domain.UserResponse;

public interface RestaurantChoiceService {

    UserResponse submitRestaurantChoice(String id, SubmitRestaurantChoiceRequest choiceRequest);
}
