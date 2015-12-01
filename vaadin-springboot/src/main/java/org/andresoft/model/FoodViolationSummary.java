package org.andresoft.model;

public class FoodViolationSummary
{
    private String businessName;
    private int violationCount;

    public FoodViolationSummary(String businessName, int violationCount)
    {
        this.businessName = businessName;
        this.violationCount = violationCount;
    }

    public String getBusinessName()
    {
        return businessName;
    }

    public void setBusinessName(String businessName)
    {
        this.businessName = businessName;
    }

    public int getViolationCount()
    {
        return violationCount;
    }

    public void setViolationCount(int violationCount)
    {
        this.violationCount = violationCount;
    }

}
