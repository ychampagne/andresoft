package org.andresoft.service;

import java.util.List;

import org.andresoft.dao.FoodEstablishmentDao;
import org.andresoft.model.FoodViolationSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FoodEstablishmentServiceImpl implements FoodEstablishmentService
{

    @Autowired
    FoodEstablishmentDao foodEstablishmentDao;

    @Override
    public List<FoodViolationSummary> getEstablishmentWithAtLeastNViolations(int n)
    {
        return foodEstablishmentDao.getEstablishmentWithAtLeastNViolations(n);
    }

}
