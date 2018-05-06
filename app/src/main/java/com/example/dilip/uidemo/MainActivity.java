package com.example.dilip.uidemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dilip.uidemo.adapter.MyAdapter;
import com.example.dilip.uidemo.utils.ApiClient;
import com.example.dilip.uidemo.utils.ApiInterface;
import com.example.dilip.uidemo.utils.CheckNetwork;
import com.example.dilip.uidemo.utils.DataFetchListner;
import com.example.dilip.uidemo.utils.ItemModel;
import com.example.dilip.uidemo.utils.MyDataBase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.widget.LinearLayout.VERTICAL;

public class MainActivity extends AppCompatActivity implements DataFetchListner {

    private TextView mTextMessage;
    private RecyclerView mRecyclerView;
    private static final String HEADER_FIRST = "XMLHttpRequest";
    private static final String HEADER_SECOND = "LozC7H0rINg8kO1FMrm1RRBIzjw9AozsQwguKB3J";
    private static final String HEADER_THIRD = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9jcm0ubWVkZXhwZXJ0ei5jb21cL2FwaVwvdmVyaWZ5LW90cCIsImlhdCI6MTUyNTUwNDcxOCwiZXhwIjoxNTI2NzE0MzE4LCJuYmYiOjE1MjU1MDQ3MTgsImp0aSI6IjJoUFpGUHY3Z1hwdm9DcDYiLCJzdWIiOjMxLCJwcnYiOiI3NTI4OTU2NzEwZDFjNzViNjcxMzBkNGU0YzVjMGVlOWEwYWViNjE0In0.Ta9FD12fW9R55Y36JlAWUH9lFDzITdKHkA-8fmQJB7A";
    private MyDataBase myDataBase;
    private CheckNetwork checkNetwork;
    public static final Gson gson = new Gson();
    List<ItemModel> itemModels = new ArrayList<>();
    MyAdapter adapter = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.title_home),Toast.LENGTH_LONG).show();
                    return true;
                case R.id.navigation_dashboard:
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.title_dashboard),Toast.LENGTH_LONG).show();

                    return true;
                case R.id.navigation_notifications:
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.title_notifications),Toast.LENGTH_LONG).show();

                    return true;
                case R.id.navigation_more:
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.title_notifications),Toast.LENGTH_LONG).show();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDataBase = new MyDataBase(getApplicationContext());
        checkNetwork = new CheckNetwork();

        if (checkNetwork.isDataAvailable(getApplicationContext())) {
            adapter = new MyAdapter(getApplicationContext(), itemModels, true);
        }else {
            adapter = new MyAdapter(getApplicationContext(),itemModels,false);
        }

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mRecyclerView = findViewById(R.id.list_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(adapter);

        getFeed();
    }

    @Override
    public void onDeliverData(ItemModel dataModel) {
        adapter.addData(dataModel);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onHideDialog() {

    }

    private void getFeed() {
        adapter.reset();
        if(CheckNetwork.isDataAvailable(getApplicationContext())) {
            callAPI();
        }else getFeedFromDatabase();

    }

    private void getFeedFromDatabase() {
        myDataBase.fetchData(this);
    }
    private void callAPI() {

        ApiInterface applicationDAO = ApiClient.getClient().create(ApiInterface.class);

        Call<List<ItemModel>> call = applicationDAO.getItems(HEADER_FIRST, HEADER_SECOND,HEADER_THIRD);

        System.out.println("url" + call.request().url());
        System.out.println("header" + call.request().headers());

        call.enqueue(new retrofit2.Callback<List<ItemModel>>() {

            @Override
            public void onResponse(Call<List<ItemModel>> call, Response<List<ItemModel>> response) {
                List<ItemModel> list = response.body();
                for(ItemModel model : list){
                    SaveIntoDatabase task = new SaveIntoDatabase();
                    task.execute(model);
                }
                mRecyclerView.setAdapter(new MyAdapter(getApplicationContext(), list,true));
            }

            @Override
            public void onFailure(Call<List<ItemModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public class SaveIntoDatabase extends AsyncTask<ItemModel,Void,Void> {
        // can use UI thread here
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        // automatically done on worker thread (separate from UI thread)
        @Override
        protected Void doInBackground(ItemModel... params) {
            ItemModel dataModel = params[0];
            try {
                InputStream inputStream = new URL(dataModel.getImage_path()).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                dataModel.setImage(bitmap);
                //set bitmap value to Picture
                //add data to database (shows in next step)
                myDataBase.insertSubmissionData(dataModel.getId(),dataModel.getSpecialization(),dataModel.getDescriptions(),checkNetwork.getPictureByteOfArray(dataModel.getImage_path()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
