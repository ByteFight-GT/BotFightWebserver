package com.example.botfightwebserver.submission;

import java.time.LocalDateTime;

public record SubmissionDTO(Long id, Long playerId, SUBMISSION_VALIDITY validity, LocalDateTime createdAt) {
    public static SubmissionDTO fromEntity(Submission submission) {
        return new SubmissionDTO(
            submission.getId(),
            submission.getPlayer().getId(),
            submission.getSubmissionValidity(),
            submission.getCreatedAt()
        );
    }
}
