package com.arcasolutions.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.arcasolutions.api.model.Account;
import com.arcasolutions.database.Database;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class AccountHelper {

    public static final String ACTION_ACCOUNT_CHANGED
            = "account_change";

    private static final Intent INTENT_ACCOUNT_CHANGED
            = new Intent(ACTION_ACCOUNT_CHANGED);

    private static final String TAG = "AccountHelper";

    private final Database mDatabase;
    private final Context mContext;

    public AccountHelper(Context context) {
        mDatabase = new Database(context);
        mContext = context;
    }

    public void store(Account account) throws SQLException {
        Dao<Account, Long> dao = getAccountDao();
        dao.createOrUpdate(account);
        sendBroadcastAccountChanged();
    }

    public void remove() {
        try {
            Dao<Account, Long> dao = getAccountDao();
            List<Account> accounts = dao.queryForAll();
            dao.delete(accounts);
            sendBroadcastAccountChanged();
        } catch (Exception ignored) {
            Log.e(TAG, ignored.getMessage(), ignored);
        }
     }

    public Account getAccount() {
        try {
            Dao<Account, Long> dao = getAccountDao();
            List<Account> accounts = dao.queryForAll();
            if (accounts != null && !accounts.isEmpty()) {
                return accounts.get(0);
            }
        } catch (SQLException ignored) {
            Log.e(TAG, ignored.getMessage(), ignored);
        }
        return null;
    }

    public boolean hasAccount() {
        return getAccount() != null;
    }

    private Dao<Account, Long> getAccountDao() throws SQLException {
        return mDatabase.getDao(Account.class);
    }

    private void sendBroadcastAccountChanged() {
        mContext.sendBroadcast(INTENT_ACCOUNT_CHANGED);
    }

}
