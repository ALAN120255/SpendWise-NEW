package com.example.spendwise.fragments;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J6\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u0002J\u0010\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J&\u0010\u0013\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\u0005\u001a\u0004\u0018\u00010\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0004H\u0016J\u001e\u0010\u001a\u001a\u00020\u00042\u0006\u0010\u001b\u001a\u00020\u001c2\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00040\u001eH\u0002J&\u0010\u001f\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\b2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u00a8\u0006!"}, d2 = {"Lcom/example/spendwise/fragments/CategoriesFragment;", "Landroidx/fragment/app/Fragment;", "()V", "addCategoryCard", "", "container", "Landroid/widget/LinearLayout;", "summary", "Lcom/example/spendwise/CategorySummary;", "pct", "", "sym", "", "expenses", "", "Lcom/example/spendwise/Expense;", "loadCategories", "root", "Landroid/view/View;", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onResume", "showAddCategoryDialog", "repo", "Lcom/example/spendwise/repositories/AppRepository;", "onAdded", "Lkotlin/Function0;", "showCategoryDetail", "catExpenses", "app_debug"})
public final class CategoriesFragment extends androidx.fragment.app.Fragment {
    
    public CategoriesFragment() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    private final void loadCategories(android.view.View root) {
    }
    
    private final void addCategoryCard(android.widget.LinearLayout container, com.example.spendwise.CategorySummary summary, int pct, java.lang.String sym, java.util.List<com.example.spendwise.Expense> expenses) {
    }
    
    private final void showCategoryDetail(com.example.spendwise.CategorySummary summary, java.util.List<com.example.spendwise.Expense> catExpenses, java.lang.String sym) {
    }
    
    private final void showAddCategoryDialog(com.example.spendwise.repositories.AppRepository repo, kotlin.jvm.functions.Function0<kotlin.Unit> onAdded) {
    }
}