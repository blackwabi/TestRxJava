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
        })
        .filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return !s.equals("google");
            }
        })
        .take(2)
        .doOnNext(new Action1<String>() {
            @Override
            public void call(String s) {
                save(s);
            }
        })
        .subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("MARTIN: " + s);
            }
        });
    }

    private void save(String s) {
        // Method that simulates saving an item
        System.out.println("Saving item = " + s);
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
