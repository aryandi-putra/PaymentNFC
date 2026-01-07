This is a Kotlin Multiplatform project targeting Android and iOS. I use a fintech application as the use case, and the UI is inspired by these free Figma templates:

[Fintech Figma](https://www.figma.com/community/file/1352194292067779907)
[Home Transaction Figma](https://www.figma.com/community/file/1333755496595563984)

<img width="270" height="555" alt="Screenshot_20251220_134118" src="https://github.com/user-attachments/assets/c6a04da1-7d5c-488c-a3c9-8d0081ae98e0" />

<img width="270" height="555" alt="Screenshot_20251220_134131" src="https://github.com/user-attachments/assets/17159e93-8e2c-4bd6-ac2b-79bbf0644b9c" />

You can use "123456" for dummy OTP

The project follows several architectural approaches. Currently, I use a shared ViewModel in the shared module. For future development, the decision to keep ViewModels in the shared module or move them to platform-specific layers will depend on the iOS implementation requirements.

This application uses a mock Beeceptor API to handle registration, login, and dummy transaction history.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
