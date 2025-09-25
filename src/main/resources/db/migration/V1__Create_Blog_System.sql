-- Blog system migration
-- Create blog_posts table

CREATE TABLE blog_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    slug VARCHAR(250) NOT NULL UNIQUE,
    excerpt TEXT,
    content LONGTEXT NOT NULL,
    image_url VARCHAR(500),
    author_id BIGINT NOT NULL,
    reading_time INT,
    featured BOOLEAN DEFAULT FALSE,
    published BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    published_at TIMESTAMP NULL,
    view_count BIGINT DEFAULT 0,
    
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_blog_posts_slug (slug),
    INDEX idx_blog_posts_featured (featured),
    INDEX idx_blog_posts_created_at (created_at),
    INDEX idx_blog_posts_published (published),
    INDEX idx_blog_posts_author (author_id)
);

-- Create blog_post_tags table for tags
CREATE TABLE blog_post_tags (
    blog_post_id BIGINT NOT NULL,
    tag VARCHAR(100) NOT NULL,
    
    PRIMARY KEY (blog_post_id, tag),
    FOREIGN KEY (blog_post_id) REFERENCES blog_posts(id) ON DELETE CASCADE,
    INDEX idx_blog_post_tags_tag (tag)
);

-- Insert sample data (optional)
-- You can uncomment these lines if you want some sample blog posts

/*
INSERT INTO blog_posts (title, slug, excerpt, content, author_id, featured, published, published_at) VALUES
('Welcome to Busify Blog', 'welcome-to-busify-blog', 'Chào mừng bạn đến với blog của Busify - nơi chia sẻ những thông tin hữu ích về du lịch và vận chuyển.', 
'<h2>Chào mừng bạn đến với Busify Blog!</h2><p>Chúng tôi rất vui mừng được giới thiệu blog mới của Busify - một nền tảng chia sẻ thông tin, kinh nghiệm và những câu chuyện thú vị về du lịch, vận chuyển và khám phá.</p><p>Tại đây, bạn sẽ tìm thấy:</p><ul><li>Những tips du lịch hữu ích</li><li>Thông tin về các tuyến đường mới</li><li>Câu chuyện từ các tài xế và nhà điều hành</li><li>Tin tức cập nhật về dịch vụ</li></ul>', 
1, TRUE, TRUE, NOW()),

('Tips cho chuyến đi an toàn', 'tips-cho-chuyen-di-an-toan', 'Những lời khuyên quan trọng để có một chuyến đi an toàn và thoải mái.', 
'<h2>Những tips quan trọng cho chuyến đi an toàn</h2><p>Khi đi du lịch bằng xe khách, việc chuẩn bị kỹ lưỡng sẽ giúp bạn có một chuyến đi an toàn và thoải mái.</p><h3>Trước khi khởi hành:</h3><ul><li>Kiểm tra thông tin chuyến xe</li><li>Chuẩn bị giấy tờ tùy thân</li><li>Mang theo thuốc cần thiết</li></ul>', 
1, FALSE, TRUE, NOW());

INSERT INTO blog_post_tags (blog_post_id, tag) VALUES
(1, 'welcome'),
(1, 'introduction'),
(1, 'busify'),
(2, 'travel-tips'),
(2, 'safety'),
(2, 'guide');
*/