package com.example.botfightwebserver.submission;

import com.example.botfightwebserver.ClockConfig;
import com.example.botfightwebserver.player.Player;
import com.google.common.annotations.VisibleForTesting;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Clock;
import java.time.LocalDateTime;

@Entity
@Table
@Builder
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String storagePath;

    private Long playerId;

    @Enumerated(EnumType.STRING)
    private SUBMISSION_VALIDITY submissionValidity;

    @Enumerated(EnumType.STRING)
    private STORAGE_SOURCE source;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime validateAt;

    private static Clock clock = Clock.systemDefaultZone();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(clock);
    }


    public void setSubmissionValidity(SUBMISSION_VALIDITY submissionValidity) {
        this.submissionValidity = submissionValidity;
        if (submissionValidity == SUBMISSION_VALIDITY.VALID) {
            validateAt = LocalDateTime.now(clock);
        }
    }

    @VisibleForTesting
    public static void setClock(Clock testClock) {
        clock = testClock;
    }
}
