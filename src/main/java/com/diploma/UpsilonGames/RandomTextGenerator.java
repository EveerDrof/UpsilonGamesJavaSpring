package com.diploma.UpsilonGames;

import java.util.Random;

public class RandomTextGenerator {
    private final Random random;

    public RandomTextGenerator() {
        random = new Random();
    }

    private <T> T getRandomValue(T[] arr) {
        return arr[Math.abs(random.nextInt()) % arr.length];
    }

    private String getPositiveAdjective(String ending) {
        String[] adjectives = new String[]{"хорош", "отличн", "классн", "крут", "прекрасн"};
        String result = "";
        if (random.nextBoolean()) {
            result = "просто ";
        }
        return result + getRandomValue(adjectives) + ending;
    }

    private String getReviewStart() {
        String[] reviewStartings = new String[]{"Игра", "Эта игра", "Данная игра"};
        return getRandomValue(reviewStartings);
    }

    private String getCharacteristic() {
        String[] characteristics = new String[]{"Геймплейная составляющая", "Графика", "Физика", "История"};
        return getRandomValue(characteristics);
    }

    public String getReviewText() {
        String review = getReviewStart() + " ";
        review += getPositiveAdjective("ая");
        review += ". " + getCharacteristic() + " " + getPositiveAdjective("ая");
        return review;
    }

    public String getOpinion() {
        String[] opinions = new String[]{"Понравилась", "Не понравилась", "мне кажется средней", "мне очень понравилась",
                "мне очень не понравилась", "мне кажется ниже среднего", "мне кажется выше среднего", "не очень"};
        return getRandomValue(opinions);
    }

    public String getComment() {
        String comment = getCharacteristic() + " " + getOpinion();
        return comment;
    }
}
