package org.app.liber.helper;

import org.app.liber.model.WalletModel;

public class WalletTxn {


    private DatabaseHelper databaseHelper;


    public WalletTxn(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void addToWallet(WalletModel model){
        databaseHelper.addWaletPoint(model);
    }

    public int getWalletAmnt(String mob){
        return databaseHelper.getWalletAmnt(mob);
    }
}
