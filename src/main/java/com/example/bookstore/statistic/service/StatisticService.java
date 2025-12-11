package com.example.bookstore.statistic.service;

import com.example.bookstore.statistic.entity.Statistic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StatisticService {

    public List<Statistic> getStatisticsForBook(Long bookId) {
        // 실제 전환/조회 데이터가 없으므로 기본값 반환
        return List.of(
                new Statistic("favoritedThenPurchasedUserCount", 0),
                new Statistic("viewedThenPurchasedUserCount", 0)
        );
    }
}
