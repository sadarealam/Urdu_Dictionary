package io.github.yesalam.urdudictionary.data;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import io.github.yesalam.urdudictionary.data.DataContract.*;

import java.util.HashMap;

/**
 * Created by yesalam on 30-07-2016.
 */
public class DictionaryOpenHelper extends SQLiteAssetHelper {

    private final String TAG = "DBOpenHelper" ;
    private static final String DATABASE_NAME = "urdu_1.db" ;
    private static final int DATABASE_VERSION = 1 ;

    private HashMap<String,String> mAliasMap;

    public DictionaryOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // This HashMap is used to map table fields to Custom Suggestion fields
        mAliasMap = new HashMap<String, String>();

        // Unique id for the each Suggestions ( Mandatory )
        mAliasMap.put("_ID",DictionaryEntry._ID + " as " + "_id" );

        // Text for Suggestions ( Mandatory )
        mAliasMap.put(SearchManager.SUGGEST_COLUMN_TEXT_1, DictionaryEntry.COLUMN_WORD + " as " + SearchManager.SUGGEST_COLUMN_TEXT_1);

        // Icon for Suggestions ( Optional )
        //mAliasMap.put( SearchManager.SUGGEST_COLUMN_ICON_1, FIELD_FLAG + " as " + SearchManager.SUGGEST_COLUMN_ICON_1);

        // This value will be appended to the Intent data on selecting an item from Search result or Suggestions ( Optional )
        mAliasMap.put( SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, DictionaryEntry._ID + " as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID );

    }

    public Cursor getWord(String[] selectionArgs){
        String selection = DictionaryEntry.COLUMN_WORD + " like ? " ;
        if(selectionArgs != null){
            selectionArgs[0] = selectionArgs[0] + "%" ;
        }

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setProjectionMap(mAliasMap);
        queryBuilder.setTables(DictionaryEntry.TABLE_NAME);

        Cursor result = queryBuilder.query(getReadableDatabase(),
                new String[]{"_ID",SearchManager.SUGGEST_COLUMN_TEXT_1,SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID},
                selection,
                selectionArgs,
                null,
                null,
                null);

        return result ;
    }

    public Cursor getAllWord(String query){
        String sql = "select "+DictionaryEntry._ID+","+DictionaryEntry.COLUMN_WORD+" from "+DictionaryEntry.TABLE_NAME+" where "+DictionaryEntry.COLUMN_WORD+" like '"+query+"%'";
        Cursor result = getReadableDatabase().rawQuery(sql, null);
        return result ;
    }

    public Cursor getDetail(int id){
        String sql = "select * from urdu where "+DictionaryEntry._ID+"= '"+id+"'" ;
        Log.e("DATABASE",sql);
        Cursor result = getReadableDatabase().rawQuery(sql,null);
        return result ;
    }

    public Cursor getDetail(String word){
        String sql = "select * from "+DictionaryEntry.TABLE_NAME+" where "+DictionaryEntry.COLUMN_WORD+" = '"+word+"'" ;
        Log.e("Database",sql);
        Cursor result = getReadableDatabase().rawQuery(sql,null);
        return result ;
    }

    public String getWord(String wordid){
        String sql = "select word from urdu where "+DictionaryEntry._ID+" = '"+wordid+"'" ;
        Log.e(TAG,sql);
        Cursor result = getReadableDatabase().rawQuery(sql,null);
        result.moveToFirst();
        String word = result.getString(0);
        return word;
    }
}
