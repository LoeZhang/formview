package com.loe.formview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FormView extends FrameLayout
{
    private Context context;
    private TypedArray typedArray;

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_EDIT = 1;
    public static final int TYPE_SELECT = 2;

    protected boolean isAutoVisible;

    private InputFilter maxLenFilter;
    private InputFilter limitFilter;

    public FormView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, context.obtainStyledAttributes(attrs, R.styleable.FormView));
    }

    public FormView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, context.obtainStyledAttributes(attrs, R.styleable.FormView, defStyleAttr, 0));
    }

    @SuppressLint("NewApi")
    public FormView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, context.obtainStyledAttributes(attrs, R.styleable.FormView, defStyleAttr, defStyleRes));
    }

    private LinearLayout layout;
    private TextView viewNotNull;
    private ImageView imageIco;
    private TextView textTitle;

    private TextView textView;
    private TextView textTail;

    private String value;

    private void init(Context context, TypedArray typedArray)
    {
        this.context = context;
        this.typedArray = typedArray;
        setType(typedArray.getInt(R.styleable.FormView_form_type, 0));
    }

    public void setType(int type)
    {
        removeAllViews();
        switch (type)
        {
            case 2:
                layout = (LinearLayout) ((FrameLayout) LayoutInflater.from(context).inflate(R.layout.form_select, this)).getChildAt(0);
                break;
            default:
                layout = (LinearLayout) ((FrameLayout) LayoutInflater.from(context).inflate(R.layout.form_text, this)).getChildAt(0);
                break;
        }

        imageIco = (ImageView) findViewById(R.id.imageIco);
        viewNotNull = (TextView) findViewById(R.id.viewNotNull);
        textTitle = (TextView) findViewById(R.id.textTitle);
        textView = (TextView) findViewById(R.id.textText);
        textTail = (TextView) findViewById(R.id.textTail);

        // form_bg
        Drawable bg = typedArray.getDrawable(R.styleable.FormView_form_bg);
        if (bg != null)
        {
            layout.setBackground(bg);
        }

        // padding
        int pH = dp_px(15);
        int pLeft = getPaddingLeft();
        int pRight = getPaddingRight();
        if (pLeft == 0)
        {
            pLeft = pH;
        }
        //        if (pRight == 0)
        //        {
        //            pRight = pH - 2;
        //        }
        layout.setPadding(pLeft, getPaddingTop(), pRight, getPaddingBottom());
        setPadding(0, 0, 0, 0);

        // not_null
        setNotNull(typedArray.getBoolean(R.styleable.FormView_form_not_null, false));

        // ico
        Drawable ico = typedArray.getDrawable(R.styleable.FormView_form_ico);
        if (ico != null)
        {
            imageIco.setImageDrawable(ico);
        }
        else
        {
            imageIco.setVisibility(GONE);
        }

        // title
        setTitle(typedArray.getString(R.styleable.FormView_form_title));
        // text
        setText(typedArray.getString(R.styleable.FormView_form_text));
        // type
        switch (type)
        {
            case 1:
                textView.setEnabled(true);
                textView.setHint("请输入");
                break;
            case 2:
                textView.setEnabled(true);
                layout.setClickable(true);
                if (imageIco.getVisibility() == GONE)
                {
                    textView.setHint("请选择");
                }
                break;
        }
        // hint
        setHint(typedArray.getString(R.styleable.FormView_form_hint));
        // text_color
        setTextColor(typedArray.getColorStateList(R.styleable.FormView_form_text_color));
        // tail
        setTail(typedArray.getString(R.styleable.FormView_form_tail));
        // edit_type
        switch (typedArray.getInt(R.styleable.FormView_form_edit_type, 0))
        {
            case 1:
                textView.setInputType(INPUT_PASSWORD);
                break;
            case 2:
                textView.setInputType(INPUT_NUMBER);
                break;
            case 3:
                textView.setInputType(INPUT_DECIMAL);
                break;
            default:
                textView.setInputType(INPUT_TEXT);
                break;
        }
        // max
        setMaxLen(typedArray.getInt(R.styleable.FormView_form_max_len, Integer.MAX_VALUE));
        setMaxLine(typedArray.getInt(R.styleable.FormView_form_max_line, Integer.MAX_VALUE));
        // limit
        setLimit(typedArray.getInt(R.styleable.FormView_form_limit_number, -1), typedArray.getInt(R.styleable.FormView_form_limit_decimal, -1));
        // filter
        setFilter(typedArray.getString(R.styleable.FormView_form_filter));
        // auto_visible
        setAutoVisible(typedArray.getBoolean(R.styleable.FormView_form_auto_visible, false));
    }

    public static final int INPUT_TEXT = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE;
    public static final int INPUT_DECIMAL = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL;
    public static final int INPUT_NUMBER = InputType.TYPE_CLASS_NUMBER;
    public static final int INPUT_PASSWORD = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;

    public void setIco(int resId)
    {
        if (resId == 0)
        {
            imageIco.setVisibility(GONE);
        }
        else
        {
            imageIco.setVisibility(VISIBLE);
            imageIco.setImageResource(resId);
        }
    }

    public TextView getTextView()
    {
        return textView;
    }

    public String getTitle()
    {
        return textTitle.getText().toString();
    }

    public void setTitle(String value)
    {
        textTitle.setText(value == null ? "" : value);
    }

    public String getText()
    {
        return textView.getText().toString();
    }

    public void setText(String value)
    {
        textView.setText(value == null ? "" : value);
        notifyVisible(value);
    }

    public void putText(String value)
    {
        setText(value);
        if (value != null)
        {
            setSelection(value.length());
        }
    }

    public void setSelection(int i)
    {
        ((EditText) textView).setSelection(i);
    }

    public ColorStateList getTextColor()
    {
        return textView.getTextColors();
    }

    public String getHint()
    {
        return String.valueOf(textView.getHint());
    }

    public void setHint(String value)
    {
        if (value != null)
        {
            textView.setHint(value);
        }
    }

    public void setTextColor(int value)
    {
        textView.setTextColor(value);
    }

    public void setTextColor(ColorStateList value)
    {
        if (value != null)
        {
            textView.setTextColor(value);
        }
    }

    public String getTail()
    {
        return textTail.getText().toString();
    }

    public void setTail(String value)
    {
        textTail.setText(value == null ? "" : value);
        textTail.setVisibility(value == null || value.isEmpty() ? GONE : VISIBLE);
    }

    public int getEditType()
    {
        return textView.getInputType();
    }

    public void setEditType(int value)
    {
        textView.setInputType(value);
    }

    public void setMaxLen(int max)
    {
        maxLenFilter = new InputFilter.LengthFilter(max);
        resetFilters();
    }

    public void setMaxLine(int max)
    {
        textView.setMaxLines(max);
    }

    public void setLimit(int limitNumber, int limitDecimal)
    {
        if (limitNumber < 0 && limitDecimal < 0)
        {
            return;
        }
        if (limitNumber <= 0)
        {
            limitNumber = 16;
        }
        if (limitDecimal <= 0)
        {
            limitDecimal = 2;
        }
        limitFilter = new DecimalInputFilter(limitNumber, limitDecimal);
        resetFilters();
    }

    public void setFilter(String s)
    {
        if (s != null)
        {
            textView.setKeyListener(DigitsKeyListener.getInstance(s));
        }
    }

    public boolean isAutoVisible()
    {
        return isAutoVisible;
    }

    public void setAutoVisible(boolean autoVisible)
    {
        isAutoVisible = autoVisible;
        notifyVisible(textView.getText().toString());
    }

    protected void notifyVisible(String s)
    {
        if (isAutoVisible)
        {
            setVisibility(s == null || s.trim().isEmpty() ? GONE : VISIBLE);
        }
    }

    public void focus()
    {
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
        textView.clearFocus();
        textView.requestFocus();
        setSelection(getText().length());
    }

    public void setNotNull(boolean notNull)
    {
        viewNotNull.setVisibility(notNull ? VISIBLE : GONE);
    }

    public boolean isNotNull()
    {
        return viewNotNull.getVisibility() == VISIBLE;
    }

    public void addTextAfterListener(final StringCallback callBack)
    {
        textView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                callBack.callback(s.toString());
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener listener)
    {
        layout.setOnClickListener(listener);
    }

    interface StringCallback
    {
        void callback(String s);
    }

    public int dp_px(float dpValue)
    {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void resetFilters()
    {
        ArrayList<InputFilter> filters = new ArrayList<>();
        if (maxLenFilter != null)
        {
            filters.add(maxLenFilter);
        }
        if (limitFilter != null)
        {
            filters.add(limitFilter);
        }
        textView.setFilters(filters.toArray(new InputFilter[filters.size()]));
    }

    public String getName()
    {
        String s = getString();
        return s.isEmpty() ? null : s;
    }

    public void setName(String name)
    {
        setText(name);
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    ///////////////////////////////////////////////////////////////////////////////////////

    public String getString()
    {
        return getText().trim();
    }

    public int getInt()
    {
        String s = getString();
        try
        {
            return Integer.parseInt(s);
        } catch (Exception e)
        {
            return 0;
        }
    }

    public double getDouble()
    {
        String s = getString();
        try
        {
            return Double.parseDouble(s);
        } catch (Exception e)
        {
            return 0;
        }
    }

    public String getTagString()
    {
        try
        {
            return getTag().toString().trim();
        } catch (Exception e)
        {
            return "";
        }
    }

    public String getTagStringNull()
    {
        String s = getTagString();
        return s.isEmpty() ? null : s;
    }

    public JSONArray getTagJsonArray()
    {
        try
        {
            return new JSONArray(getTagString());
        } catch (Exception e)
        {
            return null;
        }
    }

    public JSONObject getTagJson()
    {
        try
        {
            return new JSONObject(getTagString());
        } catch (Exception e)
        {
            return null;
        }
    }
}