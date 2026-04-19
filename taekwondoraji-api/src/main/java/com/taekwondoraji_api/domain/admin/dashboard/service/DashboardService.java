package com.taekwondoraji_api.domain.admin.dashboard.service;

import com.taekwondoraji_api.domain.admin.dashboard.dto.DashboardPage;
import com.taekwondoraji_api.domain.admin.dashboard.dto.DashboardResponse;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    public DashboardPage getDashboardPage() {
        return new DashboardPage(
                "서비스 운영 관리자",
                "도장 고객, 전체 회원, 구독 상품, 공지와 운영 정책을 통합 관리하는 운영 화면입니다."
        );
    }

    public DashboardResponse getDashboardResponse() {
        return new DashboardResponse(
                "서비스 운영 관리자",
                "도장 고객, 전체 회원, 구독 상품, 공지와 운영 정책을 통합 관리하는 운영 화면입니다."
        );
    }
}
