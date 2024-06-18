# Whisper ~ The Chat App
 A beautifully designed chat app made with Compose
 
![Whisper Poster](https://github.com/DaChelimo/Whisper/assets/62815445/7159491d-e253-4b09-b92d-7aa77f98a1f3)

## Features
- Real-Time messaging
- Offline mode support {chats are synced when device is connected to a network later}
- Supports sharing images

## Setup instructions
1) Create a Firebase project
2) Generate `SHA-1` and `MD-5` keys by running `gradle/signingReport` in Gradle Tasks and upload them to Firebase
3) Download `google-services.json` file in your Firebase console
4) Open the Android Studio project in `Project mode` and put the google-services.json in the `app` folder
5) Build the app & cross your fingers that everything works out well :)
6) Maybe do a PR with a couple of UI, unit and instrumentation tests coz I'm still clueless in testing (Many crying emojis)

## Technologies
- Firebase {Authentication, Data storage, Media storage}
- Compose { UI development }
- Koin for DI
- Room for Offline caching
- Architecture type: Feature based {Domain, Repository, Presentation}

## Upcoming features
- Sending voice notes
- User status: Online, Typing, Last Seen
- Sending more media types: e.g videos, pdf
- Introduce Iphone Emojis {Key Feature}
- Uploading stories
- Group chats
- Invite others {in the contacts page}
- Audio and video calls using WebRtc

## App Screenshots

<img src="https://github.com/DaChelimo/Whisper/assets/62815445/2874f637-f023-47c3-a982-276fe97aac28" width="250">
<img src="https://github.com/DaChelimo/Whisper/assets/62815445/724ef083-a3a2-4178-a68c-4be07fc696d1" width="250">
<img src="https://github.com/DaChelimo/Whisper/assets/62815445/5a7925d7-76fb-4aa1-aae2-6849929f6598" width="250">
<img src="https://github.com/DaChelimo/Whisper/assets/62815445/ee09cb65-18c5-43d4-8696-2bfc68e7a1d9" width="250">
<img src="https://github.com/DaChelimo/Whisper/assets/62815445/4e92428c-8581-4c5c-9539-682c49ccfd85" width="250">

