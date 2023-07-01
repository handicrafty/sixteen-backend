CREATE TABLE IF NOT EXISTS sixteen.`user`
(
    id                      BIGINT AUTO_INCREMENT,
    nickname                VARCHAR(12)            NOT NULL COMMENT '닉네임',
    mbti                    VARCHAR(4)             NOT NULL COMMENT 'MBTI 유형',
    gender                  VARCHAR(6)             NOT NULL COMMENT '성별(MALE, FEMALE)',
    age                     INT                    NOT NULL COMMENT '연령대',
    modify_count            INT      default 0     NOT NULL COMMENT 'MBTI, 프로필 수정 횟수',
    like_and_feedback_count BIGINT   default 0     NOT NULL COMMENT '추천수(게시글, 댓글)',
    primary_badge_id        BIGINT                 NULL,
    created_at              DATETIME default NOW() NOT NULL COMMENT '생성일시',
    updated_at              DATETIME default NOW() NOT NULL COMMENT '수정일시',
    CONSTRAINT user_pk
        PRIMARY KEY (id)
) COMMENT '사용자 정보' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sixteen.`mission`
(
    id                     BIGINT AUTO_INCREMENT,
    name                   VARCHAR(255)           NOT NULL COMMENT '미션 이름',
    image_url              VARCHAR(2048)          NOT NULL COMMENT '미션 이미지 URL',
    created_at             DATETIME default NOW() NOT NULL COMMENT '생성일시',
    updated_at             DATETIME default NOW() NOT NULL COMMENT '수정일시',
    CONSTRAINT mission_pk
        PRIMARY KEY (id)
) COMMENT '미션' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sixteen.`user_mission`
(
    id         BIGINT AUTO_INCREMENT,
    user_id    BIGINT                 NOT NULL,
    mission_id BIGINT                 NOT NULL,
    created_at DATETIME default NOW() NOT NULL COMMENT '생성일시',
    CONSTRAINT user_mission_pk
        PRIMARY KEY (id),
    CONSTRAINT user_mission_uindex
        UNIQUE (user_id, mission_id)
) COMMENT '유저의 미션 수행이력' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sixteen.`badge`
(
    id                     BIGINT AUTO_INCREMENT,
    name                   VARCHAR(255)           NOT NULL COMMENT '뱃지 이름',
    image_url              VARCHAR(2048)          NOT NULL COMMENT '뱃지 이미지 URL',
    required_mission_count INT                    NOT NULL COMMENT '획득에 필요한 완료 미션 개수',
    created_at             DATETIME default NOW() NOT NULL COMMENT '생성일시',
    updated_at             DATETIME default NOW() NOT NULL COMMENT '수정일시',
    CONSTRAINT badge_pk
        PRIMARY KEY (id)
) COMMENT '뱃지' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sixteen.`notification`
(
    id                BIGINT AUTO_INCREMENT,
    user_id           BIGINT                 NOT NULL COMMENT '대상 유저',
    title             VARCHAR(255)           NOT NULL COMMENT '알림 제목',
    body              VARCHAR(5000)           NOT NULL COMMENT '알림 내용',
    notification_type VARCHAR(50)            NOT NULL COMMENT '알림 유형',
    metadata          JSON                   NOT NULL COMMENT '알림 메타데이터',
    created_at        DATETIME default NOW() NOT NULL COMMENT '생성일시',
    CONSTRAINT notification_pk
        PRIMARY KEY (id)
) COMMENT '알림' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sixteen.`post`
(
    id         BIGINT AUTO_INCREMENT,
    writer_id  BIGINT                 NOT NULL COMMENT '작성자',
    mbti       VARCHAR(4)             NOT NULL COMMENT 'MBTI 유형(필터링용)',
    title      VARCHAR(255)           NOT NULL COMMENT '제목',
    body       VARCHAR(5000)           NOT NULL COMMENT '내용',
    image_url  VARCHAR(2048)          NULL COMMENT '이미지 URL',
    status     VARCHAR(50)            NOT NULL COMMENT '게시글 상태',
    created_at DATETIME default NOW() NOT NULL COMMENT '생성일시',
    updated_at DATETIME default NOW() NOT NULL COMMENT '수정일시',
    CONSTRAINT post_pk
        PRIMARY KEY (id)
) COMMENT '게시글' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sixteen.`bookmark`
(
    id         BIGINT AUTO_INCREMENT,
    user_id    BIGINT                 NOT NULL,
    post_id    BIGINT                 NOT NULL,
    created_at DATETIME default NOW() NOT NULL COMMENT '생성일시',
    CONSTRAINT bookmark_pk
        PRIMARY KEY (id),
    CONSTRAINT bookmark_uindex
        UNIQUE (user_id, post_id)
) COMMENT '게시글 북마크' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sixteen.`like`
(
    id         BIGINT AUTO_INCREMENT,
    user_id    BIGINT                 NOT NULL,
    post_id    BIGINT                 NOT NULL,
    created_at DATETIME default NOW() NOT NULL COMMENT '생성일시',
    CONSTRAINT like_pk
        PRIMARY KEY (id),
    CONSTRAINT like_uindex
        UNIQUE (user_id, post_id)
) COMMENT '게시글 좋아요' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sixteen.`tag`
(
    id         BIGINT AUTO_INCREMENT,
    name       VARCHAR(30)            NOT NULL COMMENT '태그 이름',
    created_at DATETIME default NOW() NOT NULL COMMENT '생성일시',
    updated_at DATETIME default NOW() NOT NULL COMMENT '수정일시',
    CONSTRAINT tag_pk
        PRIMARY KEY (id)
) COMMENT '태그' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sixteen.`post_tag`
(
    id         BIGINT AUTO_INCREMENT,
    post_id    BIGINT                 NOT NULL,
    tag_id     BIGINT                 NOT NULL,
    created_at DATETIME default NOW() NOT NULL COMMENT '생성일시',
    CONSTRAINT post_tag_pk
        PRIMARY KEY (id),
    CONSTRAINT post_tag_uindex
        UNIQUE (post_id, tag_id)
) COMMENT '게시글 태그' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sixteen.`comment`
(
    id                BIGINT AUTO_INCREMENT,
    post_id           BIGINT                 NOT NULL COMMENT '게시글',
    parent_comment_id BIGINT                 NULL COMMENT '부모 댓글',
    writer_id         BIGINT                 NOT NULL COMMENT '작성자',
    body              VARCHAR(500)           NOT NULL COMMENT '내용',
    image_url         VARCHAR(2048)          NULL COMMENT '이미지 URL',
    status            VARCHAR(50)            NOT NULL COMMENT '댓글 상태',
    created_at        DATETIME default NOW() NOT NULL COMMENT '생성일시',
    updated_at        DATETIME default NOW() NOT NULL COMMENT '수정일시',
    CONSTRAINT comment_pk
        PRIMARY KEY (id)
) COMMENT '댓글' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sixteen.`feedback`
(
    id         BIGINT AUTO_INCREMENT,
    user_id    BIGINT                 NOT NULL,
    comment_id BIGINT                 NOT NULL,
    is_upvote  BOOLEAN                NOT NULL,
    created_at DATETIME default NOW() NOT NULL COMMENT '생성일시',
    CONSTRAINT feedback_pk
        PRIMARY KEY (id),
    CONSTRAINT feedback_uindex
        UNIQUE (user_id, comment_id)
) COMMENT '댓글 피드백' COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS sixteen.`oauth`
(
    id         BIGINT AUTO_INCREMENT,
    CONSTRAINT oauth_pk
        PRIMARY KEY (id)
) COMMENT 'OAuth' COLLATE = utf8mb4_unicode_ci;
