package id.my.match.parkir.utility.restapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.StrictMode;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import id.my.match.parkir.utility.sqlite.DBHelper;

import static id.my.match.parkir.utility.restapi.NukeSSLCerts.nuke;

/**
 * Created by M. Ebni Hannibal on 16 Agu 2019.
 * S1Creative,
 * ebni.match@gmail.com,
 * Palembang, INA.
 */

public class VolleyRequest {
    IResult mResultCallback = null;
    Context mContext;
    Alamat alamat = new Alamat();
    int socketTimeout = 15000;  //1000 = 1 seconds
    DBHelper dbh;
    Map<String, String> params = new Hashtable<String, String>();
    NetworkResponse response;
    String link_url;
    StringRequest stringRequest;

    public interface IResult {
        void notifySuccess(String response);
        void notifyError(VolleyError error);
    }

    VolleyRequest(IResult resultCallback, Context context){
        mResultCallback = resultCallback;
        mContext = context;
    }

    public void RestFull(Context context, final int request_method, String link, final Map<String, String> params1,
                         final ProgressDialog loading, String STO){
        if(STO != null){
            socketTimeout = Integer.parseInt(STO+"000");
        }
//        nuke();
        dbh = new DBHelper(context);
        link_url = alamat.getAlamat() + link;

//        Log.d("DATA LINK", link_url);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if(request_method == Request.Method.POST){
            // Request.Method.POST
            JSONObject params = new JSONObject(params1);
            RequestPOST(context, request_method, link_url, params1, loading);
        } else if(request_method == Request.Method.GET){
            // Request.Method.GET
            RequestGET(context, request_method, link_url, params1, loading);
        } else if( request_method == Request.Method.PUT || request_method == Request.Method.DELETE ){
            // Request.Method.PUT
            RequestPUT_DELETE(context, request_method, link_url, params1, loading);
        }
    }

    private void RequestGET(Context context, final int request_method, String url, final Map<String, String> params1,
                            final ProgressDialog loading){
        List<NameValuePair> paramsB = new ArrayList<>(params1.size());
        for (Map.Entry<String, String> entry : params1.entrySet()) {
            paramsB.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        String paramString = URLEncodedUtils.format(paramsB, "utf-8");
        String link_url = url + "?" + paramString;

        stringRequest = new StringRequest(request_method, link_url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if(loading != null){
                        loading.dismiss();
                    }
                    if(mResultCallback != null)
                        mResultCallback.notifySuccess(s);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if(loading != null){
                        loading.dismiss();
                    }
                    response = volleyError.networkResponse;
                    if (volleyError instanceof TimeoutError ||
                            volleyError instanceof ServerError && response == null) {
                        if(mResultCallback != null)
                            mResultCallback.notifyError(volleyError);
                    }else if (volleyError instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers));
                            JSONObject obj = new JSONObject(res);
                            if(mResultCallback != null)
                                mResultCallback.notifySuccess(obj.toString());
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                            if(mResultCallback != null)
                                mResultCallback.notifyError(volleyError);
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                            if(mResultCallback != null)
                                mResultCallback.notifyError(volleyError);
                        }
                    }
                }
            }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                params.clear();
                params.put("Authorization", "BearerTOKEN");
                return params;
            }
        };
        request(context);
    }

    private void RequestPOST(Context context, final int request_method, String link_url,
                             final Map<String, String> params2, final ProgressDialog loading){
        stringRequest = new StringRequest(request_method, link_url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if(loading != null){
                        loading.dismiss();
                    }
                    if(mResultCallback != null)
                        mResultCallback.notifySuccess(s);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if(loading != null){
                        loading.dismiss();
                    }
                    response = volleyError.networkResponse;
                    if (volleyError instanceof TimeoutError ||
                            volleyError instanceof ServerError && response == null) {
                        if(mResultCallback != null)
                            mResultCallback.notifyError(volleyError);
                    }else if (volleyError instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers));
                            JSONObject obj = new JSONObject(res);
                            if(mResultCallback != null)
                                mResultCallback.notifySuccess(obj.toString());
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                            if(mResultCallback != null)
                                mResultCallback.notifyError(volleyError);
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                            if(mResultCallback != null)
                                mResultCallback.notifyError(volleyError);
                        }
                    }
                }
            }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                params.clear();
//                params.put("Content-Type", "application/json; charset=utf-8");
//                return params;
//            }
            @Override
            public Map<String, String> getParams() {
                params.clear();
                params = params2;
                return params;
            }
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                try {
//                    String requestBody = params2.toString();
//                    return  requestBody == null ? null : requestBody.getBytes("utf-8");
//                } catch (UnsupportedEncodingException uee) {
//                    return  null;
//                }
//            }
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
        };
        request(context);
    }

    private void RequestPUT_DELETE(Context context, final int request_method, String link_url, final Map<String, String> params1,
                                   final ProgressDialog loading){
        stringRequest = new StringRequest(request_method, link_url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if(loading != null){
                        loading.dismiss();
                    }
                    if(mResultCallback != null)
                        mResultCallback.notifySuccess(s);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if(loading != null){
                        loading.dismiss();
                    }
                    response = volleyError.networkResponse;
                    if (volleyError instanceof TimeoutError ||
                            volleyError instanceof ServerError && response == null) {
                        if(mResultCallback != null)
                            mResultCallback.notifyError(volleyError);
                    }else if (volleyError instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers));
                            JSONObject obj = new JSONObject(res);
                            if(mResultCallback != null)
                                mResultCallback.notifySuccess(obj.toString());
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                            if(mResultCallback != null)
                                mResultCallback.notifyError(volleyError);
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                            if(mResultCallback != null)
                                mResultCallback.notifyError(volleyError);
                        }
                    }
                }
            }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                params.clear();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "BearerTOKEN");
                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                params.clear();
                params = params1;
                return params;
            }
        };
        request(context);
    }

    private void request(Context context){
        RequestQueue request = Volley.newRequestQueue(context);
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        request.getCache().clear();
        request.add(stringRequest);
    }

//
//    How To Use From Other Class :
//
//    VolleyRequest.IResult mResultCallback = null;
//    JSONObject json;
//    final ProgressDialog;
//
//    mResultCallback = new VolleyRequest.IResult() {
//        @Override
//        public void notifySuccess(String response) {
//            try {
//                json = new JSONObject(response);
//            } catch (JSONException e) {
//                FancyToast.makeText(context, context.getString(R.string.server_error), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//            }
//            ((Activity)(context)).finish();
//            loading.dismiss();
//        }
//        @Override
//        public void notifyError(VolleyError error) {
//            if (error instanceof TimeoutError) {
//                FancyToast.makeText(context, context.getString(R.string.server_rto), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//            } else {
//                FancyToast.makeText(context, context.getString(R.string.server_error), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
//            }
//            ((Activity)(context)).finish();
//            loading.dismiss();
//        }
//    };
//
//    loading = ProgressDialog.show(context, "Loading Title", "Loading....", false, false);
//    VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
//    mVolleyService.RestFull(context, Request.Method.PUT, link_url, params, loading);
//

}
