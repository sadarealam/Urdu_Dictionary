package io.github.yesalam.urdudictionary.data;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by yesalam on 30-07-2016.
 */
public class WordSuggestion implements SearchSuggestion {

    String word ;

    public WordSuggestion(String word){
        this.word = word ;
    }

    public WordSuggestion(Parcel in){
        this.word = in.readString() ;
    }

    @Override
    public String getBody() {
        return word;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
    }

    public static Creator<WordSuggestion> CREATOR = new Creator<WordSuggestion>() {
        @Override
        public WordSuggestion createFromParcel(Parcel in) {
            return new WordSuggestion(in);
        }

        @Override
        public WordSuggestion[] newArray(int size) {
            return new WordSuggestion[size];
        }
    };
}
