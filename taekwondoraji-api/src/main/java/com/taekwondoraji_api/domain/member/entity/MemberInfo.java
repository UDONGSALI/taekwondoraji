package com.taekwondoraji_api.domain.member.entity;

import com.taekwondoraji_api.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "login_id", nullable = false, length = 50)
    private String loginId;

    @Column(name = "login_password", nullable = false, length = 255)
    private String loginPassword;

    @Column(name = "member_name", nullable = false, length = 50)
    private String memberName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "address_road", length = 255)
    private String addressRoad;

    @Column(name = "address_detail", length = 255)
    private String addressDetail;

    @Column(name = "motto", length = 100)
    private String motto;

    public static MemberInfo create(
            String loginId,
            String loginPassword,
            String memberName,
            Integer age,
            String phoneNumber,
            String postalCode,
            String addressRoad,
            String addressDetail
    ) {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.loginId = loginId;
        memberInfo.loginPassword = loginPassword;
        memberInfo.memberName = memberName;
        memberInfo.age = age;
        memberInfo.phoneNumber = phoneNumber;
        memberInfo.postalCode = postalCode;
        memberInfo.addressRoad = addressRoad;
        memberInfo.addressDetail = addressDetail;
        return memberInfo;
    }

    public void updateProfile(
            String memberName,
            Integer age,
            String phoneNumber,
            String motto
    ) {
        this.memberName = memberName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.motto = motto;
    }
}
