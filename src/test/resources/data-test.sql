-- Seed data for tests
INSERT INTO users (id, fullname, email, enabled, auth_provider, user_role, created_at)
VALUES (8, 'Davis Test', 'davis@example.com', true, 'LOCAL', 'ROLE_STUDENT', CURRENT_TIMESTAMP);

INSERT INTO courses (id, author, category, price, rating, subtitle, thumb_url, title, is_featured)
VALUES (10011, 'Dani Krossing', 'Development', 18.99, 4.50, 'Master fundamentals with JavaScript', 'https://i3.ytimg.com/vi/jS4aFq5-91M/maxresdefault.jpg', 'Learn JavaScript Programming', true),
       (10018, 'Bill Hilton', 'Music', 15.99, 4.50, 'Learn Piano in WEEKS not years', 'https://i3.ytimg.com/vi/WJ3-F02-F_Y/maxresdefault.jpg', 'How To Play Piano for Beginners', true);

INSERT INTO enrollments (id, user_id, course_id, is_completed, next_position, progress, created_at, updated_at)
VALUES (1, 8, 10018, false, 1, 0.0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO wishlist (id, user_id, course_id, created_at)
VALUES (1, 8, 10011, CURRENT_TIMESTAMP);
