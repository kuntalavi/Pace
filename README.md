<div align="center">

<table border="0" width="100%">
    <tr>
        <td width="30%" align="center" valign="middle">
            <img width="180" alt="pace_icon" src="https://github.com/user-attachments/assets/d3e48168-ed4d-45c7-bfa9-141450fd088e" />
        </td>
        <td width="70%" valign="middle">
            <h1>Pace</h1>
            <h3>Your Personal Running Companion</h3>
            <p><i>Precision-engineered running application designed to help you track, share and achieve your run goals.</i></p>
            <br>
        </td>
    </tr>
</table>

<br>

<p>
  <a href="https://github.com/kuntalavi/Pace/releases"><img width="200" alt="Get it on github" src="https://github.com/user-attachments/assets/4f754ea3-d626-4476-892b-a55398c99c84" /></a>
</p>

</div>

<br clear="all" />

---

## Overview
**Pace** is a minimalist, offline-first GPS running tracker designed for modern athletes. Track your runs with precision, share achievements socially, and analyze performance with beautiful visualizations—all powered by cutting-edge Android architecture.

---

## 📱 Screenshots

| Start Run | Active Run |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/52e74288-aac1-472d-b8f6-3cd6aa17708c" width="250"/> | <img src="https://github.com/user-attachments/assets/c443ad49-e6b3-49aa-a227-457c4ce6b1ca" width="250"/> |
| **Analysis** | **Social Feed** |
| <img src="https://github.com/user-attachments/assets/4cc31ef8-6e53-4648-b2aa-e268462b996f" width="250"/> | <img src="https://github.com/user-attachments/assets/9a8d7265-3df2-409e-a372-4a095e2b904b" width="250"/> |

---

## ✨ Features

- **🛰️ Real-Time GPS Tracking** – High-precision location capture via `FusedLocationProviderClient`
  with adaptive accuracy and battery optimization.

- **📍 Offline-First Architecture** – Runs are saved locally to Room Database first, then seamlessly
  synced to backend when connectivity is available. No data loss, guaranteed.

- **🗺️ Interactive Route Visualization** – View live running paths with polyline encoding, adaptive
  map rendering, and segment analysis on Google Maps.

- **📊 Comprehensive Analytics** – Track distance, pace, duration, and speed variability with
  detailed breakdowns.

- **👥 Social Features** – Share runs with friends, view social feeds, and celebrate milestones
  together.

- **🏃 Foreground Service Tracking** – Robust background location service ensures accurate tracking
  even when the app is backgrounded or screen is off.

- **🔐 Secure Authentication** – Firebase Auth integration with Firebase Firestore backend for user
  data, cross-device sync, and privacy.

---

## 🗺️ Architecture

<img width="500" height="1000" alt="pace_architecture" src="https://github.com/user-attachments/assets/da65d9d6-c22a-4612-8fe3-d8e79d750d9c" />


---

## 🪛 Tech Stack

* **Kotlin** - Modern, concise, and safe programming language
* **Jetpack Compose** - Declarative UI toolkit for building native Android interfaces
* **Material 3** - Latest Material Design system for beautiful, accessible UIs
*  **Maps** - Google Maps SDK for Android
* **Clean + MVVM/MVI Architecture** - Separation of concerns for maintainable code
*  **Dependency Injection** - Hilt for managing dependencies and improving testability
* **Room Database** - Robust local data persistence
* **Firebase** - Cloud services for authentication and data sync
* **Background Work** - Android WorkManager for reliable background processing
* **Coroutines & Flow** - Asynchronous programming made simple

---

## 🚀 Quick Start

#### Option 1: Download & Install Pre-Built APK (Recommended)

1. Visit the [Releases Page](https://github.com/kuntalavi/Pace/releases)
2. Download the latest **`pace.apk`** from the Assets section
3. Install and Run Pace App

#### Option 2: Build & Run

**Prerequisites:**

- Android Studio Version Panda 4 (2025.3.4)+
- JDK 17+
- Android SDK API 24+

**Steps:**

1. Clone the repository:

```bash
git clone https://github.com/kuntalavi/Pace.git
cd Pace
```
2. Firebase Configuration:

- Create a Firebase project at [Firebase Console](https://console.firebase.google.com/).
- Download the `google-services.json` file
- Place the `google-services.json` file in the `app/` directory of the project.

3. Add API Keys

- Add API Keys in the `local.properties` file:

```properties
GOOGLE_MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
MAPBOX_ACCESS_TOKEN=YOUR_MAPBOX_TOKEN_OPTIONAL
   ```
4. Build and Run:

- Open the project in Android Studio.
- Sync Gradle files.
- Select your target device or emulator.
- Click Run ▶.

---

## 🤝 Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

Distributed under the appropriate license. See `LICENSE` file for more information.

---

## Contact & Support

If you encounter any issues or have suggestions for future updates, please open an issue on GitHub or contact the developer directly.

**Developer:** Ravi Shankar

**Email:** kuntalravi43@gmail.com

<div align="center">

### Show Your Support

If you find this project helpful, please consider giving it a ⭐!

</div>

<div align="center">
<sub>Built with ❤️ by Ravi</sub>
</div>
