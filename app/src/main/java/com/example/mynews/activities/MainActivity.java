package com.example.mynews.activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.mynews.R;
import com.example.mynews.adapters.Adapter;
import com.example.mynews.api.ApiClient;
import com.example.mynews.api.ApiInterface;
import com.example.mynews.fragments.*;
import com.example.mynews.interfaces.SelectItemClickListener;
import com.example.mynews.models.Article;
import com.example.mynews.models.NewsModel;
import com.example.mynews.utils.Tools;
import com.example.mynews.utils.Utils;
import com.sdsmdg.tastytoast.TastyToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_content)
    View parent_view;

    public RecyclerView recyclerview;

//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.swipe_container)
//    public SwipeRefreshLayout swipe_container;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lyt_tabs)
    HorizontalScrollView hrz_scroll;
    private List<Article> articleList = new ArrayList<>();
    Adapter adapter;
    String title;
    String category = "general";
    public RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_round);
        ButterKnife.bind(this);
        recyclerview = findViewById(R.id.recyclerview);
        chooseFragment(new HomeFragment());
        initToolbar();
      //  initData();
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    private void initData() {
//        layoutManager = new LinearLayoutManager(this);
//        recyclerview.setLayoutManager(layoutManager);
//        recyclerview.setItemAnimator(new DefaultItemAnimator());
//        recyclerview.setNestedScrollingEnabled(false);
//        swipe_container.setOnRefreshListener(this);
//        swipe_container.setColorSchemeResources(R.color.colorPrimary,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);
//        swipeRefresh("");
    }

    @SuppressLint("NonConstantResourceId")
    public void onButtonTabClick(View v) {
        title = ((Button) v).getText().toString();
        title = Tools.toCamelCase(title);
        switch (v.getId()) {
            case R.id.btn_business:
                chooseFragment(new BusinessFragment());
                break;

            case R.id.btn_home:
                chooseFragment(new HomeFragment());
                break;
            case R.id.btn_technology:
                chooseFragment(new TechnologyFragment());
                break;

            case R.id.btn_sports:
                chooseFragment(new SportsFragment());
                break;

            case R.id.btn_entertainment:
                chooseFragment(new EntertainmentFragment());
                break;

            case R.id.btn_health:
                chooseFragment(new HealthFragment());
                break;
            case R.id.btn_science:
                chooseFragment(new ScienceFragment());
                break;
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
        //  ViewAnimation.fadeOutIn(((NestedScrollView) findViewById(R.id.)));
    }

//    public void loadJson(String keyword) {
//        swipe_container.setRefreshing(true);
//        ApiInterface apiInterface = ApiClient.getHttpClient().create(ApiInterface.class);
//        Call<NewsModel> call;
//        String country = Utils.getCountry();
//        String language = Utils.getLanguage();
//        if (keyword.length() > 0) {
//            call = apiInterface.getNewsSearch(keyword, language, "publishedAt", getString(R.string.api_key));
//
//        } else {
//            call = apiInterface.getByCategory(country, category, getString(R.string.api_key));
//        }
//        call.enqueue(new Callback<NewsModel>() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
//
//                if (response.isSuccessful() && Objects.requireNonNull(response.body()).getArticle() != null) {
//                    if (!articleList.isEmpty()) {
//                        articleList.clear();
//                    }
//                    articleList = response.body().getArticle();
//                    adapter = new Adapter(MainActivity.this, articleList);
//                    recyclerview.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//                    swipe_container.setRefreshing(false);
//                } else {
//                    swipe_container.setRefreshing(false);
//                    TastyToast.makeText(MainActivity.this, "Sorry, no result" + response.body().getStatus(), TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewsModel> call, Throwable t) {
//                //TastyToast.makeText(MainActivity.this, "Sorry, no result" + t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
//                swipe_container.setRefreshing(false);
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search_button).getActionView();
        MenuItem searhItem = menu.findItem(R.id.search_button);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Find latest news");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 2) {
               //     swipeRefresh(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searhItem.getIcon().setVisible(false, false);
        return true;
    }

    private void chooseFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.page, newFragment).addToBackStack(title).commit();
    }

//    @Override
//    public void onRefresh() {
//        loadJson("");
//    }
//
//    private void swipeRefresh(final String keyword) {
//        swipe_container.post(new Runnable() {
//            @Override
//            public void run() {
//                loadJson(keyword);
//            }
//        });
//    }
}