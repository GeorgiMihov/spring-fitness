package org.services.project.courseproject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.guides.gs_producing_web_service.SearchRecipeRequest;
import io.spring.guides.gs_producing_web_service.SearchRecipeResponse;
import org.services.project.courseproject.recipepojos.EdamamRecipesResponse;
import org.services.project.courseproject.recipepojos.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import java.net.URI;


@Endpoint
public class RecipeSearcherEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public RecipeSearcherEndpoint() {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "searchRecipeRequest")
    @ResponsePayload
    public SearchRecipeResponse searchForRecipe(@RequestPayload SearchRecipeRequest request) {
        try {
            final String url = "https://api.edamam.com/api/recipes/v2?type=public&q=" +
                    request.getIngredient() +
                    "&app_id=db495203&app_key=fe5a91b5107db3af8b5d7e36f86ab9dc&calories=" +
                    request.getCalories() +
                    "&random=true";

            RequestEntity<Void> request1 = RequestEntity.get(URI.create(url))
                    .accept(MediaType.APPLICATION_JSON)
                    .build();
            final RestTemplate restCaller = new RestTemplate();
            final ResponseEntity<String> response = restCaller.exchange(request1, String.class);

            final EdamamRecipesResponse recipesResponse = objectMapper.readValue(response.getBody(), EdamamRecipesResponse.class);

            final SearchRecipeResponse result = new SearchRecipeResponse();
            final Recipe recipe = recipesResponse.getHits().get(0).getRecipe();

            if (recipe.getLabel() != null) {
                result.setLabel(recipe.getLabel());
            }
            if (recipe.getSource() != null) {
                result.setSource(recipe.getSource());
            }
            if (recipe.getYield() != null) {
                result.setYield(recipe.getYield());
            }
            if (recipe.getIngredientLines() != null) {
                result.getIngredientLines().addAll(recipe.getIngredientLines());
            }
            if (recipe.getCalories() != null) {
                result.setCalories(recipe.getCalories());
            }
            if (recipe.getTotalWeight() != null) {
                result.setTotalWeight(recipe.getTotalWeight());
            }
            if (recipe.getTime() != null) {
                result.setTime(recipe.getTime());
            }
            if (recipe.getMealType() != null) {
                result.getMealType().addAll(recipe.getMealType());
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new SearchRecipeResponse();
        }
    }
}