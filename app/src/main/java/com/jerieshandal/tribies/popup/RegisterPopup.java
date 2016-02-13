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
import android.content.Context;
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
import com.jerieshandal.tribies.LoginActivity;
import com.jerieshandal.tribies.MainActivity;
import com.jerieshandal.tribies.R;
import com.jerieshandal.tribies.account.AccountDAO;
import com.jerieshandal.tribies.account.AccountDTO;
import com.jerieshandal.tribies.utility.StringUtils;

/**
 * RegisterPopup
 * Created by Jeries Handal on 2/5/2016.
 * Version 1.0.0
 */
public class RegisterPopup extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RegisterTask mAuthTask;

    //UI Refences
    private View mProgressView;
    private View mRegisterFormView;
    private AutoCompleteTextView mFullNameView;
    private EditText mEmailView;
    private EditText mPhoneView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_popup_window);
        initializeWindow();

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.login_process);
        mFullNameView = (AutoCompleteTextView) findViewById(R.id.register_full_name);
        mEmailView = (EditText) findViewById(R.id.register_email);
        mPhoneView = (EditText) findViewById(R.id.register_phone);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.register_confirm_password);
        mRegisterButton = (Button) findViewById(R.id.register_confirm_btn);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
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
     * Attempts to register the account specified by the register form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mFullNameView.setError(null);
        mEmailView.setError(null);
        mPhoneView.setError(null);
        mPasswordView.setError(null);
        mConfirmPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String name = mFullNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String phone = mPhoneView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirm_password = mConfirmPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //checks for valid phone number
        if (TextUtils.isEmpty(phone)) {
            focusView = mPhoneView;
            mPhoneView.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        //Checks for a valid password
        if (TextUtils.isEmpty(password)) {
            focusView = mPasswordView;
            mPasswordView.setError(getString(R.string.error_field_required));
            cancel = true;
        } else if (!isPasswordValid(password)) {
            focusView = mPasswordView;
            mPasswordView.setError(getString(R.string.error_invalid_password));
            cancel = true;
        } else if (!password.equals(confirm_password)) {
            focusView = mPasswordView;
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mConfirmPasswordView.setError(getString(R.string.error_password_mismatch));
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else {
            if (!StringUtils.isEmailFormatValid(email)) {
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;
            }
        }
        //Checks for a valid full name
        if (TextUtils.isEmpty(name)) {
            mFullNameView.setError(getString(R.string.error_field_required));
            focusView = mFullNameView;
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
            mAuthTask = new RegisterTask(name, email, phone, password,
                    PreferenceManager.getDefaultSharedPreferences(this), RegisterPopup.this);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void initializeWindow() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));
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

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class RegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mEmail;
        private final String mPhone;
        private final String mPassword;
        private SharedPreferences mPreferences;
        private Activity mActivity;

        RegisterTask(String name, String email, String phone, String password, SharedPreferences preferences, Activity activity) {
            mName = name;
            mEmail = email;
            mPhone = phone;
            mPassword = password;
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
            AccountDTO e = dao.registerMockAccount(mName, mEmail, mPhone, mPassword);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            if (e != null) {
                mPreferences.edit().putString("account", new Gson().toJson(e)).apply();//remove this later
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
            } else {
//                mPasswordView.setError(getString(R.string.));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
