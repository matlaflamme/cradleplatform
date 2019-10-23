package com.cradlerest.web.constraints.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Defines the constraints for a user's username
 */
public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
   public void initialize(ValidUsername constraint) {
   }

   public boolean isValid(String username, ConstraintValidatorContext context) {
	   int usernameLengthMin = 6;
	   int usernameLengthMax = 25;
	   if (username.length() < usernameLengthMin) {
		   context.disableDefaultConstraintViolation();
		   context.buildConstraintViolationWithTemplate("Username length must be a minimum of " + usernameLengthMin + " characters.").addConstraintViolation();
		   return false;
	   } else if (username.length() > usernameLengthMax) {
	   	context.disableDefaultConstraintViolation();
	   	context.buildConstraintViolationWithTemplate("Username length must be a maximum of " + usernameLengthMax + " characters.").addConstraintViolation();
	   	return false;
	   }
	   return true;
   }
}
