package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Patterns;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.pojo.UserInfo;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.validator.TextValueValidator;
import com.squareup.picasso.Picasso;
import com.vicmikhailau.maskededittext.MaskedWatcher;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.design.widget.NavigationView.*;
import static com.softdesign.devintensive.utils.ConstantManager.*;
import static com.softdesign.devintensive.utils.validator.TextValueValidator.*;


public class MainActivity extends BaseActivity implements OnClickListener {

    DataManager mDataManager;
    int mCurrentEditMode = 0;
    private UserInfo mUserProfile;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mToolbarLayout;
    AppBarLayout.LayoutParams mAppbarParams = null;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.photo_placeholder)
    LinearLayout mProfilePhotoPlaceholder;
    @BindView(R.id.ll_add_photo_from_camera)
    View mAddPhotoCamera;
    @BindView(R.id.ll_add_photo_from_gallery)
    View mAddPhotoGallery;
    @BindView(R.id.user_profile_placeholder_image)
    ImageView mUserProfilePhoto;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;
    BottomSheetBehavior mBottomSheetBehavior;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindViews({R.id.et_phone, R.id.et_email, R.id.et_vk, R.id.et_git1, R.id.et_myself})
    List<EditText> mUserInfo;


    File mPhotoFile = null;
    private Uri mSelectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_main);
        ButterKnife.bind(this);

        mProfilePhotoPlaceholder.setOnClickListener(this);
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        mAddPhotoCamera.setOnClickListener(this);
        mAddPhotoGallery.setOnClickListener(this);

        setupToolbar();
        setupDrawer();

        mDataManager = DataManager.getInstance();

        mFab.setOnClickListener(this);
        mUserInfo.get(0).addTextChangedListener(new MaskedWatcher("+7(###) ###-##-##"));
        mUserInfo.get(1).setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                boolean isValid = TextValueValidator.validate(Type.Email, mUserInfo.get(1).getText().toString());
                if (!b) {
                    if (!isValid) {
                        mUserInfo.get(1).setTextColor(Color.RED);
                        showSnackbar(getString(R.string.error_email_value_message));
                    }
                } else {
                    mUserInfo.get(1).setTextColor(Color.BLACK);
                    if (!isValid) mUserInfo.get(1).setText("");
                }
            }
        });
        mUserInfo.get(2).addTextChangedListener(new MaskedWatcher("vk.com/*******************"));
        mUserInfo.get(3).addTextChangedListener(new MaskedWatcher("github.com/*******************"));

        if (savedInstanceState == null) {
            showSnackbar("Activity start is first");
        } else {
            showSnackbar("Activity is restart");
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
        }

        loadUserInfoValues();

        Picasso.with(this)
                .load(mDataManager.getPreferenceManager().loadUserPhoto())
                .into(mUserProfilePhoto);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Log.d(ConstantManager.TAG_PREFIX, "onSaveInstanceState");
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
        mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
        changeEditMode(mCurrentEditMode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mNavigationDrawer.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START) ||
                (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Receive result from another activities (camera's photo or gallery)
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //showSnackbar("onActivityResult  " + (resultCode == RESULT_OK) + " " + (data != null));
        if ((resultCode != RESULT_OK)) return;
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_PICTURE:
                showSnackbar("handle Camera");
                if (mPhotoFile != null) {
                    mSelectedImageUri = Uri.fromFile(mPhotoFile);
                    insertImage(mSelectedImageUri);
                }
                break;
            case REQUEST_CODE_GALLERY_PICTURE:
                Log.d(ConstantManager.TAG_PREFIX, data.getData().toString());
                insertImage(Uri.parse(data.getData().toString()));
                break;
        }
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    loadPhotoFromCamera();
                } else {
                    Snackbar.make(mCoordinatorLayout, "Дла корректной работы необходимо дать разрешение", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Разрешить", new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    openApplicationSetting();
                                }
                            }).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (mCurrentEditMode == 0) {
                    mCurrentEditMode = 1;
                    changeEditMode(1);
                } else {
                    mCurrentEditMode = 0;
                    changeEditMode(0);
                }
                break;
            case R.id.photo_placeholder:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }, ConstantManager.ACTIVITY_MAIN_TIME_SHOW_BOTTOM_SHEET_PICK_PHOTO);
                break;
            case R.id.ll_add_photo_from_camera:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                loadPhotoFromCamera();
                break;
            case R.id.ll_add_photo_from_gallery:
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                loadPhotoFromGallery();
                break;
        }
    }

    @OnClick({R.id.btn_dial, R.id.btn_sms, R.id.btn_send_email, R.id.btn_view_vk, R.id.btn_view_git})
    public void actionIntent(ImageView view) {
        if (mCurrentEditMode == 0) {
            switch (view.getId()) {
                case R.id.btn_dial:
                    actionDial(mUserInfo.get(0).getText().toString());
                    break;
                case R.id.btn_sms:
                    actionSendSms(mUserInfo.get(0).getText().toString());
                    break;
                case R.id.btn_send_email:
                    actionSendEmail(mUserInfo.get(1).getText().toString());
                    break;
                case R.id.btn_view_vk:
                    actionView(mUserInfo.get(2).getText().toString());
                    break;
                case R.id.btn_view_git:
                    actionView(mUserInfo.get(3).getText().toString());
                    break;
            }
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        mAppbarParams = (AppBarLayout.LayoutParams) mToolbarLayout.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupDrawer() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar("Drawer item selected " + item.getTitle());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    static final ButterKnife.Setter<View, Boolean> Enabled = new ButterKnife.Setter<View, Boolean>() {

        @Override
        public void set(@NonNull View view, Boolean value, int index) {
            view.setEnabled(value);
            view.setFocusable(value);
            view.setFocusableInTouchMode(value);
        }
    };

    private void changeEditMode(int mode) {
        if (mode == 1) {
            loadUserInfoValues();

            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            ButterKnife.apply(mUserInfo, Enabled, true);
            mProfilePhotoPlaceholder.setVisibility(VISIBLE);
            mUserInfo.get(0).requestFocus();
            mAppbarParams.setScrollFlags(0);
            mToolbarLayout.setLayoutParams(mAppbarParams);
        } else {
            saveUserInfoValues();
            mFab.setImageResource(R.drawable.ic_edit_black_24dp);
            ButterKnife.apply(mUserInfo, Enabled, false);
            mProfilePhotoPlaceholder.setVisibility(GONE);
            mAppbarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL |
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
            mToolbarLayout.setLayoutParams(mAppbarParams);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

    }

    private void loadUserInfoValues() {
        List<String> userData = mDataManager.getPreferenceManager().loadUserProfileData();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfo.get(i).setText(userData.get(i));
        }
    }

    private void saveUserInfoValues() {
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfo) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferenceManager().saveUserProfileData(userData);
    }

    private void loadPhotoFromGallery() {
        Intent takePhotoFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takePhotoFromGallery.setType("image/*");
        startActivityForResult(Intent.createChooser(takePhotoFromGallery, getString(R.string.pick_image_from_gallery)),
                ConstantManager.REQUEST_CODE_GALLERY_PICTURE);

    }

    private void loadPhotoFromCamera() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED)) {
            Intent takePhotoFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                //TODO: handle error create file
            }
            if (mPhotoFile != null) {
                takePhotoFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takePhotoFromCamera, ConstantManager.REQUEST_CODE_CAMERA_PICTURE);
            }
        } else {
            Log.d(ConstantManager.TAG_PREFIX, "request permission");
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    ConstantManager.CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Image_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        /**
         * Inserting created photo to System media library for show Gallery
         */
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());
        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return image;
    }

    private void insertImage(Uri selectedImageUri) {
        //showSnackbar("insertImage" + selectedImageUri);
        mDataManager.getPreferenceManager().saveUserPhoto(selectedImageUri);
        Picasso.with(this)
                .load(selectedImageUri)
                .into(mUserProfilePhoto);
    }

    public void openApplicationSetting() {
        Intent appSettingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingIntent, ConstantManager.PERMISSION_REQUEST_SETTING_CODE);
    }

    private void actionDial(String number) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + number));
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private void actionSendSms(String number) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + number));
        intent.setType("vnd.android-dir/mms-sms");
        intent.setData(Uri.parse("sms:" + number));
        startActivity(intent);
    }

    private void actionView(String urlString) {
        Uri address = Uri.parse("https://" + urlString);
        Intent openlink = new Intent(Intent.ACTION_VIEW, address);
        startActivity(openlink);
    }

    private void actionSendEmail(String recipientAdress) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientAdress});
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.title_send_sms_per_app)));
        } catch (android.content.ActivityNotFoundException ex) {
            showSnackbar(getString(R.string.error_no_email_client));
        }


    }


}
