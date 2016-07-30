package io.github.yesalam.urdudictionary.data;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.SQLException;

import io.github.yesalam.urdudictionary.data.DataContract.* ;

/**
 * Created by yesalam on 31-07-2016.
 */
public class UserDataProvider extends ContentProvider {



    private UserDataOpenHelper userDataOpenHelper  ;

    public static final int QUERY_TYPE_ITEM = 101 ;
    public static final int QUERY_AVAILABLE = 102;
    public static final int QUERY_ROW = 103 ;

    UriMatcher mUriMatcher = buildUriMatcher();

    private UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);


        uriMatcher.addURI(UserDataEntry.USER_CONTENT_AUTHORITY,"#",QUERY_TYPE_ITEM);


        uriMatcher.addURI(UserDataEntry.USER_CONTENT_AUTHORITY, "*/#", QUERY_AVAILABLE);

        uriMatcher.addURI(UserDataEntry.USER_CONTENT_AUTHORITY,UserDataEntry.PATH_USERDATA+"/#",QUERY_ROW);

        return uriMatcher;

    }


    @Override
    public boolean onCreate() {
        userDataOpenHelper = new UserDataOpenHelper(getContext()) ;
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor result = null ;
        switch (mUriMatcher.match(uri)){

            case QUERY_TYPE_ITEM :
                try {
                    result = userDataOpenHelper.getAllValues(Integer.parseInt(uri.getLastPathSegment()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case QUERY_AVAILABLE :
                try {
                    result = userDataOpenHelper.isPresent(Integer.parseInt(uri.getLastPathSegment()),uri.getPath()) ;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new UnsupportedOperationException() ;
        }
        return result;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = userDataOpenHelper.insertValue(values) ;
        /**
         * If record is added successfully
         */

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(UserDataEntry.CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

       return  null ;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        long stamp = Long.parseLong(uri.getLastPathSegment());
        userDataOpenHelper.deleteValues(stamp) ;
        return 1;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
