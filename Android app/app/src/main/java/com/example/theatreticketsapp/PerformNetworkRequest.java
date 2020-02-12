//package com.example.theatreticketsapp;
//
//import android.os.AsyncTask;
//import android.view.View;
//import android.widget.Toast;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//
//
//
//
//
//public class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
//        String url;
//        private static final int CODE_GET_REQUEST = 1024;
//        private static final int CODE_POST_REQUEST = 1025;
//        public String toRet = "";
//
//        //the parameters
//        HashMap<String, String> params;
//
//        //the request code to define whether it is a GET or POST
//        int requestCode;
//
//        //constructor to initialize values
//        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
//            this.url = url;
//            this.params = params;
//            this.requestCode = requestCode;
//        }
//
//        //when the task started displaying a progressbar
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            Register.registerBinding.progressBar.setVisibility(View.VISIBLE);
//        }
//
//
//        //this method will give the response from the request
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            Register.registerBinding.progressBar.setVisibility(View.GONE);
//
//            try {
//                JSONObject object = new JSONObject(s);
//                if (object.getString("error").equals("true")) {
//
//                }
//                else {
//
//                }
//            } catch (JSONException e) {
//
//                e.printStackTrace();
//            }
//        }
//
//        //the network operation will be performed in background
//        @Override
//        protected String doInBackground(Void... voids) {
//            RequestHandler requestHandler = new RequestHandler();
//
//            if (requestCode == CODE_POST_REQUEST) {
//                toRet = requestHandler.sendPostRequest(url, params);
//                return requestHandler.sendPostRequest(url, params);
//            }
//
//            if (requestCode == CODE_GET_REQUEST)
//                return requestHandler.sendGetRequest(url);
//
//            return null;
//        }
//
//
//
//
//}
//
