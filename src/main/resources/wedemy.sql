-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Feb 02, 2022 at 10:38 AM
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
-- Table structure for table `cart`
--

CREATE TABLE `cart`
(
    `id`         int(11)       NOT NULL,
    `created_at` datetime(6)   NOT NULL,
    `price`      decimal(6, 2) NOT NULL,
    `course_id`  int(11)       NOT NULL,
    `user_id`    int(11)       NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `courses`
--

CREATE TABLE `courses`
(
    `id`        int(11)                                                       NOT NULL,
    `author`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `category`  varchar(50)                                                   NOT NULL,
    `price`     decimal(6, 2)                                                 NOT NULL,
    `rating`    double                                                        NOT NULL DEFAULT '3.5',
    `thumb_url` varchar(255)                                                           DEFAULT NULL,
    `title`     varchar(255)                                                  NOT NULL,
    `subtitle`  varchar(250)                                                           DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='Courses are parents of Lessons';

--
-- Dumping data for table `courses`
--

INSERT INTO `courses` (`id`, `author`, `category`, `price`, `rating`, `thumb_url`, `title`, `subtitle`)
VALUES (10010, 'Corey Schafer', 'Development', '17.99', 4.5, 'https://i3.ytimg.com/vi/XKHEtdqhLK8/maxresdefault.jpg',
        'Complete Python Bootcamp',
        'Learn Python like a Professional. Start from the basics and go all the way to creating your own applications and games.'),
       (10011, 'Dani Krossing', 'Development', '18.99', 4.5, 'https://i3.ytimg.com/vi/jS4aFq5-91M/maxresdefault.jpg',
        'Learn JavaScript Programming',
        'Master fundamentals with JavaScript exercises, projects, live examples & more'),
       (10012, 'Caleb Curry', 'Development', '19.99', 3.5, 'https://i3.ytimg.com/vi/GoXwIVyNvX0/maxresdefault.jpg',
        'Java Programming Essentials', 'Obtain valuable Core Java Skills And Java Certification'),
       (10013, 'Gareth David Studio', 'PhotoVideo', '21.99', 4.5,
        'https://i3.ytimg.com/vi/9EGI-FSr0Ig/maxresdefault.jpg', 'Beginners Guide To Adobe Illustrator',
        'Learn graphic design, logo design, and more with this in-depth, practical, easy-to-follow course!'),
       (10014, 'chinfat', 'PhotoVideo', '22.50', 3.5, 'https://i3.ytimg.com/vi/u99i1SmDgIc/maxresdefault.jpg',
        'Learn Adobe Premiere Pro CC',
        'Learn Beginner-Advanced Video Editing, Audio Editing, Color Grading, Motion Graphics, and more'),
       (10015, 'Jacob Clifford', 'Finance', '15.99', 4.5, 'https://i3.ytimg.com/vi/g9aDizJpd_s/maxresdefault.jpg',
        'Crash Course Economics',
        'The Easiest Beginner level Course on Economics with real life examples and graphic content. Perfect for Newbies!'),
       (10016, 'MrandMrsMuscle', 'Health', '18.99', 3.5, 'https://i3.ytimg.com/vi/By6GXzcldGY/maxresdefault.jpg',
        'Lose Belly Fat in 14 Days',
        'This course will help jump-start your body to lose belly fat, lose weight and guide you with a 14 day exercise plan. NO EQUIPMENT needed'),
       (10017, 'Robert Kiyosaki', 'Real Estate', '21.99', 4.5, 'https://i3.ytimg.com/vi/UJv9-F7SN5A/maxresdefault.jpg',
        'Real Estate Investing',
        'Learn professional investment techniques for real estate investing in residential and commercial properties'),
       (10018, 'Bill Hilton', 'Music', '15.99', 4.5, 'https://i3.ytimg.com/vi/WJ3-F02-F_Y/maxresdefault.jpg',
        'How To Play Piano for Beginners',
        'Learn Piano in WEEKS not years. Play-By-Ear & learn to Read Music. Pop, Blues, Jazz, Ballads, Classical'),
       (10019, 'TTFS', 'Office', '17.99', 3.5, 'https://i3.ytimg.com/vi/Vl0H-qTclOg/maxresdefault.jpg',
        'Master Microsoft Excel',
        'Microsoft Excel Beginner to Professional. Includes Pivot Tables, Power Query, NEW Formulas');

-- --------------------------------------------------------

--
-- Table structure for table `course_objectives`
--

CREATE TABLE `course_objectives`
(
    `id`        int(11) NOT NULL,
    `objective` varchar(200) DEFAULT NULL,
    `course_id` int(11) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

--
-- Dumping data for table `course_objectives`
--

INSERT INTO `course_objectives` (`id`, `objective`, `course_id`)
VALUES (1, 'Be able to program in Python professionally', 10010),
       (2, 'Build GUIs and Desktop applications with Python', 10010),
       (3, 'Be able to build fully fledged websites and web apps with Python', 10010),
       (4, 'Create a portfolio of Python projects to apply for developer jobs', 10010),
       (5, 'Be able to build fully fledged websites and web apps with Python', 10010),
       (6, 'All about variables, functions, objects and arrays', 10011),
       (7, 'Modern ES6 from the beginning: arrow functions, destructuring, spread operator, optional chaining', 10011),
       (8, 'Project-driven learning with plenty of examples', 10011),
       (9, 'Asynchronous JavaScript: Event loop, promises, async/await, AJAX calls and APIs', 10011),
       (10, 'Complex concepts like the \'this\' keyword, higher-order functions, closures', 10011),
       (11, 'Learn industry best practices in Java software development from a professional', 10012),
       (12, 'Obtain proficiency in Java 8 and Java 11', 10012),
       (13, 'Be able to demonstrate your understanding of Java to future employers', 10012),
       (14, 'Acquire essential java basics for transitioning to the Spring Framework, Java EE, Android', 10012),
       (15, 'Master Exceptions, IO, Collections Framework, Generics, Multi-threading, Databases', 10012),
       (16, 'Master advanced Illustrator tools and techniques', 10013),
       (17, 'Design your own graphics, without any experience', 10013),
       (18, 'Create custom typography', 10013),
       (19, 'Take hand drawings and recreate them using Illustrator', 10013),
       (20, 'Export your projects for print, web, or other design projects', 10013),
       (21, 'Edit an entire video from beginning to end, using professional and efficient techniques', 10014),
       (22, 'Master Premiere Pro and be CONFIDENT Editing Your Own Videos', 10014),
       (23, 'Learn how to edit social media clips for Instagram, Facebook, Twitter & YouTube Stories', 10014),
       (24, 'How to organize your video editing footage like a Pro', 10014),
       (25, 'Add a feeling to your video with color grading', 10014),
       (26, 'Acquire a solid understanding of key economic relationships', 10015),
       (27, 'Understand business cycles', 10015),
       (28, 'Using demand and supply, illustrate price determination', 10015),
       (29, 'Examine the impact of tax/subsidies', 10015),
       (30, 'Decluttering and impact on economy', 10015),
       (31, 'Understand the fundamentals of weight loss', 10016),
       (32, 'Develop healthy eating habits', 10016),
       (33, 'Stay motivated for long term results', 10016),
       (34, 'Lose weight Naturally within 2 weeks', 10016),
       (35, 'Have a healthy relationship with food', 10016),
       (36, 'Confidently analyze multifamily real estate investment opportunities', 10017),
       (37, 'Use Professional Grade Investment Models to Evaluate Your Deals', 10017),
       (38, 'Use Smart Investment Deal Structures With Business Partners', 10017),
       (39, 'Evaluate Rental Income Properties', 10017),
       (40, 'Know a \"Good\" Investment from a \"Bad\" Investment', 10017),
       (41, 'You will learn to read sheet music AS you learn to play-by-ear', 10018),
       (42, 'You get to sound like a pro right from the start', 10018),
       (43, 'Learn to play the piano without theory', 10018),
       (44, 'Master unique tips & techniques that you won\'t find in ANY other course, guaranteed', 10019),
       (45, 'Build a solid understanding on the Basics of Microsoft Excel', 10019),
       (46, 'Maintain large sets of Excel data in a list or table', 10019),
       (47, 'Write advanced conditional, text, date and lookup functions, including XLOOKUP', 10019),
       (48, 'Get LIFETIME access to project files, quizzes, homework exercises', 10019),
       (49, 'Navigating the keyboard', 10018),
       (50, 'Basic improvisation', 10018);

-- --------------------------------------------------------

--
-- Table structure for table `enrollments`
--

CREATE TABLE `enrollments`
(
    `id`             int(11)     NOT NULL,
    `created_at`     datetime(6) NOT NULL,
    `current_lesson` binary(16)           DEFAULT NULL,
    `is_completed`   bit(1)      NOT NULL DEFAULT b'0',
    `updated_at`     datetime(6) NOT NULL,
    `course_id`      int(11)     NOT NULL,
    `user_id`        int(11)     NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

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
       (0x0d200ec44aa911ec9ce79457a5ebcddd, 'DAY 1 - Lose Weight and Lose Belly Fat', 'By6GXzcldGY', 10016),
       (0x0d2027a74aa911ec9ce79457a5ebcddd, 'DAY 2 - Lose Weight and Lose Belly Fat', '4920WOvqqPQ', 10016),
       (0x0d2029754aa911ec9ce79457a5ebcddd, 'DAY 3 - Lose Weight and Lose Belly Fat', 'rqFYiJNOZjY', 10016),
       (0x0d202aa74aa911ec9ce79457a5ebcddd, 'DAY 4 - Lose Weight and Lose Belly Fat', 'MnUflUjr2bw', 10016),
       (0x0d202b764aa911ec9ce79457a5ebcddd, 'DAY 5 - Lose Weight and Lose Belly Fat', 'lSZacNnqn7g', 10016),
       (0x0d202c434aa911ec9ce79457a5ebcddd, 'DAY 6 - Lose Weight and Lose Belly Fat', '2tHo7phAtfM', 10016),
       (0x0d202d474aa911ec9ce79457a5ebcddd, 'DAY 7 - Lose Weight and Lose Belly Fat', '0nhfE3q6ZA8', 10016),
       (0x0d202e084aa911ec9ce79457a5ebcddd, 'DAY 8 - Lose Weight and Lose Belly Fat', 'ex16_HWvYJM', 10016),
       (0x0d202ee34aa911ec9ce79457a5ebcddd, 'DAY 9 - Lose Weight and Lose Belly Fat', 'mbNl_XaWjWQ', 10016),
       (0x0d202f964aa911ec9ce79457a5ebcddd, 'DAY 10 - Lose Weight and Lose Belly Fat', 'x9MJjko7vJM', 10016),
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
       (0xf7cf71f304e311ec89079457a5ebcddd, 'Part 1 - Real Estate Investing', 'nFH8PV_jPLk', 10017),
       (0xf7cfa04204e311ec89079457a5ebcddd, 'Part 2 - Real Estate Investing', 'dgu5PaLFu5Y', 10017),
       (0xf7cfa1eb04e311ec89079457a5ebcddd, 'Part 3 - Real Estate Investing', '4c6afHE7P6M', 10017),
       (0xf7cfa32e04e311ec89079457a5ebcddd, 'Part 4 - Real Estate Investing', 'mRzoImyFMSY', 10017),
       (0xf7cfa42704e311ec89079457a5ebcddd, 'Part 5 - Real Estate Investing', '1mecyBhnJKg', 10017);

-- --------------------------------------------------------

--
-- Table structure for table `order_items`
--

CREATE TABLE `order_items`
(
    `id`             bigint(20)  NOT NULL,
    `course_id`      int(11)     NOT NULL,
    `transaction_id` varchar(20) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

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
-- Table structure for table `sales`
--

CREATE TABLE `sales`
(
    `transaction_id` varchar(20)   NOT NULL,
    `created_at`     datetime(6)   NOT NULL,
    `payment_method` varchar(30)   NOT NULL,
    `total_paid`     decimal(6, 2) NOT NULL,
    `user_id`        int(11)       NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

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
    `password`      varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                              DEFAULT NULL,
    `auth_provider` enum ('LOCAL','GOOGLE') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci         NOT NULL DEFAULT 'LOCAL',
    `user_role`     enum ('ROLE_USER','ROLE_ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ROLE_USER'
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
-- Indexes for table `cart`
--
ALTER TABLE `cart`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UK1pvbbq921pknnn6rl5hjbedq4` (`user_id`, `course_id`),
    ADD KEY `FK1vllsie342rrqn6niy90pufd5` (`course_id`);

--
-- Indexes for table `courses`
--
ALTER TABLE `courses`
    ADD PRIMARY KEY (`id`),
    ADD KEY `IDX_CATEGORY` (`category`);

--
-- Indexes for table `course_objectives`
--
ALTER TABLE `course_objectives`
    ADD PRIMARY KEY (`id`),
    ADD KEY `FKe14b2c5dqha8fmbx7vwlebss7` (`course_id`);

--
-- Indexes for table `enrollments`
--
ALTER TABLE `enrollments`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UKg1muiskd02x66lpy6fqcj6b9q` (`user_id`, `course_id`),
    ADD KEY `FKho8mcicp4196ebpltdn9wl6co` (`course_id`);

--
-- Indexes for table `lessons`
--
ALTER TABLE `lessons`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UK_e4ri79ihrl4716saiqpqeod2i` (`videokey`),
    ADD KEY `FK17ucc7gjfjddsyi0gvstkqeat` (`course_id`);

--
-- Indexes for table `order_items`
--
ALTER TABLE `order_items`
    ADD PRIMARY KEY (`id`),
    ADD KEY `IDX_TRANSACTION_ID` (`transaction_id`),
    ADD KEY `FKy4aiomvn1gl62yjreckpt6lv` (`course_id`);

--
-- Indexes for table `reviews`
--
ALTER TABLE `reviews`
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE KEY `UKgvg1ect42p0nkk171cbuwho8o` (`user_id`, `course_id`),
    ADD KEY `FKccbfc9u1qimejr5ll7yuxbtqs` (`course_id`);

--
-- Indexes for table `sales`
--
ALTER TABLE `sales`
    ADD PRIMARY KEY (`transaction_id`),
    ADD KEY `IDX_USER_ID` (`user_id`);

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
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `courses`
--
ALTER TABLE `courses`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 10021;

--
-- AUTO_INCREMENT for table `course_objectives`
--
ALTER TABLE `course_objectives`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,
    AUTO_INCREMENT = 51;

--
-- AUTO_INCREMENT for table `enrollments`
--
ALTER TABLE `enrollments`
    MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `order_items`
--
ALTER TABLE `order_items`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

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
-- Constraints for table `cart`
--
ALTER TABLE `cart`
    ADD CONSTRAINT `FK1vllsie342rrqn6niy90pufd5` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `FKg5uhi8vpsuy0lgloxk2h4w5o6` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `course_objectives`
--
ALTER TABLE `course_objectives`
    ADD CONSTRAINT `FKe14b2c5dqha8fmbx7vwlebss7` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`);

--
-- Constraints for table `enrollments`
--
ALTER TABLE `enrollments`
    ADD CONSTRAINT `FK3hjx6rcnbmfw368sxigrpfpx0` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    ADD CONSTRAINT `FKho8mcicp4196ebpltdn9wl6co` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`);

--
-- Constraints for table `lessons`
--
ALTER TABLE `lessons`
    ADD CONSTRAINT `FK17ucc7gjfjddsyi0gvstkqeat` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `order_items`
--
ALTER TABLE `order_items`
    ADD CONSTRAINT `FKceser0bpu7s99jinnuax1ys5u` FOREIGN KEY (`transaction_id`) REFERENCES `sales` (`transaction_id`),
    ADD CONSTRAINT `FKy4aiomvn1gl62yjreckpt6lv` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`);

--
-- Constraints for table `reviews`
--
ALTER TABLE `reviews`
    ADD CONSTRAINT `FKccbfc9u1qimejr5ll7yuxbtqs` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `FKcgy7qjc1r99dp117y9en6lxye` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `sales`
--
ALTER TABLE `sales`
    ADD CONSTRAINT `FK5bgaw8g0rrbqdvafq36g58smk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `wishlist`
--
ALTER TABLE `wishlist`
    ADD CONSTRAINT `FK3oou0jaquu0ulln4n7xs3ltao` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `FKtrd6335blsefl2gxpb8lr0gr7` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
