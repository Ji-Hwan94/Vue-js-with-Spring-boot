package com.example.springbootapp.test.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Arrays;

@Service
public class TightCouplingTestService {
    
    public List<String> getTestData() {
        return Arrays.asList("Data 1", "Data 2", "Data 3");
    }
    
    public String processData(String data) {
        return "Processed: " + data.toUpperCase();
    }
    
    public int getDataCount() {
        return getTestData().size();
    }
}