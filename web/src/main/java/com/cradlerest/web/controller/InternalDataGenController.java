package com.cradlerest.web.controller;

import com.cradlerest.web.controller.exceptions.BadRequestException;
import com.cradlerest.web.service.InternalDataGenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for data generation.
 */
@RestController
@RequestMapping("/api/internal")
public class InternalDataGenController {

	private static final int MAX_AMOUNT = 200;

	private InternalDataGenService dataGenService;

	public InternalDataGenController(InternalDataGenService dataGenService) {
		this.dataGenService = dataGenService;
	}

	@PostMapping("/db/danger/autogen")
	public void generateDummyData(@RequestParam(defaultValue = "20") Integer amount) throws BadRequestException {
		if (amount <= 0 || amount > MAX_AMOUNT) {
			throw new BadRequestException("amount is too large/small");
		}
		dataGenService.generateDummyData(amount);
	}
}
