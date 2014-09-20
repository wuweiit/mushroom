package org.marker.mushroom.validator;
 
import org.marker.mushroom.beans.Module;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
 


/**
 * 表单验证
 * 
 * */
public class ModuleValidator implements Validator {

	
	@Override
	public boolean supports(Class<?> clzz) { 
		return Module.class.equals(clzz);
	}

	
	@Override
	public void validate(Object obj, Errors err) {
		ValidationUtils.rejectIfEmpty(err, "name", "name.empty");
	}

}
