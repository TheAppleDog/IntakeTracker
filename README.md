<h2>Intake Tracker App</h2>
<p><strong>Admin username:</strong> ADMIN</p>
<p><strong>Admin password:</strong> appledog2002</p>
<br>
<p><strong>NOTE:</strong> Before changing dependencies versions, copy them somewhere as it may or may not affect the project. Also, use the latest Android Studio version whenever there is an update. Use Pixel 5 API 22.</p>
<p>Make font size on your physical device smallest and screen zoom fully to 0 in settings. If texts are still overlapping, you may need to adjust XML margins, paddings, etc.</p>
<p>Remove notification-related features if you don't want. You can use it, but try with a database as mine was not successful as I used static.</p>
<br>
<p><strong>Before Using Google Sign-In Option, Follow These Steps:</strong></p>
<ol>
  <li>Clone the Repo: Get the project code from your repository.</li>
  <li>Open Project in Android Studio: Open the project in Android Studio.</li>
  <li>Get SHA-1 Fingerprint: Use Android Studio to find the SHA-1 fingerprint (Gradle tab -> expand android -> double click signing report).</li>
  <li>Create Project in Google Console: Make a new project on the Google Cloud Console.</li>
  <li>Enable Google Sign-In: Turn on Google Sign-In for your project in the Google Console.</li>
  <li>Add SHA-1 to Google Console: Paste the SHA-1 fingerprint into your project's settings in the Google Console. Also download client id(JSON file).</li>
  <li>Save Changes: Save the changes in the Google Console.</li>
</ol>
<p>That's it! With these steps, Google Sign-In should work when testing the app locally.</p>
<br>
<p><strong>Thank You, Hope you like this project and learn something.</strong></p>

<hr>

<h3>Description:</h3>
<p>This comprehensive health and fitness app offers a robust set of features to help users maintain a healthy lifestyle. With a focus on user authentication, profile setup, and activity tracking, it provides a seamless experience for individuals looking to monitor their habits effectively. The app includes intuitive features for logging food intake, exercise sessions, and water consumption, empowering users to track their progress with ease.</p>

<h3>Key Features:</h3>
<ul>
  <li>User authentication and profile setup.</li>
  <li>Activity tracking for exercise sessions and water consumption.</li>
  <li>Logging feature for food intake with a diverse recipe library for nutritious meal options.</li>
  <li>Health calculators for BMI (Body Mass Index) and daily calorie needs.</li>
</ul>

<h3>Usage:</h3>
<p>To get started, users can create an account and set up their profiles. They can then log their food intake, exercise sessions, and water consumption using the intuitive interface. The app provides access to a rich library of nutritious meal options and health calculators for personalized insights into their fitness journey.</p>

<h3>Technologies Used:</h3>
<p><strong>Backend:</strong> SQLite (Android Studio)</p>
<p><strong>Frontend:</strong> Kotlin (Android Studio)</p>
<p><strong>Database:</strong> DB Browser SQLite or install SimpleSQLiteBrowser plugin in Android Studio itself</p>

<h3>Admin Access:</h3>
<p>The platform also includes login access for administrators, allowing seamless management of food, exercise, and recipes to ensure a smooth user experience.</p>
