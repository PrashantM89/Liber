package org.app.liber.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.app.liber.model.Book;
import org.app.liber.model.BookReviewModel;
import org.app.liber.model.UserTransactionModel;
import org.app.liber.model.WalletModel;

public class DatabaseHelper extends SQLiteOpenHelper {

    SharedPreferences pref;
    private static final String DATABASE_NAME = "liber_db";

    //book_tbl
    private static final String BOOKSHELVE_TAB_NAME = "my_bookshelve";
    private static final String COL1 = "book_desc";
    private static final String COL2 = "book_name";
    private static final String COL3 = "book_author";
    private static final String COL4 = "book_cover_url";
    private static final String COL_GENRE = "book_genre";
    private static final String COL_BOOK_RATING = "book_avg_rating";
    private static final String COL_BOOK_AVAILABILITY = "book_available";

    //library_tbl
    private static final String GLOBAL_LIB_TAB_NAME = "global_library";
    private static final String COL5 = "lib_book_name";
    private static final String COL6 = "lib_book_author";
    private static final String COL7 = "lib_book_cover";
    private static final String COL8 = "lib_book_desc";
    private static final String COL_LIB_GENRE = "lib_book_genre";
    private static final String COL_LIB_RATING = "book_avg_rating";

    //user_tx_tbl
    private static final String USER_TX_TAB_NAME = "user_tx_tbl";
    private static final String COL9 = "tx_id";
    private static final String COL10 = "tx_date";
    private static final String COL11 = "tx_status";
    private static final String COL12 = "tx_mode";
    private static final String COL13 = "tx_delvry_sts";
    private static final String COL14 = "tx_usr";

    //wallet_tbl
    private static final String USER_WALLET_TAB_NAME = "user_wallet";
    private static final String COL_WLT_ID = "wlt_id";
    private static final String COL_WLT_AMT_ADDED = "wlt_amnt_aded";
    private static final String COL_WLT_USR_MOB = "wlt_usr_mob";
    private static final String COL_WLT_DATE = "wlt_date";

    //Review_tbl
    private static final String REVIEW_TAB_NAME = "review_tbl";
    private static final String REVIEW_NAME = "review_name";
    private static final String REVIEW_RATING_STAR = "review_star";
    private static final String REVIEW_SUMMRY = "review_summary";
    private static final String REVIEW_BOOK_NAME = "review_book_name";


    Context c;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null  , 1);
        c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + BOOKSHELVE_TAB_NAME + "(" + COL2 + " TEXT," + COL3 + " TEXT," + COL4 +" TEXT,"+COL1 +" TEXT,"+COL_GENRE+" TEXT)";
        sqLiteDatabase.execSQL(createTable);
        String createGlobalLibTbl = "CREATE TABLE " + GLOBAL_LIB_TAB_NAME + "(" + COL5 + " TEXT," + COL6 + " TEXT," + COL7 +" TEXT,"+ COL8 +" TEXT," + COL_LIB_GENRE+ " TEXT," + COL_LIB_RATING + " TEXT)";
        sqLiteDatabase.execSQL(createGlobalLibTbl);
        String createUsrTxTbl = "CREATE TABLE " + USER_TX_TAB_NAME + "(" + COL9 + " TEXT," + COL10 + " TEXT," + COL11 +" TEXT,"+ COL12 +" TEXT,"+ COL13 +" TEXT," + COL14 + " TEXT)";
        sqLiteDatabase.execSQL(createUsrTxTbl);
        String createUsrWltTbl = "CREATE TABLE " + USER_WALLET_TAB_NAME + "(" + COL_WLT_ID + " INT," + COL_WLT_AMT_ADDED + " INT," + COL_WLT_USR_MOB +" TEXT," + COL_WLT_DATE + " TEXT)";
        sqLiteDatabase.execSQL(createUsrWltTbl);
        String createReviewTbl = "CREATE TABLE " + REVIEW_TAB_NAME + "(" + REVIEW_NAME + " TEXT," + REVIEW_RATING_STAR + " TEXT," + REVIEW_SUMMRY +" TEXT," + REVIEW_BOOK_NAME + " TEXT)";
        sqLiteDatabase.execSQL(createReviewTbl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ BOOKSHELVE_TAB_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ GLOBAL_LIB_TAB_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ USER_TX_TAB_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ USER_WALLET_TAB_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ REVIEW_TAB_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean addReview(BookReviewModel review){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REVIEW_NAME, review.getReaderName());
        contentValues.put(REVIEW_RATING_STAR, review.getRatingStars());
        contentValues.put(REVIEW_SUMMRY, review.getReviewStr());
        contentValues.put(REVIEW_BOOK_NAME, review.getBookName());

        long result = sqLiteDatabase.insert(REVIEW_TAB_NAME, null, contentValues);
        if(result == -1){
            Toast.makeText(c, "Your review couldn't be added. Try again.",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            Toast.makeText(c, "Your review is saved.",Toast.LENGTH_SHORT).show();
            return true;
        }

    }


    public Cursor getReview(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String query = "SELECT * FROM review_tbl";
        Cursor data = sqLiteDatabase.rawQuery(query,null);

        return data;
    }



    public boolean addData(Book book){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, book.getTitle());
        contentValues.put(COL3, book.getAuthors());
        contentValues.put(COL4, book.getSmallThumbnailLink());
        contentValues.put(COL1, book.getDescription());

        long result = sqLiteDatabase.insert(BOOKSHELVE_TAB_NAME, null, contentValues);
        if(result == -1){
            Toast.makeText(c, "Book couldn't be added. Try again.",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            Toast.makeText(c, "Book added in your bookshelf.",Toast.LENGTH_SHORT).show();
            return true;
        }

    }



    public boolean removeData(String book){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int i = sqLiteDatabase.delete(BOOKSHELVE_TAB_NAME, COL2 + "=?", new String[]{book});

        if(i == -1){
            Toast.makeText(c, "Book couldn't be deleted. Try again.",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            Toast.makeText(c, "Book removed from your bookshelf.",Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    public boolean addLibraryData(){
        pref = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = pref.edit();

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(COL5, "2 States");
        contentValues1.put(COL6, "Chetan Bhagat");
        contentValues1.put(COL7, "http://books.google.com/books/content?id=AsNSz-1gUcMC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api");
        contentValues1.put(COL_LIB_GENRE, "Indic fiction (English)");
        contentValues1.put(COL_LIB_RATING,"3.5");
        contentValues1.put(COL8,"Fourth book by the bestselling author Chetan Bhagat. 2 States is a story about Krish and Ananya. They are from two different states of India, deeply in love and want to get married. Of course, their parents don't agree. To convert their love story into a love marriage, the couple have a tough battle in front of them.");
        long result = sqLiteDatabase.insert(GLOBAL_LIB_TAB_NAME, null, contentValues1);
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(COL5, "The Other Side of Me");
        contentValues2.put(COL6, "Sydney Sheldon");
        contentValues2.put(COL7, "http://books.google.com/books/content?id=NunCrjDsI7kC&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api");
        contentValues2.put(COL_LIB_RATING,"4.5");
        contentValues2.put(COL_LIB_GENRE, "Fiction");
        contentValues2.put(COL8,"A brilliant, highly spirited memoir of Sidney Sheldon's early life that provides as compulsively readable and racy a narrative as any of his bestselling novels.");
        long result2 = sqLiteDatabase.insert(GLOBAL_LIB_TAB_NAME, null, contentValues2);
        ContentValues contentValues3 = new ContentValues();
        contentValues3.put(COL5, "The Silent Widow");
        contentValues3.put(COL6, "Sydney Sheldon");
        contentValues3.put(COL_LIB_GENRE, "Fiction");
        contentValues3.put(COL_LIB_RATING,"3.0");
        contentValues3.put(COL7, "http://books.google.com/books/content?id=eHaOswEACAAJ&printsec=frontcover&img=1&zoom=5&source=gbs_api");
        contentValues3.put(COL8,"New York Times Bestselling Author Young, beautiful and successful, psychotherapist Nikki Roberts should have the world at her feet. Instead, she's mourning her husband after his tragic death in a car accident - her world shattered irrevocably. Nikki's life takes another dark twist when one of her patients is brutally murdered. Before detectives Agnelli and Johnson can catch a break, another victim turns up - Nikki's PA, reformed drug addict Trey Raymond. And when the post-mortem DNA results reveal 'dead' skin cells under the victims' fingernails, LA is gripped by sensational reports of a predatory Zombie Killer. Before long, someone makes an attempt on Nikki's life. And when reports surface challenging the nature of her late husband's 'accident', it's clear that Nikki's number is marked. The question is, why? With the police at a dead end, Nikki drafts in Derek Moody, a PI with a deep and nefarious case history. Moody investigated the infamous Charlotte Clancy missing persons' case in New Mexico 10 years ago, a case that still haunts him today - and which draws terrifying comparisons to the current violence gripping LA. Nikki finds herself at the heart of a dangerous web of drug rings and corruption. One man, a shadowy master manipulator, is pulling all the strings - and he'll go to any lengths to keep Nikki silent.");
        long result3 = sqLiteDatabase.insert(GLOBAL_LIB_TAB_NAME, null, contentValues3);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL5, "Harry Potter and the Cursed Child");
        contentValues.put(COL6, "J. K. Rowling");
        contentValues.put(COL7, "http://books.google.com/books/content?id=2BMDuAEACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api");
        contentValues.put(COL_LIB_GENRE, "Juvenile Nonfiction");
        contentValues.put(COL_LIB_RATING,"2.5");
        contentValues.put(COL8,"The Eighth Story. Nineteen Years Later. Based on an original story by J.K. Rowling, John Tiffany, and Jack Thorne, a play by Jack Thorne. It was always difficult being Harry Potter and it isn't much easier now that he is an overworked employee of the Ministry of Magic, a husband, and father of three school-age children. While Harry grapples with a past that refuses to stay where it belongs, his youngest son, Albus, must struggle with the weight of a family legacy he never wanted. As past and present fuse ominously, both father and son learn the uncomfortable truth: Sometimes, darkness comes from unexpected places. The playscript for Harry Potter and the Cursed Child was originally released as a \\\"special rehearsal edition\\\" alongside the opening of Jack Thorne's play in London's West End in summer 2016. Based on an original story by J.K. Rowling, John Tiffany, and Jack Thorne, the play opened to rapturous reviews from theatergoers and critics alike, while the official playscript became an immediate global bestseller.");
        long result4 = sqLiteDatabase.insert(GLOBAL_LIB_TAB_NAME, null, contentValues);
        ContentValues contentValues5 = new ContentValues();
        contentValues5.put(COL5, "Tell Me Your Dreams");
        contentValues5.put(COL6, "Sydney Sheldon");
        contentValues5.put(COL7, "http://books.google.com/books/content?id=_J-NHwjl7N8C&printsec=frontcover&img=1&zoom=5&edge=curl&source=gbs_api");
        contentValues5.put(COL_LIB_GENRE, "Fiction");
        contentValues5.put(COL_LIB_RATING,"3.5");
        contentValues5.put(COL8,"Fourth book by the bestselling author Chetan Bhagat. 2 States is a story about Krish and Ananya. They are from two different states of India, deeply in love and want to get married. Of course, their parents don't agree. To convert their love story into a love marriage, the couple have a tough battle in front of them.");
        long result5 = sqLiteDatabase.insert(GLOBAL_LIB_TAB_NAME, null, contentValues5);

        editor.putString("data_in_lib","y");
        return true;
    }

    public Cursor getData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String query = "SELECT * FROM my_bookshelve";
        Cursor data = sqLiteDatabase.rawQuery(query,null);

        return data;
    }

    public boolean addUsrTxData(UserTransactionModel data) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put(COL9, data.getTxId());
            contentValues1.put(COL10, data.getTxDate());
            contentValues1.put(COL11, data.getTxStatus());
            contentValues1.put(COL12, data.getTxMode());
            contentValues1.put(COL13, data.getDeliveryStatus());
            contentValues1.put(COL14, "7338239977");
            long result = sqLiteDatabase.insert(USER_TX_TAB_NAME, null, contentValues1);
            return true;
    }

    public Cursor getTxData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String query = "SELECT * FROM "+USER_TX_TAB_NAME;
        Cursor data = sqLiteDatabase.rawQuery(query,null);
        return data;
    }

    public Cursor getLibraryData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String query = "SELECT * FROM "+GLOBAL_LIB_TAB_NAME;
        Cursor data = sqLiteDatabase.rawQuery(query,null);
        return data;
    }


    public boolean addWaletPoint(WalletModel wallet) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValuesWallet = new ContentValues();
        contentValuesWallet.put(COL_WLT_ID, 1);
        contentValuesWallet.put(COL_WLT_AMT_ADDED, wallet.getAmountAdd());
        contentValuesWallet.put(COL_WLT_USR_MOB, wallet.getUserMob());
        contentValuesWallet.put(COL_WLT_DATE, wallet.getTxnDate());

        long result = sqLiteDatabase.insert(USER_WALLET_TAB_NAME, null, contentValuesWallet);
        if (result == -1) {
            Toast.makeText(c, "Wallet not updated.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Toast.makeText(c, "Wallet updated.", Toast.LENGTH_SHORT).show();
            return true;
        }
    }



    public int getWalletAmnt(String userMob){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int totalAmnt = 0;
        String query = "SELECT "+ COL_WLT_USR_MOB +", SUM(" + COL_WLT_AMT_ADDED + ") AS total FROM " + USER_WALLET_TAB_NAME + " GROUP BY "+COL_WLT_USR_MOB+" HAVING "+ COL_WLT_USR_MOB +" = "+userMob;
        Cursor c = sqLiteDatabase.rawQuery(query,null);
        if(c.moveToFirst()){
            totalAmnt = c.getInt(c.getColumnIndex("total"));
        }

        System.out.println("Wallet Total "+totalAmnt);
        return totalAmnt;
    }
}
