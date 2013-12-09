package com.grootcode.base.sync;

import android.accounts.Account;
import android.content.ContentResolver;
import android.os.Bundle;

import com.grootcode.base.util.ContractUtils;

public class SyncHelper {

    public static void requestManualSync(Account mChosenAccount) {
        Bundle b = new Bundle();
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(mChosenAccount, ContractUtils.CONTENT_AUTHORITY, b);
    }
}
