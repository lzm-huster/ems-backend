package com.ems.business.model.response;

public class ApprovalRecordResponse {
    private Object originalObject;
    private String Approver;

    public ApprovalRecordResponse(Object originalObject, String approver) {
        this.originalObject = originalObject;
        this.Approver = approver;


    }
}
