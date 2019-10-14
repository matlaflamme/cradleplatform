package com.cradlerest.web.constraints.user;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Defines rules for a user's role(s)
 *
 * Expects:
 * ROLE_ADMIN, ROLE_VHT, ROLE_HEALTHWORKER
 */
public class RoleValidator implements ConstraintValidator<ValidRole, String> {

   List<String> validRoles = Arrays.stream(Role.values()).map(Role::getRole).collect(Collectors.toList());
		   ;
   @Override
   public void initialize(ValidRole constraint) {
   }

   @Override
   public boolean isValid(String roles, ConstraintValidatorContext context) {
       return validateRoles(roles);
   }

   private boolean validateRoles(String roles) {
       if (roles.isEmpty()) {
           return false;
       }
       String[] splitRoles = roles.split(",");
       for (String role : splitRoles) {
           if (!validRoles.contains(role)) {
               return false;
           }
       }
       return true;
   }
}
