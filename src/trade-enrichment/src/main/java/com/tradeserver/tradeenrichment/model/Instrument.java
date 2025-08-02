package com.tradeserver.tradeenrichment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "instruments")
public class Instrument {

    
    
    @Id
    private String ticker;
    private String instrumentName;
    private String instrumentType;

    public String getInstrumentType() {
        return instrumentType;
    }
    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getTicker() {
        return ticker;
    }  

    public void setTicker(String ticker) {
        this.ticker = ticker;   

    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }
}
