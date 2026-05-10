package com.taekwondoraji_api.domain.attendance.repository;

import com.taekwondoraji_api.domain.attendance.entity.MemberAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberAttendanceRepository extends JpaRepository<MemberAttendance, Integer> {

    List<MemberAttendance> findAllByMemberGymMap_MemberGymMapIdAndAttendanceDateBetweenOrderByAttendanceDateAsc(
            Integer memberGymMapId,
            LocalDate startDate,
            LocalDate endDate
    );

    Optional<MemberAttendance> findByMemberGymMap_MemberGymMapIdAndAttendanceDate(
            Integer memberGymMapId,
            LocalDate attendanceDate
    );
}
