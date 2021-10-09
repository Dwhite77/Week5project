package com.sparta.employee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FunctionalMakingEmployeeObjects {


    public void mainStuff(String filePath, ArrayList<EmployeeObject> employeeArrayList){
        try {

            ArrayList<String[]> cleanData = (Files.lines(Paths.get(filePath))
                    .skip(1)
                    .map(line -> line.split(","))
                    .map(e -> isValidData(e))
                    .collect(Collectors.toCollection(ArrayList::new)));

            for (String[] line : cleanData) {
                employeeArrayList.add(stringArrToEmployee(line));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


    public EmployeeObject stringArrToEmployee(String[] inputString) throws ParseException {
        java.util.Date dob = new SimpleDateFormat("MM/dd/yyyy").parse(inputString[7]);
        Date dateOfBirth = new Date(dob.getTime());
        java.util.Date doe = new SimpleDateFormat("MM/dd/yyyy").parse(inputString[8]);
        java.sql.Date dateOfEmployment = new java.sql.Date(doe.getTime());

        return new EmployeeObject(Integer.parseInt(inputString[0]),inputString[1],inputString[2],inputString[3],inputString[4],inputString[5],inputString[6],dateOfBirth,dateOfEmployment,Integer.parseInt(inputString[9]));
    }


    public String[] isValidData(String[] fields){
        boolean dataGate = true;
        RegexPatterns regPat = new RegexPatterns();

        if (Integer.parseInt(fields[0]) < 0) dataGate = false;


        if (fields[1].length() > 5 || !regPat.regexPrefix(fields[1])) dataGate = false;

        if (fields[2].length() > 50 || !regPat.regexName(fields[2])) dataGate = false;

        if (fields[3].length() > 3 || !regPat.regexMiddleInital(fields[3])) dataGate = false;

        if (fields[4].length() > 50 || !regPat.regexName(fields[4])) dataGate = false;

        if (!regPat.regexGender(fields[5])) dataGate = false;

        if (!regPat.regexEmail(fields[6])) dataGate = false;

        try {
            java.util.Date dob = new SimpleDateFormat("MM/dd/yyyy").parse(fields[7]);
            Date dateOfBirth = new Date(dob.getTime());
            fields[7] = String.valueOf(dateOfBirth);
        } catch (ParseException e) {
            dataGate = false;
        }

        try {
            java.util.Date doe = new SimpleDateFormat("MM/dd/yyyy").parse(fields[8]);
            java.sql.Date dateOfEmployment = new java.sql.Date(doe.getTime());
            fields[8] = String.valueOf(dateOfEmployment);
        } catch (ParseException e) {
            dataGate = false;
            //e.printStackTrace();
        }

        if (Integer.parseInt(fields[9]) < 0) dataGate = false;
        if(dataGate) return fields;
        else return null;
    }
}