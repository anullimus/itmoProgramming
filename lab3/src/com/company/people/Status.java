package com.company.people;

class Status {
    private final String employer;
    public Status(String employer) {
        this.employer = employer;
    }
    public String getStatusText() {
        return "Их статус - ";
    }

    public String getStatus() {
        return "работают на объект: ".concat(employer).concat(".\n");
    }
}