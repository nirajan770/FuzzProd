package com.fuzzprod.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fuzzprod.R;
import com.fuzzprod.api.FuzzAPI;
import com.fuzzprod.api.RetroService;
import com.fuzzprod.api.model.Item;
import com.fuzzprod.ui.BaseActivity;
import com.fuzzprod.ui.adapter.CardAdapter;
import com.fuzzprod.util.Log;

import java.util.List;

import butterknife.Bind;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HomeActivity extends BaseActivity implements Observer<List<Item>>, CardAdapter.onItemClickListener {

    private static final String TAG = "HomeActivity";

    @Bind(R.id.recycler_list)
    RecyclerView mRecyclerView;

    RecyclerView.LayoutManager mLayoutManager;

    private CardAdapter mCardAdapter;

    private List<Item> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_home);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mCardAdapter = new CardAdapter();
        mRecyclerView.setAdapter(mCardAdapter);

        mCardAdapter.setOnItemClickListener(this);

        getJsonFeed();
    }

    private void getJsonFeed() {
        FuzzAPI fuzzAPI = RetroService.createRetrofitService(this);
        fuzzAPI.items()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       if (id == R.id.action_filter) {
           startAlertDialog();
           return true;
       }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCompleted() {
        Log.d(TAG, "completed");
    }

    @Override
    public void onError(Throwable e) {
        Log.d(TAG, "onError");
        e.printStackTrace();
    }

    @Override
    public void onNext(List<Item> itemList) {
        Log.d(TAG, "onNext");
        listItems = itemList;
        for(Item i : listItems) {
            Log.d(TAG, "Type: " + i.getType());
            mCardAdapter.addData(i);
        }

    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onClick");
        Item clickedItem = mCardAdapter.getItem(position);
        Log.d(TAG, "Type: " + clickedItem.getType());
        if (clickedItem.getType().equals(CardAdapter.IMAGE_TYPE)) {
            startFullScreenActivity(clickedItem.getData());
        } else if (clickedItem.getType().equals(CardAdapter.TEXT_TYPE)) {
            startWebView("https://fuzzproductions.com/");
        }
    }

    /**
     * Start the Full screen view for the photo
     */
    private void startFullScreenActivity(String photoURL) {
        if (photoURL != null && !photoURL.isEmpty()){
            Intent intent = new Intent(this, FullScreenActivity.class);
            intent.putExtra(FullScreenActivity.KEY_PHOTO_URL, photoURL);
            startActivity(intent);
        }
    }

    /**
     * Start the Web view activity
     */
    private void startWebView(String webURL) {
        if (webURL != null && !webURL.isEmpty()) {
            Intent i = new Intent(this, WebViewActivity.class);
            i.putExtra(WebViewActivity.KEY_URL_TO_LOAD, webURL);
            startActivity(i);
        }
    }

    /**
     * Display alert dialog for filter
     */
    private void startAlertDialog() {
        AlertDialog alertDialog = null;
        String[] MenuItems = {"All", "Text", "Images"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter Items");
        builder.setSingleChoiceItems(MenuItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        mCardAdapter.getFilter().filter("");
                        break;
                    case 1:
                        mCardAdapter.getFilter().filter(CardAdapter.TEXT_TYPE);
                        break;
                    case 2:
                        mCardAdapter.getFilter().filter(CardAdapter.IMAGE_TYPE);
                        break;
                }
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
}
