package com.cradlerest.web.service.repository;
import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.model.Referral;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Database repository for {@code Referral} entities.
 *
 * @see com.cradlerest.web.model.Referral
 */
public interface ReferralRepository extends JpaRepository<Referral, Integer> {
	List<Referral> findAllByHealthCentre(@NotNull String string) throws NoSuchElementException;
	List<Referral> findAllByOrderByTimestampDesc();
}
