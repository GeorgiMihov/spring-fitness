package org.services.project.courseproject;

import io.spring.guides.gs_producing_web_service.CalculateCaloriesRequest;
import io.spring.guides.gs_producing_web_service.CalculateCaloriesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CalculatorEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private CalculatorRepository calculatorRepository;

    @Autowired
    public CalculatorEndpoint(CalculatorRepository calculatorRepository) {
        this.calculatorRepository = calculatorRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "calculateCaloriesRequest")
    @ResponsePayload
    public CalculateCaloriesResponse calculateCalories(@RequestPayload CalculateCaloriesRequest request) {
        if (request.getAge() <= 0 || request.getHeight() <= 0 || request.getWeight() <= 0) {
            final CalculateCaloriesResponse result = new CalculateCaloriesResponse();
            result.setCalories(-1);
            return result;
        }

        int result = calculatorRepository.calculateCalories(request.getAge(), request.getHeight(), request.getWeight());

        CalculateCaloriesResponse response = new CalculateCaloriesResponse();
        response.setCalories(result);

        return response;
    }
}
