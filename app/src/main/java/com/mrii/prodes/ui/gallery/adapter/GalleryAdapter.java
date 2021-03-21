package com.mrii.prodes.ui.gallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ItemGalleryAdapterBinding;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.utils.Objects;
import com.mrii.prodes.utils.Tools;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewobject.Image;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Panacea-Soft on 12/6/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class GalleryAdapter extends DataBoundListAdapter<Image, ItemGalleryAdapterBinding> {

    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;
    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ImageClickCallback callback;
    private ImageView imageView;
    private Context context;
    public GalleryAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                          ImageClickCallback callback,
                          DiffUtilDispatchedInterface diffUtilDispatchedInterface, Context context) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
        this.context = context;
    }

    @Override
    protected ItemGalleryAdapterBinding createBinding(ViewGroup parent) {
        ItemGalleryAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_gallery_adapter, parent, false,
                        dataBindingComponent);
        imageView = binding.itemImageView;
//        binding.menuImageView.setOnClickListener(view -> {
//            try {
//                Bitmap bitmap = Tools.getBitmapFromView(getCurrentImageView());
//                shareImageUri(saveImageExternal(bitmap));
//            } catch (Exception e) {
//                Utils.psErrorLog(" ", e);
//            }
//        });

        binding.getRoot().setOnClickListener(v -> {
            Image image = binding.getImage();
            if (image != null && callback != null) {
                Utils.psLog("Clicked Image" + image.imgDesc);

                callback.onClick(image);
            }
        });
        return binding;
    }

//    private Uri saveImageExternal(Bitmap image) {
//        Uri uri = null;
//        try {
//            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//            StrictMode.setVmPolicy(builder.build());
//            File file = new File(java.util.Objects.requireNonNull(context).getExternalFilesDir(Environment.DIRECTORY_PICTURES), "to-share.png");
//            FileOutputStream stream = new FileOutputStream(file);
//            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
//            stream.close();
//            uri = Uri.fromFile(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return uri;
//    }

    private void shareImageUri(Uri uri) {

        new Thread(() -> {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType("image/*");

                Intent shareIntent = Intent.createChooser(intent, null);
                context.startActivity(shareIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private ImageView getCurrentImageView() {
        return imageView;
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemGalleryAdapterBinding binding, Image item) {
        binding.setImage(item);
    }

    @Override
    protected boolean areItemsTheSame(Image oldItem, Image newItem) {
        return Objects.equals(oldItem.imgId, newItem.imgId);
    }

    @Override
    protected boolean areContentsTheSame(Image oldItem, Image newItem) {
        return Objects.equals(oldItem.imgId, newItem.imgId);
    }

    public interface ImageClickCallback {
        void onClick(Image item);
    }
}