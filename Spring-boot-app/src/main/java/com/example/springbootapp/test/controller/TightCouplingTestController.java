package com.example.springbootapp.test.controller;

import com.example.springbootapp.test.service.TightCouplingTestService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/test/tight-coupling")
public class TightCouplingTestController {
    
    // 강한 결합 예제 - 직접 인스턴스 생성
    // 컨트롤러가 서비스 인스턴스를 직접 생성하므로 강한 결합을 만듦
    // 테스트, 유지보수, 확장을 어렵게 만듦
    private TightCouplingTestService testService = new TightCouplingTestService();
    
    @GetMapping("/data")
    public List<String> getAllData() {
        // 컨트롤러가 구체적인 구현체와 강하게 결합되어 있음
        return testService.getTestData();
    }
    
    @GetMapping("/process/{data}")
    public String processData(@PathVariable String data) {
        // 테스트를 위해 서비스를 쉽게 모킹하거나 교체할 수 없음
        return testService.processData(data);
    }
    
    @GetMapping("/count")
    public int getDataCount() {
        // 실제 서비스 로직을 포함하지 않고 이 메서드를 단위 테스트하기 어려움
        return testService.getDataCount();
    }
}