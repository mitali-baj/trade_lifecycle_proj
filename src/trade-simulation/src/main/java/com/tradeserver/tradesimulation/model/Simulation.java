// package com.tradeserver.tradesimulation.model;

// public class Simulation {
//     private String ticker;
//     private String title;
//     private String sentiment;

//     // Getters and setters
//     public String getTicker() {
//         return ticker;
//     }
//     public void setTicker(String ticker) {
//         this.ticker = ticker;
//     }
//     public String getTitle() {
//         return title;
//     }
//     public void setTitle(String title) {
//         this.title = title;
//     }
//     public String getSentiment() {
//         return sentiment;
//     }
//     public void setSentiment(String sentiment) {
//         this.sentiment = sentiment;
//     }
//     @Override
//     public String toString() {  
//         return "Simulation{" +
//                 "ticker='" + ticker + '\'' +
//                 ", title=" + title +
//                 ", sentiment=" + sentiment +
//                 '}';
//     }
// }

package com.tradeserver.tradesimulation.model;

public class Simulation {
    private String title;
    private String ticker;
    private String sentiment;

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }

    public String getSentiment() { return sentiment; }
    public void setSentiment(String sentiment) { this.sentiment = sentiment; }

}
