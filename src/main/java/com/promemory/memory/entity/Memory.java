package com.promemory.memory.entity;

import com.promemory.memory.type.MemoryType;
import com.promemory.team.entity.Team;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MemoryType memoryType;

    private boolean isPublic;

    private Long likes;

    @OneToOne
    private Team team;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "memory")
    private List<Document> documents = new ArrayList<>();

    public void addDocument(Document document) {

        if (this.documents == null) {
            this.documents = new ArrayList<>();
        }

        document.setMemory(this);
        documents.add(document);
    }

}
