package com.mrii.prodes.ui.item.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ItemItemHorizontalWithUserBinding;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.DataBoundViewHolder;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.Objects;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewobject.Item;

public class ItemHorizontalListAdapter extends DataBoundListAdapter<Item, ItemItemHorizontalWithUserBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ItemHorizontalListAdapter.NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;

    public ItemHorizontalListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                     ItemHorizontalListAdapter.NewsClickCallback callback,
                                     DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemItemHorizontalWithUserBinding createBinding(ViewGroup parent) {
        ItemItemHorizontalWithUserBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_item_horizontal_with_user, parent, false,
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
    public void bindView(DataBoundViewHolder<ItemItemHorizontalWithUserBinding> holder, int position) {
        super.bindView(holder, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemItemHorizontalWithUserBinding binding, Item item) {
        binding.setItem(item);
        Utils.psLog("nilai :"+item.title);
        binding.conditionTextView.setText(binding.getRoot().getResources().getString(R.string.item_condition__type, item.itemCondition.name));
        String currencySymbol = "Rp.";
        String price;
        try {
            price = Utils.format(Double.parseDouble(item.price));
        } catch (Exception e) {
            price = item.price;
        }

        String currencyPrice;
        if (Config.SYMBOL_SHOW_FRONT) {
            currencyPrice = currencySymbol + " " + price;
        } else {
            currencyPrice = price + " " + currencySymbol;
        }
        binding.priceTextView.setText(currencyPrice);

        if (item.isSoldOut.equals(Constants.ONE)) {
            binding.isSoldTextView.setVisibility(View.VISIBLE);
        } else {
            binding.isSoldTextView.setVisibility(View.GONE);
        }

        if (item.paidStatus.equals(Constants.ADSPROGRESS)){
            //binding.addedDateStrTextView.setText(R.string.paid__sponsored);
        } else{
            //binding.addedDateStrTextView.setText(item.addedDateStr);
        }
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


