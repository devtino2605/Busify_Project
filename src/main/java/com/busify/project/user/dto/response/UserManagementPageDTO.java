package com.busify.project.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserManagementPageDTO {
    private List<UserManagementDTO> users;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
    private boolean isFirst;
    private boolean isLast;

    // Filter summary for UI
    private FilterSummaryDTO filterSummary;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilterSummaryDTO {
        private long totalActive;
        private long totalInactive;
        private long totalSuspended;
        private long totalEmailVerified;
        private long totalGoogleAuth;
        private long totalLocalAuth;
    }
}
