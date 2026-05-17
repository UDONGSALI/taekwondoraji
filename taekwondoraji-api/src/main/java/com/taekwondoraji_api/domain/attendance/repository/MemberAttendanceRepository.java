package com.taekwondoraji_api.domain.attendance.repository;

import com.taekwondoraji_api.domain.attendance.entity.MemberAttendance;
import com.taekwondoraji_api.domain.gym.web.dto.MemberAttendanceCountRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    void deleteByMemberGymMap_MemberGymMapIdAndAttendanceDate(
            Integer memberGymMapId,
            LocalDate attendanceDate
    );

    @Query("""
            select new com.taekwondoraji_api.domain.gym.web.dto.MemberAttendanceCountRow(
                attendance.memberGymMap.memberGymMapId,
                count(attendance)
            )
            from MemberAttendance attendance
            where attendance.memberGymMap.gym.gymId = :gymId
              and attendance.attendanceDate between :startDate and :endDate
            group by attendance.memberGymMap.memberGymMapId
            """)
    List<MemberAttendanceCountRow> countMonthlyAttendancesByGym(
            @Param("gymId") Integer gymId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
