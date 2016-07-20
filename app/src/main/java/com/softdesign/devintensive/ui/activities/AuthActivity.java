package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.eventbus.UserListLoadedEvent;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.data.storage.models.Repositiry;
import com.softdesign.devintensive.data.storage.models.RepositiryDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.net.request.UserLoginRequest;
import com.softdesign.devintensive.net.response.UserListRes;
import com.softdesign.devintensive.pojo.UserProfile;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.validator.TextValueValidator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.eventbus.util.AsyncExecutor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity {

    public static final String TAG = "AuthActivityTag";

    @BindView(R.id.et_email_auth)
    EditText mUserEmailAuth;
    @BindView(R.id.et_pass_auth)
    EditText mUserPassAuth;
    @BindView(R.id.activity_auth_root)
    LinearLayout rootView;
    @BindView(R.id.btn_auth)
    Button mBtnEnter;
    @BindView(R.id.card_auth)
    CardView mCardView;

    private UserProfile mUserProfile;
    private RepositiryDao mRepositiryDao;
    private UserDao mUserDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        super.showProgress();
        mUserProfile = DataManager.getInstance().getPreferenceManager().loadUserProfile();
        if (mUserProfile != null){
            super.showProgress();
            AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
                @Override
                public void run() throws Exception {
                    Thread thread = Thread.currentThread();
                    //Delay for show "process" loading if network very fast
                    thread.sleep(3000);
                    List<User> userList = loadUserListSync();
                    EventBus.getDefault().postSticky(new UserListLoadedEvent(userList));
                }
            });
        }else{
            mCardView.setVisibility(View.VISIBLE);
        }


        mUserDao = DataManager.getInstance().getDaoSession().getUserDao();
        mRepositiryDao = DataManager.getInstance().getDaoSession().getRepositiryDao();

        //If user login early - fill field eMail and set it validator util
        if (mUserProfile != null) {
            mUserEmailAuth.setText(mUserProfile.getData().getUser().getContacts().getEmail());
            mUserEmailAuth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    boolean isValid = TextValueValidator.validate(TextValueValidator.Type.Email, mUserEmailAuth.getText().toString());
                    if (!b) {
                        if (!isValid) {
                            mUserEmailAuth.setTextColor(Color.RED);
                            showSnackbar(getString(R.string.error_email_value_message));
                        }
                    } else {
                        mUserEmailAuth.setTextColor(Color.BLACK);
                        if (!isValid) mUserEmailAuth.setText("");
                    }
                }
            });
            //if userProfile contain token - get user list
            if ((mUserProfile.getData().getToken() != null) && (mUserProfile.getData().getToken().length() > 10)) {
                loadUserListAsync();
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserListLoadedEvent(UserListLoadedEvent event){
        super.hideProgress();
        //mCardView.setVisibility(View.VISIBLE);
        startMainActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (DataManager.getInstance().getPreferenceManager().loadVKAuth() != null){
//            startActivity(new Intent(this, MainActivity.class));
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mUserPassAuth.setText("");
    }

    @OnClick({R.id.iv_vk_auth, R.id.iv_git_auth, R.id.btn_auth, R.id.tv_forgot_pass})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_vk_auth:
                intent = new Intent(this, VKAuthActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_git_auth:
                intent = new Intent(this, GitAuthActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_auth:
//                System.exit(1);
//                finish();
                logIn();
                break;
            case R.id.tv_forgot_pass:
                rememberPass();
                break;
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPass() {
        Intent rememberIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void flashError() {
        mCardView.setBackgroundColor(Color.RED);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCardView.setBackgroundColor(Color.WHITE);

            }
        }, ConstantManager.AUTH_ERROR_FLASH_DELAY);
    }

    private void loginSuccess(Response<UserProfile> response) {
        showSnackbar(getString(R.string.enter_success));
        DataManager.getInstance().getPreferenceManager().saveUserProfile(response.body());
        startMainActivity();
    }

    private List<User> loadUserListSync(){
        List<User> allUsers = null;
        Call<UserListRes> call = DataManager.getInstance().getNetworkManager().getUserListFromNetwork();
        try {
            Response<UserListRes> response = call.execute();
            if (response.code() == HttpURLConnection.HTTP_OK){
                List<Repositiry> allRepo = new ArrayList<>();
                allUsers = new ArrayList<>();
                for (UserListRes.Data userRes: response.body().getData()) {
                    allRepo.addAll(getRepoListFromUser(userRes));
                    allUsers.add(new User(userRes));
                }
                mRepositiryDao.insertOrReplaceInTx(allRepo, true);
                mUserDao.insertOrReplaceInTx(allUsers);
            }

        } catch (IOException e) {
            showSnackbar("Невозможно загрузить список пользователей!");
        }
        return allUsers;
    }


    private void loadUserListAsync(){
        Call<UserListRes> call = DataManager.getInstance().getNetworkManager().getUserListFromNetwork();
        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if (response.code() == HttpURLConnection.HTTP_OK){
                        List<Repositiry> allRepo = new ArrayList<>();
                        List<User> allUsers = new ArrayList<>();

                        for (UserListRes.Data userRes: response.body().getData()) {
                            allRepo.addAll(getRepoListFromUser(userRes));
                            allUsers.add(new User(userRes));

                        }

                        mRepositiryDao.insertOrReplaceInTx(allRepo, true);
                        mUserDao.insertOrReplaceInTx(allUsers);
                    }
                }catch (NullPointerException e){
                    Log.d(TAG, "loadUserList NPE: " + e);
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {

            }
        });
    }

    private List<Repositiry> getRepoListFromUser(UserListRes.Data userRes){
        final String userId = userRes.getId();
        List<Repositiry> tmpList = new ArrayList<>();
        for (UserProfile.Repo repo: userRes.getRepositories().getRepo()) {
            tmpList.add(new Repositiry(repo, userId));
        }
        return tmpList;
    }

    private void logIn() {
        Call<UserProfile> call = DataManager.getInstance().getNetworkManager()
                .loginUser(new UserLoginRequest(mUserEmailAuth.getText().toString(),
                        mUserPassAuth.getText().toString()));
        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.code() == 200) {
                    loginSuccess(response);
                } else if (response.code() == 404) {
                    flashError();
                    showSnackbar(getString(R.string.login_pass_is_wrong));
                } else {
                    showSnackbar(getString(R.string.error_other_login));

                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                //TODO: handle here error logIn

            }
        });
    }

    private void startMainActivity() {
        startActivity(new Intent(AuthActivity.this, UserListActivity.class));

    }

}
