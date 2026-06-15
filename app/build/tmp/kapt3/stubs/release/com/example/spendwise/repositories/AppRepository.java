package com.example.spendwise.repositories;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0015\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010\u0017J\u0006\u0010\u0018\u001a\u00020\u0010J\u0016\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u001a\u001a\u00020\u001bH\u0086@\u00a2\u0006\u0002\u0010\u001cJ\u0016\u0010\u001d\u001a\u00020\u00102\u0006\u0010\u001e\u001a\u00020\u001bH\u0086@\u00a2\u0006\u0002\u0010\u001cJ\u000e\u0010\u001f\u001a\u00020 H\u0086@\u00a2\u0006\u0002\u0010!J\u0006\u0010\"\u001a\u00020#J\u0014\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00120%H\u0086@\u00a2\u0006\u0002\u0010!J\u0014\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00160%H\u0086@\u00a2\u0006\u0002\u0010!J\u000e\u0010\'\u001a\u00020(H\u0086@\u00a2\u0006\u0002\u0010!J\u0010\u0010)\u001a\u00020\u001b2\u0006\u0010*\u001a\u00020\u001bH\u0002J\u0006\u0010+\u001a\u00020,J,\u0010-\u001a\b\u0012\u0004\u0012\u00020#0.2\u0006\u0010/\u001a\u00020\u001b2\u0006\u0010*\u001a\u00020\u001bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b0\u00101J\u0006\u00102\u001a\u00020\u0010JN\u00103\u001a\b\u0012\u0004\u0012\u00020#0.2\u0006\u00104\u001a\u00020\u001b2\u0006\u00105\u001a\u00020\u001b2\u0006\u00106\u001a\u00020\u001b2\u0006\u0010/\u001a\u00020\u001b2\u0006\u0010*\u001a\u00020\u001b2\b\b\u0002\u00107\u001a\u00020\u001bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b8\u00109J\u000e\u0010:\u001a\u00020\u00102\u0006\u0010;\u001a\u00020#J\u0016\u0010<\u001a\u00020\u00102\u0006\u0010=\u001a\u00020 H\u0086@\u00a2\u0006\u0002\u0010>J\"\u0010?\u001a\u00020\u00102\u0006\u0010@\u001a\u00020(2\n\b\u0002\u0010A\u001a\u0004\u0018\u00010\u001bH\u0086@\u00a2\u0006\u0002\u0010BR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006C"}, d2 = {"Lcom/example/spendwise/repositories/AppRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "db", "Lcom/example/spendwise/db/SpendWiseDatabase;", "expenseDao", "Lcom/example/spendwise/db/ExpenseDao;", "incomeDao", "Lcom/example/spendwise/db/IncomeDao;", "prefs", "Landroid/content/SharedPreferences;", "userDao", "Lcom/example/spendwise/db/UserDao;", "addExpense", "", "expense", "Lcom/example/spendwise/Expense;", "(Lcom/example/spendwise/Expense;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addIncome", "income", "Lcom/example/spendwise/Income;", "(Lcom/example/spendwise/Income;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearSession", "deleteExpense", "expenseId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteIncome", "incomeId", "getBudget", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCurrentUserId", "", "getExpenses", "", "getIncomes", "getUserProfile", "Lcom/example/spendwise/UserProfile;", "hashPassword", "password", "isLoggedIn", "", "login", "Lkotlin/Result;", "email", "login-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logout", "register", "firstName", "lastName", "gender", "profileImageUrl", "register-bMdYcbs", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveSession", "userId", "setBudget", "amount", "(DLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateProfile", "profile", "newPassword", "(Lcom/example/spendwise/UserProfile;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
public final class AppRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.spendwise.db.SpendWiseDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.spendwise.db.UserDao userDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.spendwise.db.ExpenseDao expenseDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.spendwise.db.IncomeDao incomeDao = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences prefs = null;
    
    public AppRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    public final int getCurrentUserId() {
        return 0;
    }
    
    public final boolean isLoggedIn() {
        return false;
    }
    
    public final void saveSession(int userId) {
    }
    
    public final void clearSession() {
    }
    
    private final java.lang.String hashPassword(java.lang.String password) {
        return null;
    }
    
    public final void logout() {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getUserProfile(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.spendwise.UserProfile> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateProfile(@org.jetbrains.annotations.NotNull()
    com.example.spendwise.UserProfile profile, @org.jetbrains.annotations.Nullable()
    java.lang.String newPassword, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getBudget(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Double> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object setBudget(double amount, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addExpense(@org.jetbrains.annotations.NotNull()
    com.example.spendwise.Expense expense, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getExpenses(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.spendwise.Expense>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteExpense(@org.jetbrains.annotations.NotNull()
    java.lang.String expenseId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addIncome(@org.jetbrains.annotations.NotNull()
    com.example.spendwise.Income income, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getIncomes(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.spendwise.Income>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteIncome(@org.jetbrains.annotations.NotNull()
    java.lang.String incomeId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}