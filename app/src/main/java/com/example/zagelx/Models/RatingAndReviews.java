package com.example.zagelx.Models;
import java.util.List;

public class RatingAndReviews {
    private float overAllRating;
    private float ratingSum ;
    private float ratingCounter;

    private List<String> review ;


    public float getOverAllRating() {
        return overAllRating;
    }

    public List<String> getReview() {
        return review;
    }

    public float getRatingSum() {
        return ratingSum;
    }

    public float getRatingCounter() {
        return ratingCounter;
    }

    public RatingAndReviews(float overAllRating, float ratingSum, float ratingCounter, List<String> review) {
        this.overAllRating = overAllRating;
        this.ratingSum = ratingSum;
        this.ratingCounter = ratingCounter;
        this.review = review;
    }
}
