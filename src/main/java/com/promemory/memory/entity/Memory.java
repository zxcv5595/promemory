package com.promemory.memory.entity;

import com.promemory.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Memory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String roomId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "memory")
    private List<ConnectedMembers> connectedMembers = new ArrayList<>();

    public Memory(String roomId){
        this.roomId = roomId;
    }

    @OneToOne(fetch = FetchType.LAZY,mappedBy ="memory")
    private Project project;
}
