package com.company.things;

class Description {
    private String descriptionObject = "Земельные участки";
    public void setDescriptionObject(String descriptionObject) {
        this.descriptionObject = descriptionObject;
    }
    public String getDescriptionTitle() {
        return "Описание - ";
    }
    public String getDescriptionObject() {
        return descriptionObject;
    }
}