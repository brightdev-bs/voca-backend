INSERT INTO user(id, created_at, email, password, role, username, verified)
VALUES
    (1, '2023-09-20 15:40:35', 'vanille@gmail.com', '{bcrypt}1kdasdfwcv', 'USER', 'verifiedUser', true),
    (2, '2023-09-20 15:40:35','vanille2@gmail.com','{bcrypt}1kdasdfwcv', 'USER', 'unverifiedUser', false);

INSERT INTO word(id, checked, created_at, created_by, definition, note, word, user_id, vocabulary_id, deleted)
VALUES
    (1, false, '2023-09-07', 1, '망고', 'mango is delicious', 'mango', 1, null, false),
    (2, false, '2023-09-07', 1, '수박', 'watermelon is delicious', 'watermelon', 1, null, false),
    (3, false, '2023-09-07', 1, '사과', 'apple is delicious', 'apple', 1, null, false),
    (4, false, '2023-09-07', 1, '멜론', 'melon is delicious', 'melon', 1, null, true),
    (5, false, '2023-09-07', 1, '양파', 'onion is delicious', 'onion', 1, null, true);


INSERT INTO vocabulary(id, created_at, created_By, description, is_public, liked, name)
VALUES
    (1, '2023-09-07', 1, 'Test Vocabulary', 1, 30, 'First Favorite'),
    (2, '2023-09-07', 1, 'Test Vocabulary', 1, 28, 'Second Favorite'),
    (3, '2023-09-07', 1, 'Test Vocabulary', 1, 26, 'Third Favorite'),
    (4, '2023-09-07', 1, 'Test Vocabulary', 1, 25, 'Fourth Favorite'),
    (5, '2023-09-07', 1, 'Test Vocabulary', 1, 29, 'Mix Favorite'),
    (6, '2023-09-07', 1, 'Test Vocabulary', 1, 10, 'Dummy Vocabulary');