package com.github.copilot.demo.controller;


import com.github.copilot.demo.entity.Holiday;
import com.github.copilot.demo.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/holiday")
public class HolidayController {
    @Autowired
    private HolidayService holidayService;

    // Add single holiday
    @PostMapping("/single")
    public ResponseEntity<String> addSingleHoliday(@RequestBody Holiday holiday) throws IOException {
        boolean b = holidayService.addHoliday(holiday);
        if (!b) {
            return new ResponseEntity<>("Holiday with the same countryCode and holidayDate already exists",
                    HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>("Single holiday added successfully", HttpStatus.CREATED);
    }

    // Add bulk holidays
    @PostMapping("/bulk")
    public ResponseEntity<String> addBulkHolidays(@RequestBody List<Holiday> holidays) throws IOException {
        boolean b = holidayService.addBulkHolidays(holidays);
            if (!b) {
                return new ResponseEntity<>("Holiday with the same countryCode and holidayDate already exists",
                        HttpStatus.CONFLICT);
            }
        return new ResponseEntity<>("Bulk holidays added successfully", HttpStatus.CREATED);
    }

    // Update single holiday by unique key (countryCode + holidayDate)
    @PutMapping("/single")
    public ResponseEntity<String> updateSingleHoliday(@RequestBody Holiday updatedHoliday) throws IOException {
        boolean b = holidayService.updateHoliday(updatedHoliday);
        if (b) {
            return new ResponseEntity<>("Single holiday updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Holiday not found", HttpStatus.NOT_FOUND);
        }
    }

    // Update bulk holidays
    @PutMapping("/bulk")
    public ResponseEntity<String> updateBulkHolidays(@RequestBody List<Holiday> updatedHolidays) throws IOException {
        boolean b = holidayService.updateBulkHolidays(updatedHolidays);
            if (!b) {
                return new ResponseEntity<>("Holiday with the same countryCode and holidayDate not found",
                        HttpStatus.NOT_FOUND);
            }
        return new ResponseEntity<>("Bulk holidays updated successfully", HttpStatus.OK);
    }



    // Delete bulk holidays by unique keys (countryCode + holidayDate)
    @DeleteMapping("/bulk")
    public ResponseEntity<String> deleteBulkHolidays(@RequestBody List<String> keysToDelete) throws IOException {
        holidayService.deleteBulkHolidays(keysToDelete);
        return new ResponseEntity<>("Bulk holidays deleted successfully", HttpStatus.OK);
    }

    // API 1: Get next year's holiday information for a given country based on the current system date
    @GetMapping("/nextyear/{countryCode}")
    public ResponseEntity<List<Holiday>> getNextYearHolidays(@PathVariable String countryCode) throws IOException {
        List<Holiday> nextYearHolidays = holidayService.getNextYearHolidays(countryCode);
        return new ResponseEntity<>(nextYearHolidays, HttpStatus.OK);
    }

    // API 2: Get next holiday information for a given country based on the current system date
    @GetMapping("/nextholiday/{countryCode}")
    public ResponseEntity<Holiday> getNextHoliday(@PathVariable String countryCode) throws IOException {
        Holiday nextHoliday = holidayService.getNextHoliday(countryCode);
        if (nextHoliday != null) {
            return new ResponseEntity<>(nextHoliday, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // API 3: Check if a given date is a holiday or not
    @GetMapping("/checkholiday/{date}")
    public ResponseEntity<String> checkIfDateIsHoliday(@PathVariable String date) throws IOException {
        String result = holidayService.checkIfDateIsHoliday(date);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
