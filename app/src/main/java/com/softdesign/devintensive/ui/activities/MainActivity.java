package com.softdesign.devintensive.ui.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.managers.DataManager;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.design.widget.NavigationView.*;
import static com.softdesign.devintensive.utils.ConstantManager.*;


public class MainActivity extends BaseActivity implements OnClickListener {

    private DataManager mDataManager;
    private int mCurrentEditMode = 0;

    private CoordinatorLayout mCoordinatorLayout;
    private CollapsingToolbarLayout mToolbarLayout;
    private AppBarLayout.LayoutParams mAppbarParams = null;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private LinearLayout mProfilePhotoPlaceholder;
    private BottomSheetBehavior mBottomSheetBehavior;
    View mAddPhotoCamera, mAddPhotoGallery;
    ImageView mUserProfilePhoto;

    private FloatingActionButton mFab;
    private EditText mUserPhone, mUserMail, mUserVK, mUserGit, mUserMySelf;

    private List<EditText> mUserInfo;
    File mPhotoFile = null;
    private Uri mSelectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(ConstantManager.TAG_PREFIX, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_main);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar();
        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setupDrawer();
        mProfilePhotoPlaceholder = (LinearLayout) findViewById(R.id.photo_placeholder);
        mProfilePhotoPlaceholder.setOnClickListener(this);
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        mAddPhotoCamera = findViewById(R.id.ll_add_photo_from_camera);
        mAddPhotoCamera.setOnClickListener(this);
        mAddPhotoGallery = findViewById(R.id.ll_add_photo_from_gallery);
        mAddPhotoGallery.setOnClickListener(this);
        mUserProfilePhoto = (ImageView)findViewById(R.id.user_profile_placeholder_image);

        mDataManager = DataManager.getInstance();

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);
        mUserPhone = (EditText) findViewById(R.id.et_phone);
        mUserMail = (EditText) findViewById(R.id.et_email);
        mUserVK = (EditText) findViewById(R.id.et_vk);
        mUserGit = (EditText) findViewById(R.id.et_git1);
        mUserMySelf = (EditText) findViewById(R.id.et_myself);

        mUserInfo = new ArrayList<>();
        mUserInfo.add(mUserPhone);
        mUserInfo.add(mUserMail);
        mUserInfo.add(mUserVK);
        mUserInfo.add(mUserGit);
        mUserInfo.add(mUserMySelf);


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
    protected void onStart() {
        super.onStart();
        Log.d(ConstantManager.TAG_PREFIX, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(ConstantManager.TAG_PREFIX, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(ConstantManager.TAG_PREFIX, "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(ConstantManager.TAG_PREFIX, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(ConstantManager.TAG_PREFIX, "onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(ConstantManager.TAG_PREFIX, "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(ConstantManager.TAG_PREFIX, "onSaveInstanceState");
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(ConstantManager.TAG_PREFIX, "onRestoreInstanceState");
    }

    //TODO: replace this to Log.d
    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
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
        switch (requestCode){
            case REQUEST_CODE_CAMERA_PICTURE:
                showSnackbar("handle Camera");
                if (mPhotoFile != null){
                    mSelectedImageUri = Uri.fromFile(mPhotoFile);
                    insertImage(mSelectedImageUri);
                }
                break;
            case REQUEST_CODE_GALLERY_PICTURE:
                Log.d("sadbfsdaf", data.getData().toString());
                insertImage(Uri.parse(data.getData().toString()));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                if((grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && (grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                    loadPhotoFromCamera();
            }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                showSnackbar("FAB onClick");
                if (mCurrentEditMode == 0) {
                    mCurrentEditMode = 1;
                    changeEditMode(1);
                } else {
                    mCurrentEditMode = 0;
                    changeEditMode(0);
                }
                break;
            case R.id.photo_placeholder:
                //showSnackbar("Placeholder onClick");
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
                //showSnackbar("loadFromCamera onClick");
                loadPhotoFromCamera();
                break;
            case R.id.ll_add_photo_from_gallery:
                loadPhotoFromGallery();
                break;
        }
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
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackbar("Drawer item selected " + item.getTitle());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void changeEditMode(int mode) {
        if (mode == 1) {
            loadUserInfoValues();

            mFab.setImageResource(R.drawable.ic_done_black_24dp);
            for (EditText userValue : mUserInfo) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
            }
            mProfilePhotoPlaceholder.setVisibility(VISIBLE);
            mUserInfo.get(0).requestFocus();
            mAppbarParams.setScrollFlags(0);
            mToolbarLayout.setLayoutParams(mAppbarParams);
        } else {
            saveUserInfoValues();
            mFab.setImageResource(R.drawable.ic_edit_black_24dp);
            for (EditText userValue : mUserInfo) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
            }
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
                PackageManager.PERMISSION_GRANTED)){
            Intent takePhotoFromCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try{
                mPhotoFile = createImageFile();
            }catch (IOException e){
                e.printStackTrace();
                //TODO: handle error create file
            }
            if (mPhotoFile != null){
                takePhotoFromCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takePhotoFromCamera, ConstantManager.REQUEST_CODE_CAMERA_PICTURE);

            }else {
                Log.d("sdnfa", "request permission");
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        ConstantManager.CAMERA_PERMISSION_REQUEST_CODE);
            }
            Snackbar.make(mCoordinatorLayout, "Дла корректной работы необходимо дать разрешение", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Разрешить", new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openApplicationSetting();
                        }
                    }).show();
        }

    }

    private File createImageFile() throws IOException{
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

    public void openApplicationSetting(){
        Intent appSettingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingIntent, ConstantManager.PERMISSION_REQUEST_SETTING_CODE);
    }






}
