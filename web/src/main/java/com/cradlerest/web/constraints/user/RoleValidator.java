package com.cradlerest.web.constraints.user;

import com.cradlerest.web.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * Defines rules for a user's role(s)
 *
 * Expects:
 * ROLE_ADMIN, ROLE_VHT, ROLE_HEALTHWORKER
 */
public class RoleValidator implements ConstraintValidator<ValidRole, String> {

   List<String> validRoles = Arrays.asList("ROLE_ADMIN", "ROLE_VHT", "ROLE_HEALTHWORKER");

   @Override
   public void initialize(ValidRole constraint) {
   }

   @Override
   public boolean isValid(String roles, ConstraintValidatorContext context) {
      if (roles.isEmpty()) {
         return false;
      }
      String[] splitRoles = roles.split(",");
      for (String role : splitRoles) {
         if (!validRoles.contains(role)) {
            System.out.println("derpa");
            return false;
         }
      }

      return true;
   }
}
