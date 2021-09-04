package com.vgopan.utils.csvtosqlutil.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Vishnu Gopan
 */
@Component
public class FileUtils {

    private final static String FILE_TYPE = "text/csv";
    private final static String MEDIATYPE_EXCEL = "application/vnd.ms-excel";
    private final static Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    @Value("${query_format}")
    String sqlQuery;

    @Value("${indeces_needed_in_order}")
    String indices;

    @Value("${sqlexportlocation}")
    String sqlExportLocation;

    public boolean hasCSVFormat(MultipartFile file) {
        if (FILE_TYPE.equals(file.getContentType()) || file.getContentType().equals(MEDIATYPE_EXCEL)) {
            return true;
        }
        return false;
    }

    public void prcessFile(final MultipartFile file) throws IOException {

        final List<String> lines = new ArrayList<>();

        try (final BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
             final CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            final Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            final List<String> indicesToReplace = Arrays.asList(indices.split(","));

            LOG.info("Query Used: {}", sqlQuery);
            LOG.info("Indices to replace : {}", indices);
            int count = 0;
            for(CSVRecord csvRecord : csvRecords) {
                StringBuilder tmpQuery = new StringBuilder(sqlQuery);
                StringBuilder finalTmpQuery = tmpQuery;
                indicesToReplace.stream().forEach(idx -> finalTmpQuery.replace(finalTmpQuery.indexOf("$"+idx), finalTmpQuery.indexOf("$"+idx)+2, csvRecord.get(Integer.parseInt(idx))));
                lines.add(finalTmpQuery.toString());
                ++count;
            }
            LOG.info("Total number of records processed : {}", count);
            Files.write(Paths.get(sqlExportLocation+"EXPOORTED_SQL_FILE.sql"), lines, StandardCharsets.UTF_8);
            LOG.info("Exported SQL file to : {}", sqlExportLocation+"EXPOORTED_SQL_FILE"+".sql");
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to parse csv file " + e.getMessage());
        }
    }

}
