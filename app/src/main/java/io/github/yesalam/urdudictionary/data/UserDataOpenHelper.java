package io.github.yesalam.urdudictionary.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

import io.github.yesalam.urdudictionary.data.DataContract.* ;

/**
 * Created by yesalam on 30-07-2016.
 */
public class UserDataOpenHelper extends SQLiteOpenHelper {



    public UserDataOpenHelper(Context context) {
        super(context, UserDataEntry.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+UserDataEntry.TABLE_NAME+" (" +
                UserDataEntry._ID+" integer PRIMARY KEY autoincrement,"+
                UserDataEntry.COLUMN_STAMP+"INTEGER,"+
                UserDataEntry.COLUMN_TYPE+" INTEGER,"+
                UserDataEntry.COLUMN_WORD+" TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLiteAdapter", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS Preference");
        onCreate(db);
    }

    public long insertValue(ContentValues values){

        int type = values.getAsInteger(UserDataEntry.COLUMN_TYPE) ;
        String word = values.getAsString(UserDataEntry.COLUMN_WORD) ;
        if(type==UserDataEntry.TYPE_HISTORY ){
            try {
                Cursor cursor = isPresent(UserDataEntry.TYPE_HISTORY,word);
                if(cursor.moveToFirst()) {
                    long stamp = Long.parseLong(cursor.getString(1));
                    deleteValues(stamp) ;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        values.put("STAMP",Long.valueOf(System.currentTimeMillis()));

       return getWritableDatabase().insert(UserDataEntry.TABLE_NAME,null,values);
    }

    public Cursor getAllValues(int type) throws SQLException{
        String query = "Select * from "+UserDataEntry.TABLE_NAME+" where TYPE = "+type+" order by STAMP desc ;";
        return getReadableDatabase().rawQuery(query,null);
    }

    public Cursor isPresent(int type,String word) throws SQLException {
        String query = "Select _id,STAMP from "+UserDataEntry.TABLE_NAME+" where TYPE = "+type+" and WORD = '"+word+"' ;"  ;
        return getReadableDatabase().rawQuery(query,null);
    }

    public boolean deleteValues(long stamp){
        return getWritableDatabase().delete(UserDataEntry.TABLE_NAME,"STAMP ="+stamp,null)>0;
    }
}

