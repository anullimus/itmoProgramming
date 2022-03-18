package com.company.things;

public enum StockCommunity {
    GIANT_PLANTS("Гигантские растения");
    public final String translation;

    StockCommunity(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }
}
