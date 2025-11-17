package com.deepcode.deepcode_backend.dto.progress;

import com.deepcode.deepcode_backend.entity.LanguageChallenge;
import com.deepcode.deepcode_backend.entity.LevelChallenge;
import com.deepcode.deepcode_backend.entity.StatusChallenge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProgressResponse {

    private Long challengeId;
    private String challengeTitle;
    private LanguageChallenge language;
    private LevelChallenge level;
    private StatusChallenge status;
    private String notes;
    private LocalDateTime completedAt;
}