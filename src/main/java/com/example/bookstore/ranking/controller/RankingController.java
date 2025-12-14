package com.example.bookstore.ranking.controller;

import com.example.bookstore.common.api.ApiResponse;
import com.example.bookstore.ranking.entity.Ranking;
import com.example.bookstore.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/ranking")
@RequiredArgsConstructor
@Tag(name = "Ranking", description = "베스트셀러/랭킹 API")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    public ApiResponse<List<Ranking>> getTopRanking() {
        return ApiResponse.success("조회 성공", rankingService.getTopBooks());
    }
}
