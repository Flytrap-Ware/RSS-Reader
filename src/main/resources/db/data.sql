-- INSERT DATA
INSERT INTO `member`(name, email, profile, oauth_pk, oauth_server, created_at)
VALUES ('admin', 'admin@gmail.com', 'https://avatars.githubusercontent.com/u/86359180?v=4', '11111', 'GITHUB', '2023-11-16'),
       ('test01', 'test01@gmail.com', 'https://avatars.githubusercontent.com/u/86359180?v=4', '22222', 'GITHUB', '2023-11-16');

INSERT INTO `folder`(name, member_id, is_shared, is_deleted)
VALUES ('나의 폴더 1', 1, false, false),
       ('나의 폴더 2', 1, false, false),
       ('나의 폴더 3', 1, false, false),
       ('공유 폴더 1', 2, true,  false),
       ('공유 폴더 2', 1, true,  false);

INSERT INTO `folder_member`(member_id, folder_id)
VALUES (1, 4);

INSERT INTO `folder_subscribe`(folder_id, subscribe_id, description)
VALUES (1, 1, ''), (1, 2, ''),
       (1, 5, ''), (1, 6, ''),
       (2, 1, ''), (2, 3, ''),
       (3, 4, ''), (4, 2, ''),
       (4, 5, ''), (4, 6, '');

INSERT INTO `rss_subscribe`(title, url, platform)
VALUES ('조금씩, 꾸준히, 자주', 'https://v2.velog.io/rss/jinny-l', 'VELOG'),
       ('ape.log', 'https://v2.velog.io/rss/ape', 'VELOG'),
       ('janeljs.log', 'https://v2.velog.io/rss/janeljs', 'VELOG'),
       ('louie.log', 'https://v2.velog.io/rss/louie', 'VELOG'),
       ('✨ iirin''s space', 'https://new-pow.tistory.com/rss', 'TISTORY'),
       ('gamja.log', 'https://v2.velog.io/rss/leekhy02', 'VELOG');

INSERT INTO `rss_post`(guid, subscribe_id, title, description, pub_date)
VALUES ('https://velog.io/@ape/test-l4lqt827', 2, 'test', '<p>test</p>\n', '2023-11-08 10:40:18.000000'),
       ('https://velog.io/@ape/test-9myxk9zq', 2, 'test', '<p>tttt</p>\n', '2023-11-08 10:38:36.000000'),
       ('https://velog.io/@ape/test', 2, 'test test test test test test test', '<p>test</p>\n', '2023-11-08 10:36:46.000000');

INSERT INTO `bookmark`(member_id, post_id)
VALUES (1, 1), (1, 2), (1, 3), (2, 1);