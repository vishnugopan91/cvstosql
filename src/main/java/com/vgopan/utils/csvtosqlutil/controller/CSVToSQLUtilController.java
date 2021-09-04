package com.vgopan.utils.csvtosqlutil.controller;

import com.vgopan.utils.csvtosqlutil.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author: Vishnu Gopan
 */
@Controller()
public class CSVToSQLUtilController {

    @Autowired
    FileUtils fileUtils;

    @PostMapping(value="/createsql")
    public ResponseEntity<String> createSQL(@RequestParam("csvfile") final MultipartFile inputcsv) {

        if (fileUtils.hasCSVFormat(inputcsv)) {
            try {
                fileUtils.prcessFile(inputcsv);
            } catch (Exception e) {
                return new ResponseEntity<>("Can't parse file: " +
                        inputcsv.getOriginalFilename(), HttpStatus.EXPECTATION_FAILED);
            }
        } else {
            return new ResponseEntity<>("Not a CSV file", HttpStatus.BAD_GATEWAY);
        }
        return ResponseEntity.ok("Exported SQL file successfully");
    }
}
