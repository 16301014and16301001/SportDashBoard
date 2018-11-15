package edu.bjtu.example.sportsdashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import static android.support.v4.view.GravityCompat.START;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        nav.getHeaderView(0).findViewById(R.id.profile_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFrame(R.id.profile_image);
            }
        });

        if (savedInstanceState == null) {
            selectFrame(R.id.nav_home);
            nav.setCheckedItem(R.id.nav_home);
        }
    }

    private boolean selectFrame(int id) {

        Fragment frame = null;
        switch (id) {
            case R.id.profile_image:
                frame = new LoginFragment();
                break;
            case R.id.nav_home:
                frame = new DashboardFragment();
                break;
            case R.id.nav_poster:
                frame = new PosterFragment();
                break;
            case R.id.nav_info:
                frame = new AnnounceFragment();
                break;
            case R.id.nav_coachlist:
                frame = new CoachListFragment();
                break;
            case R.id.nav_schedule:
                frame = new ScheduleFragment();
                break;
            case R.id.nav_book:
                frame = new BookFragment();
                break;
            case R.id.nav_share:
                frame=new RecyclerFragment();
                break;
            case R.id.nav_send:
                frame = new WebviewFragment();
                break;
            default:
                break;
        }
        if (frame != null)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, frame).commit();
        drawer.closeDrawer(START);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return selectFrame(item.getItemId());
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(START)) {
            drawer.closeDrawer(START);
        } else {
            super.onBackPressed();
        }
    }
}
