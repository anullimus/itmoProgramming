package com.company.things;

public enum Good {
    COTTON("Хлопок") {
        @Override
        public String getTranslation() {
            return "Я люблю Java";
        }
    },
    SUGAR_BEET("Сахарная свекла"),
    MOON_RYE("Лунная рожь"),
    WHEAT("Пшеница");
    public final String translation;

    Good(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }
}
