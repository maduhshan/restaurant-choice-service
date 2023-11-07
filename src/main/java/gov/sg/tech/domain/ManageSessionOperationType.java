package gov.sg.tech.domain;

import gov.sg.tech.exception.BadRequestException;

import java.util.Arrays;

public enum ManageSessionOperationType {

    END("end"),
    EXTEND("extend");

    String value;

    ManageSessionOperationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ManageSessionOperationType findByName(String name){
        return Arrays.stream(ManageSessionOperationType.values())
                .filter(value -> value.getValue().equals(name))
                .findAny()
                .orElseThrow(() -> new BadRequestException("Un-supported manage session operation type"));
    }
}
