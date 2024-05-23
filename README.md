Project Name: KotlinQuiz
Description:

KotlinQuiz is an engaging and interactive quiz application designed to test users' knowledge through a timed quiz format. The app leverages Firebase for secure authentication and user management, offering two methods for users to sign in or register: Google Authentication and Gmail/Password Authentication.

Features:

User Authentication:

Users can sign in using Google Authentication.
Alternatively, users can register and sign in using their Gmail and Password.
Both authentication methods are supported and managed by Firebase for secure and reliable access.
Home Screen:

Upon successful sign-in or registration, users are directed to the Home Screen.
The Home Screen features a menu with the following options:
Profile Settings: Allows users to manage their profile details.
Leaderboard: Displays the top five highest-scoring users.
Sign Out: Users can securely sign out of their account.
To start the quiz, users must enter their name.
Quiz Format:

The quiz consists of 10 questions that must be answered within a 1-minute timeframe.
The timer starts as soon as the user begins the quiz.
Users must select one of the four provided answers for each question.
If the timer runs out before all questions are answered, the quiz is automatically submitted.
Users can submit the quiz upon completing all questions if done before the time runs out.
Navigation between questions is possible by sliding the screen left or right, or by clicking navigation buttons.
Result Screen:

The result screen displays the user's highest score.
Only the top five user scores are shown on the leaderboard.
Users can return to the Home Screen to start a new quiz or sign out from the app.
Usage Flow:

User signs in or registers using Google Authentication or Gmail/Password.
User is directed to the Home Screen and enters their name to start the quiz.
User answers 10 questions within 1 minute, navigating between questions as needed.
Quiz results are displayed, showing the user's highest score.
User can view the leaderboard, return to the Home Screen, start a new quiz, or sign out.
Technologies Used:

Kotlin: Primary programming language for app development.
Firebase: For authentication and user management.
Android SDK: For building and deploying the Android application.
KotlinQuiz offers a seamless and intuitive user experience, ensuring both engagement and competitive fun through its time-bound quiz challenges. Perfect for users looking to test their knowledge and compete for a spot on the leaderboard.
