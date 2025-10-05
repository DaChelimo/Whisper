# Whisper ~ Real-time messaging app

A modern, offline-first Android chat application built entirely with **Jetpack Compose**. This project demonstrates a robust, scalable architecture and a feature-set designed for a seamless user experience.

![Whisper Poster](https://github.com/DaChelimo/Whisper/assets/62815445/7159491d-e253-4b09-b92d-7aa77f98a1f3)

---

## Core Features
* **Real-Time Communication:** Leverages Firebase Cloud Firestore to deliver instant messaging with real-time updates.
* **Offline-First Architecture:** Caches data using the Room persistence library, ensuring seamless access and message queueing even without a network connection. Data syncs automatically upon reconnection.
* **Multimedia Messaging:** Utilizes Firebase Cloud Storage for efficient image sharing and retrieval.

---

### Architectural Highlights

* **Declarative UI:** The entire UI is built with **Jetpack Compose**, enabling a reactive, state-driven user interface that is more concise and powerful than the traditional XML-based approach.
* **Single Source of Truth (SSoT):** The repository pattern ensures that all application data flows from a single, reliable source (the Room database), which is synchronized with the network. This provides data consistency and a seamless offline experience.
* **State Management:** Leverages `ViewModels` and Kotlin's `StateFlow` to manage and expose UI state, ensuring that the UI is always a reflection of the underlying application data in a lifecycle-aware manner.
* **Dependency Injection:** Uses **Koin** for dependency inversion, decoupling components and making them easier to manage, replace, and test.

---

## Tech Stack

This project utilizes a curated selection of modern libraries and tools to deliver a high-quality product.

* **Core:**
    * [Kotlin](https://kotlinlang.org/): Official language for Android development.
    * [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html): For asynchronous and reactive programming.

* **UI:**
    * [Jetpack Compose](https://developer.android.com/jetpack/compose): Modern toolkit for building native Android UI.
    * [Material 3](https://m3.material.io/): The latest version of Google's open-source design system.

* **Architecture & DI:**
    * [Jetpack ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): Manages UI-related data in a lifecycle-conscious way.
    * [Koin](https://insert-koin.io/): A pragmatic and lightweight dependency injection framework.

* **Data & Networking:**
    * [Room](https://developer.android.com/training/data-storage/room): Persistence library for robust offline data caching.
    * [Firebase Suite](https://firebase.google.com/):
        * **Authentication:** For secure user sign-in and identity management.
        * **Cloud Firestore:** For real-time, persistent data synchronization.
        * **Cloud Storage:** For storing and serving user-generated content like images.

---

## App Screenshots
<img src="https://github.com/DaChelimo/Whisper/assets/62815445/2874f637-f023-47c3-a982-276fe97aac28" width="250"> <img src="https://github.com/DaChelimo/Whisper/assets/62815445/724ef083-a3a2-4178-a68c-4be07fc696d1" width="250"> <img src="https://github.com/DaChelimo/Whisper/assets/62815445/5a7925d7-76fb-4aa1-aae2-6849929f6598" width="250"> <img src="https://github.com/DaChelimo/Whisper/assets/62815445/ee09cb65-18c5-43d4-8696-2bfc68e7a1d9" width="250"> <img src="https://github.com/DaChelimo/Whisper/assets/62815445/4e92428c-8581-4c5c-9539-682c49ccfd85" width="250">


---

## Build Instructions
1.  Create a new project in your [Firebase Console](https://console.firebase.google.com/).
2.  Generate a `SHA-1` debug key by running `./gradlew signingReport` in the Android Studio terminal and add it to your Firebase project settings.
3.  Download the `google-services.json` file from your Firebase project console.
4.  Switch to `Project` view in Android Studio and place the `google-services.json` file in the `app/` directory.
5.  Build and run the application.

---

## Project Roadmap
The following features are planned for future development to further enhance the application's capabilities:
* **Presence System:** Implement user status indicators (Online, Typing, Last Seen).
* **Voice Messaging:** Add support for sending and receiving voice notes.
* **Expanded Media Support:** Allow sharing of additional media types like videos and documents.
* **Group Chat Functionality:** Develop features for multi-user conversations.
* **Contact Integration:** Introduce the ability to invite users from the device's contacts.
* **WebRTC Integration:** Implement audio and video calling features.

---

## Contributing
Contributions are welcome! Please feel free to submit a pull request. This project would particularly benefit from contributions in the area of **unit and instrumentation testing** to increase code coverage and robustness.
