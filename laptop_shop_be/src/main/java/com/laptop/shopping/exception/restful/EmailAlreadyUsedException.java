package com.laptop.shopping.exception.restful;

import com.laptop.shopping.common.ErrorConstants;

public class EmailAlreadyUsedException extends BadRequestAlertException {
    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "userManagement", "emailexists");
    }
}
