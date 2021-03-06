// Generated by data binding compiler. Do not edit!
package com.example.theatreticketsapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.example.theatreticketsapp.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityRegisterBinding extends ViewDataBinding {
  @NonNull
  public final EditText confirmPassword;

  @NonNull
  public final EditText firstname;

  @NonNull
  public final EditText lastname;

  @NonNull
  public final EditText password;

  @NonNull
  public final ProgressBar progressBar;

  @NonNull
  public final Button registerBtn;

  @NonNull
  public final EditText username;

  @Bindable
  protected String mRegisterUsername;

  @Bindable
  protected String mRegisterPassword;

  @Bindable
  protected String mRegisterConfirmPassword;

  @Bindable
  protected String mRegisterFirstName;

  @Bindable
  protected String mRegisterLastName;

  protected ActivityRegisterBinding(Object _bindingComponent, View _root, int _localFieldCount,
      EditText confirmPassword, EditText firstname, EditText lastname, EditText password,
      ProgressBar progressBar, Button registerBtn, EditText username) {
    super(_bindingComponent, _root, _localFieldCount);
    this.confirmPassword = confirmPassword;
    this.firstname = firstname;
    this.lastname = lastname;
    this.password = password;
    this.progressBar = progressBar;
    this.registerBtn = registerBtn;
    this.username = username;
  }

  public abstract void setRegisterUsername(@Nullable String registerUsername);

  @Nullable
  public String getRegisterUsername() {
    return mRegisterUsername;
  }

  public abstract void setRegisterPassword(@Nullable String registerPassword);

  @Nullable
  public String getRegisterPassword() {
    return mRegisterPassword;
  }

  public abstract void setRegisterConfirmPassword(@Nullable String registerConfirmPassword);

  @Nullable
  public String getRegisterConfirmPassword() {
    return mRegisterConfirmPassword;
  }

  public abstract void setRegisterFirstName(@Nullable String registerFirstName);

  @Nullable
  public String getRegisterFirstName() {
    return mRegisterFirstName;
  }

  public abstract void setRegisterLastName(@Nullable String registerLastName);

  @Nullable
  public String getRegisterLastName() {
    return mRegisterLastName;
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_register, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityRegisterBinding>inflateInternal(inflater, R.layout.activity_register, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_register, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityRegisterBinding>inflateInternal(inflater, R.layout.activity_register, null, false, component);
  }

  public static ActivityRegisterBinding bind(@NonNull View view) {
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
  public static ActivityRegisterBinding bind(@NonNull View view, @Nullable Object component) {
    return (ActivityRegisterBinding)bind(component, view, R.layout.activity_register);
  }
}
