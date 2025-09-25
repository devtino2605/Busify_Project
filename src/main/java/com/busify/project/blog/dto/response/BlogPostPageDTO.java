package com.busify.project.blog.dto.response;

import com.busify.project.blog.dto.BlogPostSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostPageDTO {
    private Page<BlogPostSummaryDto> posts;
}