package com.chen.baselibrary.activity;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.chen.baselibrary.R;
import com.chen.baselibrary.util.ToastUtil;
import com.chen.baselibrary.widget.AlertDialog;
import com.chen.baselibrary.widget.SimpleProgressDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;


/**
 *
 * @author chen
 *
 * 基类Activity
 * 1、显示ProgressDialog
 * 2、显示自定义Toast
 * 3、保存屏幕宽高
 * 4、使用注解代替findViewById
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 进度条
     */
    private SimpleProgressDialog progressDialog;

    /**
     * AlertDialog
     */
    private AlertDialog alertDialog;

    /**
     * 内置的广播接受
     */
    private InternalReceiver internalReceiver;
    /**
     * 默认的Receiver 优先级，优先级越高执行顺序越靠前
     */
    private int DEFAULT_PRIORITY = 10;

    /**
     * 屏幕资源
     */
    private KeyguardManager.KeyguardLock mKeyguardLock = null;
    private KeyguardManager mKeyguardManager = null;
    private PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (setFullScreen()) {
            Window window = getWindow();
            //隐藏标题栏
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //隐藏状态栏
            //设置当前窗体为全屏显示
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        //http://stackoverflow.com/questions/4341600/how-to-prevent-multiple-instances-of-an-activity-when-it-is-launched-with-differ/
        //理论上应该放在launcher activity,放在基类中所有集成此库的app都可以避免此问题
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        setContentView(getContentView());
        //view绑定，必须在setContentView之后调用
        ButterKnife.bind(this);
    }

    /**
     * 供子类重写，是否需要全屏
     *
     * @return true:全屏  false不全屏
     */
    protected boolean setFullScreen() {
        return false;
    }
    //--------------------------------------屏幕资源管理---------------------------------

    /**
     * 获取布局文件的id
     *
     * @return
     */
    protected abstract int getContentView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
        hideSoftKeyboard();
        if (this.internalReceiver != null) {
            this.unregisterReceiver(this.internalReceiver);
        }
    }
    //--------------------------------------抽象方法-------------------------------------

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = this.getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }

    /**
     * Activity内置的广播接收器处理函数
     *
     * @param context
     * @param intent
     */
    protected void handleReceiver(Context context, Intent intent) {
    }

    /**
     * 注册默认优先级的广播接受
     *
     * @param actionArray
     */
    protected final void registerReceiver(String[] actionArray) {
        registerReceiver(actionArray, DEFAULT_PRIORITY);
    }

    /**
     * 注册广播接收
     *
     * @param actionArray
     * @param priority    优先级
     */
    protected final void registerReceiver(String[] actionArray, int priority) {
        if (actionArray == null) {
            return;
        }
        IntentFilter intentfilter = new IntentFilter();
        for (String action : actionArray) {
            intentfilter.addAction(action);
        }
        if (internalReceiver == null) {
            internalReceiver = new InternalReceiver();
        }
        intentfilter.setPriority(priority);
        registerReceiver(internalReceiver, intentfilter);
    }

    //-------------------------------------权限申请---------------------------------------
    //TODO 小米有bug
    protected final void requestPermission(String[] permissions, RequestPermissionCallback callback) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(permissions)
                .subscribe(aBoolean -> callback.onHandlerPermission(aBoolean));
    }

    /**
     * 如果权限被拒绝，则显示一个该权限的说明，点击确定按钮跳转到设置页面
     *
     * @param explain
     */
    protected void showPermissionExplainDialog(String explain) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(explain + "\n\n请点击\"设置\"-\"权限\"-打开所需权限。");
        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.cancel,
                (dialog, which) -> {});
        builder.setPositiveButton(R.string.setting,
                (dialog, which) -> {
                    startAppSettings();
                });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 打开     App设置界面
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
    //--------------------------------------屏幕尺寸---------------------------------------

    /**
     * 屏幕宽度
     */
    protected int getScreenWidth() {
        return BaseApplication.getInstance().getScreenWidth();
    }

    /**
     * 屏幕高度
     */
    protected int getScreenHeight() {
        return BaseApplication.getInstance().getScreenHeight();
    }

    //--------------------------------------进度条---------------------------------------

    /**
     * 显示进度条
     *
     * @param msg
     */
    protected void showProgressDialog(String msg) {
        if (this.progressDialog == null) {
            this.progressDialog = new SimpleProgressDialog(this, msg);
            this.progressDialog.setCanceledOnTouchOutside(false);
        }
        if (!this.progressDialog.isShowing()) {
            this.progressDialog.show();
        }
    }

    /**
     * 隐藏进度条
     */
    protected void hideProgressDialog() {
        if (this.progressDialog != null && this.progressDialog.isShowing()) {
            this.progressDialog.cancel();
        }
        this.progressDialog = null;
    }
    //-------------------------------------显示AlertDialog------------------------------

    /**
     * 显示AlertDialog
     *
     * @param title    标题
     * @param listener 确定和取消按钮的监听
     */
    protected void showAlertDialog(String title, AlertDialog.OnButtonClickListener listener) {
        if (this.alertDialog != null && this.alertDialog.isShowing()) {
            this.alertDialog.dismiss();
        }
        this.alertDialog = new AlertDialog(this, title, listener);
        this.alertDialog.show();
    }

    /**
     * 关闭AlertDialog
     */
    protected void hideAlertDialog() {
        if (this.alertDialog != null && this.alertDialog.isShowing()) {
            this.alertDialog.dismiss();
        }
    }
    //--------------------------------------Toast---------------------------------------

    /**
     * 显示纯文字Toast--short
     */
    protected void showShortTxtToast(String txt) {
        ToastUtil.showShortTxt(this, txt, ToastUtil.SHOW_POSITION.CENTER);
    }

    /**
     * 显示带图标的Toast--short
     */
    protected void showShortIconToast(int icon, String txt) {
        ToastUtil.showShortIcon(this, txt, icon, ToastUtil.SHOW_POSITION.CENTER);
    }

    /**
     * 显示纯文字Toast--long
     */
    protected void showLongTxtToast(String txt) {
        ToastUtil.showLongTxt(this, txt, ToastUtil.SHOW_POSITION.CENTER);
    }

    /**
     * 显示带图标的Toast--long
     */
    protected void showLongIconToast(int icon, String txt) {
        ToastUtil.showLongIcon(this, txt, icon, ToastUtil.SHOW_POSITION.CENTER);
    }

    /**
     * 显示网络错误的Toast
     */
    protected void showNetErrorToast() {
        ToastUtil.showNetErrorToast(this);
    }

    @Override
    public void onClick(View v) {
        onWidgetClick(v);
    }

    /**
     * onClickListener
     *
     * @param v
     */
    protected abstract void onWidgetClick(View v);

    /**
     * 返回按钮
     *
     * @param view
     */
    public void back(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * 提供返回键的默认实现
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onNavigationBtnClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //----------------------------------------生命周期方法------------------------------

    /**
     * 导航按钮的默认实现
     * 如果需要重新定义导航按钮的行为，重写该方法即可
     */
    protected void onNavigationBtnClick() {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    //---------------------------------inner interface------------------------------

    /**
     * 权限申请的回调接口
     */
    protected interface RequestPermissionCallback {
        /**
         * 是否获取权限
         *
         * @param isGranted
         */
        void onHandlerPermission(boolean isGranted);
    }

    //----------------------------------inner class---------------------------------
    /**
     * 内置的广播接收器,在handleReceiver()方法中处理接收到的广播
     */
    protected class InternalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            handleReceiver(context, intent);
        }
    }
}
