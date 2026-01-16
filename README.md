# 🏃‍♂️ Pace - Your Ultimate Running Companion

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-1.5+-green.svg?style=flat&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Hilt](https://img.shields.io/badge/Dagger_Hilt-DI-orange.svg?style=flat)](https://dagger.dev/hilt/)

[//]: # ([![License]&#40;https://img.shields.io/badge/License-MIT-yellow.svg&#41;]&#40;LICENSE&#41;)

**Pace** is a modern, high-performance running tracker built with Jetpack Compose and Google Maps. Whether you're a casual jogger or a marathon enthusiast, Pace helps you track your runs, analyze your performance with beautiful charts, and stay motivated.

---

## ✨ Features

- 🛰️ **Real-time GPS Tracking**: High-accuracy location tracking using `FusedLocationProviderClient`.
- 🗺️ **Interactive Maps**: Visualize your route in real-time with Google Maps and dynamic polylines.
- 📊 **Detailed Analytics**: Comprehensive statistics including pace, distance, duration, and elevation.
- 📈 **Data Visualization**: Beautiful charts (via MPAndroidChart/Vico) to track your progress over time.
- 🔋 **Optimized Background Tracking**: Reliable foreground service to ensure your run is recorded even when the app is in the background.
- 🗄️ **Offline-First**: Powered by Room Database for local data persistence and seamless offline usage.
- 🔐 **Secure Authentication**: Built-in authentication flow to keep your data safe.
- 👟 **Gear Tracking**: (Coming Soon) Track your shoe mileage to know exactly when it's time for a new pair.

---

## 🛠️ Tech Stack & Architecture

Pace follows **Clean Architecture** principles and the **MVVM** pattern to ensure a scalable, maintainable, and testable codebase.

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material Design 3.
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android).
- **Navigation**: Type-safe [Compose Navigation](https://developer.android.com/guide/navigation/design/type-safety).
- **Database**: [Room](https://developer.android.com/training/data-storage/room) with complex relationships and views.
- **Background Work**: [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) & Foreground Services.
- **Concurrency**: Kotlin Coroutines & Flow.
- **Maps**: [Google Maps SDK for Android](https://developers.google.com/maps/documentation/android-sdk/overview).
- **Networking**: [Ktor](https://ktor.io/) / [Retrofit](https://square.github.io/retrofit/) (Ready for API integration).

---

## 🏗️ Project Structure

```text
com.rk.pace
├── auth           # Authentication logic & UI
├── common         # Extensions, Utilities, Constants
├── data           # Room DB, Repositories, Data Sources
├── di             # Hilt Modules
├── domain         # UseCases & Domain Models
├── presentation   # Compose Screens, ViewModels, Theme
└── background     # Services & WorkManager tasks
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Iguana or newer
- JDK 17
- A Google Maps API Key (Add it to `local.properties`)

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/pace.git
   ```
2. Open the project in Android Studio.
3. Add your API Keys in `local.properties`:
   ```properties
   GOOGLE_MAPS_API_KEY=YOUR_KEY_HERE
   MAPBOX_ACCESS_TOKEN=YOUR_ACCESS_TOKEN
   ```
4. Sync Gradle and run the app on an emulator or physical device.

---

## 📸 Screenshots

[//]: # (| Home | Active Run | Statistics |)

[//]: # (|:---:|:---:|:---:|)

[//]: # (| ![Home]&#40;https://via.placeholder.com/200x400?text=Home+Screen&#41; | ![Active Run]&#40;https://via.placeholder.com/200x400?text=Tracking+Screen&#41; | ![Stats]&#40;https://via.placeholder.com/200x400?text=Charts+Screen&#41; |)

---

## 🗺️ Roadmap

- [ ] Shoe mileage tracking.
- [ ] Route comparison (compare multiple attempts on the same path).
- [ ] Weather integration for run logs.
- [ ] Audio coaching updates every kilometer.
- [ ] GPX/TCX Export functionality.

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

## 📄 License

This project is licensed under the MIT License - see the file for details.

---
*Made with ❤️ for runners everywhere.*
