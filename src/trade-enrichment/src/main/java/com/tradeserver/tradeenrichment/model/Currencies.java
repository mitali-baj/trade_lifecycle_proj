package com.tradeserver.tradeenrichment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "currencies")
public class Currencies {

    
    
    @Id
    private String ticker;
    private String currencyName;
    private String currencyCode;

    public String getCurrencyName() {
        return currencyName;
    }
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getTicker() {
        return ticker;
    }  

    public void setTicker(String ticker) {
        this.ticker = ticker;   

    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
