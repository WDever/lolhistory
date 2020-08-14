package com.example.lol_history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lol_history.model.SummonerIDinfo;
import com.example.lol_history.model.SummonerRankInfo;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    MainActivityViewModel viewModel;

    ConstraintLayout infoLayout;
    ImageView tierImage;
    TextView summonerName;
    TextView gameType;
    TextView tier;
    TextView tierPoint;
    TextView winRate;
    TextView winLose;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView historyList;

    ConstraintLayout inputLayout;
    EditText editTextSummoner;
    Button buttonSearch;

    ProgressBar progressBar;

    InputMethodManager inputMethodManager;

    boolean isVisibleInfoLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputMethodManager = (InputMethodManager)  this.getSystemService(Context.INPUT_METHOD_SERVICE);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        viewModel.getSummonerIDinfoLiveData().observe(this, new Observer<SummonerIDinfo>() {
            @Override
            public void onChanged(SummonerIDinfo summonerIDinfo) {
                if (summonerIDinfo == null) {
                    Toast notExistToast = Toast.makeText(getApplicationContext(), R.string.not_exist_summoner, Toast.LENGTH_SHORT);
                    notExistToast.show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        viewModel.getSummonerRankInfoLiveData().observe(this, new Observer<SummonerRankInfo>() {
            @Override
            public void onChanged(SummonerRankInfo summonerRankInfo) {
                if (summonerRankInfo != null) {
                    inputLayout.setVisibility(View.GONE);
                    isVisibleInfoLayout = true;
                    setRankInfo(summonerRankInfo);
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        viewModel.getHistoryAdapterLiveData().observe(this, new Observer<HistoryAdapter>() {
            @Override
            public void onChanged(HistoryAdapter historyAdapter) {
                if (historyAdapter == null) {
                    Toast historyErrorToast = Toast.makeText(getApplicationContext(), R.string.history_error, Toast.LENGTH_SHORT);
                    historyErrorToast.show();
                } else {
                    historyList.setAdapter(historyAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                }
                progressBar.setVisibility(View.GONE);
            }
        });

        infoLayout = findViewById(R.id.info_layout);
        tierImage = findViewById(R.id.tier_image);
        summonerName = findViewById(R.id.name);
        gameType = findViewById(R.id.info);
        tier = findViewById(R.id.tier);
        tierPoint = findViewById(R.id.tier_point);
        winRate = findViewById(R.id.win_rate);
        winLose = findViewById(R.id.win_lose);

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.searchSummoner(editTextSummoner.getText().toString());
            }
        });
        historyList = findViewById(R.id.rv_history);
        historyList.setLayoutManager(new LinearLayoutManager(this));
        historyList.setHasFixedSize(true);

        inputLayout = findViewById(R.id.input_layout);
        editTextSummoner = findViewById(R.id.et_input_summoner);
        buttonSearch = findViewById(R.id.btn_input_summoner);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                inputMethodManager.hideSoftInputFromWindow(editTextSummoner.getWindowToken(), 0);
                viewModel.searchSummoner(editTextSummoner.getText().toString());
                editTextSummoner.setText("");
            }
        });

        progressBar = findViewById(R.id.loading);
    }

    @Override
    public void onBackPressed() {
        if (isVisibleInfoLayout) {
            infoLayout.setVisibility(View.GONE);
            inputLayout.setVisibility(View.VISIBLE);
            isVisibleInfoLayout = !isVisibleInfoLayout;
        } else {
            finish();
        }
    }

    private void setRankInfo(SummonerRankInfo summonerRankInfo) {
        setTierEmblem(summonerRankInfo.getTier());
        summonerName.setText(summonerRankInfo.getSummonerName());

        if (summonerRankInfo.getTier().equals("UNRANKED")) {
            gameType.setText("");
            tier.setText("UNRANKED");
            tierPoint.setText("");
            winRate.setText("");
            winLose.setText("");
        } else {
            gameType.setText(summonerRankInfo.getQueueType());

            String tierRank = summonerRankInfo.getTier() + " " + summonerRankInfo.getRank();
            tier.setText(tierRank);

            String point = String.valueOf(summonerRankInfo.getLeaguePoints()) + "LP";
            tierPoint.setText(point);

            double rate = (double) summonerRankInfo.getWins() / (double) (summonerRankInfo.getLosses() + summonerRankInfo.getWins()) * 100;
            String winLosses = summonerRankInfo.getWins() + getResources().getString(R.string.win) + " / " + summonerRankInfo.getLosses() + getResources().getString(R.string.defeat);

            winRate.setText(String.format(Locale.getDefault(), "%.2f%%", rate));
            winLose.setText(winLosses);
        }

        infoLayout.setVisibility(View.VISIBLE);
    }

    private void setTierEmblem(String tier) {
        switch (tier) {
            case "UNRANKED":
                tierImage.setImageResource(R.drawable.emblem_unranked);
            case "IRON":
                tierImage.setImageResource(R.drawable.emblem_iron);
                break;
            case "BRONZE":
                tierImage.setImageResource(R.drawable.emblem_bronze);
                break;
            case "SILVER":
                tierImage.setImageResource(R.drawable.emblem_silver);
                break;
            case "GOLD":
                tierImage.setImageResource(R.drawable.emblem_gold);
                break;
            case "PLATINUM":
                tierImage.setImageResource(R.drawable.emblem_platinum);
                break;
            case "DIAMOND":
                tierImage.setImageResource(R.drawable.emblem_diamond);
                break;
            case "MASETER":
                tierImage.setImageResource(R.drawable.emblem_master);
                break;
            case "GRANDMASETER":
                tierImage.setImageResource(R.drawable.emblem_grandmaster);
                break;
            case "CHALLENGER":
                tierImage.setImageResource(R.drawable.emblem_challenger);
                break;
        }
    }
}

