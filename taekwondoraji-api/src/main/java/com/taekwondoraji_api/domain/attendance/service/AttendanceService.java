package com.taekwondoraji_api.domain.attendance.service;

import com.taekwondoraji_api.common.exception.BusinessException;
import com.taekwondoraji_api.common.exception.ErrorCode;
import com.taekwondoraji_api.domain.attendance.dto.AttendanceResponse;
import com.taekwondoraji_api.domain.attendance.entity.MemberAttendance;
import com.taekwondoraji_api.domain.attendance.repository.MemberAttendanceRepository;
import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import com.taekwondoraji_api.domain.member.repository.MemberGymMapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {

    private final MemberAttendanceRepository memberAttendanceRepository;
    private final MemberGymMapRepository memberGymMapRepository;

    public List<AttendanceResponse> getMonthlyAttendances(Integer memberGymMapId, Integer year, Integer month) {
        if (!memberGymMapRepository.existsById(memberGymMapId)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return memberAttendanceRepository
                .findAllByMemberGymMap_MemberGymMapIdAndAttendanceDateBetweenOrderByAttendanceDateAsc(
                        memberGymMapId,
                        startDate,
                        endDate
                )
                .stream()
                .map(AttendanceResponse::from)
                .toList();
    }

    @Transactional
    public AttendanceResponse attendToday(Integer memberGymMapId) {
        LocalDate today = LocalDate.now();
        return memberAttendanceRepository
                .findByMemberGymMap_MemberGymMapIdAndAttendanceDate(memberGymMapId, today)
                .map(AttendanceResponse::from)
                .orElseGet(() -> createAttendance(memberGymMapId, today));
    }

    @Transactional
    public AttendanceResponse attendDate(Integer memberGymMapId, LocalDate attendanceDate) {
        return memberAttendanceRepository
                .findByMemberGymMap_MemberGymMapIdAndAttendanceDate(memberGymMapId, attendanceDate)
                .map(AttendanceResponse::from)
                .orElseGet(() -> createAttendance(memberGymMapId, attendanceDate));
    }

    @Transactional
    public AttendanceResponse attendDate(Integer gymId, Integer memberGymMapId, LocalDate attendanceDate) {
        validateGymMember(gymId, memberGymMapId);
        return attendDate(memberGymMapId, attendanceDate);
    }

    @Transactional
    public void deleteAttendanceDate(Integer memberGymMapId, LocalDate attendanceDate) {
        if (!memberGymMapRepository.existsById(memberGymMapId)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        memberAttendanceRepository.deleteByMemberGymMap_MemberGymMapIdAndAttendanceDate(
                memberGymMapId,
                attendanceDate
        );
    }

    @Transactional
    public void deleteAttendanceDate(Integer gymId, Integer memberGymMapId, LocalDate attendanceDate) {
        validateGymMember(gymId, memberGymMapId);
        deleteAttendanceDate(memberGymMapId, attendanceDate);
    }

    private void validateGymMember(Integer gymId, Integer memberGymMapId) {
        memberGymMapRepository.findByMemberGymMapIdAndGym_GymId(memberGymMapId, gymId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));
    }

    private AttendanceResponse createAttendance(Integer memberGymMapId, LocalDate attendanceDate) {
        MemberGymMap memberGymMap = memberGymMapRepository.findById(memberGymMapId)
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_INPUT_VALUE));
        MemberAttendance attendance = MemberAttendance.create(memberGymMap, attendanceDate);
        return AttendanceResponse.from(memberAttendanceRepository.save(attendance));
    }
}
