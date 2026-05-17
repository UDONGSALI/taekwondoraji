package com.taekwondoraji_api.domain.member.entity;

import com.taekwondoraji_api.common.entity.BaseEntity;
import com.taekwondoraji_api.domain.gym.entity.GymInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

@Getter
@Entity
@Table(name = "member_gym_map")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGymMap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_gym_map_id")
    private Integer memberGymMapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberInfo memberInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id", nullable = false)
    private GymInfo gym;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private MemberRole memberRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_status", nullable = false)
    private MemberStatus memberStatus;

    @Column(name = "belt_name", nullable = false, length = 20)
    private String beltName;

    @Column(name = "point", nullable = false)
    private Integer point;

    public static MemberGymMap create(MemberInfo memberInfo, GymInfo gym) {
        MemberGymMap memberGymMap = new MemberGymMap();
        memberGymMap.memberInfo = memberInfo;
        memberGymMap.gym = gym;
        memberGymMap.memberRole = MemberRole.member;
        memberGymMap.memberStatus = MemberStatus.wait;
        memberGymMap.beltName = "white";
        memberGymMap.point = 0;
        return memberGymMap;
    }

    public static MemberGymMap createManager(MemberInfo memberInfo, GymInfo gym) {
        MemberGymMap memberGymMap = new MemberGymMap();
        memberGymMap.memberInfo = memberInfo;
        memberGymMap.gym = gym;
        memberGymMap.memberRole = MemberRole.master;
        memberGymMap.memberStatus = MemberStatus.active;
        memberGymMap.beltName = "white";
        memberGymMap.point = 0;
        return memberGymMap;
    }

    public void updateMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
    }

    public void updateBeltName(String beltName) {
        this.beltName = beltName;
    }

    public void updatePoint(Integer point) {
        this.point = point;
    }

    public void addPoint(Integer point) {
        int currentPoint = this.point == null ? 0 : this.point;
        int additionalPoint = point == null ? 0 : point;
        this.point = currentPoint + additionalPoint;
    }
}
