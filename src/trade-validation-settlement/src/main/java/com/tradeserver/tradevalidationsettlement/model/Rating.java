package com.tradeserver.tradevalidationsettlement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "ratings")
public class Rating {

    @Id
    private String ticker;
    private String ratingValue;
    private String ratingAgency;
    private String ratingDescription;
    
    public String getTicker() {
        return ticker;
    }  

    public void setTicker(String ticker) {
        this.ticker = ticker;   

    }

    public String getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(String ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getRatingAgency() {
        return ratingAgency;
    }

    public void setRatingAgency(String ratingAgency) {
        this.ratingAgency = ratingAgency;
    }

    public String getRatingDescription() {
        return ratingDescription;
    }

    public void setRatingDescription(String ratingDescription) {
        this.ratingDescription = ratingDescription;
    }
}
