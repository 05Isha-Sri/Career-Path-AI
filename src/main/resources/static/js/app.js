/**
   CareerPath AI Front-End Application Logic (Vanilla JavaScript)
   Handles API calls, updates the DOM, and manages application state.
   =================================================================== */

// Global state variables
let currentUser = null;
let activeCompany = null;
let completedItems = []; // Array of { itemType, itemKey }

// Aptitude Practice State
let practiceQuestions = [];
let currentPracticeIndex = 0;
let practiceSelectedAnswer = null;

// Mock Test State
let mockQuestions = [];
let currentMockIndex = 0;
let mockAnswers = {}; // Map of { questionId: selectedOption }
let mockTimerInterval = null;
let mockTimeRemaining = 600; // 10 minutes in seconds

// Coding Practice State
let codingQuestions = [];
let activeCodingQuestion = null;

// ===================================================================
// APPLICATION INITIALIZATION & SESSION CHECK
// ===================================================================

// When the page finishes loading, check if the user is already logged in
document.addEventListener("DOMContentLoaded", () => {
    checkSession();
});

/**
 * Checks the backend session to see if a user is already authenticated.
 * Calls GET /api/auth/me.
 */
function checkSession() {
    fetch("/api/auth/me")
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error("Not authenticated");
        })
        .then(user => {
            currentUser = user;
            showPortal(); // User is logged in, show main portal
        })
        .catch(err => {
            showAuthScreen(); // User is not logged in, show login form
        });
}

/**
 * Switch between the Login and Register forms.
 */
function switchAuthTab(tab) {
    const loginForm = document.getElementById("login-form");
    const registerForm = document.getElementById("register-form");
    const loginTab = document.getElementById("tab-login-btn");
    const registerTab = document.getElementById("tab-register-btn");

    if (tab === "login") {
        loginForm.classList.remove("hidden");
        registerForm.classList.add("hidden");
        loginTab.classList.add("active");
        registerTab.classList.remove("active");
    } else {
        loginForm.classList.add("hidden");
        registerForm.classList.remove("hidden");
        loginTab.classList.remove("active");
        registerTab.classList.add("active");
    }
}

// ===================================================================
// AUTHENTICATION LOGIC (Login, Register, Logout)
// ===================================================================

/**
 * Handles the login form submission.
 */
function handleLogin(event) {
    event.preventDefault(); // Stop page reload

    const usernameInput = document.getElementById("login-username").value;
    const passwordInput = document.getElementById("login-password").value;

    const credentials = {
        username: usernameInput,
        password: passwordInput
    };

    fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(credentials)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        }
        return response.json().then(data => { throw new Error(data.error || "Login failed"); });
    })
    .then(user => {
        currentUser = user;
        showToast("Logged in successfully!", "success");
        showPortal(); // Transition to the student panel
        // Reset inputs
        document.getElementById("login-username").value = "";
        document.getElementById("login-password").value = "";
    })
    .catch(err => {
        showToast(err.message, "error");
    });
}

/**
 * Handles the registration form submission.
 */
function handleRegister(event) {
    event.preventDefault();

    const username = document.getElementById("reg-username").value;
    const email = document.getElementById("reg-email").value;
    const password = document.getElementById("reg-password").value;
    const role = document.getElementById("reg-role").value;

    const newUser = { username, email, password, role };

    fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newUser)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        }
        return response.json().then(data => { throw new Error(data.error || "Registration failed"); });
    })
    .then(user => {
        showToast("Registration successful! You can now log in.", "success");
        switchAuthTab("login"); // Redirect to login tab
        
        // Reset form inputs
        document.getElementById("reg-username").value = "";
        document.getElementById("reg-email").value = "";
        document.getElementById("reg-password").value = "";
    })
    .catch(err => {
        showToast(err.message, "error");
    });
}

/**
 * Handles log out.
 * Destroys the session on the backend.
 */
function handleLogout() {
    // Clear any running timers
    if (mockTimerInterval) {
        clearInterval(mockTimerInterval);
    }

    fetch("/api/auth/logout", { method: "POST" })
    .then(() => {
        currentUser = null;
        activeCompany = null;
        completedItems = [];
        showToast("Logged out successfully.", "info");
        showAuthScreen();
    });
}

// ===================================================================
// UI SCREEN SWITCHING & ROUTING
// ===================================================================

/**
 * Show the authentication forms and hide the app layout.
 */
function showAuthScreen() {
    document.getElementById("auth-screen").classList.remove("hidden");
    document.getElementById("app-screen").classList.add("hidden");
}

/**
 * Show the main application portal and hide auth forms.
 */
function showPortal() {
    document.getElementById("auth-screen").classList.add("hidden");
    document.getElementById("app-screen").classList.remove("hidden");

    // Update sidebar profile labels
    document.getElementById("username-display").textContent = currentUser.username;
    document.getElementById("welcome-username").textContent = currentUser.username;
    document.getElementById("role-display").textContent = currentUser.role;

    // Show Admin navigation button if role is ADMIN
    const adminNavBtn = document.getElementById("admin-nav-btn");
    if (currentUser.role === "ADMIN") {
        adminNavBtn.classList.remove("hidden");
    } else {
        adminNavBtn.classList.add("hidden");
    }

    // Load initial user progress, selected company, and stats
    loadUserProgress()
        .then(() => loadActiveCompanyDetails())
        .then(() => loadDashboardStats())
        .then(() => loadTestHistory());

    // Make sure we view the Dashboard by default
    switchView("dashboard-view");
}

/**
 * Changes panels inside the single-page application.
 */
function switchView(viewId) {
    // Stop mock test timer if navigating away from mock test view while test is running
    if (viewId !== "mocktest-view" && mockTimerInterval) {
        const confirmLeave = confirm("Warning: Leaving this page will cancel your active Mock Test. Are you sure?");
        if (!confirmLeave) return;
        resetMockView();
    }

    // Hide all panels
    const panels = document.querySelectorAll(".view-panel");
    panels.forEach(p => p.classList.add("hidden"));

    // Show selected panel
    document.getElementById(viewId).classList.remove("hidden");

    // Update sidebar navigation buttons highlighting
    const navItems = document.querySelectorAll(".nav-item");
    navItems.forEach(item => {
        if (item.getAttribute("data-target") === viewId) {
            item.classList.add("active");
        } else {
            item.classList.remove("active");
        }
    });

    // Update topbar title
    const viewTitleMap = {
        "dashboard-view": "Dashboard Overview",
        "companies-view": "Target Companies",
        "roadmap-view": "Preparation Roadmap",
        "aptitude-view": "Aptitude Practice Problems",
        "mocktest-view": "Aptitude Mock Assessment",
        "coding-view": "Coding Interview Problems",
        "resources-view": "Study Recommendations",
        "admin-view": "Administrator Dashboard"
    };
    document.getElementById("view-title").textContent = viewTitleMap[viewId] || "Portal";

    // Load data specific to each view when visited
    if (viewId === "dashboard-view") {
        loadDashboardStats();
        loadTestHistory();
    } else if (viewId === "companies-view") {
        loadCompanies();
    } else if (viewId === "roadmap-view") {
        loadRoadmap();
    } else if (viewId === "aptitude-view") {
        loadAptitudePractice("QUANTITATIVE");
    } else if (viewId === "coding-view") {
        loadCodingQuestions();
    } else if (viewId === "resources-view") {
        document.getElementById("resource-topic-select").value = "ALL";
        loadResources("ALL");
    } else if (viewId === "admin-view") {
        loadAdminConsole();
    }
}

// ===================================================================
// USER PROGRESS DATA FETCHING
// ===================================================================

/**
 * Load completed items from database into local memory list.
 */
function loadUserProgress() {
    return fetch("/api/progress/completed-items")
        .then(res => res.json())
        .then(data => {
            completedItems = data; // stores array of { itemType, itemKey }
        })
        .catch(err => console.error("Error loading user progress:", err));
}

/**
 * Checks if a specific item is marked completed in local memory.
 */
function isItemCompleted(type, key) {
    return completedItems.some(item => 
        item.itemType === type && item.itemKey.toString().toLowerCase() === key.toString().toLowerCase()
    );
}

// ===================================================================
// DASHBOARD VIEW LOGIC
// ===================================================================

/**
 * Load dashboard metrics (percentages, counts) from backend.
 */
function loadDashboardStats() {
    fetch("/api/progress/dashboard")
        .then(res => res.json())
        .then(stats => {
            // Update stats cards
            document.getElementById("stat-roadmap").textContent = stats.roadmapCompletion + "%";
            document.getElementById("stat-tests").textContent = stats.aptitudeTestsCompleted;
            document.getElementById("stat-coding").textContent = stats.codingQuestionsSolved;
            document.getElementById("stat-score").textContent = stats.averageScore + "%";

            // Update details subtitle on roadmap card
            if (stats.totalTopicsCount > 0) {
                document.getElementById("stat-roadmap-sub").textContent = 
                    `${stats.completedTopicsCount} of ${stats.totalTopicsCount} topics cleared`;
            } else {
                document.getElementById("stat-roadmap-sub").textContent = "Select a company first";
            }
        })
        .catch(err => console.error("Error loading dashboard stats:", err));
}

/**
 * Load list of previous mock test results.
 */
function loadTestHistory() {
    fetch("/api/aptitude/results")
        .then(res => res.json())
        .then(results => {
            const tbody = document.getElementById("test-history-rows");
            if (results.length === 0) {
                tbody.innerHTML = `<tr><td colspan="4" class="text-center">No tests completed yet.</td></tr>`;
                return;
            }

            let rowsHtml = "";
            results.forEach(res => {
                const date = new Date(res.completedAt).toLocaleString();
                const scoreColor = res.percentage >= 60 ? "text-green" : "text-danger";
                
                rowsHtml += `
                    <tr>
                        <td>${date}</td>
                        <td>${res.score}</td>
                        <td>${res.totalQuestions}</td>
                        <td class="${scoreColor} font-weight-bold">${res.percentage}%</td>
                    </tr>
                `;
            });
            tbody.innerHTML = rowsHtml;
        })
        .catch(err => console.error("Error loading test history:", err));
}

// ===================================================================
// COMPANY SELECTION MODULE
// ===================================================================

/**
 * Load companies and display them in a grid.
 */
function loadCompanies() {
    fetch("/api/companies")
        .then(res => res.json())
        .then(companies => {
            const grid = document.getElementById("company-grid");
            let gridHtml = "";

            companies.forEach(company => {
                const isActive = currentUser.selectedCompanyId === company.id;
                const activeClass = isActive ? "selected-active" : "";
                const btnText = isActive ? "✓ Target Set" : "Select Company";
                const btnClass = isActive ? "btn-success" : "btn-primary";

                gridHtml += `
                    <div class="company-card ${activeClass}" onclick="viewCompanyProfile(${company.id})">
                        <div class="company-logo-placeholder">🏢</div>
                        <h3>${company.name}</h3>
                        <p>${company.description.substring(0, 80)}...</p>
                        <button class="btn ${btnClass} company-card-btn" onclick="handleSelectCompany(event, ${company.id})">
                            ${btnText}
                        </button>
                    </div>
                `;
            });
            grid.innerHTML = gridHtml;
        })
        .catch(err => console.error("Error loading companies:", err));
}

/**
 * Sends a request to select a company for study.
 */
function handleSelectCompany(event, companyId) {
    event.stopPropagation(); // Avoid triggering card click

    fetch("/api/companies/select", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ companyId })
    })
    .then(res => {
        if (res.ok) return res.json();
        throw new Error("Selection failed");
    })
    .then(user => {
        currentUser = user;
        showToast("Target company updated successfully!", "success");
        
        // Refresh local views
        loadCompanies();
        loadActiveCompanyDetails();
        loadDashboardStats();
    })
    .catch(err => showToast(err.message, "error"));
}

/**
 * Fetch and show active company label at top header bar.
 */
function loadActiveCompanyDetails() {
    const badge = document.getElementById("active-company-badge");
    if (!currentUser.selectedCompanyId) {
        badge.textContent = "No Company Selected";
        badge.className = "badge badge-danger";
        activeCompany = null;
        return Promise.resolve();
    }

    return fetch(`/api/companies/${currentUser.selectedCompanyId}`)
        .then(res => res.json())
        .then(company => {
            activeCompany = company;
            badge.textContent = "Target: " + company.name;
            badge.className = "badge badge-success";
        })
        .catch(err => console.error("Error loading active company:", err));
}

/**
 * Shows company topics details below the grid when a company card is clicked.
 */
function viewCompanyProfile(companyId) {
    fetch(`/api/companies/${companyId}`)
        .then(res => res.json())
        .then(company => {
            document.getElementById("profile-company-name").textContent = company.name + " Placement Guide";
            document.getElementById("profile-company-desc").textContent = company.description;
            
            // Show details panel
            document.getElementById("company-profile-panel").classList.remove("hidden");

            // Fetch topics
            fetch(`/api/companies/${companyId}/topics`)
                .then(res => res.json())
                .then(topics => {
                    const aptList = document.getElementById("profile-aptitude-topics");
                    const codList = document.getElementById("profile-coding-topics");
                    const intList = document.getElementById("profile-interview-topics");

                    // Clear old entries
                    aptList.innerHTML = "";
                    codList.innerHTML = "";
                    intList.innerHTML = "";

                    topics.forEach(topic => {
                        const li = `<li><span class="topic-bullet">✓</span> ${topic.topicName}</li>`;
                        if (topic.category === "APTITUDE") {
                            aptList.innerHTML += li;
                        } else if (topic.category === "CODING") {
                            codList.innerHTML += li;
                        } else if (topic.category === "INTERVIEW") {
                            intList.innerHTML += li;
                        }
                    });

                    // If empty, show fallback
                    if (aptList.innerHTML === "") aptList.innerHTML = "<li>General Quantitative</li>";
                    if (codList.innerHTML === "") codList.innerHTML = "<li>Basic Array Operations</li>";
                    if (intList.innerHTML === "") intList.innerHTML = "<li>OOP Concepts</li>";
                });
        });
}

// ===================================================================
// LEARNING ROADMAP MODULE
// ===================================================================

/**
 * Load the roadmap timeline for the selected company.
 */
function loadRoadmap() {
    const timeline = document.getElementById("roadmap-timeline");
    const placeholder = document.getElementById("roadmap-placeholder");

    // If no company selected, ask to select one
    if (!currentUser.selectedCompanyId) {
        timeline.classList.add("hidden");
        placeholder.classList.remove("hidden");
        return;
    }

    placeholder.classList.add("hidden");
    timeline.classList.remove("hidden");

    // Fetch roadmap steps
    fetch(`/api/companies/${currentUser.selectedCompanyId}/roadmap`)
        .then(res => res.json())
        .then(roadmapItems => {
            if (roadmapItems.length === 0) {
                timeline.innerHTML = `<div class="alert alert-info text-center">No weekly roadmap scheduled for this company yet. Admins can add one!</div>`;
                return;
            }

            let timelineHtml = "";
            roadmapItems.forEach(item => {
                // Check if this topic has been completed by the user
                const isCompleted = isItemCompleted("TOPIC", item.topicName);
                const completedClass = isCompleted ? "completed-week" : "";
                const checkedAttr = isCompleted ? "checked" : "";

                timelineHtml += `
                    <div class="timeline-item ${completedClass}">
                        <div class="timeline-badge">${item.weekNumber}</div>
                        <div class="timeline-card">
                            <h3>Week ${item.weekNumber}: ${item.topicName}</h3>
                            <p>${item.description}</p>
                            <label class="roadmap-completion-check">
                                <input type="checkbox" ${checkedAttr} onchange="handleTopicToggle(this, '${item.topicName}')">
                                Mark Week ${item.weekNumber} Complete
                            </label>
                        </div>
                    </div>
                `;
            });
            timeline.innerHTML = timelineHtml;
        })
        .catch(err => console.error("Error loading roadmap:", err));
}

/**
 * Toggle topic completion when student checks/unchecks roadmap checkbox.
 */
function handleTopicToggle(checkbox, topicName) {
    const isChecked = checkbox.checked;
    const url = isChecked ? "/api/progress/complete" : "/api/progress/incomplete";

    fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ itemType: "TOPIC", itemKey: topicName })
    })
    .then(res => {
        if (res.ok) {
            showToast(isChecked ? `Marked "${topicName}" as completed!` : `Marked "${topicName}" as incomplete`, "info");
            
            // Reload local progress records and update stats
            loadUserProgress().then(() => {
                loadRoadmap();
                loadDashboardStats();
            });
        } else {
            checkbox.checked = !isChecked; // Revert checkbox state
            showToast("Failed to update progress.", "error");
        }
    });
}

// ===================================================================
// APTITUDE PRACTICE MODULE
// ===================================================================

/**
 * Load practice questions under selected category (Quantitative, Logical, Verbal).
 */
function loadAptitudePractice(category, buttonElement) {
    // Highlight active category tab
    if (buttonElement) {
        const btns = document.querySelectorAll(".practice-categories .btn");
        btns.forEach(b => b.classList.remove("active"));
        buttonElement.classList.add("active");
    }

    fetch(`/api/aptitude/questions?category=${category}`)
        .then(res => res.json())
        .then(questions => {
            practiceQuestions = questions;
            currentPracticeIndex = 0;
            
            const card = document.getElementById("practice-card");
            const empty = document.getElementById("practice-empty");

            if (questions.length === 0) {
                card.classList.add("hidden");
                empty.classList.remove("hidden");
                return;
            }

            empty.classList.add("hidden");
            card.classList.remove("hidden");
            
            showPracticeQuestion(0);
        })
        .catch(err => console.error("Error loading practice questions:", err));
}

/**
 * Display the practice question at the specified index.
 */
function showPracticeQuestion(index) {
    if (index < 0 || index >= practiceQuestions.length) return;

    currentPracticeIndex = index;
    const question = practiceQuestions[index];

    // Update UI elements
    document.getElementById("practice-num").textContent = `Question ${index + 1} of ${practiceQuestions.length}`;
    document.getElementById("practice-category").textContent = question.category;
    document.getElementById("practice-question-text").textContent = question.questionText;
    document.getElementById("opt-practice-a").textContent = question.optionA;
    document.getElementById("opt-practice-b").textContent = question.optionB;
    document.getElementById("opt-practice-c").textContent = question.optionC;
    document.getElementById("opt-practice-d").textContent = question.optionD;

    // Reset radio buttons selection
    const radios = document.getElementsByName("practice-option");
    radios.forEach(r => {
        r.checked = false;
        r.disabled = false;
    });

    // Hide feedback panel
    document.getElementById("practice-feedback").classList.add("hidden");

    // Update navigation button states
    document.getElementById("practice-prev-btn").disabled = (index === 0);
    document.getElementById("practice-next-btn").disabled = (index === practiceQuestions.length - 1);
    document.getElementById("practice-submit-btn").disabled = true; // disable until choice selected
    
    practiceSelectedAnswer = null;
}

/**
 * Checks if the student selected an option, enabling the submit button.
 */
function enablePracticeSubmit() {
    document.getElementById("practice-submit-btn").disabled = false;
}

/**
 * Evaluate submitted practice MCQ answer and show explanations.
 */
function submitPracticeAnswer() {
    const radios = document.getElementsByName("practice-option");
    let selected = null;
    
    radios.forEach(r => {
        if (r.checked) selected = r.value;
        r.disabled = true; // lock options after submit
    });

    if (!selected) return;

    const question = practiceQuestions[currentPracticeIndex];
    const isCorrect = (selected.toUpperCase() === question.correctOption.toUpperCase());

    const feedbackPanel = document.getElementById("practice-feedback");
    const feedbackTitle = document.getElementById("feedback-status");
    const feedbackDesc = document.getElementById("practice-explanation");

    if (isCorrect) {
        feedbackPanel.classList.remove("incorrect");
        feedbackTitle.textContent = "✓ Correct Answer!";
    } else {
        feedbackPanel.classList.add("incorrect");
        feedbackTitle.textContent = `✗ Incorrect. Correct Option is ${question.correctOption}`;
    }

    feedbackDesc.textContent = question.explanation || "No explanation details provided.";
    feedbackPanel.classList.remove("hidden");

    // Disable the check answer button now that it's submitted
    document.getElementById("practice-submit-btn").disabled = true;
}

/**
 * Move to previous/next question.
 */
function navigatePractice(direction) {
    const newIndex = currentPracticeIndex + direction;
    showPracticeQuestion(newIndex);
}

// ===================================================================
// APTITUDE MOCK TEST MODULE
// ===================================================================

/**
 * Enter assessment mode. Fetches random mock questions and starts countdown timer.
 */
function startMockTest() {
    fetch("/api/aptitude/mock")
        .then(res => res.json())
        .then(questions => {
            if (questions.length === 0) {
                showToast("No aptitude questions available in database to start mock test.", "error");
                return;
            }

            mockQuestions = questions;
            currentMockIndex = 0;
            mockAnswers = {};
            mockTimeRemaining = 600; // Reset timer to 10 minutes (600 seconds)

            // Hide start landing panel and show active test board
            document.getElementById("mock-start-panel").classList.add("hidden");
            document.getElementById("mock-result-panel").classList.add("hidden");
            document.getElementById("mock-test-panel").classList.remove("hidden");

            // Display first question
            showMockQuestion(0);

            // Start countdown interval
            mockTimerInterval = setInterval(updateMockTimer, 1000);
        })
        .catch(err => console.error("Error loading mock test questions:", err));
}

/**
 * Display current mock question in active test view.
 */
function showMockQuestion(index) {
    if (index < 0 || index >= mockQuestions.length) return;

    currentMockIndex = index;
    const question = mockQuestions[index];

    // Update numbers
    document.getElementById("test-progress-num").textContent = `Question ${index + 1} of ${mockQuestions.length}`;
    
    // Update progress bar width
    const percentage = ((index + 1) / mockQuestions.length) * 100;
    document.getElementById("test-progress-bar").style.width = percentage + "%";

    // Set texts
    document.getElementById("mock-question-text").textContent = question.questionText;
    document.getElementById("opt-mock-a").textContent = question.optionA;
    document.getElementById("opt-mock-b").textContent = question.optionB;
    document.getElementById("opt-mock-c").textContent = question.optionC;
    document.getElementById("opt-mock-d").textContent = question.optionD;

    // Check previously answered option if it exists
    const savedOption = mockAnswers[question.id];
    const radios = document.getElementsByName("mock-option");
    radios.forEach(r => {
        r.checked = (savedOption === r.value);
    });

    // Handle navigation button states
    document.getElementById("mock-prev-btn").disabled = (index === 0);
    document.getElementById("mock-next-btn").disabled = (index === mockQuestions.length - 1);
}

/**
 * Save selected option for active question in local answers dictionary.
 */
function saveMockAnswer() {
    const radios = document.getElementsByName("mock-option");
    const question = mockQuestions[currentMockIndex];
    
    radios.forEach(r => {
        if (r.checked) {
            mockAnswers[question.id] = r.value;
        }
    });
}

/**
 * Navigate through mock assessment questions.
 */
function navigateMock(direction) {
    const newIndex = currentMockIndex + direction;
    showMockQuestion(newIndex);
}

/**
 * Tick-down timer calculation.
 */
function updateMockTimer() {
    mockTimeRemaining--;

    if (mockTimeRemaining <= 0) {
        // Time expired! Auto-submit the test.
        clearInterval(mockTimerInterval);
        mockTimerInterval = null;
        showToast("Time is up! Auto-submitting your test results...", "warning");
        submitMockTestResults();
        return;
    }

    const minutes = Math.floor(mockTimeRemaining / 60);
    const seconds = mockTimeRemaining % 60;
    
    const formattedMinutes = minutes < 10 ? "0" + minutes : minutes;
    const formattedSeconds = seconds < 10 ? "0" + seconds : seconds;

    const timerSpan = document.getElementById("test-timer");
    timerSpan.textContent = `${formattedMinutes}:${formattedSeconds}`;

    // Color timer red if less than 1 minute remains
    if (mockTimeRemaining < 60) {
        timerSpan.className = "text-danger";
    } else {
        timerSpan.className = "";
    }
}

/**
 * Prompts student before manual mock test submission.
 */
function confirmSubmitMock() {
    const answeredCount = Object.keys(mockAnswers).length;
    const totalCount = mockQuestions.length;

    const confirmSub = confirm(`You have answered ${answeredCount} out of ${totalCount} questions. Are you sure you want to submit?`);
    if (confirmSub) {
        submitMockTestResults();
    }
}

/**
 * POST answers to backend to score and save test.
 */
function submitMockTestResults() {
    // Clear timer
    if (mockTimerInterval) {
        clearInterval(mockTimerInterval);
        mockTimerInterval = null;
    }

    fetch("/api/aptitude/mock/submit", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(mockAnswers)
    })
    .then(res => {
        if (res.ok) return res.json();
        throw new Error("Failed to submit test results");
    })
    .then(result => {
        // Show result stats card
        document.getElementById("res-score").textContent = `${result.score}/${result.totalQuestions}`;
        document.getElementById("res-percent").textContent = `${result.percentage}%`;

        document.getElementById("mock-test-panel").classList.add("hidden");
        document.getElementById("mock-result-panel").classList.remove("hidden");

        // Reload data to reflect new scores
        loadUserProgress();
        loadDashboardStats();
    })
    .catch(err => showToast(err.message, "error"));
}

/**
 * Return to start view after mock results review.
 */
function resetMockView() {
    // Stop timers if active
    if (mockTimerInterval) {
        clearInterval(mockTimerInterval);
        mockTimerInterval = null;
    }
    
    document.getElementById("mock-result-panel").classList.add("hidden");
    document.getElementById("mock-test-panel").classList.add("hidden");
    document.getElementById("mock-start-panel").classList.remove("hidden");
}

// ===================================================================
// CODING PRACTICE MODULE
// ===================================================================

/**
 * Fetch and load coding list.
 */
function loadCodingQuestions() {
    fetch("/api/coding/questions")
        .then(res => res.json())
        .then(questions => {
            codingQuestions = questions;
            renderCodingTable("ALL");
        })
        .catch(err => console.error("Error loading coding questions:", err));
}

/**
 * Render table rows filtering by difficulty level.
 */
function renderCodingTable(level) {
    const tbody = document.getElementById("coding-table-rows");
    
    // Filter list
    const filtered = codingQuestions.filter(q => 
        level === "ALL" || q.difficulty.toUpperCase() === level.toUpperCase()
    );

    if (filtered.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4" class="text-center">No coding questions available for this level.</td></tr>`;
        return;
    }

    let rowsHtml = "";
    filtered.forEach(q => {
        // Check if completed
        const isCompleted = isItemCompleted("CODING", q.id);
        const statusIcon = isCompleted ? "✅ Completed" : "⏳ Pending";
        const badgeClass = q.difficulty === "BEGINNER" ? "badge-success" : 
                           q.difficulty === "INTERMEDIATE" ? "badge-warning" : "badge-danger";

        rowsHtml += `
            <tr>
                <td>${statusIcon}</td>
                <td><strong>${q.title}</strong></td>
                <td><span class="badge ${badgeClass}">${q.difficulty}</span></td>
                <td>
                    <button class="btn btn-secondary btn-block" onclick="openCodingModal(${q.id})">
                        View Details
                    </button>
                </td>
            </tr>
        `;
    });
    tbody.innerHTML = rowsHtml;
}

/**
 * Filter problems inside view tab clicks.
 */
function filterCodingQuestions(level, buttonElement) {
    // Highlight tab
    const tabs = document.querySelectorAll(".coding-tabs button");
    tabs.forEach(t => t.classList.remove("active"));
    buttonElement.classList.add("active");

    renderCodingTable(level);
}

/**
 * Opens dialog showing coding problem descriptions and Java solution code blocks.
 */
function openCodingModal(id) {
    const question = codingQuestions.find(q => q.id === id);
    if (!question) return;

    activeCodingQuestion = question;

    // Load information into modal fields
    document.getElementById("modal-title").textContent = question.title;
    document.getElementById("modal-statement").textContent = question.problemStatement;
    document.getElementById("modal-input").textContent = question.sampleInput || "None";
    document.getElementById("modal-output").textContent = question.sampleOutput || "None";
    document.getElementById("modal-solution").textContent = question.solutionCode || "// Code solution not available";
    document.getElementById("modal-explanation").textContent = question.explanation || "No explanation provided.";

    const diffBadge = document.getElementById("modal-diff");
    diffBadge.textContent = question.difficulty;
    diffBadge.className = "badge " + (
        question.difficulty === "BEGINNER" ? "badge-success" : 
        question.difficulty === "INTERMEDIATE" ? "badge-warning" : "badge-danger"
    );

    // Set completed status checkbox in modal footer
    const isCompleted = isItemCompleted("CODING", question.id);
    document.getElementById("modal-complete-check").checked = isCompleted;

    // Show native dialog modal
    document.getElementById("coding-modal").showModal();
}

/**
 * Closes modal dialogue.
 */
function closeCodingModal() {
    document.getElementById("coding-modal").close();
    activeCodingQuestion = null;
}

/**
 * Handles completing/uncompleting coding problems from modal checkbox.
 */
function toggleCodingQuestionProgress() {
    if (!activeCodingQuestion) return;

    const checkbox = document.getElementById("modal-complete-check");
    const isChecked = checkbox.checked;
    const url = isChecked ? "/api/progress/complete" : "/api/progress/incomplete";

    fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ itemType: "CODING", itemKey: activeCodingQuestion.id.toString() })
    })
    .then(res => {
        if (res.ok) {
            showToast(isChecked ? "Problem marked as completed!" : "Problem marked as pending", "info");
            
            // Reload user progress and coding lists in background
            loadUserProgress().then(() => {
                loadCodingQuestions();
                loadDashboardStats();
            });
        } else {
            checkbox.checked = !isChecked; // Revert checkbox state
            showToast("Failed to save progress.", "error");
        }
    });
}

// ===================================================================
// RESOURCE RECOMMENDATIONS MODULE
// ===================================================================

/**
 * Loads recommended resources. TopicName can be 'ALL' or topic tags.
 */
function loadResources(topicName) {
    let url = "/api/resources";
    if (topicName && topicName !== "ALL") {
        url = `/api/resources?topic=${topicName}`;
    }

    fetch(url)
        .then(res => res.json())
        .then(resources => {
            const list = document.getElementById("resources-list");
            list.innerHTML = "";

            if (resources.length === 0) {
                list.innerHTML = `<div class="alert alert-info text-center" style="grid-column: 1/-1;">No study resources available for this topic yet.</div>`;
                return;
            }

            resources.forEach(res => {
                const typeIcon = res.resourceType === "VIDEO" ? "🎥 Video Lecture" : 
                                 res.resourceType === "NOTES" ? "📄 Study Notes" : "📝 Practice Problems";
                const typeBadgeClass = res.resourceType === "VIDEO" ? "badge-warning" : 
                                      res.resourceType === "NOTES" ? "badge-success" : "badge-danger";

                list.innerHTML += `
                    <div class="resource-card">
                        <span class="badge ${typeBadgeClass}">${typeIcon}</span>
                        <h3>${res.title}</h3>
                        <p class="resource-meta">Topic: <strong>${res.topicName}</strong></p>
                        <a href="${res.url}" target="_blank" class="btn btn-primary margin-top-md">
                            Open Resource 🔗
                        </a>
                    </div>
                `;
            });
        })
        .catch(err => console.error("Error loading resources:", err));
}

// ===================================================================
// ADMINISTRATOR CONSOLE MODULE
// ===================================================================

/**
 * Load admin portal metrics and registered student statistics.
 */
function loadAdminConsole() {
    fetch("/api/admin/stats")
        .then(res => {
            if (res.ok) return res.json();
            throw new Error("Admin access denied");
        })
        .then(stats => {
            // Update admin summary cards
            document.getElementById("admin-stat-students").textContent = stats.totalStudents;
            document.getElementById("admin-stat-tests").textContent = stats.totalTestsTaken;
            document.getElementById("admin-stat-companies").textContent = stats.totalCompanies;

            // Load student scores list
            const tbody = document.getElementById("admin-students-rows");
            let studentHtml = "";

            if (stats.students.length === 0) {
                tbody.innerHTML = `<tr><td colspan="4" class="text-center">No student accounts registered yet.</td></tr>`;
                return;
            }

            stats.students.forEach(student => {
                const scoreColor = student.averageScore >= 60 ? "text-green" : "text-danger";
                studentHtml += `
                    <tr>
                        <td>#${student.id}</td>
                        <td><strong>${student.username}</strong></td>
                        <td>${student.testsCompleted}</td>
                        <td class="${scoreColor} font-weight-bold">${student.averageScore}%</td>
                    </tr>
                `;
            });
            tbody.innerHTML = studentHtml;
        })
        .catch(err => {
            showToast("Access forbidden. Students cannot enter Admin Console.", "error");
            switchView("dashboard-view"); // Kick back to student dashboard
        });
}

/**
 * Controls switching data entry forms.
 */
function switchAdminForm(sectionId, buttonElement) {
    // Highlight forms tab
    const btns = document.querySelectorAll(".form-tabs button");
    btns.forEach(b => b.classList.remove("active"));
    buttonElement.classList.add("active");

    // Hide forms
    const forms = document.querySelectorAll(".admin-form-section");
    forms.forEach(f => f.classList.add("hidden"));

    // Show form
    document.getElementById(sectionId).classList.remove("hidden");
}

/**
 * Admin action: Save a company to database.
 */
function handleAdminAddCompany(event) {
    event.preventDefault();

    const name = document.getElementById("admin-comp-name").value;
    const description = document.getElementById("admin-comp-desc").value;

    fetch("/api/admin/companies", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name, description })
    })
    .then(res => {
        if (res.ok) return res.json();
        throw new Error("Failed to save company");
    })
    .then(company => {
        showToast(`Successfully added company "${company.name}"!`, "success");
        // Clear fields
        document.getElementById("admin-comp-name").value = "";
        document.getElementById("admin-comp-desc").value = "";
        loadAdminConsole();
    })
    .catch(err => showToast(err.message, "error"));
}

/**
 * Admin action: Save aptitude question to database.
 */
function handleAdminAddAptitude(event) {
    event.preventDefault();

    const category = document.getElementById("admin-apt-cat").value;
    const questionText = document.getElementById("admin-apt-text").value;
    const optionA = document.getElementById("admin-apt-a").value;
    const optionB = document.getElementById("admin-apt-b").value;
    const optionC = document.getElementById("admin-apt-c").value;
    const optionD = document.getElementById("admin-apt-d").value;
    const correctOption = document.getElementById("admin-apt-correct").value;
    const explanation = document.getElementById("admin-apt-exp").value;

    const newQuestion = {
        category, questionText, optionA, optionB, optionC, optionD, correctOption, explanation
    };

    fetch("/api/admin/aptitude", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newQuestion)
    })
    .then(res => {
        if (res.ok) return res.json();
        throw new Error("Failed to save question");
    })
    .then(() => {
        showToast("Aptitude practice question added successfully!", "success");
        // Clear fields
        document.getElementById("admin-apt-text").value = "";
        document.getElementById("admin-apt-a").value = "";
        document.getElementById("admin-apt-b").value = "";
        document.getElementById("admin-apt-c").value = "";
        document.getElementById("admin-apt-d").value = "";
        document.getElementById("admin-apt-exp").value = "";
        loadAdminConsole();
    })
    .catch(err => showToast(err.message, "error"));
}

/**
 * Admin action: Save coding question to database.
 */
function handleAdminAddCoding(event) {
    event.preventDefault();

    const title = document.getElementById("admin-code-title").value;
    const difficulty = document.getElementById("admin-code-diff").value;
    const problemStatement = document.getElementById("admin-code-stmt").value;
    const sampleInput = document.getElementById("admin-code-input").value;
    const sampleOutput = document.getElementById("admin-code-output").value;
    const solutionCode = document.getElementById("admin-code-sol").value;
    const explanation = document.getElementById("admin-code-exp").value;

    const newProblem = {
        title, difficulty, problemStatement, sampleInput, sampleOutput, solutionCode, explanation
    };

    fetch("/api/admin/coding", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newProblem)
    })
    .then(res => {
        if (res.ok) return res.json();
        throw new Error("Failed to save coding problem");
    })
    .then(() => {
        showToast("Coding practice problem added successfully!", "success");
        // Clear fields
        document.getElementById("admin-code-title").value = "";
        document.getElementById("admin-code-stmt").value = "";
        document.getElementById("admin-code-input").value = "";
        document.getElementById("admin-code-output").value = "";
        document.getElementById("admin-code-sol").value = "";
        document.getElementById("admin-code-exp").value = "";
        loadAdminConsole();
    })
    .catch(err => showToast(err.message, "error"));
}

/**
 * Admin action: Save resource to database.
 */
function handleAdminAddResource(event) {
    event.preventDefault();

    const topicName = document.getElementById("admin-res-topic").value;
    const title = document.getElementById("admin-res-title").value;
    const resourceType = document.getElementById("admin-res-type").value;
    const url = document.getElementById("admin-res-url").value;

    const newResource = { topicName, title, resourceType, url };

    fetch("/api/admin/resources", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newResource)
    })
    .then(res => {
        if (res.ok) return res.json();
        throw new Error("Failed to save resource");
    })
    .then(() => {
        showToast("Study resource recommendation saved successfully!", "success");
        // Clear fields
        document.getElementById("admin-res-topic").value = "";
        document.getElementById("admin-res-title").value = "";
        document.getElementById("admin-res-url").value = "";
        loadAdminConsole();
    })
    .catch(err => showToast(err.message, "error"));
}

// ===================================================================
// SYSTEM TOAST NOTIFICATIONS HELPER
// ===================================================================

/**
 * Show a floating temporary alert message on screen.
 * Type options: 'success', 'error', 'info', 'warning'
 */
function showToast(message, type = "info") {
    const toast = document.getElementById("toast");
    toast.textContent = message;
    
    // Reset classes
    toast.className = `toast ${type}`;
    
    // Make visible
    toast.classList.remove("hidden");

    // Remove notification after 3.5 seconds
    setTimeout(() => {
        toast.classList.add("hidden");
    }, 3500);
}
