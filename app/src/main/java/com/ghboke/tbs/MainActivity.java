package com.ghboke.tbs;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private WebView mWeb;
    private String CookieStr;
    private boolean canadd = false;
    private Honestsay honestsay;
    private ListView friendlist;
    private SimpleAdapter simpleAdapter;
    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;
    private TextView ts;
    private double exitTime;
private boolean islogin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        friendlist = (ListView) findViewById(R.id.friendlist);
        friendlist.setVisibility(View.INVISIBLE);//隐藏好友列表
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mWeb = (WebView) findViewById(R.id.web);
        ts=(TextView)findViewById(R.id.sm);
        mWeb.setVisibility(View.INVISIBLE);
        mWeb.getSettings().setJavaScriptEnabled(true);
        mWeb.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 8.0; MI 6 Build/OPR1.170623.027; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044006 Mobile Safari/537.36 V1_AND_SQ_7.5.5_806_YYB_D QQ/7.5.5.3460 NetType/4G WebP/0.3.0 Pixel/1080");
        constraintLayout = findViewById(R.id.ConstraintLayout_web);

        mWeb.setWebChromeClient(new WebChromeClient() {
            public static final String TAG = "主窗口";

            public void onProgressChanged(WebView view, int progress) {

                //加载完成
                if (progress == 100) {
                    if (mWeb.getUrl().equals("https://h5.qzone.qq.com/mqzone/index")) {

                        CookieManager cookieManager = CookieManager.getInstance();
                        CookieStr = cookieManager.getCookie("https://h5.qzone.qq.com/mqzone/index");
                        Toast.makeText(getApplicationContext(), "登陆成功,请侧边选择查看", Toast.LENGTH_LONG).show();
                        islogin=true;
                    }
                    if (canadd == true) {

                        //Toast.makeText(getApplicationContext(), "可以开始标记了！", Toast.LENGTH_LONG).show();
                        friendlist.setVisibility(View.VISIBLE);//显示好友列表
                        mWeb.setVisibility(View.INVISIBLE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                CookieManager cookieManager = CookieManager.getInstance();
                                String CookieStr = cookieManager.getCookie("https://h5.qzone.qq.com/mqzone/index");
                                honestsay.setCookies(CookieStr);
                                simpleAdapter = honestsay.GetHonestsayData(MainActivity.this);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        friendlist.setAdapter(simpleAdapter);
                                        friendlist.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        }).start();

                    }
                }
            }
        });
        mWeb.setWebViewClient(new WebViewClient() {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if (url.startsWith("jsbridge://")) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
                mWeb.loadUrl(url);
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                if (url.indexOf("cgi-node/honest-say/receive/mine?_client_version=0.0.7&_t=") != -1) {
                    Log.d("调试输出", url);
                    honestsay = new Honestsay();
                    honestsay.setFrindData(url);
                    canadd = true;
                }

                super.onLoadResource(view, url);
            }

        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            ts.setVisibility(View.VISIBLE);
            mWeb.setVisibility(View.INVISIBLE);
            friendlist.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_gallery) {
            LoginQQ();
        } else if (id == R.id.nav_slideshow) {
            Logintbs();
        } else if (id == R.id.zzzy) {
            zzhome();
        } else if (id == R.id.nav_share) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("关于软件");
            builder.setMessage("果核软件\nwww.ghboke.com\n作者：applek\n有空搞基啊：群638990115");
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void LoginQQ() {
        islogin=false;
        friendlist.setVisibility(View.INVISIBLE);
        ts.setVisibility(View.INVISIBLE);
        mWeb.setVisibility(View.VISIBLE);
        canadd = false;
        mWeb.loadUrl("https://ui.ptlogin2.qq.com/cgi-bin/login?pt_hide_ad=1&style=9&pt_ttype=1&appid=549000929&pt_no_auth=1&pt_wxtest=1&daid=5&s_url=https%3A%2F%2Fh5.qzone.qq.com%2Fmqzone%2Findex");

    }

    public void zzhome() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse("http://www.coolapk.com/u/734964");
        intent.setPackage("com.coolapk.market");
        intent.setData(uri);
        startActivity(intent);

    }

    public void Logintbs() {
        if (islogin==false){
            Toast.makeText(getApplicationContext(), "先登录QQ账号好不", Toast.LENGTH_LONG).show();
            return;
        }
        ts.setVisibility(View.INVISIBLE);
        mWeb.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        canadd = false;
        mWeb.loadUrl("https://ti.qq.com/honest-say/my-received.html?_wv=9191&_wwv=132&_qStyle=1&ADTAG=main");
    }
    //多次点击退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
}



