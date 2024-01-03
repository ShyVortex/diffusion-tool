package it.unimol.diffusiontool.properties;

public enum GeneralProperties {
    ERROR (null);

    private String value;

    GeneralProperties(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
