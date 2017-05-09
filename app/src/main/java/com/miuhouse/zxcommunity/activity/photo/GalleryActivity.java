package com.miuhouse.zxcommunity.activity.photo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.activity.BaseActivity;
import com.miuhouse.zxcommunity.photo.PhotoView;
import com.miuhouse.zxcommunity.photo.PhotoViewAttacher;
import com.miuhouse.zxcommunity.photo.ZoomOutPageTransformer;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.miuhouse.zxcommunity.widget.StatusCompat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 图片浏览器
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@SuppressLint("NewApi")
public class GalleryActivity extends BaseActivity {
    private static final String EXTERNAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String SAVE_PIC_PATH = EXTERNAL_PATH + "/ihome/image/";
    private static final String CURRENT_VISIBLE_PAGE = "currentPage";
    private HashSet<ViewGroup> unRecycledViews = new HashSet<ViewGroup>();
    private ViewPager pager;
    private TextView position;
    private TextView sum;
    private int index;
    private boolean isShow = true;
    // 图片地址集合
    private List<String> urls;
    private ImageButton imgbBack;
    private ImagePagerAdapter imagePageAdapter;

    @Override
    public void initTitle() {

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_gallery);
        StatusCompat.compat(this, StatusCompat.COLOR_DEFAULT);
        imgbBack = (ImageButton) findViewById(R.id.imgb_back);
        position = (TextView) findViewById(R.id.position);
        sum = (TextView) findViewById(R.id.sum);
        if (urls != null && urls.size() > 0) {
            sum.setText(String.valueOf(urls.size()));
        }
        pager = (ViewPager) findViewById(R.id.pager);
        imgbBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });

        imagePageAdapter = new ImagePagerAdapter();
        pager.setAdapter(imagePageAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                super.onPageSelected(position);
                GalleryActivity.this.position.setText(String.valueOf(position + 1));
            }
        });
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setCurrentItem(index);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initVariables() {
        urls = (ArrayList<String>) getIntent().getStringArrayListExtra("imgPath");
        index = getIntent().getIntExtra("index", 0);

    }

    @Override
    public String getTag() {
        return null;
    }


    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            GalleryActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @SuppressLint("NewApi")
    // 注销
    @Override
    public void onDestroy() {

        super.onDestroy();
        MyUtils.recycleViewGroupAndChildViews(pager, true);
        for (ViewGroup viewGroup : unRecycledViews) {
            MyUtils.recycleViewGroupAndChildViews(viewGroup, true);
        }
        System.gc();
    }


    private class ImagePagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;

        public ImagePagerAdapter() {
            inflater = getLayoutInflater();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            if (object instanceof ViewGroup) {
                ((ViewPager) container).removeView((View) object);
                unRecycledViews.remove(object);
                ViewGroup viewGroup = (ViewGroup) object;
                MyUtils.recycleViewGroupAndChildViews(viewGroup, true);
            }
        }

        @Override
        public int getCount() {

            return urls.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View contentView = inflater.inflate(R.layout.page_item_gallery, container, false);
            handlePage(position, contentView, true);
            ((ViewPager) container).addView(contentView, 0);
            unRecycledViews.add((ViewGroup) contentView);
            return contentView;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0.equals(arg1);
        }

        @Override
        public void setPrimaryItem(View container, int position, Object object) {

            super.setPrimaryItem(container, position, object);
            View contentView = (View) object;
            if (contentView == null) {
                return;
            }
            container.setTag(CURRENT_VISIBLE_PAGE);
            ImageView imageView = (ImageView) contentView.findViewById(R.id.image);

            if (imageView.getDrawable() != null) {
                return;
            }
        }
    }

    private void handlePage(int position, View contentView, boolean fromInstantiateItem) {
        final PhotoView imageView = (PhotoView) contentView.findViewById(R.id.image);
        final ProgressBar mProgress = (ProgressBar) contentView.findViewById(R.id.anim_progress);
        imageView.setVisibility(View.VISIBLE);
        imageView.setDrawingCacheEnabled(true);
        final String path = urls.get(position);
        Log.i("TAG", "path=" + path);
        Glide.with(GalleryActivity.this).load(path).into(new GlideDrawableImageViewTarget(imageView) {
            @Override
            public void onStart() {

                super.onStart();
            }

            @Override
            public void onResourceReady(GlideDrawable arg0, GlideAnimation<? super GlideDrawable> arg1) {

                super.onResourceReady(arg0, arg1);
                mProgress.setVisibility(View.GONE);
            }
        });
        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View view, float x, float y) {

                // GalleryActivity.this.finish();
//                if (isShow) {
//                    isShow = false;
//                    animateHide();
//                } else {
//                    isShow = true;
//                    animateBack();
//                }
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

//                DialogFragment dialog = new FireMissilesDialogFragment(path, imageView);
//                dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
//                 save(path, imageView);
                return false;
            }
        });
    }

//    public void save(String qrcode, PhotoView imageView) { // 保存图片到相册
//        // AlertDialog.
//        File folder = new File(SAVE_PIC_PATH);
//        if (!folder.exists()) {
//            folder.mkdirs();
//        }
//        int length = qrcode.split("/").length;
//        String fileName = qrcode.split("/")[length - 1];
//        File file = new File(folder, fileName);
//        Log.i("TAG", file.getAbsolutePath());
//        BufferedOutputStream bos = null;
//        try {
//            if (!file.exists()) {
//                file.createNewFile();
//            } else {
//                file.delete();
//            }
//            bos = new BufferedOutputStream(new FileOutputStream(file));
//            Log.i("TAG", "imageViews=" + imageView.getDrawingCache());
//            Bitmap bitmap = imageView.getDrawingCache().copy(Config.RGB_565, false); // getDrawingCache拿到的bitmap要copy一下，直接用会报java.lang.IllegalStateException: Can't compress a recycled bitmap
//
//            Log.i("TAG", bitmap + "");
//            if (bitmap != null) {
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                bos.flush();
//                bos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        // 通知系统更新相册
//        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri uri = Uri.fromFile(file);
//        intent.setData(uri);
//        sendBroadcast(intent);
//        ToastUtils.showToast(this, "已保存至相册中");
//    }

//    public class FireMissilesDialogFragment extends DialogFragment {
//        private String qrcode;
//        private PhotoView imageView;
//
//        public FireMissilesDialogFragment(String qrcode, PhotoView imageView) {
//            //
//            this.qrcode = qrcode;
//            this.imageView = imageView;
//        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the Builder class for convenient dialog construction
//            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
//            builder.setMessage("保存图片到相册吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    // FIRE ZE MISSILES!
//                    save(qrcode, imageView);
//                }
//            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    // User cancelled the dialog
//                }
//            });
//            // Create the AlertDialog object and return it
//            return builder.create();
//        }
//    }


}
