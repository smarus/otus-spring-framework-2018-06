package ru.otus.spring.sokolovsky.readers;

import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

@Service
public class CsvDataReader implements DataReader {

    private CSVReader driver;

    public CsvDataReader(@Value("${quiz.path}") String sourcePath) throws FileNotFoundException {
        String file = getFilePath(sourcePath);
        driver = new CSVReader(new FileReader(file), ',');
    }

    private String getFilePath(String sourcePath) {
        URL resource = CsvDataReader.class.getClassLoader().getResource(sourcePath);
        if (resource == null) {
            throw new RuntimeException(String.format("Path %s is not exists", sourcePath));
        }
        return resource.getFile();
    }

    public String[] getNext() {
        try {
            return driver.readNext();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
