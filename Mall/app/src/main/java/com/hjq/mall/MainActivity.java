package com.hjq.mall;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.hjq.mall.pro.attention.view.AttentionFragment;
import com.hjq.mall.pro.base.view.bage.BadgeView;
import com.hjq.mall.pro.essence.view.EssenceFragment;
import com.hjq.mall.pro.mine.view.MineFragment;
import com.hjq.mall.pro.newpost.view.NewpostFragment;
import com.hjq.mall.pro.publish.view.PublishActivity;
import com.hjq.mall.pro.publish.view.PublishFragment;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements TabHost.OnTabChangeListener {

    private List<TabItem> tabItemList;
    private FragmentTabHost fragmentTabHost;
    private int currentSelect = 0;

    private long lastBackPressTime = -1L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏 必须在setContentView之前设置 不然报错
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
            // getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_main);
        // 设置底部虚拟按键不遮挡内容
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        // StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimaryDark), 0);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        // StatusBarUtil.setTranslucent(this, 0);
        // StatusBarUtil.setTranslucent(this);

        //由于Java面向对象的语言(OO设计)
        initTabData();
        initTabHost();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        long currentTime = System.currentTimeMillis();
        if(lastBackPressTime == -1L || currentTime - lastBackPressTime >= 2000){
            ToastUtils.showShort("再按一次退出");
        } else {
            finish();
        }
    }

    //初始化Tab数据
    private void initTabData(){
        tabItemList = new ArrayList<TabItem>();
        //添加精华Tab
        tabItemList.add( new TabItem(this, R.drawable.main_bottom_essence_normal
                ,R.drawable.main_bottom_essence_press,R.string.main_essence_text, EssenceFragment.class));
        //添加新帖Tab
        tabItemList.add(new TabItem(this, R.drawable.main_bottom_newpost_normal
                ,R.drawable.main_bottom_newpost_press,R.string.main_newpost_text, NewpostFragment.class));
        //添加发布Tab
        tabItemList.add(new TabItem(this, R.drawable.main_bottom_public_normal
                ,R.drawable.main_bottom_public_press,0, PublishFragment.class));
        //添加关注Tab
        tabItemList.add(new TabItem(this, R.drawable.main_bottom_attention_normal
                ,R.drawable.main_bottom_attention_press,R.string.main_attention_text, AttentionFragment.class));
        //添加我的Tab
        tabItemList.add(new TabItem(this, R.drawable.main_bottom_mine_normal
                ,R.drawable.main_bottom_mine_press,R.string.main_mine_text, MineFragment.class));

    }

    //初始化主页选项卡视图
    private void initTabHost(){
        //获取FragmentTabHost
        fragmentTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        //绑定TabHost(绑定我们的body)
        fragmentTabHost.setup(this,getSupportFragmentManager(),android.R.id.tabcontent);
        //去掉分割线
        fragmentTabHost.getTabWidget().setDividerDrawable(null);
        for (int i= 0; i<tabItemList.size(); i++){
            TabItem tabItem = tabItemList.get(i);
            //绑定Fragment(将Fragment添加到FragmentTabHost组件上面)
            //newTabSpec:代表Tab名字
            //setIndicator:图片(今天我们采用布局文件--Tab到样式我们自己做)
            TabHost.TabSpec tabSpec = fragmentTabHost.
                    newTabSpec(tabItem.getTitleString())
                    .setIndicator(tabItem.getView());
            //添加Fragment
            //tabSpec:选项卡
            //tabItem.getFragmentClass():具体的Fragment
            //tabItem.getBundle():给我们的具体的Fragment传参数
            fragmentTabHost.addTab(tabSpec,tabItem.getFragmentClass(),tabItem.getBundle());
            //给我们的Tab按钮设置背景
            fragmentTabHost.getTabWidget()
                    .getChildAt(i)
                    .setBackgroundColor(getResources().getColor(R.color.main_bottom_bg));
            //监听点击Tab
            fragmentTabHost.setOnTabChangedListener(this);
            // 第一次默认选中第一个Tab
            if (i == 0){
                tabItem.setChecked(true);
            }
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        // ToastUtils.showShort(tabId);
        int lenght = tabItemList.size();
        //重置Tab样式
        for (int i = 0;i < lenght; i++){
            TabItem tabItem = tabItemList.get(i);
            if (tabId.equals(tabItem.getTitleString())){
                if(TextUtils.isEmpty(tabId)) {

                    // 默认还是选中上一次的
                    fragmentTabHost.setCurrentTab(currentSelect);
                    tabItem.setChecked(false);

                    // 加号菜单跳转
                    final Intent intent = new Intent(this, PublishActivity.class);
                    startActivity(intent);

                    return;
                }
                currentSelect = i;
                //选中设置为选中壮体啊
                tabItem.setChecked(true);

            }else {
                //没有选择Tab样式设置为正常
                tabItem.setChecked(false);
            }
        }
    }

    // 代表每一个Tab
    class TabItem {
        //正常情况下显示的图片
        private int imageNormal;
        //选中情况下显示的图片
        private int imagePress;
        //tab的名字
        private int title;
        private String titleString;
        private Class<? extends Fragment> fragmentClass;

        private View view;
        private ImageView imageView;
        private TextView textView;
        private Bundle bundle;
        private Activity context;
        private BadgeView badgeView;

        public TabItem(final Activity context, int imageNormal, int imagePress, int title, Class<? extends Fragment> fragmentClass){
            this.imageNormal = imageNormal;
            this.imagePress = imagePress;
            this.title = title;
            this.fragmentClass = fragmentClass;
            this.context = context;
        }

        public Class<? extends Fragment> getFragmentClass() {
            return fragmentClass;
        }

        public int getImageNormal() {
            return imageNormal;
        }

        public int getImagePress() {
            return imagePress;
        }

        public int getTitle() {
            return title;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public String getTitleString() {
            if (title == 0){
                return "";
            }
            if (TextUtils.isEmpty(titleString)){
                titleString = getString(title);
            }
            return titleString;
        }

        public Bundle getBundle(){
            if (bundle == null){
                bundle = new Bundle();
            }
            bundle.putString("title",getTitleString());
            return bundle;
        }

        //还需要提供一个切换Tab方法---改变Tab样式
        public void setChecked(boolean isChecked){
            if(imageView != null){
                if (isChecked){
                    imageView.setImageResource(imagePress);
                }else {
                    imageView.setImageResource(imageNormal);
                }
            }
            if (textView != null && title != 0){
                if (isChecked){
                    textView.setTextColor(getResources().getColor(R.color.main_bottom_text_select));
                }else {
                    textView.setTextColor(getResources().getColor(R.color.main_bottom_text_normal));
                }
            }
        }

        public View getView(){
            if (this.view == null){
                this.view = getLayoutInflater().inflate(R.layout.view_tab_indicator,null);
                FrameLayout fl_layout = (FrameLayout)this.view.findViewById(R.id.fl_layout);
                this.imageView = (ImageView)this.view.findViewById(R.id.iv_tab);
                this.textView = (TextView) this.view.findViewById(R.id.tv_tab);
                //判断资源是否存在,不再我就因此
                if (this.title == 0){
                    this.textView.setVisibility(View.GONE);
                }else {
                    this.textView.setVisibility(View.VISIBLE);
                    this.textView.setText(getTitleString());
                }
                //绑定图片默认资源
                this.imageView.setImageResource(imageNormal);
                badgeView = new BadgeView((MainActivity) context, fl_layout);
                badgeView.setText("12");
                badgeView.setTextSize(9f);
                badgeView.setTextColor(Color.WHITE);
                badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                badgeView.setAlpha(1f);
                badgeView.setBackgroundResource(R.drawable.shape_round_textview);
                badgeView.setBadgeMargin(0,0);
                // badgeView.setAnimation(alphaAnimation);
                badgeView.show();
            }
            return this.view;
        }
    }
}
