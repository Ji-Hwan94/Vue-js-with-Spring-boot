package com.example.springbootapp.test.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Arrays;

@Service
public class TestServiceImpl implements TestService {
    
    @Override
    public List<String> getTestData() {
        return Arrays.asList("Loose Coupling Data 1", "Loose Coupling Data 2", "Loose Coupling Data 3");
    }
    
    @Override
    public String processData(String data) {
        return "Loosely Processed: " + data.toUpperCase();
    }
    
    @Override
    public int getDataCount() {
        return getTestData().size();
    }
}