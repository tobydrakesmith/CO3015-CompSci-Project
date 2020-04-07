package com.example.theatreticketsapp.databinding;
import com.example.theatreticketsapp.R;
import com.example.theatreticketsapp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityRegisterBindingImpl extends ActivityRegisterBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.registerBtn, 6);
        sViewsWithIds.put(R.id.progressBar, 7);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityRegisterBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 8, sIncludes, sViewsWithIds));
    }
    private ActivityRegisterBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.EditText) bindings[5]
            , (android.widget.EditText) bindings[3]
            , (android.widget.EditText) bindings[4]
            , (android.widget.EditText) bindings[2]
            , (android.widget.ProgressBar) bindings[7]
            , (android.widget.Button) bindings[6]
            , (android.widget.EditText) bindings[1]
            );
        this.confirmPassword.setTag(null);
        this.firstname.setTag(null);
        this.lastname.setTag(null);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.password.setTag(null);
        this.username.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x20L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.registerUsername == variableId) {
            setRegisterUsername((java.lang.String) variable);
        }
        else if (BR.registerConfirmPassword == variableId) {
            setRegisterConfirmPassword((java.lang.String) variable);
        }
        else if (BR.registerLastName == variableId) {
            setRegisterLastName((java.lang.String) variable);
        }
        else if (BR.registerPassword == variableId) {
            setRegisterPassword((java.lang.String) variable);
        }
        else if (BR.registerFirstName == variableId) {
            setRegisterFirstName((java.lang.String) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setRegisterUsername(@Nullable java.lang.String RegisterUsername) {
        this.mRegisterUsername = RegisterUsername;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.registerUsername);
        super.requestRebind();
    }
    public void setRegisterConfirmPassword(@Nullable java.lang.String RegisterConfirmPassword) {
        this.mRegisterConfirmPassword = RegisterConfirmPassword;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.registerConfirmPassword);
        super.requestRebind();
    }
    public void setRegisterLastName(@Nullable java.lang.String RegisterLastName) {
        this.mRegisterLastName = RegisterLastName;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.registerLastName);
        super.requestRebind();
    }
    public void setRegisterPassword(@Nullable java.lang.String RegisterPassword) {
        this.mRegisterPassword = RegisterPassword;
        synchronized(this) {
            mDirtyFlags |= 0x8L;
        }
        notifyPropertyChanged(BR.registerPassword);
        super.requestRebind();
    }
    public void setRegisterFirstName(@Nullable java.lang.String RegisterFirstName) {
        this.mRegisterFirstName = RegisterFirstName;
        synchronized(this) {
            mDirtyFlags |= 0x10L;
        }
        notifyPropertyChanged(BR.registerFirstName);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        java.lang.String registerUsername = mRegisterUsername;
        java.lang.String registerConfirmPassword = mRegisterConfirmPassword;
        java.lang.String registerLastName = mRegisterLastName;
        java.lang.String registerPassword = mRegisterPassword;
        java.lang.String registerFirstName = mRegisterFirstName;

        if ((dirtyFlags & 0x21L) != 0) {
        }
        if ((dirtyFlags & 0x22L) != 0) {
        }
        if ((dirtyFlags & 0x24L) != 0) {
        }
        if ((dirtyFlags & 0x28L) != 0) {
        }
        if ((dirtyFlags & 0x30L) != 0) {
        }
        // batch finished
        if ((dirtyFlags & 0x22L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.confirmPassword, registerConfirmPassword);
        }
        if ((dirtyFlags & 0x30L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.firstname, registerFirstName);
        }
        if ((dirtyFlags & 0x24L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.lastname, registerLastName);
        }
        if ((dirtyFlags & 0x28L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.password, registerPassword);
        }
        if ((dirtyFlags & 0x21L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.username, registerUsername);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): registerUsername
        flag 1 (0x2L): registerConfirmPassword
        flag 2 (0x3L): registerLastName
        flag 3 (0x4L): registerPassword
        flag 4 (0x5L): registerFirstName
        flag 5 (0x6L): null
    flag mapping end*/
    //end
}