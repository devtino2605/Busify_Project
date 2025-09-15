package com.busify.project.complaint.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintStatsDTO {
    private long newCount;
    private long pendingCount;
    private long inProgressCount;
    private long resolvedCount;
    private long rejectedCount;
}