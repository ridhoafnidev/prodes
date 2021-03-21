package com.mrii.prodes.ui.item.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ItemItemHorizontalWithUserBinding;
import com.mrii.prodes.databinding.ItemItemPopulerHorizontalWithUserBinding;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.DataBoundViewHolder;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.Objects;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewobject.Item;

public class ItemPopulerHorizontalListAdapter extends DataBoundListAdapter<Item, ItemItemPopulerHorizontalWithUserBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ItemPopulerHorizontalListAdapter.NewsClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;

    public ItemPopulerHorizontalListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                            ItemPopulerHorizontalListAdapter.NewsClickCallback callback,
                                     DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemItemPopulerHorizontalWithUserBinding createBinding(ViewGroup parent) {
        ItemItemPopulerHorizontalWithUserBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_item_populer_horizontal_with_user, parent, false,
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
    public void bindView(DataBoundViewHolder<ItemItemPopulerHorizontalWithUserBinding> holder, int position) {
        super.bindView(holder, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemItemPopulerHorizontalWithUserBinding binding, Item item) {

        binding.setItem(item);

        binding.conditionTextView.setText(item.itemCondition.name);
//        String currencySymbol = item.itemCurrency.currencySymbol;
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


