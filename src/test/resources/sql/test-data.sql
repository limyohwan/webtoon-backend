INSERT INTO users(user_id, name, email, gender, age_type, register_date)
VALUES(1, '임요환1', 'dyghks7102@naver.com', 'MALE', 'ADULT', NOW())
,(2, '임요환2', 'dyghks7102@naver.com', 'MALE', 'ADULT', NOW())
,(3, '임요환3', 'dyghks7102@naver.com', 'MALE', 'ADULT', NOW())
,(4, '임요환4', 'dyghks7102@naver.com', 'MALE', 'ADULT', NOW())
,(5, '임요환5', 'dyghks7102@naver.com', 'MALE', 'ADULT', DATEADD('DAY', -10, CURRENT_TIMESTAMP()))
,(6, '임요환6', 'dyghks7102@naver.com', 'MALE', 'ADULT', DATEADD('DAY', -10, CURRENT_TIMESTAMP()))
,(7, '임요환7', 'dyghks7102@naver.com', 'MALE', 'ADULT', DATEADD('DAY', -10, CURRENT_TIMESTAMP()));

INSERT INTO contents(contents_id, name, author, charge_type, age_type, coin, open_date)
VALUES(1, '작품1', '저자1', 'FREE', 'ADULT', 0, NOW())
,(2, '작품2', '저자2', 'FREE', 'ADULT', 0, NOW())
,(3, '작품3', '저자3', 'FREE', 'ADULT', 0, NOW())
,(4, '작품4', '저자4', 'FREE', 'COMMON', 0, NOW())
,(5, '작품5', '저자5', 'FREE', 'COMMON', 0, NOW())
,(6, '작품6', '저자6', 'FREE', 'COMMON', 0, NOW());

INSERT INTO view_history(view_history_id, user_id, contents_id, created_date)
VALUES(1, 1, 1, NOW()) -- 임요환1 성인물 3개 감상
, (2, 1, 2, NOW())
, (3, 1, 3, NOW())
, (4, 1, 2, NOW())
, (5, 1, 3, NOW())
, (6, 2, 1, NOW()) -- 임요환2 성인물 2개 감상
, (7, 2, 2, NOW())
, (8, 2, 1, NOW())
, (9, 2, 2, NOW())
, (10, 2, 4, NOW())
, (11, 3, 1, NOW()) -- 임요환3 성인물 3개 감상
, (12, 3, 2, NOW())
, (13, 3, 3, NOW())
, (14, 3, 4, NOW())
, (15, 5, 1, NOW()) -- 임요환5(10일전 등록된 사용자) 성인물 3개 감상
, (16, 5, 2, NOW())
, (17, 5, 3, NOW())
, (18, 5, 4, NOW());

INSERT INTO evaluation(user_id, contents_id, evaluation_type, comment, register_date)
VALUES(1, 1, 'LIKE', '좋아요', NOW())
,(1, 2, 'LIKE', '좋아요', NOW())
,(1, 3, 'LIKE', '좋아요', NOW())
,(1, 4, 'LIKE', '좋아요', NOW())
,(1, 5, 'LIKE', '좋아요', NOW())
,(1, 6, 'LIKE', '좋아요', NOW())
,(2, 1, 'LIKE', '좋아요', NOW())
,(2, 2, 'LIKE', '좋아요', NOW())
,(2, 3, 'LIKE', '좋아요', NOW())
,(2, 4, 'LIKE', '좋아요', NOW())
,(2, 5, 'LIKE', '좋아요', NOW())
,(2, 6, 'LIKE', '좋아요', NOW())
,(3, 1, 'DISLIKE', '싫어요', NOW())
,(3, 2, 'DISLIKE', '싫어요', NOW())
,(3, 3, 'DISLIKE', '싫어요', NOW())
,(3, 4, 'LIKE', '좋아요', NOW())
,(3, 5, 'LIKE', '좋아요', NOW())
,(3, 6, 'LIKE', '좋아요', NOW())
,(4, 1, 'DISLIKE', '싫어요', NOW())
,(4, 2, 'DISLIKE', '싫어요', NOW())
,(4, 3, 'DISLIKE', '싫어요', NOW())
,(4, 4, 'DISLIKE', '싫어요', NOW())
,(4, 5, 'DISLIKE', '싫어요', NOW())
,(4, 6, 'DISLIKE', '싫어요', NOW())
,(5, 1, 'DISLIKE', '싫어요', NOW())
,(5, 2, 'DISLIKE', '싫어요', NOW())
,(5, 5, 'LIKE', '좋아요', NOW())
,(5, 6, 'LIKE', '좋아요', NOW())
,(6, 1, 'DISLIKE', '싫어요', NOW())
,(6, 6, 'LIKE', '좋아요', NOW());

-- 작품 1 = 좋아요 2 싫어요 4, 작품 2 = 좋아요 2 싫어요 3, 작품 3 = 좋아요 2 싫어요 2, 작품 4 = 좋아요 3 싫어요 1, 작품 5 = 좋아요 4 싫어요 1, 작품 6 = 좋아요 5 싫어요 1
-- 좋아요 Top 3 = 작품 6, 작품 5, 작품 4
-- 싫어요 Top 3 = 작품 1, 작품 2, 작품 3