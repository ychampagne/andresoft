package org.andresoft.datasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.andresoft.model.FoodViolationSummary;
import org.andresoft.service.FoodEstablishmentService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.visualization.datasource.DataSourceHelper;
import com.google.visualization.datasource.DataSourceRequest;
import com.google.visualization.datasource.base.DataSourceException;
import com.google.visualization.datasource.base.ReasonType;
import com.google.visualization.datasource.base.ResponseStatus;
import com.google.visualization.datasource.base.StatusType;
import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.value.ValueType;


@SuppressWarnings("serial")
@WebServlet("/foodservice")
public class FoodEstablismentServlet extends HttpServlet
{
    DataSourceRequest dsRequest = null;

    @Autowired
    FoodEstablishmentService foodEstablishmentService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        DataSourceRequest dsRequest = null;
        // Extract the request parameters.
        DataTable newData = new DataTable();
        try
        {
            dsRequest = new DataSourceRequest(req);
            String operation = req.getParameter("op");
            if ("count_by_business".equalsIgnoreCase(operation))
            {
                String limit = req.getParameter("limit");
                if (StringUtils.isNumeric(limit))
                {
                    newData = generateViolationsCountByEstablisment(Integer.valueOf(limit));
                }
            }
            DataSourceHelper.setServletResponse(newData, dsRequest, resp);

        }
        catch (DataSourceException e)
        {
            DataSourceHelper.setServletErrorResponse(e, req, resp);
        }
        catch (RuntimeException rte)
        {
            ResponseStatus status = new ResponseStatus(StatusType.ERROR, ReasonType.INTERNAL_ERROR, rte.getMessage());
            if (dsRequest == null)
            {
                dsRequest = DataSourceRequest.getDefaultDataSourceRequest(req);
            }
            DataSourceHelper.setServletErrorResponse(status, dsRequest, resp);
        }


    }

    public DataTable generateViolationsCountByEstablisment(int nViolations)
    {

        List<FoodViolationSummary> fvsList =
                foodEstablishmentService.getEstablishmentWithAtLeastNViolations(nViolations);

        DataTable data = new DataTable();
        ArrayList<ColumnDescription> cd = new ArrayList<ColumnDescription>();
        cd.add(new ColumnDescription("BusinessName", ValueType.TEXT, "Business name"));
        cd.add(new ColumnDescription("nViolations", ValueType.NUMBER, "Number Of Violations"));

        data.addColumns(cd);

        try
        {
            for (FoodViolationSummary fvs : fvsList)
            {
                data.addRowFromValues(fvs.getBusinessName(), fvs.getViolationCount());
            }
        }
        catch (TypeMismatchException e)
        {
            System.out.println("Invalid type!");
        }
        return data;
    }

}
