-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 07, 2021 at 10:24 AM
-- Server version: 8.0.17
-- PHP Version: 7.3.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `wedemy`
--

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE `courses`
(
    `id`        int(11)                                                       NOT NULL,
    `author`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `category`  varchar(50)                                                   NOT NULL,
    `price`     double                                                        NOT NULL,
    `rating`    double                                                        NOT NULL DEFAULT '3.5',
    `thumb_url` varchar(255)                                                           DEFAULT NULL,
    `title`     varchar(255)                                                  NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='Courses are parents of Lessons';

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`id`, `author`, `category`, `price`, `rating`, `thumb_url`, `title`)
VALUES (10010, 'Corey Schafer', 'Development', 17.99, 4.5, 'https://i3.ytimg.com/vi/XKHEtdqhLK8/maxresdefault.jpg',
        'Complete Python Bootcamp'),
       (10011, 'Dani Krossing', 'Development', 18.99, 4.5, 'https://i3.ytimg.com/vi/jS4aFq5-91M/maxresdefault.jpg',
        'Learn JavaScript Programming'),
       (10012, 'Caleb Curry', 'Development', 19.99, 3.5, 'https://i3.ytimg.com/vi/GoXwIVyNvX0/maxresdefault.jpg',
        'Java Programming Essentials'),
       (10013, 'Gareth David Studio', 'PhotoVideo', 21.99, 4.5, 'https://i3.ytimg.com/vi/9EGI-FSr0Ig/maxresdefault.jpg',
        'Beginners Guide To Adobe Illustrator'),
       (10014, 'chinfat', 'PhotoVideo', 22.5, 3.5, 'https://i3.ytimg.com/vi/u99i1SmDgIc/maxresdefault.jpg',
        'Learn Adobe Premiere Pro CC'),
       (10015, 'Jacob Clifford', 'Finance', 15.99, 4.5, 'https://i3.ytimg.com/vi/g9aDizJpd_s/maxresdefault.jpg',
        'Crash Course Economics'),
       (10016, 'MrandMrsMuscle', 'Health', 18.99, 3.5, 'https://i3.ytimg.com/vi/By6GXzcldGY/maxresdefault.jpg',
        'Lose Belly Fat in 14 Days'),
       (10017, 'Robert Kiyosaki', 'Real Estate', 21.99, 4.5, 'https://i3.ytimg.com/vi/UJv9-F7SN5A/maxresdefault.jpg',
        'Real Estate Investing'),
       (10018, 'Bill Hilton', 'Music', 15.99, 4.5, 'https://i3.ytimg.com/vi/WJ3-F02-F_Y/maxresdefault.jpg',
        'How To Play Piano for Beginners'),
       (10019, 'TfTS', 'Office', 17.99, 3.5, 'https://i3.ytimg.com/vi/Vl0H-qTclOg/maxresdefault.jpg',
        'Master Microsoft Excel');

-- --------------------------------------------------------

--
-- Table structure for table `enrollments`
--

CREATE TABLE `enrollments`
(
    `id`                int(11)    NOT NULL,
    `created_at`        datetime(6) DEFAULT NULL,
    `is_completed`      bit(1)     NOT NULL,
    `updated_at`        datetime(6) DEFAULT NULL,
    `current_lesson_id` binary(16) NOT NULL,
    `user_id`           int(11)    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='records and updates student progress';

-- --------------------------------------------------------

--
-- Table structure for table `lessons`
--

CREATE TABLE `lessons`
(
    `id`          binary(16)   NOT NULL,
    `lesson_name` varchar(255) NOT NULL,
    `videokey`    varchar(20)  NOT NULL,
    `course_id`   int(11)      NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='Lessons are children of Courses';

--
-- Dumping data for table `lessons`
--

INSERT INTO `lessons` (`id`, `lesson_name`, `videokey`, `course_id`)
VALUES (0x01fb76c505ba11ecac7d9457a5ebcddd, '1. Introduction to Java', '2dZiMBwX_5Q', 10012),
       (0x01fb8f1b05ba11ecac7d9457a5ebcddd, '2. Installation and Hello World', 'Hdf5OmERt0g', 10012),
       (0x01fb90ce05ba11ecac7d9457a5ebcddd, '3. Understanding Java Foundation', 'ovb8njlzvlA', 10012),
       (0x01fb91c305ba11ecac7d9457a5ebcddd, '4. Arguments and Parameters', 'U5b9TH14REM', 10012),
       (0x01fba1a905ba11ecac7d9457a5ebcddd, '5. Input and Output', 'JceW6zvmA_Q', 10012),
       (0x01fba30505ba11ecac7d9457a5ebcddd, '6. Variables', '7wWvSn_qiBc', 10012),
       (0x01fba3e305ba11ecac7d9457a5ebcddd, '7. Primitives and Objects', 'r4wWYgkBcpI', 10012),
       (0x01fba4e205ba11ecac7d9457a5ebcddd, '8. Variable Declaration and Initialization', '1mRN2MwdWUo', 10012),
       (0x01fba5b405ba11ecac7d9457a5ebcddd, '9. Primitive Data Types', 'qUXbJziVs_o', 10012),
       (0x2beeb834f6d611eb9dec9457a5ebcddd, '1. How to Get Started With JavaScript', 'ItYye9h_RXg', 10011),
       (0x2bf2426ef6d611eb9dec9457a5ebcddd, '2. Which Tools to Use When Developing', 'ns_L4kpxY8c', 10011),
       (0x2bf52459f6d611eb9dec9457a5ebcddd, '3. How to Install Extensions For JavaScript Development', 'ZouJQRxgem0',
        10011),
       (0x2bf7a5e1f6d611eb9dec9457a5ebcddd, '4. How to Include JavaScript in Our HTML', 'AD5hxsFJc4o', 10011),
       (0x2bf9d60bf6d611eb9dec9457a5ebcddd, '5. How to Use the Developer Tool for JavaScript Development ',
        'sjmyfwESv1g', 10011),
       (0x2bfc1df3f6d611eb9dec9457a5ebcddd, '6. Rules for Writing JavaScript Code', 'FdlBtidhAnE', 10011),
       (0x2bfe4ddcf6d611eb9dec9457a5ebcddd, '7. How to Create Variables in JavaScript', '9aGIAL16DL4', 10011),
       (0x2c009a2df6d611eb9dec9457a5ebcddd, '8. Different Data Types in JavaScript', 'O9by2KcR2v4', 10011),
       (0x2c030b2ef6d611eb9dec9457a5ebcddd, '9. Different Types of Operators in JavaScript', 'FZzyij43A54', 10011),
       (0x2c054a16f6d611eb9dec9457a5ebcddd, '10. String Operator in JavaScript Explained', 'uli67N4Z03Y', 10011),
       (0x2f202a3005bc11ecac7d9457a5ebcddd, '1. Preferences and Project Settings', 'FK-vhzvYDg', 10014),
       (0x2f20436705bc11ecac7d9457a5ebcddd, '2. Timeline Scroll: Page VS Smooth', 'GRSV6TO5sA', 10014),
       (0x2f2045db05bc11ecac7d9457a5ebcddd, '3. Scale or Set to Frame Size', 'kh5wWqyG1KU', 10014),
       (0x2f20475205bc11ecac7d9457a5ebcddd, '4. Keyboard Shortcuts', 'aJTn5pj2zZ0', 10014),
       (0x2f20482d05bc11ecac7d9457a5ebcddd, '5. How to Setup a New Project', 'Ud3ABhhJwb0', 10014),
       (0x2f2048fe05bc11ecac7d9457a5ebcddd, '6. Windows and Layouts', 'J-XuNBVG_lg', 10014),
       (0x2f2049de05bc11ecac7d9457a5ebcddd, '7. Importing Media Part 1', 't7hGBgkOJDY', 10014),
       (0x2f204a9e05bc11ecac7d9457a5ebcddd, '8. Importing Media Part 2', 'Y_aydeNr8l4', 10014),
       (0x2f204b7005bc11ecac7d9457a5ebcddd, '9. The Project Panel', 'UTtrFgapV_4', 10014),
       (0x2f204c3b05bc11ecac7d9457a5ebcddd, '10. The Source Panel', 'J7xy5l6IsMk', 10014),
       (0x3396636004e611ec89079457a5ebcddd, '1. Excel Basics Tutorial', 'rwbho0CgEAE', 10019),
       (0x33967fe904e611ec89079457a5ebcddd, '2. Intermediate Excel Skills, Tips, and Tricks', 'lxq_46nY43g', 10019),
       (0x3396818e04e611ec89079457a5ebcddd, '3. VLOOKUP Basics', 'y8ygx1Zkcgs', 10019),
       (0x339682b104e611ec89079457a5ebcddd, '4. Creating Pivot Tables in Excel', 'BkmxrvIfDGA', 10019),
       (0x3396838204e611ec89079457a5ebcddd, '5. Using Recommended Pivot Tables', 'ebdgGbsTWs8', 10019),
       (0x3396848e04e611ec89079457a5ebcddd, '6. Protecting a Sheet', 'piIWTp3qncw', 10019),
       (0x3396861604e611ec89079457a5ebcddd, '7. Advanced Excel - 3D Formulas', 'arJBUarj8u8', 10019),
       (0x3396883204e611ec89079457a5ebcddd, '8. Data Validation and Drop-Down Lists\r\n', 'SlWIgMFpsPg', 10019),
       (0x339689fd04e611ec89079457a5ebcddd, '9. Creating Your Own Excel Templates', 'dgHjAHIBvsI', 10019),
       (0x33968b4404e611ec89079457a5ebcddd, '10. Excel Split Names', 'yCxnWvD_r_Q', 10019),
       (0xba3bed7e04e411ec89079457a5ebcddd, '1. The Piano Keyboard', 'QBH6IpRkVDs', 10018),
       (0xba40aab904e411ec89079457a5ebcddd, '2. Starting to Read Music', '3BULT0-joT0', 10018),
       (0xba40ad3604e411ec89079457a5ebcddd, '3. Reading a Melody', 'NUVQIwO1SEI', 10018),
       (0xba40ae6c04e411ec89079457a5ebcddd, '4. The Left Hand And The Scale Of C Major', 'f9JI_5y0K68', 10018),
       (0xba40afde04e411ec89079457a5ebcddd, '5. Learning a Piece', '1JVtPB8VJXE', 10018),
       (0xba40b0aa04e411ec89079457a5ebcddd, '6. A New Piece, A New Scale, And Rests', 'yeP2qRcHuUM', 10018),
       (0xba40b18804e411ec89079457a5ebcddd, '7. Quavers (Eighth Notes) And Accidentals', '3UetN01yPTs', 10018),
       (0xba4b541504e411ec89079457a5ebcddd, '8. A New Piece, Phrase Marks And Ritenuto', '7x20caWKKkQ', 10018),
       (0xba4b584004e411ec89079457a5ebcddd, '9. The Concept Of Musical Key', 'aU3VEy-4qwM', 10018),
       (0xba4b59fc04e411ec89079457a5ebcddd, '10. More Scales, And Playing Staccato', 'ZCdfa9GyyuM', 10018),
       (0xc29d557204e211ec89079457a5ebcddd, '1. Intro to Economics: Crash Course Economics', '3ez10ADR_gM', 10015),
       (0xc29d6eb904e211ec89079457a5ebcddd, '2. Crash Course Economics- How it all started', 'A307rSHkJdc', 10015),
       (0xc29d700b04e211ec89079457a5ebcddd, '3. Specialization and Trade: Crash Course Economics', 'NI9TLDIPVcs',
        10015),
       (0xc29d70fd04e211ec89079457a5ebcddd, '4. Economic Systems and Macroeconomics: Crash Course Economics',
        'B43YEW2FvDs', 10015),
       (0xc29d71b604e211ec89079457a5ebcddd, '5. Supply and Demand: Crash Course Economics', 'g9aDizJpd_s', 10015),
       (0xc29d72e704e211ec89079457a5ebcddd, '6. Macroeconomics: Crash Course Economics', 'd8uTB5XorBw', 10015),
       (0xc29d739004e211ec89079457a5ebcddd, '7. Productivity and Growth: Crash Course Economics', 'UHiUYj5EA0w', 10015),
       (0xc29d745b04e211ec89079457a5ebcddd, '8. Inflation and Bubbles and Tulips: Crash Course Economics',
        'T8-85cZRI9o', 10015),
       (0xc29d759e04e211ec89079457a5ebcddd, '9. Fiscal Policy and Stimulus: Crash Course Economics', 'otmgFQHbaDo',
        10015),
       (0xc29d765304e211ec89079457a5ebcddd, '10. Deficits & Debts: Crash Course Economics', '3sUCSGVYzI0', 10015),
       (0xc572ad17f6d211eb9dec9457a5ebcddd, '1. Install and Setup for Mac and Windows', 'YYXdXT2l-Gg', 10010),
       (0xc573b6e9f6d211eb9dec9457a5ebcddd, '2. Strings - Working with Textual Data', 'k9TUPpGqYTo', 10010),
       (0xc5745d32f6d211eb9dec9457a5ebcddd, '3. Integers and Floats - Working with Numeric Data', 'khKv-8q7YmY', 10010),
       (0xc574f7c9f6d211eb9dec9457a5ebcddd, '4. Lists, Tuples, and Sets', 'W8KRzm-HUcc', 10010),
       (0xc5758cd4f6d211eb9dec9457a5ebcddd, '5. Dictionaries - Working with Key-Value Pairs', 'daefaLgNkw0', 10010),
       (0xc5761bf5f6d211eb9dec9457a5ebcddd, '6. Conditionals and Booleans - If, Else, and Elif Statements',
        'DZwmZ8Usvnk', 10010),
       (0xc576a3f7f6d211eb9dec9457a5ebcddd, '7. Loops and Iterations - For/While Loops', '6iF8Xb7Z3wQ', 10010),
       (0xc5771935f6d211eb9dec9457a5ebcddd, '8. Functions', '9Os0o3wzS_I', 10010),
       (0xc5779aaef6d211eb9dec9457a5ebcddd, '9. Import Modules and Exploring The Standard Library', 'CqvZ3vGoGs0',
        10010),
       (0xc5780405f6d211eb9dec9457a5ebcddd, '10. Setting up a Python Development Environment in Sublime Text',
        'xFciV6Ew5r4', 10010),
       (0xdce6961a05ba11ecac7d9457a5ebcddd, '1. Interface Introduction to Adobe Illustrator', 'QKWnkIPur2Q', 10013),
       (0xdce6b00205ba11ecac7d9457a5ebcddd, '2. Panels & Workspaces in Adobe Illustrator', '2E9oGKd0Ayg', 10013),
       (0xdce6b1c405ba11ecac7d9457a5ebcddd, '3. Artboards in Adobe Illustrator', '9GbLm_WXWwk', 10013),
       (0xdce6b30a05ba11ecac7d9457a5ebcddd, '4. Vector basics | Selection & Direct selection tool & more',
        'GFY0_EMVYDw', 10013),
       (0xdce6b3f905ba11ecac7d9457a5ebcddd, '5. Fill & Stroke effects in Adobe Illustrator', 'xhATZA88zC4', 10013),
       (0xdce6b53105ba11ecac7d9457a5ebcddd, '6. Using Colour | Swatches | Pantone\'s | Gradients & more', 'MX67tVC8f3s',
        10013),
       (0xdce6b65805ba11ecac7d9457a5ebcddd, '7. 10 Handy Tips | Things to know for beginners', 'wRL9rPO2SYk', 10013),
       (0xdce6b74905ba11ecac7d9457a5ebcddd, '8. Creating shape vectors in Adobe Illustrator', 'FH4-WIkHnd4', 10013),
       (0xdce6b80c05ba11ecac7d9457a5ebcddd, '9. Grouping | Compounding vectors & Using the shape builder tool',
        'aGFWmYHUQOU', 10013),
       (0xdce6b97e05ba11ecac7d9457a5ebcddd, '10. The Blob brush tool & Eraser tool in Adobe Illustrator', 'wnXDboK7FH8',
        10013),
       (0xf576636d04df11ec89079457a5ebcddd, 'DAY 1 - Lose Weight and Lose Belly Fat', 'By6GXzcldGY', 10016),
       (0xf5767d6704df11ec89079457a5ebcddd, 'DAY 2 - Lose Weight and Lose Belly Fat', '4920WOvqqPQ', 10016),
       (0xf5767f6204df11ec89079457a5ebcddd, 'DAY 3 - Lose Weight and Lose Belly Fat', 'rqFYiJNOZjY', 10016),
       (0xf57680b104df11ec89079457a5ebcddd, 'DAY 4 - Lose Weight and Lose Belly Fat', 'MnUflUjr2bw', 10016),
       (0xf576819404df11ec89079457a5ebcddd, 'DAY 5 - Lose Weight and Lose Belly Fat', 'lSZacNnqn7g', 10016),
       (0xf576827604df11ec89079457a5ebcddd, 'DAY 6 - Lose Weight and Lose Belly Fat', '2tHo7phAtfM', 10016),
       (0xf5768b8204df11ec89079457a5ebcddd, 'DAY 8 - Lose Weight and Lose Belly Fat', 'ex16_HWvYJM', 10016),
       (0xf5768cfe04df11ec89079457a5ebcddd, 'DAY 9 - Lose Weight and Lose Belly Fat', 'mbNl_XaWjWQ', 10016),
       (0xf5768e3f04df11ec89079457a5ebcddd, 'DAY 10 - Lose Weight and Lose Belly Fat', 'x9MJjko7vJM', 10016),
       (0xf7cf71f304e311ec89079457a5ebcddd, 'Part 1 - Real Estate Investing', 'nFH8PV_jPLk', 10017),
       (0xf7cfa04204e311ec89079457a5ebcddd, 'Part 2 - Real Estate Investing', 'dgu5PaLFu5Y', 10017),
       (0xf7cfa1eb04e311ec89079457a5ebcddd, 'Part 3 - Real Estate Investing', '4c6afHE7P6M', 10017),
       (0xf7cfa32e04e311ec89079457a5ebcddd, 'Part 4 - Real Estate Investing', 'mRzoImyFMSY', 10017),
       (0xf7cfa42704e311ec89079457a5ebcddd, 'Part 5 - Real Estate Investing', '1mecyBhnJKg', 10017);

-- --------------------------------------------------------

--
-- Table structure for table `reviews`
--

CREATE TABLE `reviews`
(
    `id`         int(11)                                                       NOT NULL,
    `rating`     double                                                        NOT NULL,
    `content`    varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `course_id`  int(11)                                                       NOT NULL,
    `user_id`    int(11)                                                       NOT NULL,
    `created_at` datetime(6)                                                   NOT NULL,
    `updated_at` datetime(6)                                                   NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='Student reviews. Must own the course';

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions`
(
    `transaction_id` varchar(20) NOT NULL,
    `created_at`     datetime(6) DEFAULT NULL,
    `totalprice`     double      NOT NULL,
    `course_id`      int(11)     NOT NULL,
    `user_id`        int(11)     NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='Cash transactions for buying courses';

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users`
(
    `id`            int(11)                                                                          NOT NULL,
    `datejoined`    datetime(6)                                                                      NOT NULL,
    `email`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                    NOT NULL,
    `fullname`      varchar(50)                                                                      NOT NULL,
    `password`      varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                              DEFAULT NULL COMMENT 'mapenzimatamu69=kaka@yahoo.com',
    `auth_provider` enum ('LOCAL','GOOGLE') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NOT NULL DEFAULT 'LOCAL',
    `user_role`     enum ('ROLE_USER','ROLE_ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ROLE_USER' COMMENT 'keeps user authority'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `wishlist`
--

CREATE TABLE `wishlist`
(
    `wishlist_id` int(11)     NOT NULL,
    `created_at`  datetime(6) NOT NULL,
    `course_id`   int(11)     NOT NULL,
    `user_id`     int(11)     NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='records user wishlist';

--
-- Indexes for dumped tables
--

--
-- Indexes for table `courses`
--
ALTER TABLE `courses`
    ADD PRIMARY KEY (`id`),
    ADD KEY `IDX_CATEGORY` (`category`);

--
-- Indexes for table `enrollments`
--
ALTER TABLE `enrollments`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UKsb9w22vmn0ny3dq4sau62xih1` (`user_id`, `current_lesson_id`),
    ADD UNIQUE KEY `UK9b67t8pe97iktaxdp9caj2bd9` (`user_id`, `current_lesson_id`),
    ADD KEY `FKsu6cg2f9qh1256x751mvubeuf` (`current_lesson_id`);

--
-- Indexes for table `lessons`
--
ALTER TABLE `lessons`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UK_e4ri79ihrl4716saiqpqeod2i` (`videokey`),
    ADD KEY `FK17ucc7gjfjddsyi0gvstkqeat` (`course_id`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UKgvg1ect42p0nkk171cbuwho8o` (`user_id`, `course_id`),
    ADD KEY `FKccbfc9u1qimejr5ll7yuxbtqs` (`course_id`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
    ADD PRIMARY KEY (`transaction_id`),
    ADD UNIQUE KEY `UKqfqcie6xb6blc5p61b1uy076e` (`user_id`, `course_id`),
    ADD KEY `FKgxxdhhlltjwna28jdamsu1poa` (`course_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`);

--
-- Indexes for table `wishlist`
--
ALTER TABLE `wishlist`
    ADD PRIMARY KEY (`wishlist_id`),
    ADD UNIQUE KEY `UKkboogsaq79fml6t4a7rjhphg7` (`user_id`, `course_id`),
    ADD KEY `FK3oou0jaquu0ulln4n7xs3ltao` (`course_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `courses`
--
ALTER TABLE `courses`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 10021;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `wishlist`
--
ALTER TABLE `wishlist`
    MODIFY `wishlist_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `enrollments`
--
ALTER TABLE `enrollments`
    ADD CONSTRAINT `FK3hjx6rcnbmfw368sxigrpfpx0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    ADD CONSTRAINT `FKsu6cg2f9qh1256x751mvubeuf` FOREIGN KEY (`current_lesson_id`) REFERENCES `lessons` (`id`);

--
-- Constraints for table `lessons`
--
ALTER TABLE `lessons`
    ADD CONSTRAINT `FK17ucc7gjfjddsyi0gvstkqeat` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
    ADD CONSTRAINT `FKccbfc9u1qimejr5ll7yuxbtqs` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
    ADD CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
    ADD CONSTRAINT `FKgxxdhhlltjwna28jdamsu1poa` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
    ADD CONSTRAINT `FKqwv7rmvc8va8rep7piikrojds` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Constraints for table `wishlist`
--
ALTER TABLE `wishlist`
    ADD CONSTRAINT `FK3oou0jaquu0ulln4n7xs3ltao` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`),
    ADD CONSTRAINT `FKtrd6335blsefl2gxpb8lr0gr7` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
