package org.app.liber.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;

public class LiberDBAdapter {
    private static final String TAG = "LiberDBAdapter";
    public static final String DATABASE_NAME = "Liber.db";


    /**
     * All the tables in the database
     */
    private static final String[] ALL_TABLES = {
    };

    private static Context context;

    private static volatile SQLiteDatabase db;

    public static void init(Context context) {
        LiberDBAdapter.context = context.getApplicationContext();
    }

    // Bill Pugh Singleton Implementation
    private static class SingletonHolder {
        private static final PodDBHelper dbHelper = new PodDBHelper(LiberDBAdapter.context, DATABASE_NAME, null);
        private static final LiberDBAdapter dbAdapter = new LiberDBAdapter();
    }

    public static LiberDBAdapter getInstance() {
        return SingletonHolder.dbAdapter;
    }

    private LiberDBAdapter() {
    }

    public synchronized LiberDBAdapter open(Context c) {
        init(c);
        if (db == null || !db.isOpen() || db.isReadOnly()) {
            db = openDb();
        }
        return this;
    }

    @SuppressLint("NewApi")
    private SQLiteDatabase openDb() {
        SQLiteDatabase newDb;
        try {
            newDb = SingletonHolder.dbHelper.getWritableDatabase();
            if (Build.VERSION.SDK_INT >= 16) {
                newDb.disableWriteAheadLogging();
            }
        } catch (SQLException ex) {
            Log.e(TAG, Log.getStackTraceString(ex));
            newDb = SingletonHolder.dbHelper.getReadableDatabase();
        }
        return newDb;
    }

    public synchronized void close() {
        // do nothing
    }

    /**
     * Called when a database corruption happens
     */
    public static class PodDbErrorHandler implements DatabaseErrorHandler {
        @Override
        public void onCorruption(SQLiteDatabase db) {
            Log.e(TAG, "Database corrupted: " + db.getPath());

            File dbPath = new File(db.getPath());
            File backupFolder = LiberDBAdapter.context.getExternalFilesDir(null);
            File backupFile = new File(backupFolder, "CorruptedDatabaseBackup.db");
            try {
                //FileUtils.copyFile(dbPath, backupFile);
                Log.d(TAG, "Dumped database to " + backupFile.getPath());
            } catch (Exception e) {
                Log.d(TAG, Log.getStackTraceString(e));
            }

            new DefaultDatabaseErrorHandler().onCorruption(db); // This deletes the database
        }
    }

    /**
     * Helper class for opening the Antennapod database.
     */
    private static class PodDBHelper extends SQLiteOpenHelper {

        private static final int VERSION = 1060596;

        private final Context context;

        /**
         * Constructor.
         *
         * @param context Context to use
         * @param name    Name of the database
         * @param factory to use for creating cursor objects
         */
        public PodDBHelper(final Context context, final String name,
                           final SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, VERSION, new PodDbErrorHandler());
            this.context = context;
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
                              final int newVersion) {
//            EventBus.getDefault().post(ProgressEvent.start(context.getString(R.string.progress_upgrading_database)));
//            Log.w("DBAdapter", "Upgrading from version " + oldVersion + " to "
//                    + newVersion + ".");
//            DBUpgrader.upgrade(db, oldVersion, newVersion);
//            EventBus.getDefault().post(ProgressEvent.end());
        }
    }
}
