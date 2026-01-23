package com.dinepick.dinepickbackend.controller;

import com.dinepick.dinepickbackend.service.KakaoRestaurantImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurants")
@RequiredArgsConstructor
public class AdminRestaurantController {

    private final KakaoRestaurantImportService importService;

    @PostMapping("/import")
    public String importFromKakao(
            @RequestParam String keyword
    ) {
        int count = importService.importByKeyword(keyword);
        return "저장 완료: " + count + "건";
    }
}
