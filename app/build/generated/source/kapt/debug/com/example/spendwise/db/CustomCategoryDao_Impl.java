package com.example.spendwise.db;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Long;
import java.lang.NullPointerException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class CustomCategoryDao_Impl implements CustomCategoryDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<CustomCategoryEntity> __insertAdapterOfCustomCategoryEntity;

  public CustomCategoryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfCustomCategoryEntity = new EntityInsertAdapter<CustomCategoryEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `custom_categories` (`id`,`userId`,`name`,`icon`,`color`,`budgetLimit`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final CustomCategoryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getUserId());
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getName());
        }
        if (entity.getIcon() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getIcon());
        }
        if (entity.getColor() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getColor());
        }
        statement.bindDouble(6, entity.getBudgetLimit());
      }
    };
  }

  @Override
  public Object insert(final CustomCategoryEntity category,
      final Continuation<? super Long> $completion) {
    if (category == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      return __insertAdapterOfCustomCategoryEntity.insertAndReturnId(_connection, category);
    }, $completion);
  }

  @Override
  public Object getAll(final int userId,
      final Continuation<? super List<CustomCategoryEntity>> $completion) {
    final String _sql = "SELECT * FROM custom_categories WHERE userId = ?";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, userId);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "userId");
        final int _columnIndexOfName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "name");
        final int _columnIndexOfIcon = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "icon");
        final int _columnIndexOfColor = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "color");
        final int _columnIndexOfBudgetLimit = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "budgetLimit");
        final List<CustomCategoryEntity> _result = new ArrayList<CustomCategoryEntity>();
        while (_stmt.step()) {
          final CustomCategoryEntity _item;
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          final int _tmpUserId;
          _tmpUserId = (int) (_stmt.getLong(_columnIndexOfUserId));
          final String _tmpName;
          if (_stmt.isNull(_columnIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _stmt.getText(_columnIndexOfName);
          }
          final String _tmpIcon;
          if (_stmt.isNull(_columnIndexOfIcon)) {
            _tmpIcon = null;
          } else {
            _tmpIcon = _stmt.getText(_columnIndexOfIcon);
          }
          final String _tmpColor;
          if (_stmt.isNull(_columnIndexOfColor)) {
            _tmpColor = null;
          } else {
            _tmpColor = _stmt.getText(_columnIndexOfColor);
          }
          final double _tmpBudgetLimit;
          _tmpBudgetLimit = _stmt.getDouble(_columnIndexOfBudgetLimit);
          _item = new CustomCategoryEntity(_tmpId,_tmpUserId,_tmpName,_tmpIcon,_tmpColor,_tmpBudgetLimit);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object deleteByName(final int userId, final String name,
      final Continuation<? super Unit> $completion) {
    final String _sql = "DELETE FROM custom_categories WHERE userId = ? AND name = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, userId);
        _argIndex = 2;
        if (name == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, name);
        }
        _stmt.step();
        return Unit.INSTANCE;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
