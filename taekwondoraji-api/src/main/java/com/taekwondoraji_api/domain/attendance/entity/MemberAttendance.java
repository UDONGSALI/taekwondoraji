package com.taekwondoraji_api.domain.attendance.entity;

import com.taekwondoraji_api.domain.member.entity.MemberGymMap;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "member_attendance")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Integer attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_gym_map_id", nullable = false)
    private MemberGymMap memberGymMap;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public static MemberAttendance create(MemberGymMap memberGymMap, LocalDate attendanceDate) {
        MemberAttendance attendance = new MemberAttendance();
        attendance.memberGymMap = memberGymMap;
        attendance.attendanceDate = attendanceDate;
        return attendance;
    }
}
