package org.services.project.courseproject;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/*
For women, BMR = 655.1 + (9.563 x weight in kg) + (1.850 x height in cm) - (4.676 x age in years)
For men, BMR = 66.47 + (13.75 x weight in kg) + (5.003 x height in cm) - (6.755 x age in years)

*/
@Component
public class CalculatorRepository {

    private static final double maleBase = 66.47;
    private static final double maleWeightMultiplier = 13.75;
    private static final double maleHeightMultiplier = 5.003;
    private static final double maleAgeMultiplier = 6.755;

    private static final double activityMultiplier = 1.24;

    public int calculateCalories(final int age, final double height, final double weight) {
        final double weightResult = maleWeightMultiplier * weight;
        final double heightResult = maleHeightMultiplier * height;
        final double ageResult = maleAgeMultiplier * age;

        final double BMR = maleBase + weightResult + heightResult - ageResult;
        final long calories = Math.round(BMR * activityMultiplier);

        //Casting here shouldn't be a problem since we are supposed to be working with small number
        return (int) calories;
    }

}
