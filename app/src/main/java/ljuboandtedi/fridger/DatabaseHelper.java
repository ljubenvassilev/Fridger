package ljuboandtedi.fridger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ljuben Vassilev on 9/25/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Fridger.db";
    public static final String USERS_TABLE = "users_table";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USERS_TABLE +" (" +
                "ID INTEGER PRIMARY KEY," +
                "SALTY TEXT," +
                "MEATY TEXT, " +
                "PIQUANT TEXT, " +
                "BITTER TEXT, " +
                "SOUR TEXT, " +
                "SWEET BOOLEAN  )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addUser(String userId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("create table " + userId +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, LINK TEXT)");
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", userId);
        contentValues.put("SALTY", "NO");
        contentValues.put("MEATY", "NO");
        contentValues.put("PIQUANT", "NO");
        contentValues.put("BITTER", "NO");
        contentValues.put("SOUR", "NO");
        contentValues.put("SWEET", "NO");
        db.insert(USERS_TABLE, null, contentValues);
    }

    public void editUser(String userId, boolean salty, boolean meaty, boolean piquant, boolean bitter, boolean sour, boolean sweet){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", userId);
        contentValues.put("SALTY", salty);
        contentValues.put("MEATY", meaty);
        contentValues.put("PIQUANT", piquant);
        contentValues.put("BITTER", bitter);
        contentValues.put("SOUR", sour);
        contentValues.put("SWEET", sweet);
        db.update(USERS_TABLE, contentValues, "ID = ? ", new String[] { userId } );
    }

    public boolean userExists(String userID) {
        SQLiteDatabase db = getWritableDatabase();
        String Query = "Select * from " + USERS_TABLE + " where ID = " + userID;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public Cursor getUser(String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + USERS_TABLE + " WHERE ID=?", new String[] { userID } );
        return res;
    }
}
