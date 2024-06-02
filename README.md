# Generate - The Android Template {for Compose}
 Template for creating Android apps in Compose by automatically providing commonly used libraries in most apps as well as setting up standard initial tasks.
 
![Template project glance](https://github.com/DaChelimo/Generate/assets/62815445/b705d6f1-2d4e-4002-be7f-d295f64a33e1)

## Project already contains
- Splash screen
- Basic navigation (new method that doesn't use `Route`)
- Usual Package structure (domain, data, presentation)
- Commonly used fonts explicitly stated: `Roboto`, `Poppins`, `Cabin (Normal, Semi-Condensed, Condensed)`, `QuickSand` and `MontSerrat`
- DI with Koin by creating modules (App, Local and Network) and adding them to the App.kt -> Even created a room DB
- Set up common utils: specifically, `Resource.kt` for error handling

## Libraries included
- Firebase
- Koin for DI
- Room: Local persistence
- Retrofit: Online data fetching
- Moshi: Serialization
- Glide: Image Loading
- Joda: Time and Date
- PermissionX: Perfect permission handler
- Timber: Logging library
- Google Fonts
