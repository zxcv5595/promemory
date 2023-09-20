package com.promemory.member.entity;

import com.promemory.global.entity.BaseEntity;
import com.promemory.member.type.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String profileImg;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isFirst;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

}
