package com.ems.business.model.response;

import lombok.Data;

@Data
public class DeviceScrapRecordResponse {
    private Object originalObject;
    private String Applicant;

    public DeviceScrapRecordResponse(Object originalObject, String applicant) {
        this.originalObject = originalObject;
        this.Applicant = applicant;


    }
}
