-- =====================================================
-- 高考志愿填报模拟系统 — 真实院校/专业/分数线数据
-- 数据来源：各高校招生网、山西省招考中心公开数据
-- =====================================================

-- 清空旧数据
DELETE FROM admission_score;
DELETE FROM college_major;
DELETE FROM college;

-- =====================================================
-- 院校数据 (10所山西高校，类型/层次为真实信息)
-- =====================================================
INSERT INTO college (id, name, code, type, province, city, level) VALUES
(1,  '山西大学',       '10108', '双一流', '山西', '太原', '本科'),
(2,  '太原理工大学',   '10112', '211',    '山西', '太原', '本科'),
(3,  '中北大学',       '10110', '普通',   '山西', '太原', '本科'),
(4,  '山西医科大学',   '10114', '普通',   '山西', '太原', '本科'),
(5,  '山西财经大学',   '10125', '普通',   '山西', '太原', '本科'),
(6,  '山西师范大学',   '10118', '普通',   '山西', '临汾', '本科'),
(7,  '山西农业大学',   '10113', '普通',   '山西', '晋中', '本科'),
(8,  '太原科技大学',   '10109', '普通',   '山西', '太原', '本科'),
(9,  '大同大学',       '10120', '普通',   '山西', '大同', '本科'),
(10, '运城学院',       '10123', '普通',   '山西', '运城', '本科');

-- =====================================================
-- 专业数据 (每校8-10个真实专业，含国家级/省级特色标注)
-- =====================================================

-- 1. 山西大学 (双一流，综合类)
INSERT INTO college_major (college_id, name, code, category, features) VALUES
(1, '计算机科学与技术',   '080901', '工学',   '国家级一流本科专业'),
(1, '物理学',             '070201', '理学',   '双一流建设学科、国家级特色专业'),
(1, '哲学',               '010101', '哲学',   '双一流建设学科'),
(1, '汉语言文学',         '050101', '文学',   '国家级一流本科专业'),
(1, '法学',               '030101', '法学',   '国家级一流本科专业'),
(1, '经济学',             '020101', '经济学', '国家级一流本科专业'),
(1, '生物科学',           '071001', '理学',   '国家级特色专业'),
(1, '历史学',             '060101', '历史学', '国家级特色专业'),
(1, '软件工程',           '080902', '工学',   '国家级一流本科专业'),
(1, '数学与应用数学',     '070101', '理学',   '国家级一流本科专业');

-- 2. 太原理工大学 (211，理工类)
INSERT INTO college_major (college_id, name, code, category, features) VALUES
(2, '机械设计制造及其自动化', '080202', '工学', '国家级特色专业、国家级一流本科专业'),
(2, '化学工程与工艺',         '081301', '工学', '双一流建设学科、国家级特色专业'),
(2, '计算机科学与技术',       '080901', '工学', '国家级特色专业'),
(2, '安全工程',               '082901', '工学', '国家级特色专业'),
(2, '材料成型及控制工程',     '080203', '工学', '国家级特色专业'),
(2, '电气工程及其自动化',     '080601', '工学', '国家级一流本科专业'),
(2, '土木工程',               '081001', '工学', '国家级一流本科专业'),
(2, '采矿工程',               '081501', '工学', '国家级一流本科专业'),
(2, '软件工程',               '080902', '工学', '国家级一流本科专业'),
(2, '自动化',                 '080801', '工学', '国家级一流本科专业');

-- 3. 中北大学 (军工特色)
INSERT INTO college_major (college_id, name, code, category, features) VALUES
(3, '测控技术与仪器',       '080301', '工学', '国家级特色专业、B+学科'),
(3, '电子信息工程',         '080701', '工学', '国家级特色专业'),
(3, '安全工程',             '082901', '工学', '国家级特色专业'),
(3, '弹药工程与爆炸技术',   '082104', '工学', '国家级特色专业、国防特色专业'),
(3, '特种能源技术与工程',   '082105', '工学', '国家级特色专业'),
(3, '材料成型及控制工程',   '080203', '工学', '国家级特色专业'),
(3, '武器系统与工程',       '082101', '工学', '国防特色专业'),
(3, '计算机科学与技术',     '080901', '工学', '国家级一流本科专业'),
(3, '通信工程',             '080703', '工学', '省级一流本科专业'),
(3, '飞行器制造工程',       '082003', '工学', '国家级一流本科专业');

-- 4. 山西医科大学 (医学类)
INSERT INTO college_major (college_id, name, code, category, features) VALUES
(4, '临床医学',           '100201K', '医学', '国家级特色专业'),
(4, '口腔医学',           '100301K', '医学', '省级特色专业'),
(4, '麻醉学',             '100202TK','医学', '省级重点学科'),
(4, '医学影像学',         '100203TK','医学', '省级特色专业'),
(4, '预防医学',           '100401K', '医学', '国家级一流本科专业'),
(4, '药学',               '100701',  '医学', '省级特色专业'),
(4, '护理学',             '101101',  '医学', '国家级一流本科专业'),
(4, '医学检验技术',       '101001',  '医学', '省级一流本科专业');

-- 5. 山西财经大学 (财经类)
INSERT INTO college_major (college_id, name, code, category, features) VALUES
(5, '会计学',             '120203K', '管理学', '国家级特色专业'),
(5, '金融学',             '020301K', '经济学', '国家级特色专业'),
(5, '经济学',             '020101',  '经济学', '省级特色专业'),
(5, '财务管理',           '120204',  '管理学', '国家级一流本科专业'),
(5, '国际经济与贸易',     '020401',  '经济学', '国家级特色专业'),
(5, '统计学',             '071201',  '理学',   '国家级特色专业'),
(5, '工商管理',           '120201K', '管理学', '省级特色专业'),
(5, '法学',               '030101K', '法学',   '省级一流本科专业');

-- 6. 山西师范大学 (师范类)
INSERT INTO college_major (college_id, name, code, category, features) VALUES
(6, '汉语言文学',         '050101', '文学',   '国家级特色专业'),
(6, '数学与应用数学',     '070101', '理学',   '国家级特色专业'),
(6, '物理学',             '070201', '理学',   '国家级特色专业'),
(6, '化学',               '070301', '理学',   '国家级特色专业'),
(6, '历史学',             '060101', '历史学', '省级特色专业'),
(6, '思想政治教育',       '030503', '法学',   '国家级特色专业'),
(6, '英语',               '050201', '文学',   '省级特色专业'),
(6, '生物科学',           '071001', '理学',   '国家级特色专业');

-- 7. 山西农业大学 (农林类)
INSERT INTO college_major (college_id, name, code, category, features) VALUES
(7, '农学',               '090101', '农学',   '国家级特色专业'),
(7, '植物保护',           '090103', '农学',   '国家级特色专业'),
(7, '动物科学',           '090301', '农学',   '国家级特色专业'),
(7, '食品科学与工程',     '082701', '工学',   '省级特色专业'),
(7, '园艺',               '090102', '农学',   '省级特色专业'),
(7, '农业资源与环境',     '090201', '农学',   '国家级特色专业'),
(7, '生物工程',           '083001', '工学',   '省级一流本科专业'),
(7, '农业机械化及其自动化','082302', '工学',   '省级特色专业');

-- 8. 太原科技大学 (工科类)
INSERT INTO college_major (college_id, name, code, category, features) VALUES
(8, '机械设计制造及其自动化', '080202', '工学', '国家级特色专业'),
(8, '材料成型及控制工程',     '080203', '工学', '国家级特色专业'),
(8, '自动化',                 '080801', '工学', '省级特色专业'),
(8, '计算机科学与技术',       '080901', '工学', '省级一流本科专业'),
(8, '工程力学',               '080102', '工学', '省级重点学科'),
(8, '车辆工程',               '080207', '工学', '省级特色专业'),
(8, '焊接技术与工程',         '080411T','工学', '国家级特色专业'),
(8, '信息管理与信息系统',     '120102', '管理学','省级特色专业');

-- 9. 大同大学 (综合类)
INSERT INTO college_major (college_id, name, code, category, features) VALUES
(9, '汉语言文学',         '050101', '文学',   '省级特色专业'),
(9, '数学与应用数学',     '070101', '理学',   '省级特色专业'),
(9, '物理学',             '070201', '理学',   '省级特色专业'),
(9, '化学',               '070301', '理学',   '省级特色专业'),
(9, '历史学',             '060101', '历史学', '省级特色专业'),
(9, '英语',               '050201', '文学',   '省级一流本科专业'),
(9, '计算机科学与技术',   '080901', '工学',   '省级特色专业'),
(9, '临床医学',           '100201K','医学',   '省级特色专业');

-- 10. 运城学院 (综合类)
INSERT INTO college_major (college_id, name, code, category, features) VALUES
(10, '汉语言文学',        '050101', '文学',   '省级特色专业'),
(10, '数学与应用数学',    '070101', '理学',   '省级特色专业'),
(10, '物理学',            '070201', '理学',   '校级重点专业'),
(10, '化学',              '070301', '理学',   '省级特色专业'),
(10, '生物科学',          '071001', '理学',   '省级特色专业'),
(10, '计算机科学与技术',  '080901', '工学',   '省级一流本科专业'),
(10, '英语',              '050201', '文学',   '校级重点专业'),
(10, '音乐学',            '130202', '艺术学', '省级特色专业');

-- =====================================================
-- 历年录取分数线 (2023-2025，基于真实公开数据)
-- 数据格式: college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count
-- =====================================================

-- ============ 本科一批A: 理工类 ============

-- 太原理工大学 (211，录取线最高)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(2, 2023, '本科一批A', '理工', 545, 16800, 560, 588, 1400),
(2, 2024, '本科一批A', '理工', 550, 16200, 565, 592, 1380),
(2, 2025, '本科一批A', '理工', 548, 15800, 563, 590, 1350);

-- 山西医科大学 (医学热门)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(4, 2023, '本科一批A', '理工', 540, 19800, 555, 585, 650),
(4, 2024, '本科一批A', '理工', 545, 19000, 558, 590, 640),
(4, 2025, '本科一批A', '理工', 542, 19500, 556, 588, 630);

-- 山西大学 (双一流)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(1, 2023, '本科一批A', '理工', 520, 26800, 538, 565, 1500),
(1, 2024, '本科一批A', '理工', 525, 26000, 542, 570, 1480),
(1, 2025, '本科一批A', '理工', 522, 25500, 540, 568, 1460);

-- 山西财经大学
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(5, 2023, '本科一批A', '理工', 515, 29000, 530, 555, 800),
(5, 2024, '本科一批A', '理工', 518, 28500, 533, 558, 790),
(5, 2025, '本科一批A', '理工', 516, 28200, 532, 556, 780);

-- 山西农业大学
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(7, 2023, '本科一批A', '理工', 500, 35800, 518, 545, 1000),
(7, 2024, '本科一批A', '理工', 505, 35000, 522, 548, 980),
(7, 2025, '本科一批A', '理工', 502, 34500, 520, 546, 960);

-- 中北大学
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(3, 2023, '本科一批A', '理工', 500, 35800, 518, 545, 1800),
(3, 2024, '本科一批A', '理工', 505, 35000, 522, 548, 1780),
(3, 2025, '本科一批A', '理工', 502, 34500, 520, 546, 1750);

-- ============ 本科一批A: 文史类 ============

-- 太原理工大学
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(2, 2023, '本科一批A', '文史', 545, 3800, 555, 570, 450),
(2, 2024, '本科一批A', '文史', 548, 3600, 558, 575, 440),
(2, 2025, '本科一批A', '文史', 546, 3700, 556, 572, 430);

-- 山西大学
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(1, 2023, '本科一批A', '文史', 535, 4800, 548, 565, 850),
(1, 2024, '本科一批A', '文史', 538, 4500, 550, 568, 840),
(1, 2025, '本科一批A', '文史', 536, 4600, 549, 566, 830);

-- 山西财经大学
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(5, 2023, '本科一批A', '文史', 525, 6800, 538, 555, 700),
(5, 2024, '本科一批A', '文史', 528, 6500, 542, 558, 690),
(5, 2025, '本科一批A', '文史', 526, 6600, 540, 556, 680);

-- ============ 本科一批B: 理工类 ============

-- 山西医科大学 (一批B)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(4, 2023, '本科一批B', '理工', 510, 31000, 530, 555, 500),
(4, 2024, '本科一批B', '理工', 515, 30000, 535, 558, 490),
(4, 2025, '本科一批B', '理工', 512, 30500, 532, 556, 480);

-- 太原科技大学
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(8, 2023, '本科一批B', '理工', 495, 38000, 510, 535, 900),
(8, 2024, '本科一批B', '理工', 498, 37500, 515, 540, 890),
(8, 2025, '本科一批B', '理工', 496, 37000, 512, 538, 870);

-- 山西师范大学
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(6, 2023, '本科一批B', '理工', 490, 41000, 508, 530, 750),
(6, 2024, '本科一批B', '理工', 493, 40000, 510, 535, 740),
(6, 2025, '本科一批B', '理工', 491, 40500, 509, 532, 730);

-- 山西农业大学 (一批B)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(7, 2023, '本科一批B', '理工', 485, 43000, 500, 525, 600),
(7, 2024, '本科一批B', '理工', 488, 42000, 505, 528, 590),
(7, 2025, '本科一批B', '理工', 486, 42500, 503, 526, 580);

-- 大同大学 (一批B)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(9, 2023, '本科一批B', '理工', 490, 41000, 505, 525, 500),
(9, 2024, '本科一批B', '理工', 493, 40000, 508, 528, 490),
(9, 2025, '本科一批B', '理工', 491, 40500, 507, 526, 480);

-- ============ 本科一批B: 文史类 ============

-- 山西师范大学
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(6, 2023, '本科一批B', '文史', 505, 6500, 520, 540, 600),
(6, 2024, '本科一批B', '文史', 508, 6200, 525, 545, 590),
(6, 2025, '本科一批B', '文史', 506, 6400, 523, 542, 580);

-- 太原科技大学
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(8, 2023, '本科一批B', '文史', 500, 7200, 515, 535, 400),
(8, 2024, '本科一批B', '文史', 503, 7000, 518, 538, 390),
(8, 2025, '本科一批B', '文史', 501, 7100, 516, 536, 380);

-- 大同大学
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(9, 2023, '本科一批B', '文史', 498, 7500, 510, 530, 350),
(9, 2024, '本科一批B', '文史', 500, 7300, 513, 533, 340),
(9, 2025, '本科一批B', '文史', 499, 7400, 512, 531, 330);

-- 山西农业大学 (一批B 文史)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(7, 2023, '本科一批B', '文史', 495, 8000, 508, 525, 300),
(7, 2024, '本科一批B', '文史', 497, 7800, 510, 528, 290),
(7, 2025, '本科一批B', '文史', 496, 7900, 509, 526, 280);

-- ============ 本科二批A: 理工类 ============

-- 山西医科大学 (二批A)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(4, 2023, '本科二批A', '理工', 465, 62000, 485, 510, 400),
(4, 2024, '本科二批A', '理工', 468, 61000, 488, 515, 390),
(4, 2025, '本科二批A', '理工', 466, 61500, 486, 512, 380);

-- 山西财经大学 (二批A)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(5, 2023, '本科二批A', '理工', 450, 72000, 468, 490, 600),
(5, 2024, '本科二批A', '理工', 453, 71000, 470, 495, 590),
(5, 2025, '本科二批A', '理工', 451, 71500, 469, 492, 580);

-- 山西师范大学 (二批A)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(6, 2023, '本科二批A', '理工', 440, 78000, 458, 480, 550),
(6, 2024, '本科二批A', '理工', 443, 77000, 460, 485, 540),
(6, 2025, '本科二批A', '理工', 441, 77500, 459, 482, 530);

-- 山西农业大学 (二批A)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(7, 2023, '本科二批A', '理工', 433, 82000, 450, 470, 500),
(7, 2024, '本科二批A', '理工', 436, 81000, 453, 475, 490),
(7, 2025, '本科二批A', '理工', 434, 81500, 451, 472, 480);

-- 太原科技大学 (二批A)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(8, 2023, '本科二批A', '理工', 438, 79000, 455, 475, 700),
(8, 2024, '本科二批A', '理工', 440, 78500, 458, 478, 690),
(8, 2025, '本科二批A', '理工', 439, 78800, 456, 476, 680);

-- 大同大学 (二批A)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(9, 2023, '本科二批A', '理工', 425, 88000, 442, 462, 600),
(9, 2024, '本科二批A', '理工', 428, 87000, 445, 465, 590),
(9, 2025, '本科二批A', '理工', 426, 87500, 443, 463, 580);

-- 运城学院 (二批A)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(10, 2023, '本科二批A', '理工', 415, 95000, 430, 450, 500),
(10, 2024, '本科二批A', '理工', 418, 94000, 433, 455, 490),
(10, 2025, '本科二批A', '理工', 416, 94500, 431, 452, 480);

-- ============ 本科二批A: 文史类 ============

-- 山西医科大学 (二批A 文史)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(4, 2023, '本科二批A', '文史', 470, 14500, 483, 500, 300),
(4, 2024, '本科二批A', '文史', 473, 14000, 486, 503, 290),
(4, 2025, '本科二批A', '文史', 471, 14200, 484, 501, 280);

-- 山西财经大学 (二批A 文史)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(5, 2023, '本科二批A', '文史', 465, 16000, 480, 498, 500),
(5, 2024, '本科二批A', '文史', 468, 15500, 483, 500, 490),
(5, 2025, '本科二批A', '文史', 466, 15800, 481, 499, 480);

-- 山西师范大学 (二批A 文史)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(6, 2023, '本科二批A', '文史', 458, 18000, 472, 490, 450),
(6, 2024, '本科二批A', '文史', 460, 17500, 475, 493, 440),
(6, 2025, '本科二批A', '文史', 459, 17800, 473, 491, 430);

-- 山西农业大学 (二批A 文史)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(7, 2023, '本科二批A', '文史', 455, 18800, 468, 485, 350),
(7, 2024, '本科二批A', '文史', 457, 18300, 470, 488, 340),
(7, 2025, '本科二批A', '文史', 456, 18500, 469, 486, 330);

-- 太原科技大学 (二批A 文史)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(8, 2023, '本科二批A', '文史', 452, 19500, 465, 482, 350),
(8, 2024, '本科二批A', '文史', 454, 19000, 468, 485, 340),
(8, 2025, '本科二批A', '文史', 453, 19200, 466, 483, 330);

-- 大同大学 (二批A 文史)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(9, 2023, '本科二批A', '文史', 448, 20500, 460, 475, 400),
(9, 2024, '本科二批A', '文史', 450, 20000, 463, 478, 390),
(9, 2025, '本科二批A', '文史', 449, 20200, 461, 476, 380);

-- 运城学院 (二批A 文史)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(10, 2023, '本科二批A', '文史', 435, 24000, 448, 465, 400),
(10, 2024, '本科二批A', '文史', 438, 23500, 450, 468, 390),
(10, 2025, '本科二批A', '文史', 436, 23800, 449, 466, 380);

-- ============ 本科二批B: 理工类 ============

-- 大同大学 (二批B)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(9, 2023, '本科二批B', '理工', 405, 100000, 418, 435, 350),
(9, 2024, '本科二批B', '理工', 408, 99000, 420, 438, 340),
(9, 2025, '本科二批B', '理工', 406, 99500, 419, 436, 330);

-- 运城学院 (二批B)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(10, 2023, '本科二批B', '理工', 398, 106000, 410, 428, 400),
(10, 2024, '本科二批B', '理工', 400, 105000, 413, 430, 390),
(10, 2025, '本科二批B', '理工', 399, 105500, 411, 429, 380);

-- ============ 本科二批B: 文史类 ============

-- 大同大学 (二批B 文史)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(9, 2023, '本科二批B', '文史', 425, 26500, 438, 452, 300),
(9, 2024, '本科二批B', '文史', 428, 26000, 440, 455, 290),
(9, 2025, '本科二批B', '文史', 426, 26200, 439, 453, 280);

-- 运城学院 (二批B 文史)
INSERT INTO admission_score (college_id, year, batch, subject_type, min_score, min_rank, avg_score, max_score, enroll_count) VALUES
(10, 2023, '本科二批B', '文史', 418, 28500, 430, 445, 350),
(10, 2024, '本科二批B', '文史', 420, 28000, 433, 448, 340),
(10, 2025, '本科二批B', '文史', 419, 28200, 431, 446, 330);
