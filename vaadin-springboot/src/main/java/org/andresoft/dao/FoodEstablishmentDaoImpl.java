package org.andresoft.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.andresoft.model.FoodViolationSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class FoodEstablishmentDaoImpl implements FoodEstablishmentDao
{
    //@formatter:off
    private String countViolationSql =
            "select fei.BusinessName, count(1) as NumViolations "
            + " from consumer.food_establishment_inspections_boston fei "
            + " where fei.ISSDTTM >= ? "
            + " and fei.result = 'HE_Fail'"
            + " group by fei.BusinessName"
            + " having count(1)  >= ?"
            + " order by 2 desc";
    //@formatter:on

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public List<FoodViolationSummary> getEstablishmentWithAtLeastNViolations(int n)
    {

        LocalDateTime currentYear = Year.now().atDay(1).atTime(0, 0);
        long currentYearMilli = currentYear.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Date currentYearMidnight = new java.sql.Date(currentYearMilli);

        Object[] queryParams = new Object[] {currentYearMidnight, n};
        List<FoodViolationSummary> summaries;

        try
        {
            summaries = jdbcTemplate.query(countViolationSql, queryParams, new FoodViolationSummaryRowMapper());
        }
        catch (EmptyResultDataAccessException e)
        {
            summaries = new ArrayList<FoodViolationSummary>(0);
        }

        // TODO Auto-generated method stub
        return summaries;
    }


    public static class FoodViolationSummaryRowMapper implements RowMapper<FoodViolationSummary>
    {
        public FoodViolationSummary mapRow(ResultSet rs, int rowNum) throws SQLException
        {

            FoodViolationSummary fvs = null;
            String businessName = rs.getString("BusinessName");
            int numViolations = rs.getInt("NumViolations");
            fvs = new FoodViolationSummary(businessName, numViolations);
            return fvs;
        }
    }

}
