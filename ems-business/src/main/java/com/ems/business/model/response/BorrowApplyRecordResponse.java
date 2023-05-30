package com.ems.business.model.response;

import lombok.Data;

/**
 * @author zhangxq
 */
@Data
public class BorrowApplyRecordResponse {
    private Object originalObject;
    private String Applicant;
    private String Tutor;

    public BorrowApplyRecordResponse(Object originalObject, String applicant, String tutor) {
        this.originalObject = originalObject;
        this.Applicant = applicant;
        this.Tutor = tutor;

    }
}
