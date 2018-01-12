package de.lalo.jpa.account.boundary;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * @author llorenzen
 * @since 02.01.18
 */
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FailureMessage {

    private String id;

    private String error;

    public FailureMessage(String id, String error) {
        this.id = id;
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public String getId() {
        return id;
    }
}
