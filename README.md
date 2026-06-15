 SpendWise v5.0 — Personal Finance Manager

SpendWise is a privacy-focused Android application for personal finance management. It helps users log expenses and income, set budgets and savings goals, and build better money habits, all without relying on cloud services.

## App Presentation

Watch the app in action: [SpendWise Presentation - YouTube](https://youtu.be/E2l5NXsV6Mc?si=7cyZSBrr2CwAn7Yr)

## Overview

SpendWise is built for everyday users who want a simple, private, and rewarding way to manage money. It replaces complex spreadsheets and cloud-dependent finance apps with a clean, local-first Android experience.

Target users:
- Students and young adults building their first budget
- Salaried professionals tracking spending across categories
- Anyone who values data privacy and offline-first access

Core capabilities:
- Log and categorize expenses and income
- Set and monitor monthly budgets and personal saving goals
- Visualize spending patterns with interactive charts
- Earn XP and badges for healthy financial behaviour
- Receive daily push notification reminders to stay accountable

v5.0 Migration Note: SpendWise has been fully migrated to a local-first architecture. Firebase is no longer used for authentication, storage, or data. All data lives on the device.

## Features

### Secure Local Authentication
- Registration with name, gender, email, and an optional profile photo
- Passwords hashed with SHA-256 before storage
- Persistent sessions across app restarts via shared preferences
- Profile management for updating personal details and password

### Transaction Tracking
- Record daily spending and earnings with amount, category, description, and date
- Optional start and end time logging per transaction
- Recurring transaction flag for regular payments
- Receipt photo capture via camera or gallery
- Filter transactions by type, category, and custom date range
- Chronological transaction history with expandable receipt previews

### Financial Insights & Visualizations
- Dashboard overview with Total Income, Total Expenses, Net Balance, and budget usage
- Pie chart breakdown of spending by category (MPAndroidChart)
- Budget tracker with colour-coded progress bar (green to yellow to red)
- Essential vs. discretionary spending ratio (Food, Rent, Health, Transport, Utilities, Education)
- Active saving and spending goals shown on the dashboard
- Recent expenses strip showing the 5 most recent entries

### Goals & Advice
- Create saving or spending-limit goals with target amounts, deadlines, and categories
- Visual progress bar per goal; goals auto-complete when reached
- Advice screen showing 20% savings target performance and min/max spending categories
- Login streak tracking for consistent engagement

### Custom Feature 1 — Gamified XP & Levelling System

A custom XP and levelling engine that rewards good financial habits.

| Level | Title | XP Required | Badge |
|-------|-------|-------------|-------|
| 1 | Needs Novice | 0 XP | Seedling |
| 2 | Smart Spender | 100 XP | Lightbulb |
| 3 | Savings Sidekick | 250 XP | Target |
| 4 | Essentials Expert | 500 XP | Trophy |
| 5 | Budget Ninja | 800 XP | Ninja |

How XP is earned:
- +10 XP per essential expense logged (up to 100 XP)
- +75 XP for keeping 50%+ of spending on essential categories
- +75 XP for spending within total income
- +50 XP for saving at least 10% of income
- +100 XP for saving at least 20% of income

A live progress bar on the Dashboard and a detailed level grid on the Advice screen show current standing and next steps.

### Custom Feature 2 — Smart Daily Reminder Notifications

A custom `DailyReminderWorker`, built with Jetpack WorkManager, fires a context-aware push notification once per day at a user-selected time:

- Over budget: "You're over budget! Spent R540.00 of R500.00"
- Budget set: "Today's check-in: R320.00/R500.00 budget used (64%)"
- Income tracked, no budget: "Daily reminder: You've spent R200.00 so far this period."
- No data yet: "Don't forget to log today's expenses in SpendWise!"

Users can toggle reminders on/off and pick a preferred reminder time from the Settings screen. The WorkManager job persists across reboots via `RECEIVE_BOOT_COMPLETED`.

### Settings & Customisation
- Dark mode toggle, saved persistently
- Region and currency selection (e.g. ZAR, USD, EUR)
- Daily reminder enable/disable and time configuration
- Custom categories with name, icon, colour, and optional per-category budget limits

## Design Decisions

### Local-First Architecture

The move away from Firebase was deliberate. A local-first approach means:
- No internet connection required for any core functionality
- No risk of data breaches on external servers
- Faster reads and writes via Room's SQLite layer
- Full control of user data, kept entirely on the device

### MVVM with Repository Pattern

The app is structured around the Model-View-ViewModel pattern:
- Entities and DAOs (Room) handle all database access
- Repositories (`AppRepository`, `CurrencyRepository`) abstract data sources from the UI
- ViewModels hold and survive UI state across configuration changes
- Fragments observe LiveData and only handle UI rendering

This separation keeps each layer independently testable and maintainable.

### Material Design 3 + Fragment Navigation

The UI is built on Material Design 3 with a bottom navigation bar providing four primary destinations: Dashboard, Transactions, Categories, and Advice. Settings and Profile are accessed contextually. Fragment transactions use the Jetpack Navigation Component with a `nav_graph.xml` for type-safe navigation.

### Data Binding & ViewBinding

All layouts use ViewBinding (and DataBinding for binding classes), eliminating `findViewById` boilerplate and reducing null-pointer risks.

### Gamification as Motivation

The XP system was designed to make healthy financial habits intrinsically rewarding. Rather than showing raw numbers, SpendWise gives users a sense of progression and achievement, increasing daily engagement and retention.

### Privacy by Design
- Passwords are hashed with SHA-256 before storage, never stored in plain text
- Profile images are stored in the app's internal storage directory, not accessible to other apps
- No analytics or third-party SDKs transmitting user data externally

## GitHub & GitHub Actions

### GitHub Workflow

The project was developed using GitHub for version control with a feature-branch workflow:

- The `main` branch holds stable, release-ready code
- Feature branches were created per feature or screen (e.g., `feature/xp-system`, `feature/daily-reminders`)
- Pull requests were used to review and merge completed features into `main`
- Commit messages follow a structured format: `type(scope): description` (e.g., `feat(dashboard): add budget progress card`)

### GitHub Actions — CI Pipeline

A GitHub Actions workflow (`.github/workflows/android-ci.yml`) runs on every push and pull request to `main`:

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
1. The project builds successfully on every commit
2. Unit tests are executed automatically before any merge
3. Broken builds are caught early and blocked from merging via branch protection rules

## Screenshots

Screenshots below illustrate the main screens of SpendWise. Replace placeholder paths with actual screenshot files.

### Splash & Authentication

| Splash Screen | Login | Register |
|---|---|---|
| ![Splash](screenshots/splash.png) | ![Login](screenshots/login.png) | ![Register](screenshots/register.png) |

### Core Screens

| Dashboard | Transactions | Categories |
|---|---|---|
| ![Dashboard](\SpendWise_Updated\Dashboard.jpg) | ![Transactions](\SpendWise_Updated\Transactions.jpg) | ![Categories](\SpendWise_Updated\Categories.jpg) |

### Advice & Settings

| Advice / XP | Settings | Profile |
|---|---|---|
| ![Advice](screenshots/advice.png) | ![Settings](\SpendWise_Updated\Settings.jpg) | ![Profile](\SpendWise_Updated\Profile.jpg) |

### Custom Features

| XP Level System | Daily Reminder Notification |
|---|---|
| ![XP System](\SpendWise_Updated\XP.jpg) | ![Notification](screenshots/notification.png) |

Place all screenshots in a `/screenshots` folder at the root of the repository.

## Technical Stack

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

## Setup & Requirements

- Android Studio: Ladybug (2024.2.1) or newer
- JDK: 17 or 21
- Minimum SDK: Android 8.0 (API level 26)
- Target SDK: Android 14+
- Gradle: Uses `kotlin-kapt` for Room annotation processing

### Getting Started

```bash
# 1. Clone the repository
git clone https://github.com/your-username/SpendWise.git

# 2. Open the project in Android Studio
# File -> Open -> select the SpendWise_Updated folder

# 3. Wait for Gradle sync to complete

# 4. Run the app on an emulator or physical device (API 26+)
```

No API keys or external service configuration are required. The app runs fully offline out of the box.

## Privacy & Architecture Note

SpendWise v5.0 operates entirely on-device:

- No Firebase: Firebase Auth, Firestore, and Firebase Storage have all been removed
- No external servers: no user data is transmitted anywhere
- Offline ready: every feature works without a network connection
- Secure local storage: all records and images are stored via Room and the app's internal storage directory

## Project Structure

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
