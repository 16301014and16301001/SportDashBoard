package edu.bjtu.example.sportsdashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;


public class LoginFragment extends Fragment {

    private View view;
    private TextView toRegister;
    private String response;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        // Inflate the layout for this fragment
        toRegister = view.findViewById(R.id.toRegister);
        Button button = view.findViewById(R.id.loginBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                login();
            }

        });
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toRegister.setClickable(true);
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                        new RegisterFragment()).commit();
            }
        });
    }

  
    public String getAccount() {
        EditText username = view.findViewById(R.id.userId);
        return username.getText().toString().trim();
    }

    public String getPassword() {
        EditText password = view.findViewById(R.id.pass);
        return password.getText().toString().trim();
    }

    private void login() {

        //先做一些基本的判断，比如输入的用户命为空，密码为空，网络不可用多大情况，都不需要去链接服务器了，而是直接返回提示错误
        if (getAccount().isEmpty()) {
            showToast("你输入的账号为空！");
            return;
        }
        if (getPassword().isEmpty()) {
            showToast("你输入的密码为空！");
            return;
        }
        //登录一般都是请求服务器来判断密码是否正确，要请求网络，要子线程
        Thread httpClient = new Thread() {
            @Override
            public void run() {
                HttpClient Client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://10.0.2.2:8848");
                NameValuePair pair1 = new BasicNameValuePair("name", getAccount());
                NameValuePair pair2 = new BasicNameValuePair("password", getPassword());
                List<NameValuePair> pairList = new ArrayList<NameValuePair>();
                pairList.add(pair1);
                pairList.add(pair2);
                try {

                //   httpPost.setEntity(new StringEntity(getAccount()+" "+getPassword()));
                    HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList);
                    httpPost.setEntity(requestHttpEntity);
                 HttpResponse httpResponse = Client.execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                      response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串
                        System.out.println(response);
                        showToast(response);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
                                new PosterFragment()).commit();
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

//        Thread namePair = new Thread() {
//            @Override
//            public void run() {
////                NameValuePair pair1 = new BasicNameValuePair("name", getAccount());
////                NameValuePair pair2 = new BasicNameValuePair("password", getPassword());
////                List<NameValuePair> pairList = new ArrayList<NameValuePair>();
////                pairList.add(pair1);
////                pairList.add(pair2);
////                try {
////                    HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
////                            pairList);
////                    // URl是接口地址
////                    HttpPost httpPost = new HttpPost(url);
////                    // 将请求体内容加入请求中
////                    httpPost.setEntity(requestHttpEntity);
////                    // 需要客户端对象来发送请求
////                    HttpClient httpClient = new DefaultHttpClient();
////                    // 发送请求
////                    HttpResponse response = httpClient.execute(httpPost);
////                    // 显示响应
////                    System.out.println(response);
////                    //showResponseResult(response);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//            }
//        };

        httpClient.start();

    }

    private void showToast(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

            }
        });
    }


}



