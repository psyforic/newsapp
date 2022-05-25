package com.example.mynews.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.mynews.models.Article;
import com.example.mynews.models.NewsModel;
import com.example.mynews.utils.Utils;
import com.sdsmdg.tastytoast.TastyToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HealthFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.recyclerview)
    public RecyclerView recyclerview;

    @BindView(R.id.swipe_container)
    public SwipeRefreshLayout swipe_container;

    String category = "health";
    private List<Article> articleList = new ArrayList<>();
    Adapter adapter;
    private Context context;
    public RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(false);
        swipe_container.setOnRefreshListener(this);
        swipe_container.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefresh(category);
    }

    public void loadJson(String keyword) {
        swipe_container.setRefreshing(true);
        ApiInterface apiInterface = ApiClient.getHttpClient().create(ApiInterface.class);
        Call<NewsModel> call;
        String country = Utils.getCountry();
        String language = Utils.getLanguage();
        if (keyword.length() > 0) {
            call = apiInterface.getNewsSearch(keyword, language, "publishedAt", getString(R.string.api_key));

        } else {
            call = apiInterface.getByCategory(country, category, getString(R.string.api_key));
        }
        call.enqueue(new Callback<NewsModel>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {

                if (response.isSuccessful() && Objects.requireNonNull(response.body()).getArticle() != null) {
                    if (!articleList.isEmpty()) {
                        articleList.clear();
                    }
                    articleList = response.body().getArticle();
                    adapter = new Adapter(getActivity(), articleList);
                    recyclerview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    swipe_container.setRefreshing(false);
//                    adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            Article article = articleList.get(position);
//                            ImageView image1 = view.findViewById(R.id.image1);
//                            Intent intent = new Intent(MainActivity.this, ArticleActivity.class);
//                            intent.putExtra("url", article.getUrl());
//                            intent.putExtra("title", article.getTitle());
//                            intent.putExtra("desc", article.getDecsription());
//                            intent.putExtra("published_at", article.getPublishedAt());
//                            intent.putExtra("author", article.getAuthor());
//                            intent.putExtra("source", article.getSource().getName());
//                            intent.putExtra("imageUrl", article.getUrlToImage());
//                            Pair<View, String> pair = Pair.create((View) image1, ViewCompat.getTransitionName(image1));
//
//                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                                    MainActivity.this, image1, getString(R.string.transition));
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
//                                startActivity(intent, optionsCompat.toBundle());
//                            } else {
//                                startActivity(intent);
//                            }
//
//                        }
//                    }); TastyToast.makeText(getActivity(), "Sorry, no result" + response.body().getStatus(), TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                } else {
                    swipe_container.setRefreshing(false);
                    TastyToast.makeText(context, "Sorry, no result" + response.body().getStatus(), TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                }
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                //TastyToast.makeText(MainActivity.this, "Sorry, no result" + t.getMessage(), TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();
                swipe_container.setRefreshing(false);
            }
        });
    }

    private void swipeRefresh(final String keyword) {
        swipe_container.post(new Runnable() {
            @Override
            public void run() {
                loadJson(keyword);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadJson(category);
    }
}