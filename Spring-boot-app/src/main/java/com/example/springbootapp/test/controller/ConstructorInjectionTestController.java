package com.example.springbootapp.test.controller;

import com.example.springbootapp.test.service.TestService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/test/constructor-injection")
public class ConstructorInjectionTestController {
    
    // 느슨한 결합 예제 1 - 생성자 주입 (권장 방식)
    // 이 방식은 구체적인 구현체가 아닌 추상화(인터페이스)에 의존하여
    // 느슨한 결합을 촉진함
    private final TestService testService;
    
    // 생성자 주입 - Spring이 TestService 구현체를 자동으로 주입
    // 이것이 권장되는 방식인 이유:
    // 1. 의존성을 명시적이고 필수적으로 만듦
    // 2. 불변 필드(final) 가능
    // 3. 테스트에 더 좋음 (모킹 구현체를 쉽게 전달 가능)
    // 4. 의존성이 누락되면 빠르게 실패
    public ConstructorInjectionTestController(TestService testService) {
        this.testService = testService;
    }
    
    @GetMapping("/data")
    public List<String> getAllData() {
        // 컨트롤러는 구체적인 구현체가 아닌 추상화에 의존
        return testService.getTestData();
    }
    
    @GetMapping("/process/{data}")
    public String processData(@PathVariable String data) {
        // 생성자에 모킹 구현체를 전달하여 쉽게 단위 테스트 가능
        return testService.processData(data);
    }
    
    @GetMapping("/count")
    public int getDataCount() {
        // 느슨한 결합으로 다양한 구현체를 쉽게 교체 가능
        return testService.getDataCount();
    }
}