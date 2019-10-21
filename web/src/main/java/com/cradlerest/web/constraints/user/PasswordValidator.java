package com.cradlerest.web.constraints.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Defines the constraints in which the user password must follow
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
	public void initialize(ValidPassword constraint) {
   }

   public boolean isValid(String password, ConstraintValidatorContext context) {
	   int passwordLengthMin = 8;
	   if (password.length() < passwordLengthMin) {
	   	context.disableDefaultConstraintViolation();
	   	context.buildConstraintViolationWithTemplate("Password length must be a minimum of " + passwordLengthMin + " characters.").addConstraintViolation();
	   	return false;
	   }
	   return true;
   }
}
