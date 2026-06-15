package com.example.spendwise.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u000b2\u00020\u0001:\u0001\u000bB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&\u00a8\u0006\f"}, d2 = {"Lcom/example/spendwise/db/SpendWiseDatabase;", "Landroidx/room/RoomDatabase;", "()V", "customCategoryDao", "Lcom/example/spendwise/db/CustomCategoryDao;", "expenseDao", "Lcom/example/spendwise/db/ExpenseDao;", "incomeDao", "Lcom/example/spendwise/db/IncomeDao;", "userDao", "Lcom/example/spendwise/db/UserDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.example.spendwise.db.UserEntity.class, com.example.spendwise.db.ExpenseEntity.class, com.example.spendwise.db.IncomeEntity.class, com.example.spendwise.db.CustomCategoryEntity.class}, version = 3, exportSchema = false)
public abstract class SpendWiseDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.example.spendwise.db.SpendWiseDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.spendwise.db.SpendWiseDatabase.Companion Companion = null;
    
    public SpendWiseDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.spendwise.db.UserDao userDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.spendwise.db.ExpenseDao expenseDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.spendwise.db.IncomeDao incomeDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.spendwise.db.CustomCategoryDao customCategoryDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/example/spendwise/db/SpendWiseDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/example/spendwise/db/SpendWiseDatabase;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.spendwise.db.SpendWiseDatabase getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}