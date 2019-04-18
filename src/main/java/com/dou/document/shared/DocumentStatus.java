package com.dou.document.shared;

import org.springframework.util.StringUtils;

public enum DocumentStatus {

    ORIGINAL                ("ORIGINAL", "ORIGINAL"),
    VALID                   ("VALID", "VALID"),
    INVALID                 ("INVALID", "INVALID"),

    SENDING                 ("SENDING", "SENDING"),
    RECEIVED                ("RECEIVED", "RECEIVED"),
    DISTRIBUTED             ("DISTRIBUTED", "DISTRIBUTED"),

    DEFER                   ("DEFER", "DEFER"),
    MODIFIED                ("MODIFIED", "MODIFIED"),

    PASS                    ("PASS", "PASS"),
    CANCEL                  ("CANCEL", "CANCEL"),
    CANCEL_AUTO             ("CANCEL_AUTO", "AUTO CANCEL"),
    CANCEL_STEP_RECEIVED    ("CANCEL_STEP_RECEIVED", "CANCEL STEP RECEIVED"),
    CANCEL_STEP_PASS        ("CANCEL_STEP_PASS", "CANCEL STEP PASS");

    private String code;
    private String description;

    DocumentStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static DocumentStatus fromString(String textVal) {
        if (StringUtils.hasText(textVal)) {
            for (DocumentStatus status : DocumentStatus.values()) {
                if (textVal.equalsIgnoreCase(status.toString())) {
                    return status;
                }
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
