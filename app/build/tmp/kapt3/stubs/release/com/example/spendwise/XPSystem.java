package com.example.spendwise;

/**
 * Essential categories are: Food, Transport, Health, Utilities, Rent, Education.
 * XP is earned by:
 *  - Spending on essential needs (10 XP per essential transaction, capped at 100)
 *  - Keeping essential spend ratio healthy (>= 50% of expenses go to essentials → +75 XP)
 *  - Spending within income (totalSpent <= totalIncome → +75 XP)
 *  - Saving at least 10% of income (→ +50 XP)
 *  - Saving at least 20% of income (→ +100 XP)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u00042\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\tJ\u000e\u0010\u0010\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\tR\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0011"}, d2 = {"Lcom/example/spendwise/XPSystem;", "", "()V", "levels", "", "Lcom/example/spendwise/UserLevel;", "getLevels", "()Ljava/util/List;", "calculateXP", "", "expenses", "Lcom/example/spendwise/Expense;", "totalIncome", "", "getLevelForXP", "xp", "getProgressPercent", "app_release"})
public final class XPSystem {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.example.spendwise.UserLevel> levels = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.spendwise.XPSystem INSTANCE = null;
    
    private XPSystem() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.example.spendwise.UserLevel> getLevels() {
        return null;
    }
    
    public final int calculateXP(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.spendwise.Expense> expenses, double totalIncome) {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.spendwise.UserLevel getLevelForXP(int xp) {
        return null;
    }
    
    public final int getProgressPercent(int xp) {
        return 0;
    }
}