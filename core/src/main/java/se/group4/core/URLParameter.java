package se.group4.core;

public class URLParameter {
    String keyUrl;
    String valueUrl;

    public URLParameter(String key, String value) {
        this.keyUrl = key;
        this.valueUrl = value;
    }

    public String getKey() {
        return keyUrl;
    }

    public void setKey(String key) {
        this.keyUrl = key;
    }

    public String getValueUrl() {
        return valueUrl;
    }

    public void setValueUrl(String valueUrl) {
        this.valueUrl = valueUrl;
    }

    @Override
    public String toString() {
        return "URLParameter{" +
                "keyUrl='" + keyUrl + '\'' +
                ", valueUrl='" + valueUrl + '\'' +
                '}';
    }
}
