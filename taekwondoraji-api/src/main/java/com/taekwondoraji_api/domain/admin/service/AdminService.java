package com.taekwondoraji_api.domain.admin.service;

import com.taekwondoraji_api.domain.admin.dto.AdminDashboardResponse;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    public AdminDashboardResponse getDashboard() {
        return new AdminDashboardResponse(
                "서비스 운영 관리자",
                "도장 고객, 전체 회원, 구독 상품, 공지와 운영 정책을 통합 관리하는 내부 운영 화면입니다."
        );
    }
}
