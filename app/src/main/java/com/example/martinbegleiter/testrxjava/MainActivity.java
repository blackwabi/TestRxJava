package com.example.martinbegleiter.testrxjava;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeRxCode();
            }
        });


    }

    private void executeRxCode() {
        query().flatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
        }).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return getTitle(s);
            }
        }).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                // This simulates a random exception occuring
                if (Math.random() < 0.25) {
                    throw new RuntimeException("Incorrect site!");
                }

                return s;
            }
        })
        .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("Received all items!");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("Got an error with the following exception: " + e.getMessage());
            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        });
    }

    private Observable<List<String>> query () {
        // This method simulates a method that returns an observable that is a list
        List<String> urlList = new ArrayList<>();
        urlList.add("www.google.com");
        urlList.add("www.nytimes.com");
        urlList.add("www.github.com");
        urlList.add("www.yahoo.com");
        return Observable.just(urlList);
    }

    private Observable<String> getTitle(String url) {
        // This method simulates a method that operates on a String and returns an
        // Observable
        String[] parts = url.split("\\.");
        return Observable.just(parts[1]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
