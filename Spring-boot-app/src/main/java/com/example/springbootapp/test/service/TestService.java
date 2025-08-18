package com.example.springbootapp.test.service;

import java.util.List;

public interface TestService {
    List<String> getTestData();
    String processData(String data);
    int getDataCount();
}