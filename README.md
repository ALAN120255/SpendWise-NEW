# SpendWise v5.0 — Personal Finance Manager

SpendWise is a modern, privacy-focused Android application designed to help users track their expenses, manage income, and visualize their financial health through an interactive dashboard and a gamified XP system.

## 🎥 App Presentation
Watch the app in action here: [SpendWise Presentation - YouTube](https://youtu.be/FD5ziHBSSQA)

> **Note:** SpendWise has been migrated to a **local-first architecture** (v5.0). All data is stored securely on your device. Firebase is no longer required for authentication or data storage.

## 🚀 Key Features

### 🔐 Secure Local Authentication
- **User Registration**: Create an account with your name, gender, email, and an optional profile picture.
- **Local Security**: Uses secure password hashing (SHA-256) and persistent session management.
- **Profile Management**: Update your personal details and change your password at any time.

### 💰 Comprehensive Transaction Tracking
- **Expenses & Income**: Easily record your daily spending and earnings.
- **Categorization**: Group transactions into categories (Food, Rent, Salary, Freelance, etc.) with custom icons.
- **Receipt Capture**: Attach photos of receipts to your expenses using the built-in camera or gallery.
- **History View**: A detailed list of all transactions with expandable photo previews.

### 📊 Financial Insights & Visualizations
- **Dashboard**: Real-time overview of your Total Income, Total Expenses, and Net Balance.
- **Interactive Charts**:
  - **Pie Chart**: Breakdown of expenses by category.
  - **Line Chart**: Trend analysis of your recent income.
- **Dynamic Headers**: Your user profile and name are always visible in the header for a personalized experience.

### 🎮 Gamified Financial Health (XP System)
- **Financial XP**: Earn XP by recording transactions and staying within your monthly budget.
- **Level Up**: Progress through levels from "Budget Beginner" to "Budget Ninja" and unlock unique badges (🌱, 💡, 🎯, 🏆, 🥷).
- **Progress Tracking**: Visual progress bar showing exactly how much XP you need to reach the next milestone.

### ⚙️ Customization
- **Budgeting**: Set a monthly budget goal to help control your spending.
- **Currency Support**: Adapt the app to your local currency.

---

## 🛠️ Technical Stack
- **Language**: Kotlin 2.2.10
- **Database**: Room Persistence Library (SQLite)
- **Architecture**: MVVM with Repositories and ViewModels
- **UI Components**: Material Design 3, ConstraintLayout, ViewBinding, Fragment Navigation
- **Libraries**:
  - **Glide**: High-performance image loading.
  - **MPAndroidChart**: Powerful and interactive data visualization.
  - **Jetpack Navigation**: Type-safe navigation between screens.
  - **CircleImageView**: For elegant profile avatar displays.
  - **Security Crypto**: For secure local data handling.

---

## 🏗️ Setup & Requirements
- **Android Studio**: Ladybug (2024.2.1) or newer recommended.
- **JDK**: 17 or 21.
- **Minimum SDK**: Android 8.0 (API level 26).
- **Gradle**: Uses `kotlin-kapt` for Room annotation processing.

### Getting Started
1. Clone the repository.
2. Open the project in Android Studio.
3. Wait for Gradle sync to complete.
4. Run the app on an emulator or physical device.

## 📜 Privacy & Architecture Note (Migration from v3.0)
The application no longer relies on Firebase.
- **No Firebase Required**: The app no longer uses Firebase Storage, Firestore, or Firebase Auth.
- **Data Privacy**: No sensitive information is transmitted to external servers.
- **Offline Ready**: Full functionality is available without an internet connection.
- **Local Storage**: All records and profile images are stored locally using Room and the internal storage.
# SpendWise-NEW
