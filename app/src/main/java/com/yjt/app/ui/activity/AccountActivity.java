package com.yjt.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yjt.app.R;
import com.yjt.app.constant.Constant;
import com.yjt.app.constant.Regex;
import com.yjt.app.ui.base.BaseActivity;
import com.yjt.app.ui.dialog.DateDialog;
import com.yjt.app.ui.dialog.ListDialog;
import com.yjt.app.ui.dialog.NumberDialog;
import com.yjt.app.ui.listener.OnDateDialogListener;
import com.yjt.app.ui.listener.OnDialogCancelListener;
import com.yjt.app.ui.listener.OnListDialogListener;
import com.yjt.app.ui.listener.OnNumberDialogListener;
import com.yjt.app.ui.widget.CircleImageView;
import com.yjt.app.utils.DateUtil;
import com.yjt.app.utils.FileUtil;
import com.yjt.app.utils.LogUtil;
import com.yjt.app.utils.PermissionUtil;
import com.yjt.app.utils.SharedPreferenceUtil;
import com.yjt.app.utils.ViewUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class AccountActivity extends BaseActivity implements View.OnClickListener, OnDialogCancelListener, OnNumberDialogListener, OnDateDialogListener, OnListDialogListener {

    private RelativeLayout rlHeadPortrait;
    private CircleImageView civHeadPortrait;

    private TextView tvNickname;
    private TextView tvPhoneNumber;

    private RelativeLayout rlGender;
    private TextView tvGender;

    private RelativeLayout rlHeight;
    private TextView tvHeight;

    private RelativeLayout rlWeight;
    private TextView tvWeight;

    private RelativeLayout rlBirthday;
    private TextView tvBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        findViewById();
        setViewListener();
        initialize(savedInstanceState);
        setListener();
    }

    @Override
    protected void findViewById() {
        ViewUtil.getInstance().setToolBar(this, R.id.tbTitle, true);
        rlHeadPortrait = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.rlHeadPortrait, this);
        civHeadPortrait = ViewUtil.getInstance().findView(this, R.id.civHeadPortrait);
        tvNickname = ViewUtil.getInstance().findView(this, R.id.tvNickname);
        tvPhoneNumber = ViewUtil.getInstance().findView(this, R.id.tvPhoneNumber);
        rlGender = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.rlGender, this);
        tvGender = ViewUtil.getInstance().findView(this, R.id.tvGender);
        rlHeight = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.rlHeight, this);
        tvHeight = ViewUtil.getInstance().findView(this, R.id.tvHeight);
        rlWeight = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.rlWeight, this);
        tvWeight = ViewUtil.getInstance().findView(this, R.id.tvWeight);
        rlBirthday = ViewUtil.getInstance().findViewAttachOnclick(this, R.id.rlBirthday, this);
        tvBirthday = ViewUtil.getInstance().findView(this, R.id.tvBirthday);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialize(Bundle savedInstanceState) {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void getSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void setSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected void permissionRequestIntent() {

    }

    @Override
    protected void permissionRequestResult() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlHeadPortrait:
                ListDialog.createBuilder(getSupportFragmentManager())
                        .setTitle(getString(R.string.gender))
                        .setPositiveButtonText(R.string.enter)
                        .setNegativeButtonText(R.string.cancel)
                        .setItems(getString(R.string.from_album), getString(R.string.from_camera))
                        .setRequestCode(Constant.RequestCode.DIALOG_RADIO_PICTURE)
                        .setChoiceMode(AbsListView.CHOICE_MODE_SINGLE)
                        .show();
                break;
            case R.id.rlGender:
                ListDialog.createBuilder(getSupportFragmentManager())
                        .setTitle(getString(R.string.gender))
                        .setPositiveButtonText(R.string.enter)
                        .setNegativeButtonText(R.string.cancel)
                        .setItems(getString(R.string.male), getString(R.string.female), getString(R.string.secrecy))
                        .setRequestCode(Constant.RequestCode.DIALOG_RADIO_GEDER)
                        .setChoiceMode(AbsListView.CHOICE_MODE_SINGLE)
                        .show();
                break;
            case R.id.rlHeight:
                NumberDialog.createBuilder(getSupportFragmentManager())
                        .setTitle(getString(R.string.height))
                        .setMinimumValue(120)
                        .setMaximumValue(230)
                        .setPositiveButtonText(R.string.enter)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_NUMBER_HEIGHT)
                        .show();
                break;
            case R.id.rlWeight:
                NumberDialog.createBuilder(getSupportFragmentManager())
                        .setTitle(getString(R.string.weight))
                        .setMinimumValue(45)
                        .setMaximumValue(200)
                        .setPositiveButtonText(R.string.enter)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_NUMBER_WEIGHT)
                        .show();
                break;
            case R.id.rlBirthday:
                DateDialog.createBuilder(getSupportFragmentManager())
                        .setDate(new Date())
                        .setPositiveButtonText(R.string.enter)
                        .setNegativeButtonText(R.string.cancel)
                        .setRequestCode(Constant.RequestCode.DIALOG_DATE)
                        .show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.RequestCode.ALBUM:
                    if (data != null) {
                        try {
                            File file = FileUtil.getInstance().takenAlbumPicture(data.getData());
                            if (file != null) {
                                civHeadPortrait.setText(null);
                                Picasso.with(this)
                                        .load(file)
                                        .fit()
                                        .centerCrop()
                                        .into(civHeadPortrait);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Constant.RequestCode.CAMERA:
                    PermissionUtil.getInstance().revokeWritePermission(this, Uri.parse(SharedPreferenceUtil.getInstance().getString(com.yjt.app.constant.File.FILE_NAME.getContent()
                            , Context.MODE_PRIVATE
                            , com.yjt.app.constant.File.PICTURE_URI.getContent()
                            , Regex.NONE.getRegext())));
                    File file = FileUtil.getInstance().takenCameraPicture();
                    if (file != null) {
                        civHeadPortrait.setText(null);
                        Picasso.with(this)
                                .load(file)
                                .fit()
                                .centerCrop()
                                .into(civHeadPortrait);
                    }
                    SharedPreferenceUtil.getInstance().remove(com.yjt.app.constant.File.FILE_NAME.getContent()
                            , Context.MODE_PRIVATE
                            , com.yjt.app.constant.File.PICTURE_URI.getContent());
                    SharedPreferenceUtil.getInstance().remove(com.yjt.app.constant.File.FILE_NAME.getContent()
                            , Context.MODE_PRIVATE
                            , com.yjt.app.constant.File.PICTURE_PATH.getContent());
                    break;
                default:
                    break;
            }
        } else {

        }
    }

    @Override
    public void onPositiveButtonClicked(int requestCode, int number) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_NUMBER_HEIGHT:
                tvHeight.setText(number + getString(R.string.centimeter));
                break;
            case Constant.RequestCode.DIALOG_NUMBER_WEIGHT:
                tvWeight.setText(number + getString(R.string.kilograms));
                break;
            default:
                break;
        }
    }

    @Override
    public void onPositiveButtonClicked(int requestCode, Date date) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_DATE:
                tvBirthday.setText(DateUtil.getCurrentTime(date, Regex.DATE_FORMAT1.getRegext()));
                break;
            default:
                break;
        }
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_DATE:
                LogUtil.print("---->DIALOG_DATE onNegativeButtonClicked");
                break;
            case Constant.RequestCode.DIALOG_NUMBER_HEIGHT:
                LogUtil.print("---->DIALOG_NUMBER_HEIGHT onNegativeButtonClicked");
                break;
            case Constant.RequestCode.DIALOG_NUMBER_WEIGHT:
                LogUtil.print("---->DIALOG_NUMBER_WEIGHT onNegativeButtonClicked");
                break;
            default:
                break;
        }
    }

    @Override
    public void onCanceled(int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_RADIO_GEDER:
                LogUtil.print("---->DIALOG_RADIO_GEDER onCanceled");
                break;
            case Constant.RequestCode.DIALOG_RADIO_PICTURE:
                LogUtil.print("---->DIALOG_RADIO_PICTURE onCanceled");
                break;
            default:
                break;
        }
    }

    @Override
    public void onListItemSelected(CharSequence value, int number, int requestCode) {
        switch (requestCode) {
            case Constant.RequestCode.DIALOG_RADIO_GEDER:
                tvGender.setText(value);
                break;
            case Constant.RequestCode.DIALOG_RADIO_PICTURE:
                switch (number) {
                    case Constant.ItemPosition.FROM_ALBUM:
                        Intent albumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        albumIntent.setType("image/*");
                        startActivityForResult(albumIntent, Constant.RequestCode.ALBUM);
                        break;
                    case Constant.ItemPosition.FROM_CAMERA:
                        try {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            Uri uri = FileUtil.getInstance().createCameraPictureFile();
                            PermissionUtil.getInstance().grantWritePermission(this, cameraIntent, uri);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            startActivityForResult(cameraIntent, Constant.RequestCode.CAMERA);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
