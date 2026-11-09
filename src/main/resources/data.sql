-- ===================================================================
-- CareerPath AI Default Seed Data
-- Uses INSERT IGNORE to prevent duplicates when restarted.
-- ===================================================================

-- 1. Insert Companies (TCS, Infosys, Cognizant, Accenture, Capgemini)
INSERT INTO companies (id, name, description) VALUES 
(1, 'TCS', 'TCS – Focuses on numerical ability, verbal ability, logical reasoning, advanced quantitative aptitude, and coding skills through its TCS National Qualifier Test (NQT).\n\nKey areas: Quantitative Aptitude · Verbal Ability · Reasoning · Advanced Coding'),
(2, 'Infosys', 'Infosys – Emphasizes mathematical ability, logical reasoning, verbal communication, analytical problem-solving, pseudocode interpretation, and programming skills.\n\nKey areas: Mathematical Ability · Logical Reasoning · Verbal Ability · Pseudocode · Coding'),
(3, 'Cognizant', 'Cognizant – Evaluates candidates through communication assessments, quantitative and logical aptitude, programming fundamentals, technical knowledge, and problem-solving skills.\n\nKey areas: Communication · Aptitude · Technical Skills · Programming · Problem-Solving'),
(4, 'Accenture', 'Accenture – Assesses analytical thinking, logical reasoning, problem-solving, communication, and applied technical knowledge, including coding skills relevant to the job role.\n\nKey areas: Analytical Thinking · Logical Reasoning · Communication · Technical Assessment · Coding'),
(5, 'Capgemini', 'Capgemini – Evaluates quantitative aptitude, logical reasoning, English communication, programming concepts, pseudocode, and technical problem-solving. Some drives also include game-based cognitive assessments.\n\nKey areas: Quantitative Aptitude · Logical Reasoning · English · Pseudocode · Technical Skills')
ON DUPLICATE KEY UPDATE description = VALUES(description);

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
INSERT INTO roadmaps (id, company_id, week_number, topic_name, description) VALUES
(1, 1, 1, 'Quantitative Aptitude', 'Percentages, Profit & Loss\nRatio, Proportion & Averages\nTime & Work, Time & Distance\nNumber System & Data Interpretation'),
(2, 1, 2, 'Verbal Ability', 'Error Spotting & Sentence Correction\nReading Comprehension\nSentence Completion & Para Jumbles'),
(3, 1, 3, 'Logical Reasoning', 'Number & Letter Series\nCoding-Decoding\nBlood Relations & Seating Arrangements'),
(4, 1, 4, 'Coding Practice', 'General coding preparation.'),
(13, 1, 5, 'Programming Language', 'General programming language preparation covering core concepts, OOP, DSA and problem-solving. Do not specify a fixed programming language.'),
(14, 1, 6, 'Resume & Project Preparation', 'Resume explanation and project technical discussion.'),
(15, 1, 7, 'HR Interview Preparation', 'Self-introduction, strengths, weaknesses, career goals and common HR questions.')
ON DUPLICATE KEY UPDATE topic_name = VALUES(topic_name), description = VALUES(description), week_number = VALUES(week_number);

-- Infosys (Company ID: 2)
INSERT INTO roadmaps (id, company_id, week_number, topic_name, description) VALUES
(5, 2, 1, 'Quantitative Aptitude', 'Percentages, Profit & Loss, Mixtures & Allegations\nPermutations & Combinations, Probability\nTime & Work, Time, Speed & Distance\nNumber System & Data Interpretation'),
(6, 2, 2, 'Verbal Ability', 'Error Spotting & Sentence Correction\nReading Comprehension\nSentence Completion & Para Jumbles'),
(7, 2, 3, 'Logical Reasoning', 'Seating Arrangements & Analytical Puzzles\nSyllogisms & Data Sufficiency\nCoding-Decoding & Number Series'),
(8, 2, 4, 'Coding Practice', 'General coding preparation, pseudocode interpretation and output prediction.'),
(20, 2, 5, 'Programming Language', 'General programming language preparation covering core concepts, OOP, DSA and problem-solving. Do not specify a fixed programming language.'),
(21, 2, 6, 'Resume & Project Preparation', 'Resume explanation and project technical discussion.'),
(22, 2, 7, 'HR Interview Preparation', 'Self-introduction, strengths, weaknesses, career goals and common HR questions.')
ON DUPLICATE KEY UPDATE topic_name = VALUES(topic_name), description = VALUES(description), week_number = VALUES(week_number);

-- Cognizant (Company ID: 3)
INSERT INTO roadmaps (id, company_id, week_number, topic_name, description) VALUES
(9, 3, 1, 'Quantitative Aptitude', 'Percentages, Profit & Loss\nRatio, Proportion & Averages\nTime & Work, Time, Speed & Distance\nNumber System & Data Interpretation'),
(10, 3, 2, 'Verbal Ability & Communication', 'Error Spotting & Sentence Correction\nReading Comprehension\nSentence Completion & Para Jumbles'),
(11, 3, 3, 'Logical Reasoning', 'Number & Letter Series\nSeating Arrangements & Puzzles\nCoding-Decoding & Syllogisms'),
(12, 3, 4, 'Coding Practice', 'General coding preparation, pseudocode interpretation and output prediction.'),
(23, 3, 5, 'Programming Language', 'General programming language preparation covering core concepts, OOP, DSA and problem-solving. Do not specify a fixed programming language.'),
(24, 3, 6, 'Resume & Project Preparation', 'Resume explanation and project technical discussion.'),
(25, 3, 7, 'HR Interview Preparation', 'Self-introduction, strengths, weaknesses, career goals and common HR questions.')
ON DUPLICATE KEY UPDATE topic_name = VALUES(topic_name), description = VALUES(description), week_number = VALUES(week_number);

-- Accenture (Company ID: 4)
INSERT INTO roadmaps (id, company_id, week_number, topic_name, description) VALUES
(26, 4, 1, 'Quantitative Aptitude', 'Percentages, Profit & Loss\nRatio, Proportion & Averages\nTime & Work, Time, Speed & Distance\nNumerical Ability & Data Interpretation'),
(27, 4, 2, 'Verbal Ability', 'Error Spotting & Sentence Correction\nReading Comprehension\nVocabulary & Sentence Completion'),
(28, 4, 3, 'Logical Reasoning', 'Critical Thinking & Analytical Reasoning\nAbstract Reasoning & Pattern Recognition\nLogical Puzzles & Problem-Solving'),
(29, 4, 4, 'Coding Practice', 'General coding preparation, pseudocode interpretation, output prediction and basic technical MCQs covering MS Office, Networking, Security and Cloud fundamentals.'),
(30, 4, 5, 'Programming Language', 'General programming language preparation covering core concepts, OOP, DSA and problem-solving. Do not specify a fixed programming language.'),
(31, 4, 6, 'Resume & Project Preparation', 'Resume explanation and project technical discussion.'),
(32, 4, 7, 'HR Interview Preparation', 'Self-introduction, strengths, weaknesses, career goals and common HR questions.')
ON DUPLICATE KEY UPDATE topic_name = VALUES(topic_name), description = VALUES(description), week_number = VALUES(week_number);

-- Capgemini (Company ID: 5)
INSERT INTO roadmaps (id, company_id, week_number, topic_name, description) VALUES
(33, 5, 1, 'Quantitative Aptitude', 'Percentages, Profit & Loss\nRatio, Proportion & Averages\nTime & Work, Time, Speed & Distance\nNumber System & Data Interpretation'),
(34, 5, 2, 'Verbal Ability', 'Error Spotting & Sentence Correction\nReading Comprehension\nVocabulary, Synonyms & Antonyms'),
(35, 5, 3, 'Logical Reasoning', 'Pattern Recognition & Number Series\nSeating Arrangements & Logical Puzzles\nDeductive & Inductive Reasoning'),
(36, 5, 4, 'Coding Practice', 'General coding preparation, pseudocode interpretation, output prediction and programming logic.'),
(37, 5, 5, 'Programming Language', 'General programming language preparation covering core concepts, OOP, DSA and problem-solving. Do not specify a fixed programming language.'),
(38, 5, 6, 'Resume & Project Preparation', 'Resume explanation and project technical discussion.'),
(39, 5, 7, 'HR Interview Preparation', 'Self-introduction, strengths, weaknesses, career goals and common HR questions.')
ON DUPLICATE KEY UPDATE topic_name = VALUES(topic_name), description = VALUES(description), week_number = VALUES(week_number);


-- 4. Insert Default Users (for testing)
INSERT IGNORE INTO users (id, username, password, email, role, selected_company_id) VALUES
(1, 'admin', 'admin', 'admin@careerpath.com', 'ADMIN', 1),
(2, 'student', 'student', 'student@careerpath.com', 'STUDENT', 1);


-- 5. Insert Aptitude Questions (MCQs)
-- Seeded Practice Questions for TCS (16), Infosys (15), Cognizant (19), Accenture (15), Capgemini (20)
INSERT IGNORE INTO aptitude_questions VALUES (1,1,'QUANTITATIVE','A train travels 120 km in 2 hours. Find its speed in km/h.','40','50','60','70','C','Speed = Distance / Time = 120 km / 2 hours = 60 km/h.');
INSERT IGNORE INTO aptitude_questions VALUES (101,1,'VERBAL','Choose the word that is most nearly opposite in meaning to \"Scarce\".','Rare','Abundant','Limited','Insufficient','B','Abundant means existing in large quantities, which is the opposite of scarce.');
INSERT IGNORE INTO aptitude_questions VALUES (102,1,'VERBAL','Choose the word that is most nearly similar in meaning to \"Diligent\".','Lazy','Careless','Hardworking','Slow','C','Diligent means hardworking, careful, and persistent.');
INSERT IGNORE INTO aptitude_questions VALUES (103,1,'VERBAL','Choose the correctly spelled word.','Accomodation','Accommodation','Acommodation','Accommadation','B','Accommodation is the correct spelling.');
INSERT IGNORE INTO aptitude_questions VALUES (104,1,'VERBAL','Fill in the blank: She has been working in this company _____ 2021.','For','Since','From','At','B','Since is used with a specific point in time.');
INSERT IGNORE INTO aptitude_questions VALUES (105,1,'VERBAL','Choose the correct meaning of the idiom \"Hit the nail on the head.\"','To hit someone','To speak loudly','To describe exactly what is causing a situation','To repair something','C','The idiom means to identify or describe something accurately.');
INSERT IGNORE INTO aptitude_questions VALUES (106,2,'VERBAL','Choose the one-word substitution for \"A person who loves books.\"','Bibliophile','Philanthropist','Linguist','Orator','A','A bibliophile is a person who loves or collects books.');
INSERT IGNORE INTO aptitude_questions VALUES (107,2,'VERBAL','Find the error in the sentence: \"Neither of the players are ready for the match.\"','Neither','are','ready','No Error','B','Neither is singular, so the correct verb is is, not are.');
INSERT IGNORE INTO aptitude_questions VALUES (108,2,'VERBAL','Arrange the words to form a meaningful sentence: P. always, Q. speaks, R. politely, S. she','PQRS','SPQR','SQRP','SRQP','B','The correct sentence is \"She always speaks politely.\"');
INSERT IGNORE INTO aptitude_questions VALUES (109,2,'VERBAL','Choose the sentence with the correct punctuation.','Let\'s eat, Grandma!','Lets eat Grandma!','Lets eat, Grandma.','Let\'s eat Grandma.','A','The comma correctly separates the person being addressed, changing the meaning of the sentence.');
INSERT IGNORE INTO aptitude_questions VALUES (110,2,'VERBAL','Choose the word that best completes the sentence: \"The manager appreciated his _____ approach to solving problems.\"','Careless','Systematic','Random','Carefree','B','Systematic means organized and methodical, making it the most appropriate choice.');
INSERT IGNORE INTO aptitude_questions VALUES (111,3,'VERBAL','Choose the word that is most nearly similar in meaning to \"Reluctant.\"','Willing','Hesitant','Excited','Brave','B','Reluctant means unwilling or hesitant.');
INSERT IGNORE INTO aptitude_questions VALUES (112,3,'VERBAL','Choose the word that is most nearly opposite in meaning to \"Expand.\"','Increase','Extend','Contract','Develop','C','Contract means to become smaller, the opposite of expand.');
INSERT IGNORE INTO aptitude_questions VALUES (113,3,'VERBAL','Choose the correctly spelled word.','Embarrasment','Embarrassment','Embarassment','Embarresment','B','Embarrassment is the correct spelling.');
INSERT IGNORE INTO aptitude_questions VALUES (114,3,'VERBAL','Fill in the blank: If I _____ earlier, I would have caught the train.','leave','had left','left','have left','B','The third conditional uses had + past participle.');
INSERT IGNORE INTO aptitude_questions VALUES (115,3,'VERBAL','Choose the correct meaning of the idiom \"Once in a blue moon.\"','Every month','Very rarely','Every year','Frequently','B','The idiom means something that happens very rarely.');
INSERT IGNORE INTO aptitude_questions VALUES (116,4,'VERBAL','Choose the one-word substitution for \"A person who cannot read or write.\"','Literate','Illiterate','Scholar','Orator','B','An illiterate person cannot read or write.');
INSERT IGNORE INTO aptitude_questions VALUES (117,4,'VERBAL','Find the error in the sentence: \"She don\'t like coffee.\"','She','don\'t','like','No Error','B','The correct verb is doesn\'t because the subject is singular.');
INSERT IGNORE INTO aptitude_questions VALUES (118,4,'VERBAL','Choose the correct passive voice of: \"The teacher praised the student.\"','The student praised the teacher.','The student was praised by the teacher.','The teacher was praised.','The student has praised the teacher.','B','The object becomes the subject in passive voice.');
INSERT IGNORE INTO aptitude_questions VALUES (119,4,'VERBAL','Choose the correct indirect speech: He said, \"I am busy.\"','He said that he is busy.','He said that I was busy.','He said that he was busy.','He told he was busy.','C','Present tense changes to past tense in reported speech.');
INSERT IGNORE INTO aptitude_questions VALUES (120,4,'VERBAL','Choose the word that best completes the sentence: The scientist made an _____ discovery.','important','impossible','accidental','astonishing','D','Astonishing discovery is the most appropriate collocation.');
INSERT IGNORE INTO aptitude_questions VALUES (121,5,'VERBAL','Choose the synonym of \"Brief.\"','Long','Short','Large','Slow','B','Brief means short in duration or length.');
INSERT IGNORE INTO aptitude_questions VALUES (122,5,'VERBAL','Choose the antonym of \"Generous.\"','Kind','Helpful','Selfish','Honest','C','Selfish is the opposite of generous.');
INSERT IGNORE INTO aptitude_questions VALUES (123,5,'VERBAL','Fill in the blank: The meeting has been postponed _____ next Monday.','to','for','at','by','A','Meetings are commonly postponed to a future date.');
INSERT IGNORE INTO aptitude_questions VALUES (124,5,'VERBAL','Choose the correct meaning of the idiom \"Burn the midnight oil.\"','Waste electricity','Work or study late into the night','Sleep peacefully','Cook dinner','B','The idiom means working or studying late at night.');
INSERT IGNORE INTO aptitude_questions VALUES (125,5,'VERBAL','Choose the one-word substitution for \"A speech delivered without preparation.\"','Lecture','Debate','Extempore','Monologue','C','An extempore speech is delivered without prior preparation.');
INSERT IGNORE INTO aptitude_questions VALUES (126,1,'LOGICAL','Four balls and four boxes are colored Yellow, Pink, Red, and Green. The Red ball is in a box whose color matches the ball in the Yellow box. The Red box contains the Green ball. In which box is the Yellow ball?','Yellow Box','Pink Box','Red Box','Green Box','D','Applying all the clues leaves the Green box as the only possible location.');
INSERT IGNORE INTO aptitude_questions VALUES (127,1,'LOGICAL','Every day in his business, a merchant had to weigh amounts from 1 kg to 121 kg to the nearest kilogram. What is the minimum number of weights required, and what should their weights be?','4 weights (1, 2, 4, 8)','5 weights (1, 3, 9, 27, 81)','6 weights (1, 2, 4, 8, 16, 32)','7 weights (1, 3, 9, 27, 54, 81, 121)','B','Using balanced ternary, the minimum weights are 1, 3, 9, 27, and 81 kg.');
INSERT IGNORE INTO aptitude_questions VALUES (128,1,'QUANTITATIVE','Y catches 5 times more fish than X. If the total number of fish caught by X and Y is 42, how many fish did X catch?','5','6','7','8','C','Let X=x, Y=5x. Then 6x=42, so X=7.');
INSERT IGNORE INTO aptitude_questions VALUES (129,1,'QUANTITATIVE','Three companies independently receive savings of 20%, 30%, and 40%, respectively. If the companies are considered together, what is their net percentage saving?','0.25','0.3','0.35','0.4','B','Assume ₹100 each. Total savings = ₹90 out of ₹300, so net saving = 30%.');
INSERT IGNORE INTO aptitude_questions VALUES (130,1,'QUANTITATIVE','The ratio of the incomes of C and D is 3:4, and the ratio of their expenditures is 4:5. If C\'s savings are one-fourth of his income, find the ratio of their savings.','12:17:00','12:19:00','09:16:00','03:04:00','B','Using the given ratios, the savings ratio simplifies to 12:19.');
INSERT IGNORE INTO aptitude_questions VALUES (131,1,'QUANTITATIVE','I lost Rs.68 in two races. My second race loss is Rs.6 more than the first race. My friend lost Rs.4 more than me in the second race. What is the amount lost by my friend in the second race?','39','40','41','42','C','Let the first race loss be x. Then x+(x+6)=68, so x=31. Second race loss=37, friend\'s loss=41.');
INSERT IGNORE INTO aptitude_questions VALUES (132,1,'QUANTITATIVE','A face of a clock is divided into three parts. The total hours in the first part are equal to the sum of the hours in the second and third parts. What is the total number of hours in the largest part?','4','5','6','8','C','Let the three parts be x, y, and z. Since x=y+z and x+y+z=12, we get 2x=12, so x=6.');
INSERT IGNORE INTO aptitude_questions VALUES (133,1,'QUANTITATIVE','Anand finishes a work in 7 days, Bittu in 8 days, and Chandu in 6 days. They work in turns: Anand on Day 1, Bittu on Day 2, Chandu on Day 3, and then repeat the cycle. On which day will the work be completed?','3rd Day','6th Day','9th Day','7th Day','D','In every 3 days they complete 73/168 of the work. After 6 days, 146/168 is completed, and Anand finishes the remaining work on the 7th day.');
INSERT IGNORE INTO aptitude_questions VALUES (134,1,'QUANTITATIVE','Two numbers are in the ratio 8:9. If the smaller number is increased by 12 and the larger number is reduced by 19, the ratio becomes 5:9. Find the larger number.','36','45','54','No valid answer','D','The given data is inconsistent. Solving (8x+12)/(9x-19)=5/9 gives a negative value of x, so no positive solution exists.');
INSERT IGNORE INTO aptitude_questions VALUES (135,1,'LOGICAL','A bag contains 20 yellow, 10 green, 5 white, 8 black, and 1 red ball. What is the minimum number of balls that must be picked to guarantee at least two balls of the same color?','5','6','7','8','B','There are 5 colors. In the worst case, pick one of each color first; the 6th ball must match one of them.');
INSERT IGNORE INTO aptitude_questions VALUES (136,2,'QUANTITATIVE','Complete the series: 5, 20, 24, 6, 2, 8, ?','10','12','14','16','B','The pattern repeats as ×4, +4, ÷4, −4. Hence, 8 + 4 = 12.');
INSERT IGNORE INTO aptitude_questions VALUES (137,2,'LOGICAL','Mr. Mathur\'s jewels were stolen from his bank locker. Twelve lockers are arranged in a 3×4 grid with clues about the positions of Jones, Black, Millar, Booth, Smith, Gray, Green, Wilson, Davis, Herd, and White. Which locker belonged to Mr. Mathur?','7','8','9','10','C','Using the given positional clues, the only remaining locker is Box 9.');
INSERT IGNORE INTO aptitude_questions VALUES (138,2,'LOGICAL','Fifty minutes ago, it was four times as many minutes past 3 o\'clock. How many minutes is it to 6 o\'clock?','24','26','28','30','B','Let the current time be x minutes past 4. Solving gives 26 minutes to 6.');
INSERT IGNORE INTO aptitude_questions VALUES (139,2,'QUANTITATIVE','If a clock takes 7 seconds to strike 7, how long will the same clock take to strike 10?','9.5 seconds','10 seconds','10.5 seconds','11 seconds','C','Seven strikes have 6 intervals. One interval = 7/6 sec. Ten strikes have 9 intervals, so time = 9×7/6 = 10.5 sec.');
INSERT IGNORE INTO aptitude_questions VALUES (140,2,'LOGICAL','Three criminals-Albert, Bruce, and Clive-each made two statements. Only one person told the truth in both statements, while the other two each told one truth and one lie. Who passed the merchandise, who created the diversion, and who took the goods out?','Albert','Bruce','Clive','None','A','Albert passed the goods, Bruce created the diversion, and Clive took the goods out.');
INSERT IGNORE INTO aptitude_questions VALUES (141,2,'QUANTITATIVE','A merchant must weigh every whole number of kilograms from 1 kg to 121 kg. What is the minimum number of weights required?','4','5','6','7','B','Balanced ternary weights: 1, 3, 9, 27, and 81 kg.');
INSERT IGNORE INTO aptitude_questions VALUES (142,2,'LOGICAL','A hotel has 10 storeys. Which floor is above the floor below the floor, below the floor above the floor, below the floor above the fifth floor?','5th','6th','7th','8th','B','Evaluating the directions step by step gives the 6th floor.');
INSERT IGNORE INTO aptitude_questions VALUES (143,2,'LOGICAL','Seven members sat around a table for three days following seating constraints. Which arrangement satisfies all the conditions for the second and third evenings?','Arrangement A','Arrangement B','Arrangement C','Given arrangement','D','The arrangement provided in the question is the only one satisfying all conditions.');
INSERT IGNORE INTO aptitude_questions VALUES (144,2,'QUANTITATIVE','Two trains 50 km apart travel toward each other at 25 km/h each while a bird flies between them at 100 km/h until they collide. How far does the bird travel?','50 km','75 km','100 km','125 km','C','The trains meet after 1 hour, so the bird travels 100 km.');
INSERT IGNORE INTO aptitude_questions VALUES (145,2,'LOGICAL','Four prisoners named East, West, North, and South escaped using different roads. Based on the given clues, which road did Mr. East take?','East Road','West Road','North Road','South Road','C','Applying all constraints gives Mr. East → North Road.');
INSERT IGNORE INTO aptitude_questions VALUES (146,3,'QUANTITATIVE','The profit obtained by selling an article for Rs.480 is twice the loss obtained by selling it for Rs.300. What is the cost price of the article?','120','180','300','360','D','Let CP=x. Then 480−x=2(x−300). Solving gives x=360.');
INSERT IGNORE INTO aptitude_questions VALUES (147,3,'QUANTITATIVE','In the India-Australia one-day match, India needed 324 runs in 48 overs. The run rates in the first four 10-over spells were 6, 8.5, 5.5, and 7 respectively. What run rate is needed in the remaining 8 overs to win?','3.24','5.6','6.75','8.7','C','Runs scored in 40 overs = 270. Remaining = 54 runs in 8 overs ⇒ 6.75.');
INSERT IGNORE INTO aptitude_questions VALUES (148,3,'QUANTITATIVE','Rs.8400 is divided among A, B, C and D such that A:B=2:3, B:C=4:5 and C:D=6:7. Find A\'s share.','1280','1345','2345','5643','A','Combining the ratios gives A:B:C:D = 16:24:30:35. A\'s share = Rs.1280.');
INSERT IGNORE INTO aptitude_questions VALUES (149,3,'QUANTITATIVE','A train 170 meters long travels at 63 km/h. If it crosses a bridge in 30 seconds, what is the length of the bridge?','312 m','325 m','355 m','365 m','C','Speed = 17.5 m/s. Distance in 30 s = 525 m. Bridge = 525−170 = 355 m.');
INSERT IGNORE INTO aptitude_questions VALUES (150,3,'QUANTITATIVE','A cask full of wine has 8 litres removed and replaced with water. This process is repeated three more times. If the final ratio of wine to water is 16:65, what was the original capacity of the cask?','23','24','25','26','B','Using the replacement formula, the original capacity is 24 litres.');
INSERT IGNORE INTO aptitude_questions VALUES (151,3,'QUANTITATIVE','Jaggu set his clock correctly at 1:00 AM on Sunday. The clock gains 20 minutes every 24 hours. He set an alarm for Monday 11:00 AM. When the alarm rang, what was the actual time?','28 min 20 sec before 10 AM','28 min 20 sec past 11 AM','31 min 40 sec past 10 AM','31 min 40 sec past 11 AM','C','The clock gains 20 minutes in 24 hours, so the actual time is 10:31:40 AM.');
INSERT IGNORE INTO aptitude_questions VALUES (152,3,'QUANTITATIVE','A batsman scored 110 runs including 3 fours and 8 sixes. What percentage of his runs were scored by running between the wickets?','0.11','0.45','0.545454545454545','0.454545454545455','D','Runs by boundaries = 60. Running runs = 50. Percentage = (50/110)×100 = 45 5/11%.');
INSERT IGNORE INTO aptitude_questions VALUES (153,3,'QUANTITATIVE','Mahesh hired 32 workers to complete a house in 20 days. After 12 days, 80% of the work was completed. How many workers are needed to finish the remaining work in the remaining time?','12','16','20','32','B','Remaining work = 20% in 8 days. Required workers = (32×20×20)/(80×8)=16.');
INSERT IGNORE INTO aptitude_questions VALUES (154,3,'QUANTITATIVE','Ramakant deposits Rs.5000 at 2.5% simple interest and wants to earn Rs.1500 as interest. How long will it take?','8 years','10 years','12 years','15 years','C','SI=(P×R×T)/100. Solving gives T=12 years.');
INSERT IGNORE INTO aptitude_questions VALUES (155,4,'QUANTITATIVE','If a car travels 420 km in 7 hours, what is its average speed?','50 km/hour','55 km/hour','60 km/hour','65 km/hour','C','Average speed = Distance ÷ Time = 420 ÷ 7 = 60 km/hour.');
INSERT IGNORE INTO aptitude_questions VALUES (156,4,'QUANTITATIVE','What is 15% of 80?','10','12','15','18','B','15% of 80 = 0.15 × 80 = 12.');
INSERT IGNORE INTO aptitude_questions VALUES (157,4,'QUANTITATIVE','If 18 workers can complete a project in 10 days, how many days will it take 15 workers to complete the same project?','10','11','12','15','C','Total work = 18 × 10 = 180 worker-days. Required days = 180 ÷ 15 = 12.');
INSERT IGNORE INTO aptitude_questions VALUES (158,4,'QUANTITATIVE','If A can complete a work in 10 days and B can complete the same work in 15 days, in how many days can they complete the work together?','5 days','6 days','7.5 days','8 days','B','Combined work rate = 1/10 + 1/15 = 1/6. Hence they finish in 6 days.');
INSERT IGNORE INTO aptitude_questions VALUES (159,4,'QUANTITATIVE','The average of five consecutive even numbers is 22. What is the largest number?','22','24','26','28','C','The numbers are 18, 20, 22, 24, and 26.');
INSERT IGNORE INTO aptitude_questions VALUES (160,4,'LOGICAL','In a row of trees, one tree is fifth from the left end and fourth from the right end. How many trees are there in the row?','7','8','9','10','B','Total trees = 5 + 4 − 1 = 8.');
INSERT IGNORE INTO aptitude_questions VALUES (161,4,'LOGICAL','If PALE is coded as 2134 and EARTH is coded as 41576, how is PEARL coded?','21435','24153','24315','25143','B','Using the given letter-to-digit mapping, PEARL is coded as 24153.');
INSERT IGNORE INTO aptitude_questions VALUES (162,4,'LOGICAL','Complete the series: 2, 6, 12, 20, 30, ?','40','42','44','46','B','The differences are +4, +6, +8, +10, +12. Hence the next term is 42.');
INSERT IGNORE INTO aptitude_questions VALUES (163,4,'LOGICAL','A is B\'s sister, C is B\'s mother, D is C\'s father, and E is D\'s mother. How is A related to D?','Daughter','Granddaughter','Niece','Sister','B','D is A\'s maternal grandfather, so A is D\'s granddaughter.');
INSERT IGNORE INTO aptitude_questions VALUES (164,4,'LOGICAL','In a row of children, Ram is 7th from the left and 4th from the right. How many children are there in the row?','9','10','11','12','B','Total children = 7 + 4 − 1 = 10.');
INSERT IGNORE INTO aptitude_questions VALUES (165,5,'QUANTITATIVE','The cost price of 10 Dairy Milk chocolates is equal to the selling price of 9 Dairy Milk chocolates. Find the profit percentage.','101/9%','100/9%','102/9%','103/9%','B','Let CP of one chocolate be 9 and SP be 10. Profit = (1/9)×100 = 100/9%.');
INSERT IGNORE INTO aptitude_questions VALUES (166,5,'QUANTITATIVE','Rs.5000 is divided among 5 men, 6 women and 5 boys such that the ratio of the total shares of men, women and boys is 5:3:2. What is the share of each boy?','100','150','200','250','C','Total shares = 5:3:2. Boys receive Rs.1000 in total. Each of the 5 boys gets Rs.200.');
INSERT IGNORE INTO aptitude_questions VALUES (167,5,'QUANTITATIVE','The cost of organizing a party has a fixed cost and a variable cost per invitee. If 40 invitees cost Rs.200 per head and 50 invitees cost Rs.180 per head, find the variable cost per person and the fixed cost.','Rs.100 & Rs.4000','Rs.100 & Rs.2000','Rs.200 & Rs.3000','Rs.200 & Rs.2000','A','Total costs are Rs.8000 and Rs.9000. Solving gives variable cost Rs.100 and fixed cost Rs.4000.');
INSERT IGNORE INTO aptitude_questions VALUES (168,5,'QUANTITATIVE','A man cycles from Delhi to Rohtak while another cycles from Rohtak to Delhi at the same time. After meeting, they take 10/3 hours and 16/3 hours respectively to complete their journeys. If the first man\'s speed is 8 kmph, find the second man\'s speed.','6.12 kmph','6.22 kmph','6.32 kmph','6.42 kmph','C','Using the relation v₁/v₂ = √(t₂/t₁), the second man\'s speed is approximately 6.32 kmph.');
INSERT IGNORE INTO aptitude_questions VALUES (169,5,'QUANTITATIVE','The sixth term of an A.P. is 12 and the eighth term is 22. Find the sixteenth term.','61','62','63','64','B','The common difference is 5 and the first term is −13. Hence the 16th term is 62.');
INSERT IGNORE INTO aptitude_questions VALUES (170,5,'QUANTITATIVE','In a school, 20% of the students are below 8 years of age. The number of students above 8 years of age is two-thirds of the number of students who are exactly 8 years old, which is 48. What is the total number of students in the school?','72','80','100','120','C','Students aged 8 = 48, above 8 = 32, below 8 = 20. Total = 100.');
INSERT IGNORE INTO aptitude_questions VALUES (171,5,'QUANTITATIVE','Sugar costing Rs.20 per kg is mixed with cinnamon costing Rs.52 per kg to obtain a mixture sold at Rs.35.50 per kg. Find the ratio of sugar to cinnamon.','03:31:00','1 day, 9:03:00','1 day, 9:31:00','1 day, 11:31:00','C','By the rule of alligation, the ratio is (52−35.5):(35.5−20)=16.5:15.5=33:31.');
INSERT IGNORE INTO aptitude_questions VALUES (172,5,'QUANTITATIVE','How many teams of 4 boys and 2 girls can be formed from a group of 10 boys and 5 girls?','1000','1500','2100','3200','C','Number of teams = 10C4 × 5C2 = 210 × 10 = 2100.');
INSERT IGNORE INTO aptitude_questions VALUES (173,5,'QUANTITATIVE','The ratio of the length and breadth of a rectangle is 4:3 and its area is 6912 sq.cm. Find the ratio of the breadth to the area of the rectangle.','01:48:00','0.0888888888888889','0.1','0.108333333333333','D','Let length=4x and breadth=3x. Area=12x²=6912 ⇒ x=24. Breadth=72, so ratio=72:6912=1:96.');
INSERT IGNORE INTO aptitude_questions VALUES (174,5,'QUANTITATIVE','A train travels at 54 km/hr and takes 20 seconds to pass a platform. If the train is 170 m long, what is the length of the platform?','300 m','325 m','355 m','365 m','C','Speed = 15 m/s. Total distance covered in 20 s = 300 m. Since the train length is 170 m, platform length = 300 − 170 = 130 m. However, based on the provided options/source, the intended answer is 355 m, indicating the original question data is inconsistent.');
INSERT IGNORE INTO aptitude_questions VALUES (175,3,'LOGICAL','Find the missing number:\n2, 6, 12, 20, 30, ?','36','40','42','44','C','2 × 1 = 2\n2 × 3 = 6\n3 × 4 = 12\n4 × 5 = 20\n5 × 6 = 30\n6 × 7 = 42');
INSERT IGNORE INTO aptitude_questions VALUES (176,3,'LOGICAL','If APPLE is coded as BQQMF, how is MANGO coded?','NBOHP','NBOGO','MBOHP','NBPHP','A','Each letter is replaced by the next letter in the alphabet.\n\nM → N, A → B, N → O, G → H, O → P');
INSERT IGNORE INTO aptitude_questions VALUES (177,3,'LOGICAL','Pointing to a woman, Rahul says, \"She is the daughter of my father\'s only son.\"\nHow is the woman related to Rahul?','Sister','Daughter','Mother','Cousin','B','Rahul\'s father\'s only son = Rahul.\nRahul\'s daughter = the woman.');
INSERT IGNORE INTO aptitude_questions VALUES (178,3,'LOGICAL','Five people A, B, C, D and E are sitting in a row.\n- A sits to the left of B.\n- C sits to the right of B.\n- D sits to the left of A.\n- E sits to the right of C.\nWho sits in the middle?','A','B','C','D','B','The arrangement is:\nD → A → B → C → E\nTherefore, B occupies the middle position.');
INSERT IGNORE INTO aptitude_questions VALUES (179,3,'LOGICAL','Statements:\n1. All roses are flowers.\n2. Some flowers are red.\nConclusions:\n1. All roses are flowers.\n2. All roses are red.\nWhich conclusion follows?','Only conclusion 1','Only conclusion 2','Both conclusions','Neither conclusion','A','Conclusion 1 directly follows from the first statement.\n\n\n\nConclusion 2 does not necessarily follow because only some flowers are red. We cannot conclude that all roses are red.');
INSERT IGNORE INTO aptitude_questions VALUES (180,5,'LOGICAL','Find the missing number:\n3, 8, 15, 24, 35, ?','46','48','49','50','B','Differences between numbers:\n- 8 − 3 = 5\n- 15 − 8 = 7\n- 24 − 15 = 9\n- 35 − 24 = 11\n- Next difference = 13\n35 + 13 = 48');
INSERT IGNORE INTO aptitude_questions VALUES (181,5,'LOGICAL','If TABLE is coded as UBCMF, how is CHAIR coded?','DIBJS','DIBJR','DHCJS','EIBJS','A','Each letter is replaced by the next letter in the alphabet.\n\nC → D, H → I, A → B, I → J, R → S.');
INSERT IGNORE INTO aptitude_questions VALUES (182,5,'LOGICAL','A is the brother of B. B is the mother of C. D is the father of A.\nHow is D related to C?','Father','Maternal Grandfather','Uncle','Brother','B','B is C\'s mother.\nA is B\'s brother.\nD is the father of B\'s brother, making D also B\'s father.\nTherefore, D is C\'s maternal grandfather.');
INSERT IGNORE INTO aptitude_questions VALUES (183,5,'LOGICAL','Six people P, Q, R, S, T and U are sitting in a row facing north.\n- P sits to the left of Q.\n- R sits immediately to the right of Q.\n- S sits to the left of P.\n- T sits immediately to the right of R.\n- U sits to the right of T.\nWho sits third from the left?','P','Q','R','S','B','Arrangement:\n\nS → P → Q → R → T → U\n\nThe third person from the left is Q.');
INSERT IGNORE INTO aptitude_questions VALUES (184,5,'LOGICAL','Statements:\n1. All laptops are computers.\n2. Some computers are expensive.\n3. No expensive items are cheap.\nConclusions:\n1. All laptops are computers.\n2. Some computers are not cheap.\n3. No laptop is cheap.\nWhich conclusions follow?','Only 1','Only 1 and 2','Only 2 and 3','All three','B','Conclusion 1 directly follows from statement 1.\n\n\n\nConclusion 2 follows because some computers are expensive, and expensive items cannot be cheap.\n\n\n\nConclusion 3 does not necessarily follow because we do not know whether laptops are expensive or cheap.');


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
