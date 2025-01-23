
# Guardian Angel - AI Assistant

Guardian Angel is an AI-powered mobile application that automates the tasks in the Features section for users.

The application leverages the OpenAI API to parse and generate intelligent responses based on user inputs. It offers a user-friendly interface and supports both text and speech inputs.


## Features


- **Email Automation**: Type or speak your email content, and the app will generate and send an email using SMTP.  
- **Text Summarization**: Paste or dictate text, and the app provides a concise summary.  
- **Music Recommendations**: Get music suggestions based on mood, song, or artist. OpenAI parses the input and integrates with the Spotify API for playback.  
- **Task Scheduling**: Input natural language descriptions of tasks (e.g., "Schedule a meeting with Sarah tomorrow at 3 PM") and the app parses the details to schedule tasks using Google Calendar API.


## Tech Stack

### Frontend
- **Language**: Kotlin
- **Design**: Android UI Components
- **Libraries**:  
  - Retrofit  
  - Gson  
  - OkHttp  
  - Speech Recognizer (Android APIs)

### Backend
- **Framework**: Python Flask  
- **APIs**:  
  - OpenAI ChatCompletion API  
  - Spotify API (for music recommendations)  
  - Google Calendar API (for task scheduling)  
  - SMTP (for email automation)

---


## Installation

### Frontend - Kotlin (Mobile Application)

1. Clone the repository:
   ```bash
   git clone https://github.com/naijoe-srinivasan/guardian-angel-ai-assistant.git
   cd guardian-angel-ai/mobile
    ```
2. Sync the project and install all Gradle dependencies in Android Studio.
3. Configure your mobile device:
- Option A: Use USB for development
    1. Run:

    ```bash 
    adb reverse tcp:5000 tcp:5000
    ```
    2. Ensure Windows Firewall is disabled or has an inbound rule for port 5000.

- Option B: Use Wi-Fi pairing:
    1. Install ngrok
    2. Run ngrok

    ``` bash
    ngrok http 5000
    ```
    3. Replace the base URL in MainActivity (Line 191)

    ```kotlin
    .baseUrl("http://<ngrok-url>/")

    ```
    4. Build and run the application.

### Backend - Python Flask Server
1. Install dependencies:
    ```bash
    pip install -r requirements.txt
    ```
2. Run the flask Server 
    ```bash 
    python app.py
    ```
3. Ensure the backend server is running on port 5000.







## Usage

- Launch the mobile application.
- Choose to input text or record speech.
- Perform one of the following tasks:
    - Send Email: Speak or type the email content. OpenAI generates the email, and it is sent via SMTP.
    - Summarize Text: Paste or dictate the text to receive a concise summary.
    - Recommend Music: Enter a mood, artist, or song name. The app fetches a Spotify URL for the recommendation.
    - Schedule Tasks: Input a natural language task (e.g., "Schedule a meeting tomorrow at 2 PM") and the app schedules it using the Google Calendar API.

