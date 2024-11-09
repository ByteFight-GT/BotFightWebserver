package com.example.botfightwebserver.submission;

import com.example.botfightwebserver.player.Player;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storagePath;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="player_id", nullable = false)
    private Player player;

    @Enumerated(EnumType.STRING)
    private SUBMISSION_VALIDITY submissionValidity;

    @Enumerated(EnumType.STRING)
    private STORAGE_SOURCE source;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime validateAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void setSubmissionValidity(SUBMISSION_VALIDITY submissionValidity) {
        this.submissionValidity = submissionValidity;
        validateAt = LocalDateTime.now();
    }
}
