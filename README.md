Project Name: KotlinQuiz

Description: KotlinQuiz is an engaging and interactive quiz application designed to test users' knowledge through a timed quiz format. The app leverages Firebase for secure authentication and user management, offering two methods for users to sign in or register: Google Authentication and Gmail/Password Authentication.
KotlinQuiz offers a seamless and intuitive user experience, ensuring both engagement and competitive fun through its time-bound quiz challenges. Perfect for users looking to test their knowledge and compete for a spot on the leaderboard.

Features:

User Authentication: Users can sign in using Google Authentication.
Alternatively, users can register and sign in using their Gmail and Password.
Both authentication methods are supported and managed by Firebase for secure and reliable access.

Home Screen: Upon successful sign-in or registration, users are directed to the Home Screen.
The Home Screen features a menu with the following options:

Profile Settings: Allows users to manage their profile details.
Leaderboard: Displays the top five highest-scoring users.
Sign Out: Users can securely sign out of their account.
To start the quiz, users must enter their name.

Quiz Format: The quiz consists of 10 questions that must be answered within a 1-minute timeframe.
The timer starts as soon as the user begins the quiz.
Users must select one of the four provided answers for each question.
If the timer runs out before all questions are answered, then quiz is automatically submitted and user has to navigate to see result.
Users can submit the quiz upon completing all questions if done before the time runs out.
Navigation between questions is possible by sliding the screen left or right, or by clicking navigation buttons.

Result Screen: The result screen displays the user's highest score.
Only the top five user scores are shown on the leaderboard.
Users can return to the Home Screen to start a new quiz or sign out from the app.

Technologies Used:

Kotlin: Primary programming language for app development.

Firebase: For authentication and user management.

Android SDK: For building and deploying the Android application.

Usage Flow:
1. User signs in or registers using Google Authentication or Gmail/Password.

Note:  .If the user first register with Gmail/Password and then SignIn with Google then both provider linked together.

.Otherwise if the user SignIn with Google first then user can not register with that Gmail

![IMG_20240523_213524](https://github.com/ArpitAswal/KotlinQuizApp/assets/87036588/d755fbf1-b6a9-4f57-9214-a71c82ad2b1c)
![Screenshot_2024-05-23-19-52-55-997_com example quizapp](https://github.com/ArpitAswal/KotlinQuizApp/assets/87036588/ff082e02-3e5d-44d3-8396-2c46232e4dea)

2. User is directed to the Home Screen where user can start the Quiz and menu option( Profile Setting, Leaderboard, SignOut)

 ![Screenshot_2024-05-24-22-53-28-747_com example quizapp](https://github.com/ArpitAswal/KotlinQuizApp/assets/87036588/4b8f1828-0333-4b9b-ac97-bbbeda39319b)
 
3. In Profile setting screen a user can set his name, password and profile image

![Screenshot_2024-05-24-22-53-28-747_com example quizapp](https://github.com/ArpitAswal/KotlinQuizApp/assets/87036588/b6e4bfd3-2745-4bd3-a78c-fd53a1513e9f)

4. After starting quiz, user has to answers 10 questions within 1 minute, navigating between questions as needed.

![Screenshot_2024-05-23-21-32-31-130_com example quizapp](https://github.com/ArpitAswal/KotlinQuizApp/assets/87036588/557e5462-3698-4ab1-ba87-2b767a480b56)

![Screenshot_2024-05-23-21-32-34-992_com example quizapp](https://github.com/ArpitAswal/KotlinQuizApp/assets/87036588/362bb878-4419-4074-bc5f-bc374cbd94dc)

5. After submitting quiz before or after time complete results are displayed, it will showing the user's highest score. for now only top 5 highest score will display and only highest score will store and the best time of user

   ![Screenshot_2024-05-24-22-40-13-673_com example quizapp](https://github.com/ArpitAswal/KotlinQuizApp/assets/87036588/59b44980-d8f0-49ef-a46d-193f51c2b6da)
   


