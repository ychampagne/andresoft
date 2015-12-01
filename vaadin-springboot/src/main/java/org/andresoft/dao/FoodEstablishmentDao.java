package org.andresoft.dao;

import java.util.List;

import org.andresoft.model.FoodViolationSummary;

public interface FoodEstablishmentDao
{
    public List<FoodViolationSummary> getEstablishmentWithAtLeastNViolations(int n);
}
