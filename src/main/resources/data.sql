-- ===================================================================
-- CareerPath AI Default Seed Data
-- Uses INSERT IGNORE to prevent duplicates when restarted.
-- ===================================================================

-- 1. Insert Companies (TCS, Infosys, Cognizant, Accenture, Capgemini)
INSERT IGNORE INTO companies (id, name, description) VALUES 
(1, 'TCS', 'Tata Consultancy Services - Focuses on quantitative aptitude, English verbal ability, and foundation programming.'),
(2, 'Infosys', 'Infosys - Focuses on logical reasoning, analytical puzzle-solving, and pseudo-code analysis.'),
(3, 'Cognizant', 'Cognizant - Focuses on quantitative aptitude, analytical capability, English communication, and basic coding.'),
(4, 'Accenture', 'Accenture - Tests cognitive assessment, critical reasoning, problem-solving, and technical logic.'),
(5, 'Capgemini', 'Capgemini - Focuses on arithmetic, English, game-based logical reasoning, and programming logic.');

-- 2. Insert Company Topics (Aptitude, Coding, Interview)
-- TCS (Company ID: 1)
INSERT IGNORE INTO company_topics (id, company_id, category, topic_name) VALUES
(1, 1, 'APTITUDE', 'Percentages'),
(2, 1, 'APTITUDE', 'Profit and Loss'),
(3, 1, 'APTITUDE', 'Time and Work'),
(4, 1, 'CODING', 'Strings'),
(5, 1, 'CODING', 'Arrays'),
(6, 1, 'CODING', 'Hashing'),
(7, 1, 'INTERVIEW', 'OOP'),
(8, 1, 'INTERVIEW', 'DBMS'),
(9, 1, 'INTERVIEW', 'SQL');

-- Infosys (Company ID: 2)
INSERT IGNORE INTO company_topics (id, company_id, category, topic_name) VALUES
(10, 2, 'APTITUDE', 'Logical Reasoning'),
(11, 2, 'APTITUDE', 'Data Interpretation'),
(12, 2, 'CODING', 'Arrays'),
(13, 2, 'CODING', 'Searching & Sorting'),
(14, 2, 'INTERVIEW', 'OOP'),
(15, 2, 'INTERVIEW', 'Computer Networks');

-- Cognizant (Company ID: 3)
INSERT IGNORE INTO company_topics (id, company_id, category, topic_name) VALUES
(16, 3, 'APTITUDE', 'Quantitative Aptitude'),
(17, 3, 'APTITUDE', 'English Communication'),
(18, 3, 'CODING', 'Basic Data Structures'),
(19, 3, 'INTERVIEW', 'OOP Concepts'),
(20, 3, 'INTERVIEW', 'DBMS & SQL Joins');

-- Accenture (Company ID: 4)
INSERT IGNORE INTO company_topics (id, company_id, category, topic_name) VALUES
(21, 4, 'APTITUDE', 'Critical Reasoning'),
(22, 4, 'APTITUDE', 'Abstract Reasoning'),
(23, 4, 'CODING', 'Algorithms'),
(24, 4, 'INTERVIEW', 'Cloud Fundamentals'),
(25, 4, 'INTERVIEW', 'DBMS & Java');

-- Capgemini (Company ID: 5)
INSERT IGNORE INTO company_topics (id, company_id, category, topic_name) VALUES
(26, 5, 'APTITUDE', 'Game-Based Aptitude'),
(27, 5, 'APTITUDE', 'Numerical Ability'),
(28, 5, 'CODING', 'Data Structures'),
(29, 5, 'INTERVIEW', 'Core Java Programming'),
(30, 5, 'INTERVIEW', 'SQL Queries');


-- 3. Insert Roadmaps (Weekly Plans)
-- TCS (Company ID: 1)
INSERT IGNORE INTO roadmaps (id, company_id, week_number, topic_name, description) VALUES
(1, 1, 1, 'Percentages & Ratios', 'Study percentages, ratio, proportion, and average. Practice basic MCQ questions.'),
(2, 1, 2, 'Time and Work', 'Learn Time & Work, Time & Distance formulas and shortcut tricks for train problems.'),
(3, 1, 3, 'Arrays & Strings', 'Master linear search, array traversal, reversing strings, and palindrome checks in Java.'),
(4, 1, 4, 'OOP & DBMS', 'Revise OOP features (Inheritance, Polymorphism) and practice basic SQL Select queries.');

-- Infosys (Company ID: 2)
INSERT IGNORE INTO roadmaps (id, company_id, week_number, topic_name, description) VALUES
(5, 2, 1, 'Logical Reasoning', 'Solve series completion, coding-decoding, blood relations, and syllogisms.'),
(6, 2, 2, 'Pseudo-code Analysis', 'Learn to trace outputs and analyze logic in pseudo-code scripts.'),
(7, 2, 3, 'Searching & Sorting', 'Practice Linear Search, Binary Search, Bubble Sort, and Selection Sort in Java.'),
(8, 2, 4, 'OOP & Networking', 'Study OOP fundamentals and basic networking concepts (TCP/IP, OSI layers).');

-- Cognizant (Company ID: 3)
INSERT IGNORE INTO roadmaps (id, company_id, week_number, topic_name, description) VALUES
(9, 3, 1, 'Quantitative Focus', 'Revise Profit & Loss, Percentages, and Simple Interest formulas.'),
(10, 3, 2, 'Verbal Ability', 'Improve sentence correction, vocabulary, and reading comprehension.'),
(11, 3, 3, 'Basic Arrays', 'Practice basic operations like finding max/min elements in arrays.'),
(12, 3, 4, 'DBMS Concepts', 'Revise database normalization, keys, and basic SQL commands.');


-- 4. Insert Default Users (for testing)
INSERT IGNORE INTO users (id, username, password, email, role, selected_company_id) VALUES
(1, 'admin', 'admin', 'admin@careerpath.com', 'ADMIN', 1),
(2, 'student', 'student', 'student@careerpath.com', 'STUDENT', 1);


-- 5. Insert Aptitude Questions (MCQs)
-- Clear out bulk default questions. We keep only ONE template question
-- so that you have a visual syntax model when writing your own imports.
INSERT IGNORE INTO aptitude_questions (id, company_id, category, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES
(1, 1, 'QUANTITATIVE', 'Template Question: A train travels 120 km in 2 hours. Find its speed in km/h.', '40', '50', '60', '70', 'C', 'Speed = Distance / Time = 120 km / 2 hours = 60 km/h.');

-- Seeded Verbal Questions for Specific Companies (TCS, Infosys, Cognizant, Accenture, Capgemini)
-- First 5: TCS (Company ID: 1)
INSERT IGNORE INTO aptitude_questions (id, company_id, category, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES
(101, 1, 'VERBAL', 'Choose the word that is most nearly opposite in meaning to "Scarce".', 'Rare', 'Abundant', 'Limited', 'Insufficient', 'B', 'Abundant means existing in large quantities, which is the opposite of scarce.'),
(102, 1, 'VERBAL', 'Choose the word that is most nearly similar in meaning to "Diligent".', 'Lazy', 'Careless', 'Hardworking', 'Slow', 'C', 'Diligent means hardworking, careful, and persistent.'),
(103, 1, 'VERBAL', 'Choose the correctly spelled word.', 'Accomodation', 'Accommodation', 'Acommodation', 'Accommadation', 'B', 'Accommodation is the correct spelling.'),
(104, 1, 'VERBAL', 'Fill in the blank: She has been working in this company _____ 2021.', 'For', 'Since', 'From', 'At', 'B', 'Since is used with a specific point in time.'),
(105, 1, 'VERBAL', 'Choose the correct meaning of the idiom "Hit the nail on the head."', 'To hit someone', 'To speak loudly', 'To describe exactly what is causing a situation', 'To repair something', 'C', 'The idiom means to identify or describe something accurately.');

-- Next 5: Infosys (Company ID: 2)
INSERT IGNORE INTO aptitude_questions (id, company_id, category, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES
(106, 2, 'VERBAL', 'Choose the one-word substitution for "A person who loves books."', 'Bibliophile', 'Philanthropist', 'Linguist', 'Orator', 'A', 'A bibliophile is a person who loves or collects books.'),
(107, 2, 'VERBAL', 'Find the error in the sentence: "Neither of the players are ready for the match."', 'Neither', 'are', 'ready', 'No Error', 'B', 'Neither is singular, so the correct verb is is, not are.'),
(108, 2, 'VERBAL', 'Arrange the words to form a meaningful sentence: P. always, Q. speaks, R. politely, S. she', 'PQRS', 'SPQR', 'SQRP', 'SRQP', 'B', 'The correct sentence is "She always speaks politely."'),
(109, 2, 'VERBAL', 'Choose the sentence with the correct punctuation.', 'Let''s eat, Grandma!', 'Lets eat Grandma!', 'Lets eat, Grandma.', 'Let''s eat Grandma.', 'A', 'The comma correctly separates the person being addressed, changing the meaning of the sentence.'),
(110, 2, 'VERBAL', 'Choose the word that best completes the sentence: "The manager appreciated his _____ approach to solving problems."', 'Careless', 'Systematic', 'Random', 'Carefree', 'B', 'Systematic means organized and methodical, making it the most appropriate choice.');

-- Next 5: Cognizant (Company ID: 3)
INSERT IGNORE INTO aptitude_questions (id, company_id, category, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES
(111, 3, 'VERBAL', 'Choose the word that is most nearly similar in meaning to "Reluctant."', 'Willing', 'Hesitant', 'Excited', 'Brave', 'B', 'Reluctant means unwilling or hesitant.'),
(112, 3, 'VERBAL', 'Choose the word that is most nearly opposite in meaning to "Expand."', 'Increase', 'Extend', 'Contract', 'Develop', 'C', 'Contract means to become smaller, the opposite of expand.'),
(113, 3, 'VERBAL', 'Choose the correctly spelled word.', 'Embarrasment', 'Embarrassment', 'Embarassment', 'Embarresment', 'B', 'Embarrassment is the correct spelling.'),
(114, 3, 'VERBAL', 'Fill in the blank: If I _____ earlier, I would have caught the train.', 'leave', 'had left', 'left', 'have left', 'B', 'The third conditional uses had + past participle.'),
(115, 3, 'VERBAL', 'Choose the correct meaning of the idiom "Once in a blue moon."', 'Every month', 'Very rarely', 'Every year', 'Frequently', 'B', 'The idiom means something that happens very rarely.');

-- Next 5: Accenture (Company ID: 4)
INSERT IGNORE INTO aptitude_questions (id, company_id, category, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES
(116, 4, 'VERBAL', 'Choose the one-word substitution for "A person who cannot read or write."', 'Literate', 'Illiterate', 'Scholar', 'Orator', 'B', 'An illiterate person cannot read or write.'),
(117, 4, 'VERBAL', 'Find the error in the sentence: "She don''t like coffee."', 'She', 'don''t', 'like', 'No Error', 'B', 'The correct verb is doesn''t because the subject is singular.'),
(118, 4, 'VERBAL', 'Choose the correct passive voice of: "The teacher praised the student."', 'The student praised the teacher.', 'The student was praised by the teacher.', 'The teacher was praised.', 'The student has praised the teacher.', 'B', 'The object becomes the subject in passive voice.'),
(119, 4, 'VERBAL', 'Choose the correct indirect speech: He said, "I am busy."', 'He said that he is busy.', 'He said that I was busy.', 'He said that he was busy.', 'He told he was busy.', 'C', 'Present tense changes to past tense in reported speech.'),
(120, 4, 'VERBAL', 'Choose the word that best completes the sentence: The scientist made an _____ discovery.', 'important', 'impossible', 'accidental', 'astonishing', 'D', 'Astonishing discovery is the most appropriate collocation.');

-- Next 5: Capgemini (Company ID: 5)
INSERT IGNORE INTO aptitude_questions (id, company_id, category, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES
(121, 5, 'VERBAL', 'Choose the synonym of "Brief."', 'Long', 'Short', 'Large', 'Slow', 'B', 'Brief means short in duration or length.'),
(122, 5, 'VERBAL', 'Choose the antonym of "Generous."', 'Kind', 'Helpful', 'Selfish', 'Honest', 'C', 'Selfish is the opposite of generous.'),
(123, 5, 'VERBAL', 'Fill in the blank: The meeting has been postponed _____ next Monday.', 'to', 'for', 'at', 'by', 'A', 'Meetings are commonly postponed to a future date.'),
(124, 5, 'VERBAL', 'Choose the correct meaning of the idiom "Burn the midnight oil."', 'Waste electricity', 'Work or study late into the night', 'Sleep peacefully', 'Cook dinner', 'B', 'The idiom means working or studying late at night.'),
(125, 5, 'VERBAL', 'Choose the one-word substitution for "A speech delivered without preparation."', 'Lecture', 'Debate', 'Extempore', 'Monologue', 'C', 'An extempore speech is delivered without prior preparation.');


-- 6. Insert Coding Questions
-- Beginner Questions
INSERT IGNORE INTO coding_questions (id, title, difficulty, problem_statement, sample_input, sample_output, solution_code, explanation) VALUES
(1, 'Reverse String', 'BEGINNER', 
'Write a function that reverses a given string in-place.', 
'Input: "hello"', 
'Output: "olleh"', 
'public static String reverse(String s) {\n    StringBuilder sb = new StringBuilder(s);\n    return sb.reverse().toString();\n}', 
'Use Java''s StringBuilder class which has a built-in reverse() method, then convert it back to a String.'),

(2, 'Palindrome Check', 'BEGINNER', 
'Check if a string is a palindrome. A palindrome reads the same backwards as forwards (e.g., "radar").', 
'Input: "radar"', 
'Output: true', 
'public static boolean isPalindrome(String s) {\n    int left = 0;\n    int right = s.length() - 1;\n    while (left < right) {\n        if (s.charAt(left) != s.charAt(right)) {\n            return false;\n        }\n        left++;\n        right--;\n    }\n    return true;\n}', 
'Use two pointers starting at both ends of the string. Compare characters and move them inwards. If any characters mismatch, it is not a palindrome.'),

(3, 'Factorial of a Number', 'BEGINNER', 
'Calculate the factorial of a positive integer N (N! = N * (N-1) * ... * 1).', 
'Input: 5', 
'Output: 120', 
'public static int factorial(int n) {\n    int result = 1;\n    for (int i = 1; i <= n; i++) {\n        result *= i;\n    }\n    return result;\n}', 
'Initialize a result variable to 1. Loop from 1 to N, multiplying the result by the loop index in each iteration.');

-- Intermediate Questions
INSERT IGNORE INTO coding_questions (id, title, difficulty, problem_statement, sample_input, sample_output, solution_code, explanation) VALUES
(4, 'Two Sum', 'INTERMEDIATE', 
'Given an array of integers and a target sum, return the indices of the two numbers that add up to the target.', 
'Input: nums = [2, 7, 11, 15], target = 9', 
'Output: [0, 1]', 
'public static int[] twoSum(int[] nums, int target) {\n    Map<Integer, Integer> map = new HashMap<>();\n    for (int i = 0; i < nums.length; i++) {\n        int complement = target - nums[i];\n        if (map.containsKey(complement)) {\n            return new int[] { map.get(complement), i };\n        }\n        map.put(nums[i], i);\n    }\n    return new int[] {};\n}', 
'Use a HashMap to store the numbers we have seen so far along with their indices. For each number, check if its complement (target - current_number) is in the map.'),

(5, 'Kadane''s Algorithm (Max Subarray)', 'INTERMEDIATE', 
'Find the contiguous subarray within a one-dimensional array of numbers which has the largest sum.', 
'Input: [-2, 1, -3, 4, -1, 2, 1, -5, 4]', 
'Output: 6 (Subarray is [4, -1, 2, 1])', 
'public static int maxSubArray(int[] nums) {\n    int maxSoFar = nums[0];\n    int currentMax = nums[0];\n    for (int i = 1; i < nums.length; i++) {\n        currentMax = Math.max(nums[i], currentMax + nums[i]);\n        maxSoFar = Math.max(maxSoFar, currentMax);\n    }\n    return maxSoFar;\n}', 
'Maintain a running maximum sum. At each position, decide whether to add the current element to the existing subarray or start a new subarray from the current element.');

-- Advanced Questions
INSERT IGNORE INTO coding_questions (id, title, difficulty, problem_statement, sample_input, sample_output, solution_code, explanation) VALUES
(6, 'Binary Tree Inorder Traversal', 'ADVANCED', 
'Perform inorder traversal of a Binary Tree (Left, Root, Right) recursively.', 
'Input: Tree [1, null, 2, 3]', 
'Output: [1, 3, 2]', 
'public List<Integer> inorderTraversal(TreeNode root) {\n    List<Integer> res = new ArrayList<>();\n    helper(root, res);\n    return res;\n}\nprivate void helper(TreeNode node, List<Integer> res) {\n    if (node != null) {\n        helper(node.left, res);\n        res.add(node.val);\n        helper(node.right, res);\n    }\n}', 
'Use helper function to recursively visit the left subtree, process/print the root node, and then recursively visit the right subtree.'),

(7, 'Longest Common Subsequence (DP)', 'ADVANCED', 
'Find the length of the longest subsequence common to two sequences.', 
'Input: "abcde", "ace"', 
'Output: 3 (Subsequence "ace")', 
'public static int lcs(String s1, String s2) {\n    int m = s1.length();\n    int n = s2.length();\n    int[][] dp = new int[m + 1][n + 1];\n    for (int i = 1; i <= m; i++) {\n        for (int j = 1; j <= n; j++) {\n            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {\n                dp[i][j] = dp[i - 1][j - 1] + 1;\n            } else {\n                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);\n            }\n        }\n    }\n    return dp[m][n];\n}', 
'Use a 2D dynamic programming grid (dp table) to store length of common subsequences. If characters match, increment from diagonal; otherwise take the maximum of top or left cell.');


-- 7. Insert Learning Resources
INSERT IGNORE INTO resources (id, topic_name, title, resource_type, url) VALUES
(1, 'OOP', 'Java Object Oriented Programming (OOP) Notes', 'NOTES', 'https://docs.oracle.com/javase/tutorial/java/concepts/'),
(2, 'OOP', 'OOP Concepts Explained in 10 Minutes', 'VIDEO', 'https://www.youtube.com/watch?v=pTB0EiLXUC8'),
(3, 'DBMS', 'Introduction to Database Management Systems', 'NOTES', 'https://www.geeksforgeeks.org/introduction-of-dbms/'),
(4, 'DBMS', 'DBMS SQL Joins & Queries Tutorial', 'VIDEO', 'https://www.youtube.com/watch?v=HXV3zeQKqGY'),
(5, 'SQL', 'SQL Queries Cheat Sheet and Practice Problems', 'QUESTIONS', 'https://www.w3schools.com/sql/'),
(6, 'Percentages', 'Aptitude Tricks: Solving Percentages Easily', 'VIDEO', 'https://www.youtube.com/watch?v=y7M71Gv3fW0'),
(7, 'Time and Work', 'Time and Work Aptitude Shortcut Methods', 'NOTES', 'https://www.indiabix.com/aptitude/time-and-work/formulas');
