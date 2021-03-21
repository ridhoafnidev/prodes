package com.mrii.prodes.ui.item.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ItemCoverItemDetailBinding;
import com.mrii.prodes.databinding.ItemItemPopulerHorizontalWithUserBinding;
import com.mrii.prodes.ui.chathistory.adapter.PagerAdapter;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.DataBoundViewHolder;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.Objects;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewobject.Item;

import java.util.List;

public class ImageCoverItemDetailAdapter extends DataBoundListAdapter<Item, ItemCoverItemDetailBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ImageCoverItemDetailAdapter.NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;

    public ImageCoverItemDetailAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                       ImageCoverItemDetailAdapter.NewsClickCallback callback,
                                            DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemCoverItemDetailBinding createBinding(ViewGroup parent) {
        ItemCoverItemDetailBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_cover_item_detail, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Item item = binding.getItem();
            if (item != null && callback != null) {
                callback.onClick(item);
            }
        });
        return binding;
    }


    @Override
    public void bindView(DataBoundViewHolder<ItemCoverItemDetailBinding> holder, int position) {
        super.bindView(holder, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemCoverItemDetailBinding binding, Item item) {

        binding.setItem(item);

    }

    @Override
    protected boolean areItemsTheSame(Item oldItem, Item newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.title.equals(newItem.title)
                && oldItem.isFavourited.equals(newItem.isFavourited)
                && oldItem.favouriteCount.equals(newItem.favouriteCount)
                && oldItem.isSoldOut.equals(newItem.isSoldOut);
    }

    @Override
    protected boolean areContentsTheSame(Item oldItem, Item newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.title.equals(newItem.title)
                && oldItem.isFavourited.equals(newItem.isFavourited)
                && oldItem.favouriteCount.equals(newItem.favouriteCount)
                && oldItem.isSoldOut.equals(newItem.isSoldOut);
    }

    public interface NewsClickCallback {
        void onClick(Item item);
    }

}
