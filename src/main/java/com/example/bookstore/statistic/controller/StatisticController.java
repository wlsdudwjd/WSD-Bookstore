package com.example.bookstore.statistic.controller;

import com.example.bookstore.common.api.ApiResponse;
import com.example.bookstore.statistic.entity.Statistic;
import com.example.bookstore.statistic.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Tag(name = "Statistic", description = "통계 API")
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/books/{bookId}")
    public ApiResponse<List<Statistic>> getBookStatistics(@PathVariable Long bookId) {
        return ApiResponse.success("조회 성공", statisticService.getStatisticsForBook(bookId));
    }
}
