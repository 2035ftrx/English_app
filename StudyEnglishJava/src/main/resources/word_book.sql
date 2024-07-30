create table word_book
(
    id       INTEGER
        primary key auto_increment,
    book_id  TEXT,
    pic_url  TEXT,
    title    TEXT,
    word_num INTEGER,
    tags     TEXT
);

INSERT INTO word_book (id, book_id, pic_url, title, word_num, tags) VALUES (1, 'CET4_1', 'https://nos.netease.com/ydschool-online/1491037568440CET4_1.jpg', '四级真题核心词（正序版）', 1162, '有道');
INSERT INTO word_book (id, book_id, pic_url, title, word_num, tags) VALUES (2, 'CET4_2', 'https://nos.netease.com/ydschool-online/youdao_CET4_2.jpg', '四级英语词汇（正序版）', 3739, '四级、有道');
INSERT INTO word_book (id, book_id, pic_url, title, word_num, tags) VALUES (3, 'CET4_3', 'https://nos.netease.com/ydschool-online/newOriental_CET4_3.jpg', '新东方四级词汇', 2607, '四级、新东方');
INSERT INTO word_book (id, book_id, pic_url, title, word_num, tags) VALUES (4, 'CET6_1', 'https://nos.netease.com/ydschool-online/1491037677590CET6_1.jpg', '六级真题核心词（正序版）', 1228, '六级、有道');
INSERT INTO word_book (id, book_id, pic_url, title, word_num, tags) VALUES (5, 'CET6_2', 'https://nos.netease.com/ydschool-online/youdao_CET6_2.jpg', '六级英语词汇', 2078, '六级、有道');
INSERT INTO word_book (id, book_id, pic_url, title, word_num, tags) VALUES (6, 'CET6_3', 'https://nos.netease.com/ydschool-online/newOriental_CET6_3.jpg', '新东方六级词汇', 2345, '六级、新东方');
INSERT INTO word_book (id, book_id, pic_url, title, word_num, tags) VALUES (7, 'Level4_1', 'https://nos.netease.com/ydschool-online/1491037690141Level4_1.jpg', '专四真题高频词（正序版）', 595, '专四、有道');
INSERT INTO word_book (id, book_id, pic_url, title, word_num, tags) VALUES (8, 'Level4_2', 'https://nos.netease.com/ydschool-online/youdao_Level4_2.jpg', '专四核心词汇（正序版）', 4025, '专四、有道');
