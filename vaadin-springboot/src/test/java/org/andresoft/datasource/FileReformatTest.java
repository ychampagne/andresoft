package org.andresoft.datasource;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class FileReformatTest
{
    @Test
    public void testReformatChicagoFoodInpectionCsv() throws IOException
    {
        // Inspection ID | DBA Name | AKA Name| License # | Facility Type| Risk| Address| City|
        // State| Zip| Inspection Date| Inspection Type| Results| Violations| Latitude| Longitude|
        // Location
        System.setProperty("line.separator", "\n");
        Reader in = new FileReader("/Development/andresoft/hadoop_data/Food_Inspections_chicago.csv");
        File file = new File("/Development/andresoft/hadoop_data/Food_Inspections_chicago_nomalized-2.csv");
        FileWriter fw = new FileWriter(file);

        final CSVPrinter printer =
                CSVFormat.DEFAULT.withHeader("Inspection ID", "DBA Name", "AKA Name", "License #", "Facility Type",
                        "Risk", "Address", "City", "State", "Zip", "Inspection Date", "Inspection Type", "Results",
                        "Violation Number", "Violation", "Comments", "Latitude", "Longitude", "Loacation").print(fw);


        final CSVParser parser = new CSVParser(in, CSVFormat.EXCEL.withHeader());

        // Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
        for (CSVRecord record : parser)
        {
            String inspectionId = record.get("Inspection ID");
            String dbaName = record.get("DBA Name");
            String akaName = record.get("AKA Name");
            String licenseNum = record.get("License #");
            String facility = record.get("Facility Type");
            String risk = record.get("Risk");
            String address = record.get("Address");
            String city = record.get("City");
            String state = record.get("State");
            String zip = record.get("Zip");
            String inspectionDate = record.get("Inspection Date");
            String inspectionType = record.get("Inspection Type");
            String results = record.get("Results");
            String violations = record.get("Violations");
            String latitude = record.get("Latitude");
            String longitude = record.get("Longitude");
            String location = record.get("Location");

            String violationsArray[] = violations.split("\\|");
            for (String v : violationsArray)
            {
                String comments = "None";
                String violation = "None";
                String[] violationWihComment = v.split("Comments:");
                if (violationWihComment.length == 2)
                {
                    violation = violationWihComment[0];
                    comments = violationWihComment[1];
                }
                else
                {
                    violation = violationWihComment[0];
                }
                if (!StringUtils.isBlank(violation))
                {
                    int violationNumberEndIndex = violation.indexOf('.');
                    int viloationNumber = Integer.valueOf((violation.substring(0, violationNumberEndIndex)).trim());


                    printer.printRecord(inspectionId, dbaName, akaName, licenseNum, facility, risk, address, city,
                            state, zip, inspectionDate, inspectionType, results, viloationNumber,
                            violation.substring(violationNumberEndIndex + 1), comments, latitude, longitude, location);
                }
            }

        }
        printer.close();
        in.close();
    }

}
