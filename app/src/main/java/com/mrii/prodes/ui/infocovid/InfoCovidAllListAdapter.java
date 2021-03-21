package com.mrii.prodes.ui.infocovid;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.databinding.DataBindingUtil;

import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ItemInfoCovidAllListAdapterBinding;
import com.mrii.prodes.databinding.ItemInfoCovidListAdapterBinding;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.DataBoundViewHolder;
import com.mrii.prodes.utils.Objects;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewobject.InfoCovid;

public class InfoCovidAllListAdapter extends DataBoundListAdapter<InfoCovid, ItemInfoCovidAllListAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final InfoCovidAllListAdapter.NewsClickCallback callback;
    private DiffUtilDispatchedInterface diffUtilDispatchedInterface;
    private int lastPosition = -1;

    public InfoCovidAllListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                   InfoCovidAllListAdapter.NewsClickCallback callback,
                                   DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemInfoCovidAllListAdapterBinding createBinding(ViewGroup parent) {
        ItemInfoCovidAllListAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_info_covid_all_list_adapter, parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            InfoCovid infoCovid = binding.getInfoCovid();
            if (infoCovid != null && callback != null) {
                callback.onClick(infoCovid);
            }
        });
        return binding;
    }


    @Override
    public void bindView(DataBoundViewHolder<ItemInfoCovidAllListAdapterBinding> holder, int position) {

        super.bindView(holder, position);

    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemInfoCovidAllListAdapterBinding binding, InfoCovid infoCovid) {

        String infoCovidDesc =infoCovid.description;
        binding.desc.setText(Html.fromHtml(infoCovidDesc));
        binding.setInfoCovid(infoCovid);
        binding.date.setText(Utils.setDateTime(infoCovid.addedDate));

    }

    @Override
    protected boolean areItemsTheSame(InfoCovid oldItem, InfoCovid newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(InfoCovid oldItem, InfoCovid newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    public interface NewsClickCallback {
        void onClick(InfoCovid infoCovid);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        } else {
            lastPosition = position;
        }
    }
}



