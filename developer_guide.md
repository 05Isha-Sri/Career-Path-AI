# Developer Reference & Interview Guide - CareerPath AI

This guide is designed to help you understand every component of the **CareerPath AI** project. It outlines the role of each file, explains the data flow, and prepares you for potential technical questions that an interviewer might ask.

---

## 1. Project Directory & File Guide

Here is the exact purpose of every file in your project. If an interviewer asks you about your file layout, you can use this section as a reference:

### Configuration Files
*   **`pom.xml`**
    *   *Purpose*: The Maven project file. It defines the libraries (dependencies) used in your project:
        *   `spring-boot-starter-web`: For building the REST API controllers.
        *   `spring-boot-starter-data-jpa`: For database persistence using Hibernate.
        *   `mysql-connector-j`: The driver that allows Java to talk to MySQL.
    *   *Java Version*: Set to Java 17.
*   **`src/main/resources/application.properties`**
    *   *Purpose*: Houses application configurations. It defines the MySQL database URL (`careerpath_db`), root credentials, database updates strategy (`ddl-auto=update`), SQL logging, and enables automatic execution of database seed files on startup.

### Database Init SQL Scripts (under `src/main/resources/`)
*   **`schema.sql`**
    *   *Purpose*: Defines the raw SQL commands to create the 9 database tables (`companies`, `users`, `aptitude_questions`, etc.) if they do not already exist.
*   **`data.sql`**
    *   *Purpose*: Seeds the database with default data on startup. It inserts 4 companies (TCS, Infosys, Zoho, Amazon), topics, roadmaps, 10 aptitude MCQs, 7 coding questions, and recommended resources using `INSERT IGNORE` to prevent duplication.

### Backend Java Code (under `src/main/java/com/careerpath/`)
*   **`CareerPathApplication.java`**
    *   *Purpose*: The entry point of your application. Contains the standard `main()` method which starts the embedded Tomcat server on port 8080.
*   **`entity/` (The Model Layer)**
    *   *Purpose*: These files contain standard Java classes mapped directly to your database tables using JPA annotations.
    *   *Files*:
        *   `User.java` (maps to `users` table)
        *   `Company.java` (maps to `companies` table)
        *   `CompanyTopic.java` (maps to `company_topics` table)
        *   `Roadmap.java` (maps to `roadmaps` table)
        *   `AptitudeQuestion.java` (maps to `aptitude_questions` table)
        *   `CodingQuestion.java` (maps to `coding_questions` table)
        *   `Resource.java` (maps to `resources` table)
        *   `TestResult.java` (maps to `test_results` table)
        *   `UserProgress.java` (maps to `user_progress` table)
*   **`repository/` (The Data Access Layer)**
    *   *Purpose*: Java interfaces extending `JpaRepository`. They tell Spring Data JPA to automatically handle SQL queries (insert, select, update, delete) for each database entity.
    *   *Files*: Contains repository interfaces for each Entity.
*   **`controller/` (The REST API Layer)**
    *   *Purpose*: Java classes annotated with `@RestController` that handle HTTP requests from the browser, process parameters, call repositories, and return JSON responses.
    *   *Files*:
        *   `AuthController.java`: Manages logins, logouts, and user registration.
        *   `CompanyController.java`: Manages company listing and company selection.
        *   `PracticeController.java`: Exposes practice questions, coding problems, and mock test scoring.
        *   `ProgressController.java`: Updates completed topics and computes dashboard statistics.
        *   `AdminController.java`: Restricts access to ADMIN users and saves newly created content.

### Frontend Web Files (under `src/main/resources/static/`)
*   **`index.html`**
    *   *Purpose*: The Single Page Application layout. It holds all HTML containers (Dashboard, Practice, Coding, Admin Panel) and swaps visibility using JavaScript.
*   **`css/style.css`**
    *   *Purpose*: Styles the entire website. Uses a premium **Dark Theme** featuring card layout shadows, linear gradients, custom scrollbars, and micro-hover transitions.
*   **`js/app.js`**
    *   *Purpose*: Handles dynamic client-side behavior. It calls the REST API controllers using `fetch()`, logs in users, manages mock assessment timers, and updates dashboard figures.

---

## 2. Key Technical Workflows (How the Code Works)

If the interviewer asks: **"Explain how data flows in your application when a user logs in or takes a test."** here is how you explain it:

### A. How Login Works (Session Authentication)
1.  **Frontend**: The student enters credentials in `index.html`. The JavaScript in `app.js` runs `handleLogin()`, making a POST request to `/api/auth/login` with the username and password.
2.  **Backend**: `AuthController.java` intercepts the request. It queries the `UserRepository` to find the user.
3.  **Validation**: It compares the entered password with the database password. If they match:
    *   It saves the User object in the Servlet session: `session.setAttribute("user", user)`.
    *   It returns the user details as JSON.
4.  **Frontend Session Save**: JavaScript receives the success response, hides the login card, displays the dashboard portal, and periodically calls `/api/auth/me` to ensure the session remains active.

### B. How Progress is Tracked & Calculated
1.  **Marking Complete**: When a student ticks a topic checkbox or clicks "Completed" on a coding modal:
    *   JavaScript calls `POST /api/progress/complete` sending `{ itemType, itemKey }`.
    *   `ProgressController.java` saves a record in the `user_progress` table with the user ID.
2.  **Dashboard Load**: When opening the dashboard:
    *   JavaScript calls `GET /api/progress/dashboard`.
    *   `ProgressController.java` queries database counts:
        *   Roadmap Completion %: Divides the count of completed topics by the total topics for the selected company, multiplying by 100.
        *   Average Score: Sums up the percentage scores in `test_results` and divides by the total mock tests completed.
    *   The values are returned to the client and updated on the dashboard dynamically.

---

## 3. Potential Interview Questions & Answers

Be prepared to answer these questions directly:

### Q1. Why did you use Session-based authentication instead of JWT?
> *"For a monolithic single-origin web application where the frontend is served directly by the Spring Boot server, Session-based auth (`HttpSession`) is standard, highly secure, and very simple. It keeps user credentials in secure server memory and avoids the complexity of generating, signing, and refreshing JWT tokens on the client side."*

### Q2. Why did you map selectedCompanyId as a Long variable instead of a full @ManyToOne relationship inside User.java?
> *"Storing references as simple foreign keys (`Long selectedCompanyId`) instead of full `@ManyToOne` entity mappings prevents common JPA issues like LazyInitializationExceptions and infinite loops during JSON serialization. It makes database queries cleaner and the code much easier to explain and modify."*

### Q3. How does the Mock Test select random questions?
> *"In `AptitudeQuestionRepository.java`, I wrote a native SQL query: `SELECT * FROM aptitude_questions ORDER BY RAND() LIMIT 20`. This tells MySQL to randomize the row order and return exactly 20 questions. It is a highly optimized way to implement randomized testing."*

### Q4. If I ask you to add a feature—like a custom time limit for different tests—where would you change it?
1.  **Database**: I would update the `aptitude_questions` table or a test settings table to store time limits.
2.  **Backend**: In `PracticeController.java`, I would return the time limit along with the question list.
3.  **Frontend**: In `app.js` under `startMockTest()`, instead of hardcoding `mockTimeRemaining = 600` (10 minutes), I would set it dynamically based on the value received from the API.

---

## 4. How to Run and Test
1. Make sure MySQL is running on your computer.
2. Open terminal inside the project directory:
   ```bash
   cd /Users/ishasris/.gemini/antigravity/scratch/careerpath-ai
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Open your browser and navigate to `http://localhost:8080`.
