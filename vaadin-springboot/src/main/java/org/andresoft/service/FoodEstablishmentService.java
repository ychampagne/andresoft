package org.andresoft.service;

import java.util.List;

import org.andresoft.model.FoodViolationSummary;

public interface FoodEstablishmentService
{
    public List<FoodViolationSummary> getEstablishmentWithAtLeastNViolations(int n);
}
