INSERT INTO `post`(id, guid, title, thumbnail_url, description, pub_date, rss_source_id)
VALUES
    (1, 'guid01', 'title01', 'url01', 'description01', '2024-01-01 00:00:00.000000', 1),
    (2, 'guid02', 'title02', 'url02', 'description02', '2024-01-01 00:00:00.000000', 1),
    (3, 'guid03', 'title03', 'url03', 'description03', '2024-01-01 00:00:00.000000', 2),
    (4, 'guid04', 'title04', 'url04', 'description04', '2024-01-01 00:00:00.000000', 2),
    (5, 'guid05', 'title05', 'url05', 'description05', '2024-01-01 00:00:00.000000', 3),
    (6, 'guid06', 'title06', 'url06', 'description06', '2024-01-01 00:00:00.000000', 3),
    (7, 'guid07', 'title07', 'url07', 'description07', '2024-01-01 00:00:00.000000', 4),
    (8, 'guid08', 'title08', 'url08', 'description08', '2024-01-01 00:00:00.000000', 4);

INSERT INTO `rss_source`(id, title, url, platform, last_collected_at)
VALUES
    (1, 'title01', 'url01', 'VELOG', '2024-01-01 00:00:00.000000'),
    (2, 'title02', 'url02', 'TISTORY', '2024-01-01 00:00:00.000000'),
    (3, 'title03', 'url03', 'MEDIUM', '2024-01-01 00:00:00.000000'),
    (4, 'title04', 'url04', 'ETC', '2024-01-01 00:00:00.000000');