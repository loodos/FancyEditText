package com.loodos.fancyedittext;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by canberkozcelik on 16.03.2018.
 */

public class FancyEditText extends AppCompatEditText {
    private boolean clearEnabled;
    private Drawable mClearIcon;
    private boolean inputTypePwd;
    private boolean pwdVisible;
    private int mShowPwdIconId;
    private int mHidePwdIconId;
    private Drawable mTogglePwdIcon;
    private Bitmap mBitmap;
    private boolean emojiEnabled;
    private boolean hasFocus;
    private boolean dynamicBackgroundEnabled;
    private Drawable mFocusedBackgroundDrawable;
    private Drawable mErrorBackgroundDrawable;
    private Drawable mSuccessBackgroundDrawable;
    private OnFTextChangeListener mFTextChangeListener;

    public FancyEditText(Context context) {
        super(context);
    }

    public FancyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public FancyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        initAttrs(context, attrs, defStyleAttr);
        if (!emojiEnabled) {
            setFilters(new InputFilter[]{new EmojiExcludeFilter()});
        }

        FancyTextWatcher mTextWatcher = new FancyTextWatcher();
        this.addTextChangedListener(mTextWatcher);

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                FancyEditText.this.hasFocus = hasFocus;
                handleFocusChange();
            }
        });
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FancyEditText, defStyleAttr, 0);
        try {
            clearEnabled = typedArray.getBoolean(R.styleable.FancyEditText_clearEnabled, false);
            boolean pwdIconEnabled = typedArray.getBoolean(R.styleable.FancyEditText_pwdIconEnabled, false);
            dynamicBackgroundEnabled = typedArray.getBoolean(R.styleable.FancyEditText_dynamicBackgroundEnabled, false);

            if (clearEnabled) {
                int clearIconId = typedArray.getResourceId(R.styleable.FancyEditText_clearIcon, -1);
                if (clearIconId == -1) {
                    clearIconId = R.drawable.ic_clear_black_24dp;
                }
                mClearIcon = AppCompatResources.getDrawable(context, clearIconId);
                if (mClearIcon != null) {
                    mClearIcon.setBounds(0, 0, mClearIcon.getIntrinsicWidth(), mClearIcon.getIntrinsicHeight());
                    if (clearIconId == R.drawable.ic_clear_black_24dp) {
                        DrawableCompat.setTint(mClearIcon, getCurrentHintTextColor());
                    }
                }
            }

            int inputType = getInputType();
            if (pwdIconEnabled && (inputType == 129 || inputType == 145 || inputType == 18 || inputType == 225)) {
                inputTypePwd = true;
                pwdVisible = inputType == 145;
                mShowPwdIconId = typedArray.getResourceId(R.styleable.FancyEditText_showPwdIcon, -1);
                mHidePwdIconId = typedArray.getResourceId(R.styleable.FancyEditText_hidePwdIcon, -1);
                if (mShowPwdIconId == -1) {
                    mShowPwdIconId = R.drawable.ic_visibility_black_24dp;
                }
                if (mHidePwdIconId == -1) {
                    mHidePwdIconId = R.drawable.ic_visibility_off_black_24dp;
                }
                int toggleId = pwdVisible ? mShowPwdIconId : mHidePwdIconId;
                mTogglePwdIcon = ContextCompat.getDrawable(context, toggleId);
                if (mShowPwdIconId == R.drawable.ic_visibility_black_24dp || mHidePwdIconId == R.drawable.ic_visibility_off_black_24dp) {
                    DrawableCompat.setTint(mTogglePwdIcon, getCurrentHintTextColor());
                }
                mTogglePwdIcon.setBounds(0, 0, mTogglePwdIcon.getIntrinsicWidth(), mTogglePwdIcon.getIntrinsicHeight());
                int clearIconId = typedArray.getResourceId(R.styleable.FancyEditText_clearIcon, -1);
                if (clearIconId == -1) {
                    clearIconId = R.drawable.ic_clear_black_24dp;
                }
                if (clearEnabled) {
                    mBitmap = getBitmapFromVectorDrawable(context, clearIconId, clearIconId == R.drawable.ic_clear_black_24dp);
                }
                emojiEnabled = typedArray.getBoolean(R.styleable.FancyEditText_emojiEnabled, false);
            }

            if (dynamicBackgroundEnabled) {
                int mFocusedBackgroundId = typedArray.getResourceId(R.styleable.FancyEditText_focusedBackground, -1);
                int mErrorBackgroundId = typedArray.getResourceId(R.styleable.FancyEditText_errorBackground, -1);
                int mSuccessBackgroundId = typedArray.getResourceId(R.styleable.FancyEditText_successBackground, -1);
                if (mFocusedBackgroundId == -1) {
                    mFocusedBackgroundId = R.drawable.selector_rectangle_default;
                }
                if (mErrorBackgroundId == -1) {
                    mErrorBackgroundId = R.drawable.shape_rectangle_default_error;
                }
                if (mSuccessBackgroundId == -1) {
                    mSuccessBackgroundId = R.drawable.shape_rectangle_default_success;
                }
                mFocusedBackgroundDrawable = ContextCompat.getDrawable(context, mFocusedBackgroundId);
                mErrorBackgroundDrawable = ContextCompat.getDrawable(context, mErrorBackgroundId);
                mSuccessBackgroundDrawable = ContextCompat.getDrawable(context, mSuccessBackgroundId);
                setBackground(mFocusedBackgroundDrawable);
                setPadding(dpToPixels(5), dpToPixels(5), dpToPixels(5), dpToPixels(5));
            }
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (hasFocus && mBitmap != null && inputTypePwd && !isEmpty()) {
            int left = getMeasuredWidth() - getPaddingRight() -
                    mTogglePwdIcon.getIntrinsicWidth() - mBitmap.getWidth() - dpToPixels(5);
            int top = (getMeasuredHeight() - mBitmap.getHeight()) >> 1;
            canvas.drawBitmap(mBitmap, left, top, null);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            performClick();
        }

        if (hasFocus && inputTypePwd && event.getAction() == MotionEvent.ACTION_UP) {
            int w = mTogglePwdIcon.getIntrinsicWidth();
            int h = mTogglePwdIcon.getIntrinsicHeight();
            int top = (getMeasuredHeight() - h) >> 1;
            int right = getMeasuredWidth() - getPaddingRight();
            boolean isAreaX = event.getX() <= right && event.getX() >= right - w;
            boolean isAreaY = event.getY() >= top && event.getY() <= top + h;
            if (isAreaX && isAreaY) {
                pwdVisible = !pwdVisible;
                if (pwdVisible) {
                    setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                setSelection(getSelectionStart(), getSelectionEnd());
                mTogglePwdIcon = ContextCompat.getDrawable(getContext(), pwdVisible ?
                        mShowPwdIconId : mHidePwdIconId);
                if (mShowPwdIconId == R.drawable.ic_visibility_black_24dp ||
                        mHidePwdIconId == R.drawable.ic_visibility_off_black_24dp) {
                    DrawableCompat.setTint(mTogglePwdIcon, getCurrentHintTextColor());
                }
                mTogglePwdIcon.setBounds(0, 0, mTogglePwdIcon.getIntrinsicWidth(),
                        mTogglePwdIcon.getIntrinsicHeight());

                setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                        mTogglePwdIcon, getCompoundDrawables()[3]);

                invalidate();
            }

            if (clearEnabled) {
                right -= w + dpToPixels(5);
                isAreaX = event.getX() <= right && event.getX() >= right - mBitmap.getWidth();
                if (isAreaX && isAreaY) {
                    setError(null);
                    setText("");
                }
            }
        }

        if (hasFocus && clearEnabled && !inputTypePwd && event.getAction() == MotionEvent.ACTION_UP) {
            Rect rect = mClearIcon.getBounds();
            int rectW = rect.width();
            int rectH = rect.height();
            int top = (getMeasuredHeight() - rectH) >> 1;
            int right = getMeasuredWidth() - getPaddingRight();
            boolean isAreaX = event.getX() <= right && event.getX() >= right - rectW;
            boolean isAreaY = event.getY() >= top && event.getY() <= (top + rectH);
            if (isAreaX && isAreaY) {
                setError(null);
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    private class FancyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (mFTextChangeListener != null) {
                mFTextChangeListener.beforeTextChanged(s, start, count, after);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mFTextChangeListener != null) {
                mFTextChangeListener.onTextChanged(s, start, before, count);
            }
            if (dynamicBackgroundEnabled) {
                setBackground(mFocusedBackgroundDrawable);
                invalidate();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mFTextChangeListener != null) {
                mFTextChangeListener.afterTextChanged(s);
            }
            handleFocusChange();
        }
    }

    private void handleFocusChange() {
        if (!hasFocus || (isEmpty() && !inputTypePwd)) {
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                    null, getCompoundDrawables()[3]);
            if (!isEmpty() && inputTypePwd) {
                invalidate();
            }
        } else {
            if (inputTypePwd) {
                if (mShowPwdIconId == R.drawable.ic_visibility_black_24dp || mHidePwdIconId == R.drawable.ic_visibility_off_black_24dp) {
                    DrawableCompat.setTint(mTogglePwdIcon, getCurrentHintTextColor());
                }
                setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                        mTogglePwdIcon, getCompoundDrawables()[3]);
            } else if (!isEmpty() && clearEnabled) {
                setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1],
                        mClearIcon, getCompoundDrawables()[3]);
            }
        }
    }

    public interface OnFTextChangeListener {

        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void onTextChanged(CharSequence s, int start, int before, int count);

        void afterTextChanged(Editable s);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("save_instance", super.onSaveInstanceState());

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable("save_instance"));
            return;
        }

        super.onRestoreInstanceState(state);
    }

    @Override
    public void setError(CharSequence error) {
        if (dynamicBackgroundEnabled) {
            setBackground(mErrorBackgroundDrawable);
        } else {
            super.setError(error);
        }
    }

    public void success() {
        if (dynamicBackgroundEnabled) {
            setBackground(mSuccessBackgroundDrawable);
        }
    }

    private boolean isEmpty() {
        return getText().toString().trim().length() == 0;
    }

    private int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private Bitmap getBitmapFromVectorDrawable(Context context, int drawableId, boolean tint) {
        Drawable drawable = AppCompatResources.getDrawable(context, drawableId);
        if (drawable == null)
            return null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        if (tint)
            DrawableCompat.setTint(drawable, getCurrentHintTextColor());

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public boolean isClearEnabled() {
        return clearEnabled;
    }

    public void setClearEnabled(boolean clearEnabled) {
        this.clearEnabled = clearEnabled;
    }

    public Drawable getmClearIcon() {
        return mClearIcon;
    }

    public void setmClearIcon(Drawable mClearIcon) {
        this.mClearIcon = mClearIcon;
    }

    public boolean isInputTypePwd() {
        return inputTypePwd;
    }

    public void setInputTypePwd(boolean inputTypePwd) {
        this.inputTypePwd = inputTypePwd;
    }

    public boolean isPwdVisible() {
        return pwdVisible;
    }

    public void setPwdVisible(boolean pwdVisible) {
        this.pwdVisible = pwdVisible;
    }

    public int getmShowPwdIconId() {
        return mShowPwdIconId;
    }

    public void setmShowPwdIconId(int mShowPwdIconId) {
        this.mShowPwdIconId = mShowPwdIconId;
    }

    public int getmHidePwdIconId() {
        return mHidePwdIconId;
    }

    public void setmHidePwdIconId(int mHidePwdIconId) {
        this.mHidePwdIconId = mHidePwdIconId;
    }

    public boolean isEmojiEnabled() {
        return emojiEnabled;
    }

    public void setEmojiEnabled(boolean emojiEnabled) {
        this.emojiEnabled = emojiEnabled;
    }

    public boolean isDynamicBackgroundEnabled() {
        return dynamicBackgroundEnabled;
    }

    public void setDynamicBackgroundEnabled(boolean dynamicBackgroundEnabled) {
        this.dynamicBackgroundEnabled = dynamicBackgroundEnabled;
    }

    public Drawable getmFocusedBackgroundDrawable() {
        return mFocusedBackgroundDrawable;
    }

    public void setmFocusedBackgroundDrawable(Drawable mFocusedBackgroundDrawable) {
        this.mFocusedBackgroundDrawable = mFocusedBackgroundDrawable;
    }

    public Drawable getmErrorBackgroundDrawable() {
        return mErrorBackgroundDrawable;
    }

    public void setmErrorBackgroundDrawable(Drawable mErrorBackgroundDrawable) {
        this.mErrorBackgroundDrawable = mErrorBackgroundDrawable;
    }

    public Drawable getmSuccessBackgroundDrawable() {
        return mSuccessBackgroundDrawable;
    }

    public void setmSuccessBackgroundDrawable(Drawable mSuccessBackgroundDrawable) {
        this.mSuccessBackgroundDrawable = mSuccessBackgroundDrawable;
    }

    public OnFTextChangeListener getmFTextChangeListener() {
        return mFTextChangeListener;
    }

    public void setmFTextChangeListener(OnFTextChangeListener mFTextChangeListener) {
        this.mFTextChangeListener = mFTextChangeListener;
    }

    /**
     * disable emoji and special symbol input
     */
    private class EmojiExcludeFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    }
}
