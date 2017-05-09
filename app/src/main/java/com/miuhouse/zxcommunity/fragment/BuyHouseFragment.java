package com.miuhouse.zxcommunity.fragment;

import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.SimpleSelectAdapter;
import com.miuhouse.zxcommunity.bean.BaseBean;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.SPUtils;
import com.miuhouse.zxcommunity.utils.StringUtils;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.miuhouse.zxcommunity.widget.ProgressFragment;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的需求 买房
 * Created by kings on 1/20/2016.
 */
public class BuyHouseFragment extends BuyAndRentHouseBaseFragment {
    private final static int TYPE = 1;
    private TextView etDefindYear;
    private ProgressFragment progress;
    public List<String> buildingAgeList = new ArrayList<>();
    private SimpleSelectAdapter buildAdapter;
    private String buildingAge;

    @Override
    public int provideViewId() {
        return R.layout.fragment_buy_and_renting;
    }

    @Override
    public List<String> getPriceList() {

        return Arrays.asList(SPUtils.getSPData(Constants.ESFPRICE));
    }


    @Override
    public void viewfill() {
        etDefindYear = (TextView) view.findViewById(R.id.et_defind_year);

        view.findViewById(R.id.linear_building_age).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyUtils.hideSoftKeyboard(v);
                if (buildAdapter == null) {
                    buildingAgeList = Arrays.asList(SPUtils.getSPData(Constants.FLOORAGE));
                    buildAdapter = new SimpleSelectAdapter(getActivity(), buildingAgeList);
                }
                showOnlyContentDialog(new ListHolder(), buildAdapter, itemClickListener, true,"楼龄");
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    //楼龄
    OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
            if (position == 0) {
                dialog.dismiss();
                return;
            }
            buildingAge = buildingAgeList.get(position - 1);
            etDefindYear.setText(buildingAge);
            dialog.dismiss();

        }
    };

    /**
     *
     */
    public void sendRequest() {
        showProgress();
        String urlPath = FinalData.URL_VALUE + "demand";
        Map<String, Object> map = new HashMap<>();
        map.put("propertyId", getPropertyID());
        map.put("type", TYPE);
        map.put("huxing", getHouseType());
        map.put("address", getEtAddressToString());
        map.put("price", getPrice());
        map.put("year", etDefindYear.getText().toString());
        map.put("other", etMessage.getText().toString());
        map.put("mobile", etPhoneNumber.getText().toString());
        GsonRequest<BaseBean> request = new GsonRequest<>(Request.Method.POST, urlPath, BaseBean.class, map, new Response.Listener<BaseBean>() {
            @Override
            public void onResponse(BaseBean baseBean) {
                progress.dismissAllowingStateLoss();
                ToastUtils.showToast(getActivity(), baseBean.getMsg());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.dismissAllowingStateLoss();
                ToastUtils.showToast(getActivity(), "提交失败");
            }
        });
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void showProgress() {
        progress = ProgressFragment.newInstance();
        progress.show(getFragmentManager(), "buy");
    }

    public boolean isFill() {
        if (StringUtils.isEmpty(getEtAddressToString())) {
            etAddress.setError("请输入地址");
            etAddress.requestFocus();
            return true;
        }
        if (StringUtils.isEmpty(getPrice())) {
            ToastUtils.showToast(getActivity(), "请选择价格");
            return true;
        }
        if (StringUtils.isEmpty(getHouseType())) {
            ToastUtils.showToast(getActivity(), "请选择户型");
            return true;
        }
        if (StringUtils.isEmpty(getEtDefindYearToString())) {
            etDefindYear.setError("请输入年限");
            etDefindYear.requestFocus();
            return true;
        }
        if (StringUtils.isEmpty(getEtPhoneNumberToString())) {
            etPhoneNumber.setError("请输入电话号码");
            etPhoneNumber.requestFocus();
            return true;
        }
        return false;
    }


    private String getEtDefindYearToString() {
        return etDefindYear.getText().toString();
    }


}
