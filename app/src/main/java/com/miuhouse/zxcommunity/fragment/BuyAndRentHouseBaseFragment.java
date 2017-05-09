package com.miuhouse.zxcommunity.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.miuhouse.zxcommunity.R;
import com.miuhouse.zxcommunity.adapter.SimpleSelectAdapter;
import com.miuhouse.zxcommunity.application.MyApplication;
import com.miuhouse.zxcommunity.bean.Property;
import com.miuhouse.zxcommunity.bean.PropertyListBean;
import com.miuhouse.zxcommunity.http.FinalData;
import com.miuhouse.zxcommunity.http.GsonRequest;
import com.miuhouse.zxcommunity.http.VolleySingleton;
import com.miuhouse.zxcommunity.utils.Constants;
import com.miuhouse.zxcommunity.utils.MyCount;
import com.miuhouse.zxcommunity.utils.MyUtils;
import com.miuhouse.zxcommunity.utils.SPUtils;
import com.miuhouse.zxcommunity.utils.StringUtils;
import com.miuhouse.zxcommunity.utils.ToastUtils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 买房子 和租房子 的基类
 * Created by kings on 1/20/2016.
 */
public abstract class BuyAndRentHouseBaseFragment extends BaseFragment implements AMapLocationListener {
    public List<String> houseTypes = new ArrayList<>();
    private List<String> propertyList = new ArrayList<>();
    private List<Long> propertyIDList = new ArrayList<>();
    public List<String> priceList = new ArrayList<>();
    public View view;
    //价格
    private LinearLayout linearPrice;
    //房屋类型
    private LinearLayout linearHouseType;

    public SimpleSelectAdapter priceAdapter;
    public SimpleSelectAdapter houseTypeAdapter;
    //小区
    private SimpleSelectAdapter propertyAdapter;
    private DialogPlus dialog;
    public TextView tvPrice;
    public TextView tvHouseType;
    public TextView tvProperty;
    //验证码
    private EditText etCode;
    //获取验证码
    private TextView btnGetCode;
    public Button btnSubmit;
    public TextView etAddress;
    public EditText etMessage;
    public EditText etPhoneNumber;
    private String houseType;

    public String propertyName;
    private String price;
    private long propertyID;
    private String strPhone;
    private MyCount mc;
    private EventHandler eventHandler;

    private MyApplication myApplication;
    //城市定位
    public AMapLocationClient mLocationClient = null;
    //声明配置定位参数对象
    public AMapLocationClientOption mLocationOption = null;

    public abstract int provideViewId();

    public abstract List<String> getPriceList();

    public abstract void viewfill();

    public abstract void sendRequest();

    public abstract boolean isFill();

    @Override
    public void initData() {
        SMSSDK.initSDK(activity, Constants.SMSSDK_APP_KEY, Constants.SMSSDK_APP_SECRET);
        eventHandler = new EventHandler() {
            @Override
            public void onRegister() {
                super.onRegister();
            }

            @Override
            public void onUnregister() {
                super.onUnregister();
            }

            @Override
            public void beforeEvent(int i, Object o) {
                super.beforeEvent(i, o);
            }

            @Override
            public void afterEvent(int event, int result, Object data) {
                Log.w("TAG", "event = " + event + "  result = " + result);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Map<String, Object> map = (HashMap<String, Object>) data;
                        Log.w("TAG", "EVENT_SUBMIT_VERIFICATION_CODE " + map.toString());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendRequest();
//                                goToStepTwo();
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
//                        isMsg = true;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToast(getActivity(), getResources().getString(R.string.verifycode_sent));
                            }
                        });
                        Log.w("TAG", "get code success");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                        ArrayList<HashMap<String, Object>> countryList = (ArrayList<HashMap<String, Object>>) data;
                        Log.d("TAG", "countryList = " + countryList.toString());
                    }
                } else {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            ToastUtils.showToast(activity, "验证码验证失败");
//                            progress.dismissAllowingStateLoss();
                        }
                    });

                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
        SMSSDK.getSupportedCountries();
    }

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(provideViewId(), null);
        myApplication = MyApplication.getInstance();
        linearPrice = (LinearLayout) view.findViewById(R.id.linear_price);
        linearHouseType = (LinearLayout) view.findViewById(R.id.linear_house_type);
        tvPrice = (TextView) view.findViewById(R.id.tv_price);
        tvHouseType = (TextView) view.findViewById(R.id.tv_house_type);
        etCode = (EditText) view.findViewById(R.id.et_code);
        btnGetCode = (TextView) view.findViewById(R.id.btn_get_code);
        etPhoneNumber = (EditText) view.findViewById(R.id.et_phone_number);
        btnSubmit = (Button) view.findViewById(R.id.btn_submit);
        etAddress = (TextView) view.findViewById(R.id.et_address);
        etMessage = (EditText) view.findViewById(R.id.et_message);

        viewfill();

        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btnGetCode.setBackgroundResource(R.color.btn_green);
                } else {
                    btnGetCode.setBackgroundResource(R.color.btn_default);

                }
            }
        });
        //户型
        linearHouseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.hideSoftKeyboard(linearHouseType);
                if (houseTypeAdapter == null) {
                    houseTypes = Arrays.asList(SPUtils.getSPData(Constants.FX));
                    houseTypeAdapter = new SimpleSelectAdapter(getActivity(), houseTypes);
                }
                showOnlyContentDialog(new ListHolder(), houseTypeAdapter, houseTypeClickListener, true, "户型");

            }
        });
        //价格
        linearPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyUtils.hideSoftKeyboard(linearPrice);
                if (priceAdapter == null) {
                    priceAdapter = new SimpleSelectAdapter(getActivity(), getPriceList());
                }
                showOnlyContentDialog(new ListHolder(), priceAdapter, itemClickListener, true, "价格");
            }
        });
        //区域选择
        view.findViewById(R.id.linear_property).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.hideSoftKeyboard(v);
                if (propertyAdapter == null) {
                    propertyAdapter = new SimpleSelectAdapter(getActivity(), propertyList);
                    if (myApplication.getCity() != null) {
                        RequestProperty(myApplication.getCity());
                    } else {
                        setparameter();
                    }
                }
                showOnlyContentDialog(new ListHolder(), propertyAdapter, propertyClickListener, true, "小区选择");
            }
        });
        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vCode = etCode.getText().toString();
                if (isFill())
                    return;
                if (StringUtils.isEmpty(vCode)) {
                    ToastUtils.showToast(activity, "请输入验证码");
                    return;
                }
                if (!StringUtils.isMobile(etPhoneNumber.getText().toString().trim())) {
                    ToastUtils.showToast(activity, "请输入正确的电话格式");
                    return;
                }
                SMSSDK.submitVerificationCode(Constants.SMSSDK_COUNTRYCODE, etPhoneNumber.getText().toString().trim(), vCode);

            }
        });
        return view;
    }

    private void sendCode() {
        strPhone = etPhoneNumber.getText().toString().trim();
        if (StringUtils.isEmpty(strPhone)) {
            etPhoneNumber.setError("请输入手机号码");
            etPhoneNumber.requestFocus();
            return;
        }
        if (!StringUtils.isMobile(strPhone)) {
            etPhoneNumber.setError("手机号码格式不对");
            etPhoneNumber.requestFocus();
            return;
        }
        SMSSDK.getVerificationCode("86", strPhone);
        if (mc == null) {
            mc = new MyCount(60000, 1000, btnGetCode, getActivity()); // 第一参数是总的时间，第二个是间隔时间 都是毫秒为单位
        }
        mc.start();
    }

    private void setparameter() {
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        mLocationOption = new AMapLocationClientOption();
        mLocationClient.setLocationListener(this);

        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    //价格
    OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
            if (position == 0) {
                dialog.dismiss();
                return;
            }
            price = getPriceList().get(position - 1);
            tvPrice.setText(price);
            dialog.dismiss();

        }
    };
    //
    OnItemClickListener houseTypeClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
            if (position == 0) {
                dialog.dismiss();
                return;
            }
            houseType = houseTypes.get(position - 1);
            tvHouseType.setText(houseType);
            dialog.dismiss();

        }
    };
    //小区选择
    OnItemClickListener propertyClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(DialogPlus dialogPlus, Object o, View view, int position) {
            if (position == 0) {
                dialog.dismiss();
                return;
            }
            propertyName = propertyList.get(position - 1);
            propertyID = propertyIDList.get(position - 1);
            etAddress.setText(propertyName);
            dialog.dismiss();
        }
    };

    public void showOnlyContentDialog(Holder holder, BaseAdapter adapter,
                                      OnItemClickListener itemClickListener, boolean expanded, final String title) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_head_price, null);
        dialog = DialogPlus.newDialog(getActivity())
                .setContentHolder(holder)
                .setHeader(view)
                .setGravity(Gravity.BOTTOM)
                .setAdapter(adapter)
                .setOnItemClickListener(itemClickListener)
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialogPlus) {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogPlus dialogPlus) {

                    }
                })
                .setExpanded(expanded)
                .setCancelable(true)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();
        dialog.show();
        ((TextView) view.findViewById(R.id.dialog_title)).setText(title);
        view.findViewById(R.id.tv_dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }


    private void RequestProperty(final String city) {
        String url = FinalData.URL_VALUE + "propertyList";
        Map<String, Object> map = new HashMap<>();
        map.put("city", city);
        GsonRequest<PropertyListBean> request = new GsonRequest<>(Request.Method.POST, url, PropertyListBean.class, map, getListener(), new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse response = volleyError.networkResponse;

                if (volleyError instanceof NetworkError) {
                } else if (volleyError instanceof ServerError) {
                    //服务器出错 重试或者显示相应的错误（服务器处理出错了）
                } else if (volleyError instanceof AuthFailureError) {
                    //登录 出错
                } else if (volleyError instanceof ParseError) {

                } else if (volleyError instanceof NoConnectionError) {
                    //连接出错
                } else if (volleyError instanceof TimeoutError) {
                    //超时
                }

            }
        });

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }

    private Response.Listener<PropertyListBean> getListener() {
        return new Response.Listener<PropertyListBean>() {
            @Override
            public void onResponse(PropertyListBean str) {
                Log.i("TAG", "str=" + str);

                if (str.getCode() == 0 && str.getList() != null && str.getList().size() > 0) {
//                    if (!EditCourseActivity.this.isFinishing()) {
//                        linearAddCourse.setVisibility(View.GONE);
                    for (Property property : str.getList()) {
                        propertyList.add(property.getName());
                        propertyIDList.add(property.getId());
                    }
//                    propertyList.addAll(str.getList().);
                    propertyAdapter.notifyDataSetChanged();
//                    }
                } else {
//                    linearAddCourse.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    public String getHouseType() {
        return houseType;
    }

    public String getPrice() {
        return price;
    }

    public long getPropertyID() {
        return propertyID;
    }

    public String getEtAddressToString() {

        return etAddress.getText().toString();
    }

    public String getEtPhoneNumberToString() {
        return etPhoneNumber.getText().toString();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                String city = aMapLocation.getCity();
                Log.i("TAG", "city=" + city);

                int length = city.length() - 1;
                city = city.substring(0, length);
                myApplication.setCity(city);
                RequestProperty(city);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
    }
}
