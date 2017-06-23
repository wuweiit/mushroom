package org.marker.mushroom.validator;

import org.marker.mushroom.beans.Page;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;



public class PageValidator implements Validator {

	@Override
	public boolean supports(Class<?> clzz) {
		return Page.class.equals(clzz);
	}

	@Override
	public void validate(Object obj, Errors e) {
		ValidationUtils.rejectIfEmpty(e, "currentPageNo", "email.empty");
	}

}
