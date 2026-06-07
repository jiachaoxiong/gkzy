-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    nickname VARCHAR(50) COMMENT '昵称',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    province VARCHAR(20) DEFAULT '山西' COMMENT '省份',
    subject_type VARCHAR(10) COMMENT '科类: 文史/理工',
    score INT COMMENT '高考分数',
    `rank` INT COMMENT '全省位次',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 院校表
CREATE TABLE IF NOT EXISTS college (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '院校名称',
    code VARCHAR(20) UNIQUE COMMENT '院校代码',
    type VARCHAR(50) COMMENT '类型: 985/211/双一流/普通',
    province VARCHAR(20) COMMENT '所在省份',
    city VARCHAR(50) COMMENT '所在城市',
    level VARCHAR(20) COMMENT '办学层次',
    website VARCHAR(200) COMMENT '官网',
    logo VARCHAR(200) COMMENT '校徽URL',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='院校表';

-- 专业表
CREATE TABLE IF NOT EXISTS college_major (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    college_id BIGINT NOT NULL COMMENT '所属院校',
    name VARCHAR(100) NOT NULL COMMENT '专业名称',
    code VARCHAR(20) COMMENT '专业代码',
    category VARCHAR(50) COMMENT '学科门类',
    level VARCHAR(20) COMMENT '专业层次',
    features VARCHAR(500) COMMENT '专业特色',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_college_id (college_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='专业表';

-- 录取分数线表
CREATE TABLE IF NOT EXISTS admission_score (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    college_id BIGINT NOT NULL COMMENT '院校ID',
    major_id BIGINT COMMENT '专业ID(NULL=院校投档线)',
    year INT NOT NULL COMMENT '年份',
    batch VARCHAR(20) NOT NULL COMMENT '批次',
    subject_type VARCHAR(10) NOT NULL COMMENT '科类',
    min_score INT COMMENT '最低分',
    min_rank INT COMMENT '最低位次',
    avg_score INT COMMENT '平均分',
    max_score INT COMMENT '最高分',
    enroll_count INT COMMENT '录取人数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_college_year (college_id, year),
    INDEX idx_major_year (major_id, year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='录取分数线表';

-- 志愿方案表
CREATE TABLE IF NOT EXISTS volunteer_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    name VARCHAR(100) NOT NULL COMMENT '方案名称',
    batch VARCHAR(20) NOT NULL COMMENT '批次',
    subject_type VARCHAR(10) NOT NULL COMMENT '科类',
    total_score INT COMMENT '高考总分',
    year INT NOT NULL COMMENT '年份',
    status VARCHAR(20) DEFAULT '草稿' COMMENT '状态: 草稿/已提交',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿方案表';

-- 志愿明细表
CREATE TABLE IF NOT EXISTS volunteer_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plan_id BIGINT NOT NULL COMMENT '所属方案',
    sort_order INT NOT NULL COMMENT '志愿序号(1-8)',
    college_id BIGINT NOT NULL COMMENT '院校ID',
    major1 BIGINT COMMENT '专业1',
    major2 BIGINT COMMENT '专业2',
    major3 BIGINT COMMENT '专业3',
    major4 BIGINT COMMENT '专业4',
    major5 BIGINT COMMENT '专业5',
    major6 BIGINT COMMENT '专业6',
    strategy VARCHAR(10) COMMENT '策略: 冲/稳/保',
    admit_probability DECIMAL(5,2) COMMENT '录取概率',
    is_obey_adjust TINYINT DEFAULT 1 COMMENT '是否服从调剂',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_plan_id (plan_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿明细表';
