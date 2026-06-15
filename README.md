# SpendWise v5.0 — Personal Finance Manager

SpendWise is a modern, privacy-focused Android application designed to help individuals take control of their personal finances. Whether you're a student managing a tight budget, a professional tracking multiple income streams, or anyone who wants to build smarter money habits — SpendWise gives you the tools to log, visualize, and improve your financial health, all without ever leaving your device.

---

## 🎥 App Presentation

Watch the app in action here: [SpendWise Presentation - YouTube](https://youtu.be/E2l5NXsV6Mc?si=7cyZSBrr2CwAn7Yr)

---

## 📱 What SpendWise Does & Who It's For

SpendWise is built for **everyday users** who want a simple, private, and rewarding way to manage money. It replaces complex spreadsheets and cloud-dependent finance apps with a clean local-first Android experience.

**Target users:**
- Students and young adults building their first budget
- Salaried professionals tracking spending across categories
- Anyone who values data privacy and offline-first access

**Core capabilities:**
- Log and categorize expenses and income
- Set and monitor monthly budgets and personal saving goals
- Visualize spending patterns with interactive charts
- Earn XP and badges for healthy financial behaviour
- Receive daily push notification reminders to keep you accountable

> **v5.0 Migration Note:** SpendWise has been fully migrated to a **local-first architecture**. Firebase is no longer used for authentication, storage, or data. All data lives securely on your device.

---

## 🚀 Key Features

### 🔐 Secure Local Authentication
- **Registration**: Create an account with your name, gender, email, and an optional profile photo.
- **SHA-256 Password Hashing**: Passwords are never stored in plain text.
- **Persistent Sessions**: Stay logged in across app restarts via shared preferences.
- **Profile Management**: Update personal details and change your password at any time via the Profile screen.

### 💰 Comprehensive Transaction Tracking
- **Expenses & Income**: Record daily spending and earnings with amount, category, description, and date.
- **Time Tracking**: Optionally log start and end times per transaction for granular tracking.
- **Recurring Transactions**: Mark transactions as recurring to reflect regular payments.
- **Receipt Capture**: Attach photos to expenses — shoot with the camera or pick from gallery.
- **Search & Filter**: Filter transactions by type (expense/income), category chip selection, and custom date range.
- **Transaction History**: Full chronological list with expandable receipt photo previews via a bottom sheet.

### 📊 Financial Insights & Visualizations
- **Dashboard Overview**: At-a-glance cards showing Total Income, Total Expenses, Net Balance, and budget usage.
- **Pie Chart**: MPAndroidChart-powered breakdown of spending by category.
- **Budget Tracker**: Set a monthly budget and track live progress with a colour-coded progress bar (green → yellow → red).
- **Essential vs. Discretionary Split**: Real-time ratio of essential spending (Food, Rent, Health, Transport, Utilities, Education) to total spending.
- **Goals on Dashboard**: Active saving and spending goals displayed inline on the main dashboard.
- **Recent Expenses Strip**: Quick view of the 5 most recent expenses without navigating away.

### 🎯 Goals & Advice
- **Financial Goals**: Create saving or spending-limit goals with target amounts, deadlines, and categories.
- **Goal Progress**: Visual progress bar per goal; goals auto-complete when the target is reached.
- **Savings Analysis**: The Advice screen shows your 20% savings target performance and min/max spending categories.
- **Login Streak Tracking**: The app tracks consecutive daily logins, rewarding consistent engagement.

### 🎮 Custom Feature 1 — Gamified XP & Levelling System
SpendWise includes a fully custom XP and levelling engine that turns good financial habits into a game:

| Level | Title | XP Required | Badge |
|-------|-------|-------------|-------|
| 1 | Needs Novice | 0 XP | 🌱 |
| 2 | Smart Spender | 100 XP | 💡 |
| 3 | Savings Sidekick | 250 XP | 🎯 |
| 4 | Essentials Expert | 500 XP | 🏆 |
| 5 | Budget Ninja | 800 XP | 🥷 |

**How XP is earned:**
- +10 XP per essential expense logged (up to 100 XP)
- +75 XP for keeping 50%+ of spending on essential categories
- +75 XP for spending within your total income
- +50 XP for saving at least 10% of income
- +100 XP for saving at least 20% of income

A live progress bar on the Dashboard and detailed level grid on the Advice screen show exactly where you stand and what to do next.

### 🔔 Custom Feature 2 — Smart Daily Reminder Notifications
A custom `DailyReminderWorker` (built with Jetpack WorkManager) fires a context-aware push notification once per day at a user-selected time:

- **Over budget**: "⚠️ You're over budget! Spent R540.00 of R500.00"
- **Budget set**: "💰 Today's check-in: R320.00/R500.00 budget used (64%)"
- **Income tracked, no budget**: "📊 Daily reminder: You've spent R200.00 so far this period."
- **No data yet**: "📝 Don't forget to log today's expenses in SpendWise!"

Users toggle reminders on/off and pick their preferred reminder time (hour:minute) from the Settings screen. The WorkManager job persists across reboots via `RECEIVE_BOOT_COMPLETED`.

### ⚙️ Settings & Customisation
- **Dark Mode**: Toggle system-wide dark theme, saved persistently.
- **Region & Currency**: Select your region from a spinner to auto-set the correct currency symbol (e.g. ZAR, USD, EUR).
- **Daily Reminders**: Enable/disable and set the exact time for budget push notifications.
- **Custom Categories**: Create, name, icon, and colour-code your own expense categories with optional per-category budget limits.

---

## 🎨 Design Decisions

### Local-First Architecture
The move away from Firebase was deliberate. A local-first approach means:
- No internet connection required for any core functionality.
- No risk of data breaches on external servers.
- Faster reads and writes via Room's SQLite layer.
- Full control of user data — it stays on the device.

### MVVM with Repository Pattern
The app is structured around the **Model-View-ViewModel** pattern:
- **Entities & DAOs** (Room) handle all database access.
- **Repositories** (`AppRepository`, `CurrencyRepository`) abstract data sources from UI.
- **ViewModels** hold and survive UI state across configuration changes.
- **Fragments** observe LiveData and only handle UI rendering.

This separation keeps each layer independently testable and maintainable.

### Material Design 3 + Fragment Navigation
The UI is built on **Material Design 3** with a bottom navigation bar providing four primary destinations: Dashboard, Transactions, Categories, and Advice. Settings and Profile are accessed contextually. Fragment transactions use the **Jetpack Navigation Component** with a `nav_graph.xml` for type-safe navigation.

### Data Binding & ViewBinding
All layouts use **ViewBinding** (and DataBinding for binding classes), eliminating `findViewById` boilerplate and reducing null-pointer risks.

### Gamification as Motivation
The XP system was designed to make healthy financial habits intrinsically rewarding. Rather than showing raw numbers, SpendWise gives users a sense of progression and achievement — a deliberate design choice to increase daily engagement and retention.

### Privacy by Design
- Passwords are hashed with SHA-256 before storage — never stored in plain text.
- Profile images are stored in the app's internal storage directory — not accessible to other apps.
- No analytics, no third-party SDKs transmitting user data externally.

---

## 🔧 GitHub & GitHub Actions

### GitHub Workflow
The project was developed using **GitHub** for version control with a feature-branch workflow:

- The `main` branch holds stable, release-ready code.
- Feature branches were created per feature or screen (e.g., `feature/xp-system`, `feature/daily-reminders`).
- Pull Requests were used to review and merge completed features into `main`.
- Commit messages follow a structured format: `type(scope): description` (e.g., `feat(dashboard): add budget progress card`).

### GitHub Actions — CI Pipeline
A GitHub Actions workflow (`.github/workflows/android-ci.yml`) is configured to run on every push and pull request to `main`:

```yaml
name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Grant execute permission for gradlew
        run: chmod +x gradlew
      - name: Build with Gradle
        run: ./gradlew assembleDebug
      - name: Run Unit Tests
        run: ./gradlew test
```

The pipeline ensures:
1. The project **builds successfully** on every commit.
2. **Unit tests** are executed automatically before any merge.
3. Broken builds are caught early and block merges via branch protection rules.

---

## 📸 Screenshots

> Screenshots below illustrate the main screens of SpendWise. Replace placeholder paths with your actual screenshot files.

### Splash & Authentication
| Splash Screen | Login | Register |
|---|---|---|
| ![Splash](screenshots/splash.png) | ![Login](screenshots/login.png) | ![Register](screenshots/register.png) |

### Core Screens
| Dashboard | Transactions | Categories |
|---|---|---|
| ![Dashboard](screenshots/dashboard.png) | ![Transactions](screenshots/transactions.png) | ![Categories](screenshots/categories.png) |

### Advice & Settings
| Advice / XP | Settings | Profile |
|---|---|---|
| ![Advice](screenshots/advice.png) | ![Settings](screenshots/settings.png) | ![Profile](screenshots/profile.png) |

### Custom Features
| XP Level System | Daily Reminder Notification |
|---|---|
| ![XP System](screenshots/xp_levels.png) | ![Notification](screenshots/notification.png) |

> 📁 Place all screenshots in a `/screenshots` folder at the root of the repository.

---

## 🛠️ Technical Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.2.10 |
| Database | Room Persistence Library (SQLite) |
| Architecture | MVVM + Repository Pattern |
| UI | Material Design 3, ConstraintLayout, ViewBinding |
| Navigation | Jetpack Navigation Component |
| Background Work | Jetpack WorkManager |
| Image Loading | Glide |
| Charts | MPAndroidChart |
| Profile Avatars | CircleImageView |
| Build System | Gradle (Kotlin DSL) |

---

## 🏗️ Setup & Requirements

- **Android Studio**: Ladybug (2024.2.1) or newer
- **JDK**: 17 or 21
- **Minimum SDK**: Android 8.0 (API level 26)
- **Target SDK**: Android 14+
- **Gradle**: Uses `kotlin-kapt` for Room annotation processing

### Getting Started

```bash
# 1. Clone the repository
git clone https://github.com/your-username/SpendWise.git

# 2. Open the project in Android Studio
# File → Open → select the SpendWise_Updated folder

# 3. Wait for Gradle sync to complete

# 4. Run the app on an emulator or physical device (API 26+)
```

**No API keys or external service configuration required.** The app runs fully offline out of the box.

---

## 📜 Privacy & Architecture Note

SpendWise v5.0 operates entirely on-device:

- **No Firebase**: Firebase Auth, Firestore, and Firebase Storage have all been removed.
- **No External Servers**: No user data is transmitted anywhere.
- **Offline Ready**: Every feature works without a network connection.
- **Secure Local Storage**: All records and images are stored via Room and the app's internal storage directory.

---

## 📁 Project Structure

```
app/src/main/
├── java/com/example/spendwise/
│   ├── db/                  # Room entities, DAOs, database
│   ├── fragments/           # Dashboard, Transactions, Categories, Advice, Settings
│   ├── repositories/        # AppRepository, CurrencyRepository
│   ├── viewmodels/          # DashboardViewModel
│   ├── workers/             # DailyReminderWorker (WorkManager)
│   ├── utils/               # CameraHelper
│   ├── Models.kt            # Data classes + XPSystem object
│   ├── TransactionAdapter.kt
│   ├── MainActivity.kt
│   ├── LoginActivity.kt
│   ├── RegisterActivity.kt
│   ├── ProfileActivity.kt
│   └── SplashActivity.kt
└── res/
    ├── layout/              # XML layouts for all screens
    ├── navigation/          # nav_graph.xml
    ├── drawable/            # Icons and backgrounds
    └── values/              # Colors, strings, dimensions
```
