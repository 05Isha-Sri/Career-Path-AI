-- ===================================================================
-- CareerPath AI Default Seed Data
-- Uses INSERT IGNORE to prevent duplicates when restarted.
-- ===================================================================

-- 1. Insert Companies
INSERT IGNORE INTO companies (id, name, description) VALUES 
(1, 'TCS', 'Tata Consultancy Services - Focuses on quantitative aptitude, English verbal ability, and foundation programming.'),
(2, 'Infosys', 'Infosys - Focuses on logical reasoning, analytical puzzle-solving, and pseudo-code analysis.'),
(3, 'Zoho', 'Zoho Corporation - Emphasizes core programming logic, algorithms, OOP concepts, and design rounds.'),
(4, 'Amazon', 'Amazon - Expects high-level coding, advanced Data Structures (Trees, Graphs, DP), and System Design.');

-- 2. Insert Company Topics (Aptitude, Coding, Interview)
-- For TCS (Company ID: 1)
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

-- For Infosys (Company ID: 2)
INSERT IGNORE INTO company_topics (id, company_id, category, topic_name) VALUES
(10, 2, 'APTITUDE', 'Logical Reasoning'),
(11, 2, 'APTITUDE', 'Data Interpretation'),
(12, 2, 'CODING', 'Arrays'),
(13, 2, 'CODING', 'Searching & Sorting'),
(14, 2, 'INTERVIEW', 'OOP'),
(15, 2, 'INTERVIEW', 'Computer Networks');

-- For Zoho (Company ID: 3)
INSERT IGNORE INTO company_topics (id, company_id, category, topic_name) VALUES
(16, 3, 'APTITUDE', 'Aptitude Puzzles'),
(17, 3, 'CODING', 'Arrays & Strings'),
(18, 3, 'CODING', 'Recursion'),
(19, 3, 'INTERVIEW', 'OOP Principles'),
(20, 3, 'INTERVIEW', 'DBMS & SQL');

-- For Amazon (Company ID: 4)
INSERT IGNORE INTO company_topics (id, company_id, category, topic_name) VALUES
(21, 4, 'APTITUDE', 'Advanced Aptitude'),
(22, 4, 'CODING', 'Dynamic Programming'),
(23, 4, 'CODING', 'Binary Trees'),
(24, 4, 'INTERVIEW', 'System Design'),
(25, 4, 'INTERVIEW', 'OOP & OOP Design Patterns');


-- 3. Insert Roadmaps (Weekly Plans)
-- TCS (Company ID: 1)
INSERT IGNORE INTO roadmaps (id, company_id, week_number, topic_name, description) VALUES
(1, 1, 1, 'Percentages & Ratios', 'Study percentages, ratio, proportion, and average. Practice 20 basic MCQ questions.'),
(2, 1, 2, 'Time and Work', 'Learn Time & Work, Time & Distance formulas and shortcut tricks for train problems.'),
(3, 1, 3, 'Arrays & Strings', 'Master linear search, array traversal, reversing strings, and palindrome checks in Java.'),
(4, 1, 4, 'OOP & DBMS', 'Revise OOP features (Inheritance, Polymorphism) and practice basic SQL Select queries.');

-- Zoho (Company ID: 3)
INSERT IGNORE INTO roadmaps (id, company_id, week_number, topic_name, description) VALUES
(5, 3, 1, 'Arrays & Matrices', 'Work on array elements, sub-arrays, transposing matrices, and sorting algorithms.'),
(6, 3, 2, 'Recursion Basics', 'Understand recursion call stack. Practice factorial, fibonacci, and string permutations recursively.'),
(7, 3, 3, 'Object-Oriented Design', 'Build custom classes representing real-world systems (e.g., Library Management System).'),
(8, 3, 4, 'DBMS & SQL Joins', 'Revise Normalization rules, database indexing, and write complex JOIN SQL queries.');


-- 4. Insert Default Users (for testing)
-- Admin User: admin / admin
-- Student User: student / student
INSERT IGNORE INTO users (id, username, password, email, role, selected_company_id) VALUES
(1, 'admin', 'admin', 'admin@careerpath.com', 'ADMIN', 1),
(2, 'student', 'student', 'student@careerpath.com', 'STUDENT', 1);


-- 5. Insert Aptitude Questions (MCQs)
-- Category: QUANTITATIVE
INSERT IGNORE INTO aptitude_questions (id, category, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES
(1, 'QUANTITATIVE', 'A train travels 120 km in 2 hours. Find its speed in km/h.', '40', '50', '60', '70', 'C', 'Speed = Distance / Time = 120 km / 2 hours = 60 km/h.'),
(2, 'QUANTITATIVE', 'If the cost price of an item is Rs. 150 and selling price is Rs. 180, what is the profit percentage?', '10%', '15%', '20%', '25%', 'C', 'Profit = 180 - 150 = 30. Profit % = (Profit / Cost Price) * 100 = (30 / 150) * 100 = 20%.'),
(3, 'QUANTITATIVE', 'A can complete a work in 10 days, and B can do it in 15 days. How long will they take to complete it together?', '5 days', '6 days', '7 days', '8 days', 'B', 'Together work rate = 1/10 + 1/15 = (3+2)/30 = 5/30 = 1/6. So they take 6 days.'),
(4, 'QUANTITATIVE', 'What is 15% of 200?', '15', '20', '30', '40', 'C', '15% of 200 = (15 / 100) * 200 = 30.');

-- Category: LOGICAL
INSERT IGNORE INTO aptitude_questions (id, category, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES
(5, 'LOGICAL', 'Look at the series: 2, 4, 8, 16, 32, ... What number should come next?', '48', '56', '64', '72', 'C', 'Each number is multiplied by 2 to get the next number: 32 * 2 = 64.'),
(6, 'LOGICAL', 'Find the odd one out: Apple, Mango, Carrot, Banana.', 'Apple', 'Mango', 'Carrot', 'Banana', 'C', 'Apple, Mango, and Banana are fruits, while Carrot is a vegetable.'),
(7, 'LOGICAL', 'If CAT is coded as 3120, how is DOG coded?', '4157', '4156', '4167', '4158', 'A', 'Coding is based on alphabet positions: D=4, O=15, G=7. So DOG is coded as 4157.');

-- Category: VERBAL
INSERT IGNORE INTO aptitude_questions (id, category, question_text, option_a, option_b, option_c, option_d, correct_option, explanation) VALUES
(8, 'VERBAL', 'Choose the correct synonym for "ABANDON":', 'Keep', 'Forsake', 'Adopt', 'Cherish', 'B', '"Abandon" means to leave or desert. "Forsake" shares the same meaning.'),
(9, 'VERBAL', 'Complete the sentence: She is ______ honest girl.', 'a', 'an', 'the', 'no article', 'B', 'The word "honest" begins with a silent "h", producing a vowel sound ("on-ist"). Therefore, we use "an".'),
(10, 'VERBAL', 'Identify the antonym of "ANCIENT":', 'Old', 'Modern', 'Historic', 'Antique', 'B', '"Ancient" means very old. Its opposite/antonym is "Modern".');


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
