package it.unimol.diffusiontool.properties;

public enum GeneralProperties {
    ERROR (null),
    NULL (0),
    GENERATE (1),
    UPSCALE (2),
    MAX_CAPACITY (3),
    AVAILABLE (0),
    BUSY (1);

    private String code;
    private int value;

    GeneralProperties(String code) {
        this.code = code;
    }

    GeneralProperties(int value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public int getValue() {
        return value;
    }
}
