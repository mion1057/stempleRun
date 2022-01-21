package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.network.Server;
import com.example.myapplication.network.ServerAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private ServerAPI api;
    private Call<Integer> request;
    private MainViewModel modell;
    private  View header;
    int check;



    private NavigationView navigationView;
    private Menu menu;
    private MenuItem menuItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //권한 체크
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한 없을경우 최초 권한 요청 또는 사용자에 의한 재요청 확인
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // 권한 재요청
                ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
            else {
                ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        BottomNavigationView bottomView = findViewById(R.id.bottomNavigation);
/*        NavigationView navigationView = findViewById(R.id.nav_view);*/
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_story_area, R.id.nav_bingo, R.id.nav_hotspot, R.id.nav_review, R.id.nav_idea, R.id.nav_signup)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment); // 저 자리에 fragment들을 갈아 끼워라  //1.번
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);  // 45, 46 , 47라인을 서로 연결하는 코드
        NavigationUI.setupWithNavController(bottomView, navController);

        final MainViewModel[] model = {new ViewModelProvider(this).get(MainViewModel.class)};

        initSideMenu();

        /*super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_story_area, R.id.nav_bingo, R.id.nav_hotspot, R.id.nav_review, R.id.nav_idea, R.id.nav_login)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment); // 저 자리에 fragment들을 갈아 끼워라  //1.번
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);  // 38, 39 , 40라인을 서로 연결하는 코드
        NavigationUI.setupWithNavController(navigationView, navController);

        MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);*/

        // 로그인 화면 띄우기 코드 여기 넣기

        SharedPreferences login_data = getSharedPreferences("login_data", Activity.MODE_PRIVATE);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        header = navigationView.getHeaderView(0);

        menu = navigationView.getMenu();

        //menuItem =  menu.findItem(R.id.nav_login);

//        menuItem.setOnMenuItemClickListener(v-> {
//            if(v.getTitle() == "로그아웃") {
//                modell.login_num.setValue(null);
//                modell.login_id.setValue(null);
//                modell.login_name.setValue(null);
//                modell.login_email.setValue(null);
//                Intent intent = new Intent(this, LoginActivity.class);
//                startActivityForResult(intent, 1);
//            }
//            return false;
//        });


        String loginNum = login_data.getString("num", null);
        String loginId = login_data.getString("id", null);
        String loginPw = login_data.getString("pw", null);
        String loginName = login_data.getString("name", null);
        String loginEmail = login_data.getString("email", null);



        Log.i("start", "1");

        modell = new ViewModelProvider(MainActivity.this).get(MainViewModel.class);

        if(loginId != null && loginPw != null) {
            Log.i("start", "2");
            api  = Server.getInstance().getApi();
            Log.i("start : ", loginId);
            Log.i("start : ", loginPw);
            request = api.appAutoLoginCheck(loginId, loginPw);
            request.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    Log.i("start", "3");
                    if(response.code() == 200) {
                        Log.i("start", "4");
                        check = response.body();

                        if(check == 1) {
                            Log.i("start", "6");


                            modell.login_num.setValue(Integer.parseInt(loginNum));
                            modell.login_id.setValue(loginId);
                            modell.login_name.setValue(loginName);
                            modell.login_email.setValue(loginEmail);
                            Log.i("asdfasdf : ", modell.login_id.getValue());
                            Log.i("asdfasdf : ", modell.login_name.getValue());



                            //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                            //header = LayoutInflater.from(MainActivity.this).inflate(R.layout.nav_header_main, null);
                            //navigationView.addHeaderView(header);

                            //header = navigationView.getHeaderView(0);

                            TextView name = (TextView) header.findViewById(R.id.nav_name);
                            TextView email = (TextView) header.findViewById(R.id.nav_email);

                            name.setText(loginName + "님 환영합니다.");
                            email.setText(loginEmail);

//                            Menu menu = navigationView.getMenu();
//
//                            MenuItem menuItem = menu.findItem(R.id.nav_login);

                            //menuItem.setTitle("로그아웃");

                            Toast.makeText(MainActivity.this, loginName + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.i("start", "7");

                            Toast.makeText(MainActivity.this, "로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivityForResult(intent, 1);

                        }
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Log.i("start", "5");
                    check = 0;
                }
            });

        }
        else if(loginId == null && loginPw == null) {
            Log.i("start", "8");

            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 1);
            Toast.makeText(this, "로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT);
        }
    }

    private void initSideMenu() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.nav_login) {
                Toast.makeText(this, "로그인 클릭", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);    // side 네비게이션바를 접어주는 코드
                startActivityForResult(new Intent(this, LoginActivity.class), 1);
            } else if(item.getItemId() == R.id.nav_signup) {
                Toast.makeText(this, "회원가입", Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                drawer.closeDrawer(GravityCompat.START);
                navController.navigate(R.id.nav_signup);

            }
            return true;
        });
    }

    // 이게 맞는걸까?
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            if(resultCode == Activity.RESULT_OK) {
                if(data != null) {
                    //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                    //header = LayoutInflater.from(MainActivity.this).inflate(R.layout.nav_header_main, null);
                    //navigationView.addHeaderView(header);
                    //header = navigationView.getHeaderView(0);

                    TextView name = (TextView) header.findViewById(R.id.nav_name);
                    TextView email = (TextView) header.findViewById(R.id.nav_email);

//                Log.i("씨발", modell.login_num.getValue());
//                Log.i("씨발", modell.login_id.getValue());
//                Log.i("씨발", modell.login_name.getValue());
//                Log.i("씨발", modell.login_email.getValue());

                    String l_num = data.getStringExtra("num");
                    String l_id = data.getStringExtra("id");
                    String l_name = data.getStringExtra("name");
                    String l_email = data.getStringExtra("email");

                    name.setText(l_name + "님 환영합니다.");
                    email.setText(l_email);

                    //Menu menu = navigationView.getMenu();

                    //menuItem = menu.findItem(R.id.nav_login);

                    //menuItem.setTitle("로그아웃");

                    modell.login_num.setValue(Integer.parseInt(l_num));
                    modell.login_id.setValue(l_id);
                    modell.login_name.setValue(l_name);
                    modell.login_email.setValue(l_email);
                }
            }
        }
    }

    // back키 눌럿을때 실행해주는 메소드?
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public interface onKeyBackPressedListener{
        void onBackKey();
    }

    private onKeyBackPressedListener mOnKeyBackPressedListener;

    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener){
        mOnKeyBackPressedListener = listener;
    }


    @Override
    public void onBackPressed() {

        //Fragment 로 뒤로가기 callback 보내기위한 로직
        if (mOnKeyBackPressedListener != null) {
            Log.i("back", "눌림aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            mOnKeyBackPressedListener.onBackKey();
        }else{
            Log.i("back", "눌림aaaaaa");
            super.onBackPressed();
        }

//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_story_start);
//
//        if(!(fragment instanceof OnBackPressedListener)) {
//            super.onBackPressed();
//            Log.i("aaaaaaaaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//        }

//        int count = getSupportFragmentManager().getBackStackEntryCount();
//
//        if(count == 0) {
//            super.onBackPressed();
//        }
//        else {
//            getSupportFragmentManager().popBackStack();
//        }

//        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
//        if(fragmentList != null) {
//            for(Fragment fragment : fragmentList) {
//                ((OnBackPressed)fragment).onBackPressed();
//            }
//        }
    }
}