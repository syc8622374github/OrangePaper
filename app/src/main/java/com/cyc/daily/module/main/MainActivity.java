package com.cyc.daily.module.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;

import com.cyc.daily.R;
import com.cyc.mvp.base.BaseActivity;

public class MainActivity extends BaseActivity<MainDelegate> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected MainDelegate createDelegate(Activity activity) {
        return new MainDelegate(activity);
    }

    @Override
    public void onBackPressed() {
        if (viewDelegate.drawer.isDrawerOpen(GravityCompat.START)) {
            viewDelegate.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
