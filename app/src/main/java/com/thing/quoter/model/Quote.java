package com.thing.quoter.model;


public class Quote {

    private String quote;

    private String speaker;

    public Quote() {
        
    }

    public Quote(String quote, String speaker) {
        this.quote = quote;
        this.speaker = speaker;
    }

    public String getQuote() {
        return this.quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getSpeaker() {
        return this.speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

}
