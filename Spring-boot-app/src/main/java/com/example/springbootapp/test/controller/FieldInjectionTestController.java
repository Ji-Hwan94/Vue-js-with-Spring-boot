package com.example.springbootapp.test.controller;

import com.example.springbootapp.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/test/field-injection")
public class FieldInjectionTestController {
    
    // 느슨한 결합 예제 2 - @Autowired를 사용한 필드 주입
    // 이 방식도 추상화에 의존하여 느슨한 결합을 촉진
    // 하지만 생성자 주입에 비해 몇 가지 단점이 있음:
    // 1. 필드를 final로 만들 수 없음 (변경 가능)
    // 2. 생성자에서 의존성이 명시적이지 않음
    // 3. 테스트하기 더 어려움 (리플렉션이나 Spring 컨텍스트 필요)
    // 4. 너무 많은 의존성을 숨길 수 있음 (단일 책임 원칙 위반)
    @Autowired
    private TestService testService;
    
    @GetMapping("/data")
    public List<String> getAllData() {
        // 여전히 추상화에 의존하여 느슨한 결합 가능
        return testService.getTestData();
    }
    
    @GetMapping("/process/{data}")
    public String processData(@PathVariable String data) {
        // 필드 주입은 작동하지만 테스트에 더 많은 설정이 필요
        return testService.processData(data);
    }
    
    @GetMapping("/count")
    public int getDataCount() {
        // Spring이 리플렉션을 통해 자동으로 주입을 처리
        return testService.getDataCount();
    }
}