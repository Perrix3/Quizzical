# Quizzical: Spaced Repetition Quiz App

[Link to Quizzical Server](https://github.com/Perrix3/Quizzical-Server/)

## Description

Quizzical is an Android application designed to help users learn and memorize information effectively through spaced repetition quizzes.  It leverages the principles of spaced repetition to optimize learning by presenting quiz questions at intervals that are dynamically adjusted based on the user's performance. This approach helps reinforce memory and makes studying more efficient.

**Key Features (Based on available information, likely to be implemented or planned):**

*   **Quiz Creation:** Users can create their own quizzes on any topic. This would likely involve defining questions and their corresponding answers.
*   **Spaced Repetition Algorithm:** The core of the app is its spaced repetition system.  This algorithm determines when to show a quiz question again based on how well the user answered it previously. Correctly answered questions are shown less frequently over time, while incorrectly answered questions are presented more often to reinforce learning.
*   **Quiz Taking:** Users can take quizzes created by themselves or potentially shared by others (depending on planned features).
*   **Progress Tracking:** The app likely tracks user progress, showing statistics on quiz performance, learning history, and areas needing more focus.
*   **User-Friendly Interface:** The provided layout XML suggests a clean and intuitive user interface with elements like a main menu, game history display, and a sliding menu for navigation.
*   **Customizable Settings:**  Potentially includes settings to adjust the spaced repetition algorithm, notification preferences, or other aspects of the user experience.
*   **Offline Functionality:**  As a study tool, offline access to quizzes and learning data would be a valuable feature.

**Likely Technologies Used (Based on Android project):**

*   **Android SDK:** Native Android development using Java or Kotlin.
*   **ConstraintLayout:** For flexible and efficient UI layouts (as seen in the provided XML).
*   **RecyclerView:** For displaying lists of game history or quiz questions efficiently.
*   **Potentially Room Persistence Library:** For local data storage of quizzes, user progress, and spaced repetition scheduling.

## To-Do List

This is a potential To-Do list for the Quizzical app, focusing on features that would enhance its functionality and user experience:

**Core Functionality & Learning Algorithm:**

*   [ ] **Implement Spaced Repetition Algorithm:**  Develop and integrate the core spaced repetition algorithm that dynamically schedules quiz question reviews.  This is the central feature of the app.
    *   [ ] Define clear intervals and scheduling logic based on user performance (e.g., different intervals for "easy," "medium," "hard" answers).
    *   [ ] Implement algorithm persistence so review schedules are maintained across app sessions.
*   [ ] **Quiz Question Management:**
    *   [ ] Implement robust question creation and editing interface.
    *   [ ] Support different question types (e.g., multiple choice, true/false, short answer - *stretch goal*).
    *   [ ] Allow users to categorize and tag quizzes for better organization.
*   [ ] **Quiz Taking Flow:**
    *   [ ] Develop a clear and engaging quiz taking interface.
    *   [ ] Implement answer submission and immediate feedback mechanisms.
    *   [ ] Track user performance on each question for the spaced repetition algorithm.

**User Interface & User Experience (UI/UX):**

*   [ ] **Sliding Menu Functionality:** Implement the sliding menu (already laid out in XML) with functional buttons for "Profile," "Stats," and "Settings."
    *   [ ] Link buttons to their respective activities/fragments.
    *   [ ] Implement menu opening and closing logic (likely using the `menuButton` and `closeButton`).
*   [ ] **Game History/Progress Display:**  Populate the `RecyclerView` (`gameHistory`) with meaningful data.
    *   [ ] Show quiz completion history, scores, and progress over time.
    *   [ ] Consider visual representations of progress (graphs, charts - *stretch goal*).
*   [ ] **Settings Screen Implementation:** Create a settings screen accessible from the sliding menu.
    *   [ ] Include settings for customizing the spaced repetition algorithm (e.g., initial intervals - *advanced*).
    *   [ ] Add notification settings for review reminders ( *stretch goal*).
    *   [ ] Implement theme selection (light/dark mode - *stretch goal*).
*   [ ] **Profile Screen Implementation:** Design and implement a user profile screen.
    *   [ ] Allow users to view their account information (if user accounts are planned).
    *   [ ] Potentially allow for profile customization (avatar - *stretch goal*).
*   [ ] **Statistics/Stats Screen Implementation:** Develop a screen to display learning statistics.
    *   [ ] Show overall quiz performance, strengths and weaknesses by topic/category.
    *   [ ] Visualizations of learning progress (graphs, charts - *stretch goal*).

**Data Storage & Persistence:**

*   [ ] **Local Data Storage:**  Implement local storage for quizzes, user data, and the spaced repetition schedule.
    *   [ ] Consider using Room Persistence Library for efficient and structured database management.
    *   [ ] Design database schema for quizzes, questions, user progress, and scheduling.
*   [ ] **Data Backup and Restore (Optional but Recommended):**
    *   [ ] Implement functionality to backup and restore user data (locally or to cloud - *stretch goal*).

**Testing & Refinement:**

*   [ ] **Unit Testing:** Write unit tests for core logic, especially the spaced repetition algorithm.
*   [ ] **UI Testing:** Implement UI tests to ensure UI elements function correctly.
*   [ ] **User Testing:** Conduct user testing to gather feedback and iterate on the app's design and usability.
*   [ ] **Performance Optimization:** Optimize the app for performance and battery efficiency, especially regarding data storage and algorithm execution.

**Future Enhancements (Stretch Goals):**

*   [ ] **Quiz Sharing/Community Features:** Allow users to share quizzes with others or access a community library of quizzes.
*   [ ] **Cloud Sync:** Implement cloud synchronization for user data to enable cross-device learning.
*   [ ] **Gamification:** Integrate gamification elements (points, badges, leaderboards) to increase user engagement.
*   [ ] **Import/Export Functionality:** Allow users to import quizzes from external sources (e.g., CSV, text files) and export their data.
*   [ ] **Advanced Spaced Repetition Algorithms:** Explore and potentially implement more sophisticated spaced repetition algorithms (like SM-2 algorithm).

This To-Do list provides a roadmap for developing Quizzical into a fully featured and effective spaced repetition quiz app. The prioritization and order of tasks can be adjusted based on development resources and project goals.
