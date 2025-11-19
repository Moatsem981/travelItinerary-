# 🌍 Travel Itinerary & Translation App (Android SDK, Java, Google ML Kit)

## Overview
This is a comprehensive Android mobile travel application designed to streamline international travel by providing on-demand, real-time linguistic assistance. The application integrates **Google ML Kit** for advanced neural translation and speech recognition across six major languages, enabling seamless user interaction and itinerary management abroad.

## ✨ Key Features

### 1. Real-Time Neural Translation & Speech Recognition
* **6-Language Support:** Implements real-time, on-device neural translation across **English, French, Spanish, German, Chinese, and Japanese**.
* **Google ML Kit Integration:** Utilizes Google's Translation API and Speech Recognition capabilities for fast, accurate, and offline-capable language processing.
* **Efficient Deployment:** Designed with a focus on efficient model download, deployment, and resource management within the mobile environment.

### 2. Intelligent Itinerary Management
* **Speech-to-Text Input:** Allows users to populate and edit travel logs, receipts, and bookings using speech input, which is converted to editable text via ML Kit.
* **NLP for Organization:** Integrates natural language processing capabilities to categorize and save user experiences into a structured travel log.
* **Dedicated Booking Views:** Features multiple screen adapters (`HotelAdapter`, `ItineraryAdapter`, etc.) to clearly display complex information like hotel reservations, flight details, and multi-step booking forms.

### 3. User Experience & Scalability
* **Backend Synchronization:** Implements a custom Firebase backend architecture for user authentication, data persistence, and real-time synchronization of booking and itinerary information.
* **Error Handling:** Features robust, user-centric error handling for network failures, model download issues, and speech recognition errors.
* **Mapping Integration:** Uses the Google Maps API for itinerary visualization and location tracking.

## 🛠️ Technologies Used
* **Platform:** Android SDK
* **Language:** Java
* **Machine Learning:** Google ML Kit (Translation API, Speech Recognition)
* **Backend:** Google Firebase
* **APIs:** Google Maps API
* **Design Patterns:** Adapter, ViewHolder (for efficient UI rendering)

## 🚀 Getting Started

### Prerequisites
* Android Studio
* Android SDK (Targeting API level 30 or higher)
* A device or emulator with Google Play Services.

### Installation and Run
1.  Clone the repository:
    ```bash
    git clone [(https://github.com/Moatsem981/travelItinerary-.git)]
    cd [travelItinerary-]
    ```
2.  Open the project in Android Studio.
3.  Configure your **Google Maps API Key** and **Firebase Connection** in the project's resource files (detailed instructions can be found in the `DOCS/SETUP_GUIDE.md` file [if you create one]).
4.  Build and run the app on your selected Android device or emulator.
