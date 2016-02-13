/*
 * Copyright (c)  Jeries Handal - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Jeries Handal <Jeries Handal>,  2016.
 *
 */

package com.jerieshandal.tribies.popup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.jerieshandal.tribies.MainActivity;
import com.jerieshandal.tribies.R;
import com.jerieshandal.tribies.account.AccountDAO;
import com.jerieshandal.tribies.account.AccountDTO;
import com.jerieshandal.tribies.security.TokenGenerator;
import com.jerieshandal.tribies.utility.StringUtils;

/**
 * LoginPopup
 * Created by Jeries Handal on 2/8/2016.
 * Version 1.0.0
 */
public class LoginPopup extends Activity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOGGED_IN_ID = "logCredTken";

    private LoginTask mAuthTask;
    
    //UI References
    private View mProgressView;
    private View mLoginFormView;
    private AutoCompleteTextView mLoginCredentials;
    private EditText mPasswordView;
    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_popup_window);

        initializeWindow();

        mProgressView = findViewById(R.id.login_process);
        mLoginFormView = findViewById(R.id.login_form);
        mLoginCredentials = (AutoCompleteTextView) findViewById(R.id.login_credential);
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mLoginBtn = (Button) findViewById(R.id.login_confirm_btn);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin(){
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mLoginCredentials.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String loginCredentials = mLoginCredentials.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        //Checks for a valid password
        if (TextUtils.isEmpty(password)) {
            focusView = mPasswordView;
            mPasswordView.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if(TextUtils.isEmpty(loginCredentials)){
            focusView = mLoginCredentials;
            mLoginCredentials.setError(getString(R.string.error_field_required));
            cancel = true;
        }else if(loginCredentials.contains("@")){ //if its an email
            if(!StringUtils.isEmailFormatValid(loginCredentials)){
                focusView = mLoginCredentials;
                mLoginCredentials.setError(getString(R.string.error_login_credential));
                cancel = true;
            }
        }else if(!loginCredentials.matches("\\d+")){//phone
            focusView = mLoginCredentials;
            mLoginCredentials.setError(getString(R.string.error_login_credential));
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new LoginTask(loginCredentials, password,
                    PreferenceManager.getDefaultSharedPreferences(this), LoginPopup.this);
            mAuthTask.execute((Void) null);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void initializeWindow() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.3));
    }

    public class LoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mLoginCredential;
        private final String mPasswordView;
        private SharedPreferences mPreferences;
        private Activity mActivity;

        LoginTask(String userCredential,  String password, SharedPreferences preferences, Activity activity) {
            mLoginCredential = userCredential;
            mPasswordView = password;
            mPreferences = preferences;
            mActivity = activity;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean result = false;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
//            try {
//                try (Connection connection = ConnectionFactory.getTribies()) {
            AccountDAO dao = new AccountDAO();
            AccountDTO e = dao.checkMockAccount(mLoginCredential, mPasswordView);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            if (e != null) {
                mPreferences.edit().putString("account", new Gson().toJson(e)).apply();//replace this later
                mPreferences.edit().putString(LoginPopup.LOGGED_IN_ID, e.getToken()).apply();
                result = true;
            }
//                }
//            } catch (SQLException | ClassNotFoundException ex) {
//                ex.printStackTrace();
//            }
            return result;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(intent);
                finish();
            }else{
                mLoginCredentials.setError(getString(R.string.error_login_credential));
                mLoginCredentials.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
