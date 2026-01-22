# PaymentNFC

This is a **Kotlin Multiplatform** fintech application targeting **Android** and **iOS**. The UI is inspired by modern Figma templates and built using **Compose Multiplatform**.

## 📱 Screenshots

<div style="display: flex; flex-direction: row; gap: 10px;">
  <img width="270" height="555" alt="Home Screen" src="https://github.com/user-attachments/assets/c6a04da1-7d5c-488c-a3c9-8d0081ae98e0" />
  <img width="270" height="555" alt="Transaction History" src="https://github.com/user-attachments/assets/17159e93-8e2c-4bd6-ac2b-79bbf0644b9c" />
</div>

## 🏗️ Project Structure

The project follows a modular Clean Architecture approach with a shared presentation layer:

```
PaymentNFC/
├── composeApp/                     # Android Application
│   └── src/
│       └── androidMain/
│           └── kotlin/
│               └── com.aryandi.paymentnfc/
│                   ├── features/           # UI Screens (Compose)
│                   │   ├── cards/
│                   │   ├── home/
│                   │   ├── landing/
│                   │   ├── login/
│                   │   ├── otp/
│                   │   └── register/
│                   └── navigation/         # Navigation Graph
│
├── shared/                         # Kotlin Multiplatform Module
│   └── src/
│       ├── commonMain/             # Shared Code (Android & iOS)
│       │   └── kotlin/
│       │       └── com.aryandi.paymentnfc/
│       │           ├── data/               # Data Layer
│       │           │   ├── dto/            # Data Transfer Objects
│       │           │   ├── network/        # API Services (Ktor)
│       │           │   └── repository/     # Repository Implementations
│       │           ├── domain/             # Domain Layer
│       │           │   ├── model/          # Domain Models
│       │           │   ├── repository/     # Repository Interfaces
│       │           │   └── usecase/        # Use Cases
│       │           ├── presentation/       # Presentation Layer
│       │           │   └── viewmodel/      # Shared ViewModels
│       │           └── di/                 # Dependency Injection (Koin)
│       │
│       ├── androidMain/            # Android-specific implementations
│       │   └── kotlin/             # (e.g., Ktor Android Engine)
│       │
│       ├── iosMain/                # iOS-specific implementations
│       │   └── kotlin/             # (e.g., Ktor Darwin Engine)
│       │
│       └── commonTest/             # Shared Unit Tests
│           └── kotlin/
│               └── com.aryandi.paymentnfc/
│                   ├── data/repository/
│                   ├── domain/usecase/
│                   └── presentation/viewmodel/
│
└── iosApp/                         # iOS Application (Xcode Project)
    └── iosApp/
        └── ContentView.swift       # SwiftUI Entry Point
```

### Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                         Presentation Layer                          │
│  ┌───────────────────┐              ┌───────────────────────────┐   │
│  │   composeApp      │              │        iosApp             │   │
│  │  (Android UI)     │              │      (iOS UI)             │   │
│  │  Jetpack Compose  │              │      SwiftUI              │   │
│  └─────────┬─────────┘              └─────────────┬─────────────┘   │
│            │                                      │                 │
│            └──────────────┬───────────────────────┘                 │
│                           ▼                                         │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                  shared (commonMain)                         │   │
│  │  ┌─────────────────────────────────────────────────────┐    │   │
│  │  │              ViewModels (MVI Pattern)                │    │   │
│  │  │   LoginViewModel │ HomeViewModel │ OtpViewModel     │    │   │
│  │  └──────────────────────────┬──────────────────────────┘    │   │
│  └─────────────────────────────┼───────────────────────────────┘   │
└────────────────────────────────┼────────────────────────────────────┘
                                 ▼
┌─────────────────────────────────────────────────────────────────────┐
│                          Domain Layer                               │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                        Use Cases                             │   │
│  │   LoginUseCase │ GetTransactionsUseCase │ VerifyOtpUseCase  │   │
│  └──────────────────────────┬──────────────────────────────────┘   │
│                              ▼                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │               Repository Interfaces                          │   │
│  │          AuthRepository │ HomeRepository                     │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                                 ▼
┌─────────────────────────────────────────────────────────────────────┐
│                           Data Layer                                │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │              Repository Implementations                      │   │
│  │        AuthRepositoryImpl │ HomeRepositoryImpl               │   │
│  └──────────────────────────┬──────────────────────────────────┘   │
│                              ▼                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    API Services (Ktor)                       │   │
│  │            AuthApiService │ HomeApiService                   │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                                 ▼
┌─────────────────────────────────────────────────────────────────────┐
│                        External Services                            │
│                     Beeceptor Mock API Server                       │
└─────────────────────────────────────────────────────────────────────┘
```

## 🛠️ Libraries & Tech Stack

- **UI Framework**: [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- **Dependency Injection**: [Koin](https://insert-koin.io/)
- **Networking**: [Ktor](https://ktor.io/)
- **Asynchronous Logic**: [Kotlinx Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- **Data Serialization**: [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- **Logging**: [Kermit](https://github.com/touchlab/Kermit)
- **Testing**:
  - [Kotest](https://kotest.io/): Testing framework.
  - [Mokkery](https://github.com/lwasyl/Mokkery): Mocking library for KMP.
  - [Turbine](https://github.com/cashapp/turbine): Testing library for Flows.

## 🚀 How to Run

### Android
1. Open the project in **Android Studio**.
2. Run the `composeApp` configuration from the IDE's toolbar.
3. Or build via terminal:
   ```bash
   ./gradlew :composeApp:assembleDebug
   ```

### iOS
1. Open the `iosApp/iosApp.xcodeproj` in **Xcode**.
2. Run the application from Xcode.
3. Or use the run configuration in Android Studio (requires Kotlin Multiplatform plugin).

## 💡 Notes
- Use **"123456"** as the dummy OTP for verification.
- The app uses a mock [Beeceptor](https://beeceptor.com/) API for testing registration, login, and transaction history.

---

Learn more about [Kotlin Multiplatform Development](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html).
