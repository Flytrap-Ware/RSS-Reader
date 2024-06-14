INSERT INTO `post`(id, guid, title, thumbnail_url, description, pub_date, rss_source_id)
VALUES
    ('20240101000000-1', 'guid01', 'title01', 'url01', 'description01', '2024-01-01 00:00:00.000000', 1),
    ('20240101010000-1', 'guid02', 'title02', 'url02', 'description02', '2024-01-01 01:00:00.000000', 1),
    ('20240101020000-2', 'guid03', 'title03', 'url03', 'description03', '2024-01-01 02:00:00.000000', 2),
    ('20240101030000-2', 'guid04', 'title04', 'url04', 'description04', '2024-01-01 03:00:00.000000', 2),
    ('20240101040000-3', 'guid05', 'title05', 'url05', 'description05', '2024-01-01 04:00:00.000000', 3),
    ('20240101050000-3', 'guid06', 'title06', 'url06', 'description06', '2024-01-01 05:00:00.000000', 3),
    ('20240101060000-4', 'guid07', 'title07', 'url07', 'description07', '2024-01-01 06:00:00.000000', 4),
    ('20240101070000-4', 'guid08', 'title08', 'url08', 'description08', '2024-01-01 07:00:00.000000', 4);

INSERT INTO `rss_source`(id, title, url, platform, last_collected_at)
VALUES
    (1, 'title01', 'url01', 'VELOG', '2024-01-01 00:00:00.000000'),
    (2, 'title02', 'url02', 'TISTORY', '2024-01-01 00:00:00.000000'),
    (3, 'title03', 'url03', 'MEDIUM', '2024-01-01 00:00:00.000000'),
    (4, 'title04', 'url04', 'ETC', '2024-01-01 00:00:00.000000');

INSERT INTO `folder`(id, account_id, name, is_shared, is_deleted)
VALUES
    (1, 1, 'folder 1', false, false),
    (2, 1, 'folder 2', false, false);

INSERT INTO `subscription`(id, folder_id, rss_source_id)
VALUES
    (1, 1, 1),
    (2, 1, 2),
    (3, 2, 3),
    (4, 2, 4);