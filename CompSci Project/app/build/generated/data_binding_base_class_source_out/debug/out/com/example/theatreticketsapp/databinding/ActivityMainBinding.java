// Generated by data binding compiler. Do not edit!
package com.example.theatreticketsapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.example.theatreticketsapp.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityMainBinding extends ViewDataBinding {
  @NonNull
  public final Button loginBtn;

  @NonNull
  public final EditText passwordEditText;

  @NonNull
  public final TextView passwordLbl;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final Button registerBtn;

  @NonNull
  public final TextView textView5;

  @NonNull
  public final EditText usernameEditText;

  @Bindable
  protected String mLgnUsername;

  @Bindable
  protected String mLgnPassword;

  protected ActivityMainBinding(Object _bindingComponent, View _root, int _localFieldCount,
      Button loginBtn, EditText passwordEditText, TextView passwordLbl, ProgressBar progressBar,
      Button registerBtn, TextView textView5, EditText usernameEditText) {
    super(_bindingComponent, _root, _localFieldCount);
    this.loginBtn = loginBtn;
    this.passwordEditText = passwordEditText;
    this.passwordLbl = passwordLbl;
    this.progressBar = progressBar;
    this.registerBtn = registerBtn;
    this.textView5 = textView5;
    this.usernameEditText = usernameEditText;
  }

  public abstract void setLgnUsername(@Nullable String lgnUsername);

  @Nullable
  public String getLgnUsername() {
    return mLgnUsername;
  }

  public abstract void setLgnPassword(@Nullable String lgnPassword);

  @Nullable
  public String getLgnPassword() {
    return mLgnPassword;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_main, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityMainBinding>inflateInternal(inflater, R.layout.activity_main, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_main, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityMainBinding>inflateInternal(inflater, R.layout.activity_main, null, false, component);
  }

  public static ActivityMainBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static ActivityMainBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityMainBinding)bind(component, view, R.layout.activity_main);
  }
}
