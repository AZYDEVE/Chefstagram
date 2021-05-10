package edu.neu.madcourse.numad20f_yang_ylescupidez;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import edu.neu.madcourse.numad20f_yang_ylescupidez.ui.recipes.RecipeFragment;
import edu.neu.madcourse.numad20f_yang_ylescupidez.ui.recipes.createRecipe.CreateRecipeFragment;
import edu.neu.madcourse.numad20f_yang_ylescupidez.ui.profile.ProfileFragment;
import edu.neu.madcourse.numad20f_yang_ylescupidez.ui.search.SearchFragment;
import edu.neu.madcourse.numad20f_yang_ylescupidez.ui.achievements.AchievementsFragment;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            BottomNavigationView navView = findViewById(R.id.nav_view);
            navView.setOnNavigationItemSelectedListener(this);
            loadFragment(new ProfileFragment());
        }

    }

    // put the clicked fragment in nav_host_fragment
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            getSupportFragmentManager().popBackStack(null, POP_BACK_STACK_INCLUSIVE);
            transaction.replace(R.id.nav_host_fragment, fragment);
            // transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return false;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()){
            case R.id.navigation_profile:
                fragment=new ProfileFragment();
                break;
            case R.id.navigation_createRecipe:
                fragment=new RecipeFragment();
                break;
            case R.id.navigation_search:
                fragment = new SearchFragment();
                break;
            case R.id.navigation_achievements:
                fragment = new AchievementsFragment();
                break;

        }
        return loadFragment(fragment);
    }

    // Not sure if navhostfragment takes care of this or not
//    public void onCreate(Bundle savedInstanceState) {
//        ...
//        if (savedInstanceState != null) {
//            //Restore the fragment's instance
//            mMyFragment = getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
//            ...
//        }
//        ...
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        //Save the fragment's instance
//        getSupportFragmentManager().putFragment(outState, "myFragmentName", mMyFragment);
//    }
}