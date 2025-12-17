package org.example.kiosk_manage.order.domain;

public enum Payment {
    KKP("KKP"),
    CASH("CASH"),
    ETC("ETC");


    private final String value;

    Payment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
