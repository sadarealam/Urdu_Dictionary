package io.github.yesalam.urdudictionary.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import io.github.yesalam.urdudictionary.R;

import java.util.ArrayList;
import java.util.List;

import io.github.yesalam.urdudictionary.R ;
import io.github.yesalam.urdudictionary.adapter.FragmentAdapter;
import io.github.yesalam.urdudictionary.data.ColorSuggestion;
import io.github.yesalam.urdudictionary.data.ColorWrapper;
import io.github.yesalam.urdudictionary.data.DataHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = MainActivity.class.getSimpleName();
    protected static final String EXTRA_KEY_VERSION = "version";
    protected static final String EXTRA_KEY_THEME = "theme";
    private static final String EXTRA_KEY_VERSION_MARGINS = "version_margins";
    private static final String EXTRA_KEY_TEXT = "text";
    protected static final int NAV_ITEM_INVALID = -1;

    public static final int SPEECH_REQUEST_CODE = 4000;
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;

    private Toolbar mToolbar ;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout ;
    private FloatingSearchView mSearchView ;
    private FloatingActionButton mFab ;


    private int[] tabIcons = {
            R.drawable.ic_home_white_36dp,
            R.drawable.ic_favorite_white_36dp,
            R.drawable.ic_history_white_36dp
    } ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(null);
        setViewPager();
        setupFloatingSearch();


        // -----------------------------------------------------------------------------------------

        mSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {
                mDrawerLayout.openDrawer(GravityCompat.START); // finish();
            }

            @Override
            public void onMenuClosed() {

            }
        } );
        // -----------------------------------------------------------------------------------------

       // mSearchView.inflateOverflowMenu(R.menu.menu_main);

        setFab();
        setDrawer();
        setNavigationView();


    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getToolbar();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            mViewPager.setCurrentItem(0);
            /*Intent intent = new Intent(this, ToolbarActivity.class);
            intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR);
            intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
            intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_LIGHT);
            startActivity(intent);
            finish();*/
        }

        if (id == R.id.nav_fav) {
            mViewPager.setCurrentItem(1);
          /*  Intent intent = new Intent(this, ToolbarActivity.class);
            intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_TOOLBAR);
            intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_TOOLBAR_SMALL);
            intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_DARK);
            startActivity(intent);
            finish();*/
        }

        if (id == R.id.nav_history) {
            mViewPager.setCurrentItem(2);
            /*Intent intent = new Intent(this, MenuItemActivity.class);
            intent.putExtra(EXTRA_KEY_VERSION, SearchView.VERSION_MENU_ITEM);
            intent.putExtra(EXTRA_KEY_VERSION_MARGINS, SearchView.VERSION_MARGINS_MENU_ITEM);
            intent.putExtra(EXTRA_KEY_THEME, SearchView.THEME_LIGHT);
            startActivity(intent);
            finish();*/
        }



        if (id == R.id.nav_about) {
            /*Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            finish();*/
        }

        mDrawerLayout.closeDrawer(GravityCompat.START); // mDrawer.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else //noinspection StatementWithEmptyBody
            if (mSearchView != null && mSearchView.isSearchBarFocused()) { // TODO
                // mSearchView.close(true);
                mSearchView.setSearchFocused(false) ;
            } else {
                super.onBackPressed();
            }
    }

    private void getToolbar(){
        if(mToolbar == null ) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar) ;
            if(mToolbar != null) {
                mToolbar.setNavigationContentDescription(getString(R.string.app_name));
                setSupportActionBar(mToolbar);
            }
        }
    }

    private void setFab() {
        mFab = (FloatingActionButton) findViewById(R.id.fab_delete);
        if (mFab != null) {
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mSearchView.isSearchBarFocused()){
                        mSearchView.setSearchFocused(true);
                    }
                }
            });
        }
    }

    private void setDrawer(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (mDrawerLayout != null) {
            mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() { // new DrawerLayout.DrawerListener();
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    invalidateOptionsMenu();
                    if (mSearchView != null && mSearchView.isSearchBarFocused()) {
                        mSearchView.setSearchFocused(false);
                    }
                    if (mFab != null) {
                        mFab.hide();
                    }
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    invalidateOptionsMenu();
                    if (mFab != null) {
                        mFab.show();
                    }
                    mSearchView.setLeftMenuOpen(false);
                }
            });
        }
    }

    private void setNavigationView(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
            if (getNavItem() > -1) {
                navigationView.getMenu().getItem(getNavItem()).setChecked(true);
            }
        }
    }

    private int getNavItem() {
        return NAV_ITEM_INVALID;
    }

    private void setViewPager() {
        final FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), getString(R.string.home));
        adapter.addFragment(new FavFragment(), getString(R.string.fav));
        adapter.addFragment(new HistoryFragment(),getString(R.string.history));

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        if (mViewPager != null) {
            mViewPager.setAdapter(adapter);
        }

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);

            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        }
    }

    private void setupFloatingSearch() {
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        if(isVoiceAvailable()){
            mSearchView.inflateOverflowMenu(R.menu.menu_main);
        } else mSearchView.inflateOverflowMenu(R.menu.menu_main_novoice);
        mSearchView.setShowSearchKey(true);
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mSearchView.showProgress();

                    //simulates a query call to a data source
                    //with a new query.
                    DataHelper.findSuggestions(MainActivity.this, newQuery, 5, FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                        @Override
                        public void onResults(List<ColorSuggestion> results) {

                            //this will swap the data and
                            //render the collapse/expand animations as necessary
                            mSearchView.swapSuggestions(results);

                            //let the users know that the background
                            //process has completed
                            mSearchView.hideProgress();
                        }
                    });
                }

                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

                /*ColorSuggestion colorSuggestion = (ColorSuggestion) searchSuggestion;
                DataHelper.findColors(MainActivity.this, colorSuggestion.getBody(),
                        new DataHelper.OnFindColorsListener() {

                            @Override
                            public void onResults(List<ColorWrapper> results) {
                                mSearchResultsAdapter.swapData(results);
                            }

                        });*/
                Log.d(TAG, "onSuggestionClicked()");
            }

            @Override
            public void onSearchAction(String query) {
/*
                DataHelper.findColors(MainActivity.this, query,
                        new DataHelper.OnFindColorsListener() {

                            @Override
                            public void onResults(List<ColorWrapper> results) {
                                mSearchResultsAdapter.swapData(results);
                            }

                        });*/
                Log.d(TAG, "onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                mSearchView.clearQuery();

                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(DataHelper.getHistory(MainActivity.this, 3));
                if (mFab != null) {
                    mFab.hide();
                }
                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {
                if (mFab != null) {
                    mFab.show();
                }
                Log.d(TAG, "onFocusCleared()");
            }
        });


        //handle menu clicks the same way as you would
        //in a regular activity
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.action_voice_speak) {
                    onVoiceClicked();
                } else if(item.getItemId() == R.id.action_seting){
                    //just print action
                    Toast.makeText(getApplicationContext(), "Setting Clicked",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHamburger"
        mSearchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {

                Log.d(TAG, "onMenuOpened()");
                mDrawerLayout.openDrawer(GravityCompat.START);
            }

            @Override
            public void onMenuClosed() {
                Log.d(TAG, "onMenuClosed()");
            }
        });

        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHome"
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {

                Log.d(TAG, "onHomeClicked()");
            }
        });

        /*
         * Here you have access to the left icon and the text of a given suggestion
         * item after as it is bound to the suggestion list. You can utilize this
         * callback to change some properties of the left icon and the text. For example, you
         * can load the left icon images using your favorite image loading library, or change text color.
         *
         *
         * Important:
         * Keep in mind that the suggestion list is a RecyclerView, so views are reused for different
         * items in the list.
         */
       /* mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                ColorSuggestion colorSuggestion = (ColorSuggestion) item;

                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";

                if (colorSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }

                textView.setTextColor(Color.parseColor(textColor));
                String text = colorSuggestion.getBody()
                        .replaceFirst(mSearchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });*/

        //listen for when suggestion list expands/shrinks in order to move down/up the
        //search results list
       /* mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                mSearchResultsList.setTranslationY(newHeight);
            }
        });*/
    }

    @SuppressWarnings("SameParameterValue")
    private void perm(String permission, int permission_request) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, permission_request);
            }
        }
    }

    // implements ActivityCompat.OnRequestPermissionsResultCallback
    // http://developer.android.com/training/permissions/requesting.html
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                //noinspection StatementWithEmptyBody
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                break;
            default:
                break;
        }
    }


   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) {
                String searchWrd = results.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mSearchView.setSearchText(searchWrd);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private boolean isVoiceAvailable() {
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        return activities.size() != 0;
    }

    private void onVoiceClicked() {
        perm(Manifest.permission.RECORD_AUDIO, 0);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.action_voice_speak));
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);

        startActivityForResult(intent,SPEECH_REQUEST_CODE);
    }

}
