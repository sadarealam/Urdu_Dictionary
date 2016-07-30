package io.github.yesalam.urdudictionary.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by yesalam on 30-07-2016.
 */
public class DataContract {









    public static final class DictionaryEntry implements BaseColumns {

        // The "Content authority" is a name for the entire content provider, similar to the
        // relationship between a domain name and its website.  A convenient string to use for the
        // content authority is the package name for the app, which is guaranteed to be unique on the
        // device.
        public static final String DICTIONARY_CONTENT_AUTHORITY = "io.github.yesalam.urdudictionary.dictionary";

        // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
        // the content provider.
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + DICTIONARY_CONTENT_AUTHORITY);

        // Possible paths (appended to base content URI for possible URI's)
        // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
        // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
        // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
        // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
        public static final String PATH_DICTIONARY = "dictionary";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DICTIONARY).build();

        public static final String TABLE_NAME = "urdu" ;

        public static final String COLUMN_WORD = "word" ;

        public static final String COLUMN_ROMAN = "roman" ;

        public static final String COLUMN_URDU = "urdu" ;

    }

    public static final class UserDataEntry implements BaseColumns {

        // The "Content authority" is a name for the entire content provider, similar to the
        // relationship between a domain name and its website.  A convenient string to use for the
        // content authority is the package name for the app, which is guaranteed to be unique on the
        // device.
        public static final String USER_CONTENT_AUTHORITY = "io.github.yesalam.urdudictionary.user";

        // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
        // the content provider.
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + USER_CONTENT_AUTHORITY);

        // Possible paths (appended to base content URI for possible URI's)
        // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
        // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
        // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
        // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
        public static final String PATH_USERDATA = "userdata";

        public static final int TYPE_WOTD = 1 ;
        public static final int TYPE_HISTORY = 2 ;
        public static final int TYPE_FAB = 3 ;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERDATA).build();

        public static final String DATABASE_NAME = "user" ;

        public static final String TABLE_NAME = "user" ;

        public static final String COLUMN_STAMP = "stamp" ;

        public static final String COLUMN_TYPE = "type" ;

        public static final String COLUMN_WORD = "word" ;
    }

}
