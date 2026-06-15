package com.example.spendwise.db;

import androidx.annotation.NonNull;
import androidx.room.EntityDeleteOrUpdateAdapter;
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
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<UserEntity> __insertAdapterOfUserEntity;

  private final EntityDeleteOrUpdateAdapter<UserEntity> __updateAdapterOfUserEntity;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfUserEntity = new EntityInsertAdapter<UserEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `users` (`id`,`firstName`,`lastName`,`gender`,`email`,`passwordHash`,`profileImageUrl`,`monthlyBudget`,`currency`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final UserEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getFirstName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getFirstName());
        }
        if (entity.getLastName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getLastName());
        }
        if (entity.getGender() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getGender());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getEmail());
        }
        if (entity.getPasswordHash() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getPasswordHash());
        }
        if (entity.getProfileImageUrl() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getProfileImageUrl());
        }
        statement.bindDouble(8, entity.getMonthlyBudget());
        if (entity.getCurrency() == null) {
          statement.bindNull(9);
        } else {
          statement.bindText(9, entity.getCurrency());
        }
      }
    };
    this.__updateAdapterOfUserEntity = new EntityDeleteOrUpdateAdapter<UserEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `users` SET `id` = ?,`firstName` = ?,`lastName` = ?,`gender` = ?,`email` = ?,`passwordHash` = ?,`profileImageUrl` = ?,`monthlyBudget` = ?,`currency` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          @NonNull final UserEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getFirstName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getFirstName());
        }
        if (entity.getLastName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getLastName());
        }
        if (entity.getGender() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getGender());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getEmail());
        }
        if (entity.getPasswordHash() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getPasswordHash());
        }
        if (entity.getProfileImageUrl() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getProfileImageUrl());
        }
        statement.bindDouble(8, entity.getMonthlyBudget());
        if (entity.getCurrency() == null) {
          statement.bindNull(9);
        } else {
          statement.bindText(9, entity.getCurrency());
        }
        statement.bindLong(10, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final UserEntity user, final Continuation<? super Long> $completion) {
    if (user == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      return __insertAdapterOfUserEntity.insertAndReturnId(_connection, user);
    }, $completion);
  }

  @Override
  public Object update(final UserEntity user, final Continuation<? super Unit> $completion) {
    if (user == null) throw new NullPointerException();
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      __updateAdapterOfUserEntity.handle(_connection, user);
      return Unit.INSTANCE;
    }, $completion);
  }

  @Override
  public Object findByEmail(final String email,
      final Continuation<? super UserEntity> $completion) {
    final String _sql = "SELECT * FROM users WHERE email = ? LIMIT 1";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        if (email == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindText(_argIndex, email);
        }
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfFirstName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "firstName");
        final int _columnIndexOfLastName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastName");
        final int _columnIndexOfGender = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "gender");
        final int _columnIndexOfEmail = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "email");
        final int _columnIndexOfPasswordHash = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "passwordHash");
        final int _columnIndexOfProfileImageUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "profileImageUrl");
        final int _columnIndexOfMonthlyBudget = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "monthlyBudget");
        final int _columnIndexOfCurrency = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "currency");
        final UserEntity _result;
        if (_stmt.step()) {
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          final String _tmpFirstName;
          if (_stmt.isNull(_columnIndexOfFirstName)) {
            _tmpFirstName = null;
          } else {
            _tmpFirstName = _stmt.getText(_columnIndexOfFirstName);
          }
          final String _tmpLastName;
          if (_stmt.isNull(_columnIndexOfLastName)) {
            _tmpLastName = null;
          } else {
            _tmpLastName = _stmt.getText(_columnIndexOfLastName);
          }
          final String _tmpGender;
          if (_stmt.isNull(_columnIndexOfGender)) {
            _tmpGender = null;
          } else {
            _tmpGender = _stmt.getText(_columnIndexOfGender);
          }
          final String _tmpEmail;
          if (_stmt.isNull(_columnIndexOfEmail)) {
            _tmpEmail = null;
          } else {
            _tmpEmail = _stmt.getText(_columnIndexOfEmail);
          }
          final String _tmpPasswordHash;
          if (_stmt.isNull(_columnIndexOfPasswordHash)) {
            _tmpPasswordHash = null;
          } else {
            _tmpPasswordHash = _stmt.getText(_columnIndexOfPasswordHash);
          }
          final String _tmpProfileImageUrl;
          if (_stmt.isNull(_columnIndexOfProfileImageUrl)) {
            _tmpProfileImageUrl = null;
          } else {
            _tmpProfileImageUrl = _stmt.getText(_columnIndexOfProfileImageUrl);
          }
          final double _tmpMonthlyBudget;
          _tmpMonthlyBudget = _stmt.getDouble(_columnIndexOfMonthlyBudget);
          final String _tmpCurrency;
          if (_stmt.isNull(_columnIndexOfCurrency)) {
            _tmpCurrency = null;
          } else {
            _tmpCurrency = _stmt.getText(_columnIndexOfCurrency);
          }
          _result = new UserEntity(_tmpId,_tmpFirstName,_tmpLastName,_tmpGender,_tmpEmail,_tmpPasswordHash,_tmpProfileImageUrl,_tmpMonthlyBudget,_tmpCurrency);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object findById(final int id, final Continuation<? super UserEntity> $completion) {
    final String _sql = "SELECT * FROM users WHERE id = ? LIMIT 1";
    return DBUtil.performSuspending(__db, true, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfFirstName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "firstName");
        final int _columnIndexOfLastName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "lastName");
        final int _columnIndexOfGender = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "gender");
        final int _columnIndexOfEmail = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "email");
        final int _columnIndexOfPasswordHash = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "passwordHash");
        final int _columnIndexOfProfileImageUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "profileImageUrl");
        final int _columnIndexOfMonthlyBudget = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "monthlyBudget");
        final int _columnIndexOfCurrency = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "currency");
        final UserEntity _result;
        if (_stmt.step()) {
          final int _tmpId;
          _tmpId = (int) (_stmt.getLong(_columnIndexOfId));
          final String _tmpFirstName;
          if (_stmt.isNull(_columnIndexOfFirstName)) {
            _tmpFirstName = null;
          } else {
            _tmpFirstName = _stmt.getText(_columnIndexOfFirstName);
          }
          final String _tmpLastName;
          if (_stmt.isNull(_columnIndexOfLastName)) {
            _tmpLastName = null;
          } else {
            _tmpLastName = _stmt.getText(_columnIndexOfLastName);
          }
          final String _tmpGender;
          if (_stmt.isNull(_columnIndexOfGender)) {
            _tmpGender = null;
          } else {
            _tmpGender = _stmt.getText(_columnIndexOfGender);
          }
          final String _tmpEmail;
          if (_stmt.isNull(_columnIndexOfEmail)) {
            _tmpEmail = null;
          } else {
            _tmpEmail = _stmt.getText(_columnIndexOfEmail);
          }
          final String _tmpPasswordHash;
          if (_stmt.isNull(_columnIndexOfPasswordHash)) {
            _tmpPasswordHash = null;
          } else {
            _tmpPasswordHash = _stmt.getText(_columnIndexOfPasswordHash);
          }
          final String _tmpProfileImageUrl;
          if (_stmt.isNull(_columnIndexOfProfileImageUrl)) {
            _tmpProfileImageUrl = null;
          } else {
            _tmpProfileImageUrl = _stmt.getText(_columnIndexOfProfileImageUrl);
          }
          final double _tmpMonthlyBudget;
          _tmpMonthlyBudget = _stmt.getDouble(_columnIndexOfMonthlyBudget);
          final String _tmpCurrency;
          if (_stmt.isNull(_columnIndexOfCurrency)) {
            _tmpCurrency = null;
          } else {
            _tmpCurrency = _stmt.getText(_columnIndexOfCurrency);
          }
          _result = new UserEntity(_tmpId,_tmpFirstName,_tmpLastName,_tmpGender,_tmpEmail,_tmpPasswordHash,_tmpProfileImageUrl,_tmpMonthlyBudget,_tmpCurrency);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    }, $completion);
  }

  @Override
  public Object updateBudget(final int userId, final double budget,
      final Continuation<? super Unit> $completion) {
    final String _sql = "UPDATE users SET monthlyBudget = ? WHERE id = ?";
    return DBUtil.performSuspending(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, budget);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, userId);
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
