package com.github.copilot.demo.service;

import com.github.copilot.demo.entity.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

@Service
public class HolidayService {


    // Method to read data from CSV file and populate the holidayMap
    private Map<String, Holiday> readDataFromCSVForMap() throws IOException {
            ResourceLoader resourceRenderer = new DefaultResourceLoader();
            Resource resource = resourceRenderer.getResource("classpath:holidays.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            Map<String, Holiday> holidayMap = new HashMap<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    Holiday holiday = new Holiday(data[0], data[1], data[2], data[3]);
                    holidayMap.put(holiday.getKey(), holiday);
                }
            }
        return holidayMap;
    }

    private List<Holiday> readDataFromCSForList() throws IOException {
        ResourceLoader resourceRenderer = new DefaultResourceLoader();
        Resource resource = resourceRenderer.getResource("classpath:holidays.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        List<Holiday> holidayList = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length == 4) {
                Holiday holiday = new Holiday(data[0], data[1], data[2], data[3]);
                holidayList.add( holiday);
            }
        }
        return holidayList;
    }

    // Method to write data to CSV file from the holidayMap
    private void writeDataToCSV( Map<String, Holiday> holidayMap) throws IOException {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/holidays.csv"));
            for (Holiday holiday : holidayMap.values()) {
                bw.write(holiday.getCountryCode() + "," + holiday.getCountryDesc() + ","
                        + holiday.getHolidayDate() + "," + holiday.getHolidayName());
                bw.newLine();
            }
    }



    // Method to add single holiday
    public boolean addHoliday(Holiday holiday) throws IOException {
        Map<String, Holiday> holidayMap = readDataFromCSVForMap();
        if(holidayMap.containsKey(holiday.getKey())){
            return false;
        }
        holidayMap.put(holiday.getKey(), holiday);
        writeDataToCSV(holidayMap);
        return true;
    }

    // Method to add bulk holidays
    public boolean addBulkHolidays(List<Holiday> holidays) throws IOException {
        Map<String, Holiday> holidayMap = readDataFromCSVForMap();
        for (Holiday holiday : holidays) {
            if(holidayMap.containsKey(holiday.getKey())){
                return false;
            }
        }
        for (Holiday holiday : holidays) {
            holidayMap.put(holiday.getKey(), holiday);
        }
        writeDataToCSV(holidayMap);
        return true;
    }

    // Method to update single holiday by unique key (countryCode + holidayDate)
    public boolean updateHoliday(Holiday updatedHoliday) throws IOException {
        Map<String, Holiday> holidayMap = readDataFromCSVForMap();
        if(!holidayMap.containsKey(updatedHoliday.getKey())){
            return false;
        }
        holidayMap.put(updatedHoliday.getKey(), updatedHoliday);
        writeDataToCSV(holidayMap);
        return true;
    }

    // Method to update bulk holidays
    public boolean updateBulkHolidays(List<Holiday> updatedHolidays) throws IOException {
        Map<String, Holiday> holidayMap = readDataFromCSVForMap();
        for (Holiday updatedHoliday : updatedHolidays) {
            if(!holidayMap.containsKey(updatedHoliday.getKey())){
                return false;
            }
        }
        for (Holiday updatedHoliday : updatedHolidays) {
            holidayMap.put(updatedHoliday.getKey(), updatedHoliday);
        }
        writeDataToCSV(holidayMap);
        return true;
    }


    // Method to delete bulk holidays
    public void deleteBulkHolidays(List<String> keysToDelete) throws IOException {
        Map<String, Holiday> holidayMap = readDataFromCSVForMap();
        for (String key : keysToDelete) {
            holidayMap.remove(key);
        }
        writeDataToCSV(holidayMap);
    }


    // Method to get next year's holiday information for a given country based on the current system date
    public List<Holiday> getNextYearHolidays(String countryCode) throws IOException {
        int currentYear = LocalDate.now().getYear();
        int nextYear = currentYear + 1;
        List<Holiday> holidayList = readDataFromCSForList();
        List<Holiday> nextYearHolidays = new ArrayList<>();
        for (Holiday holiday : holidayList) {
            if (holiday.getCountryCode().equalsIgnoreCase(countryCode) && Integer.parseInt(holiday.getHolidayDate().substring(0, 4)) == nextYear) {
                nextYearHolidays.add(holiday);
            }
        }
        return nextYearHolidays;
    }

    // Method to get next holiday information for a given country based on the current system date
    public Holiday getNextHoliday(String countryCode) throws IOException {
        LocalDate currentDate = LocalDate.now();
        List<Holiday> holidayList = readDataFromCSForList();
        for (Holiday holiday : holidayList) {
            if (holiday.getCountryCode().equalsIgnoreCase(countryCode) && LocalDate.parse(holiday.getHolidayDate()).isAfter(currentDate)) {
                return holiday;
            }
        }
        return null;
    }

    // Method to check if a given date is a holiday or not
    public String checkIfDateIsHoliday(String date) throws IOException {
        Set<String> set=new HashSet<>();
        Set<String> set1=new HashSet<>();
        LocalDate checkDate = LocalDate.parse(date);
        List<Holiday> holidayList = readDataFromCSForList();
        for (Holiday holiday : holidayList) {
            if (LocalDate.parse(holiday.getHolidayDate()).isEqual(checkDate)) {
                set.add(holiday.getCountryDesc());
            }else{
                set1.add(holiday.getCountryDesc());
            }
        }
        if(set.size()>0){
            if (set1.size()>0){
                return "Date " + date + " is a holiday in " + set + " and not a holiday in " + set1;
            }
            return "Date " + date + " is a holiday in " + set;
        }
        return "Date " + date + " is not a holiday in any country";
    }

}
