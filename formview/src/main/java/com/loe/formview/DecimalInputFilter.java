package com.loe.formview;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalInputFilter implements InputFilter
{
    private Pattern mPattern;

    public DecimalInputFilter(int limitNumber, int limitDecimal)
    {
        mPattern = Pattern.compile("[0-9]{0," + (limitNumber) + "}+((\\.[0-9]{0," + (limitDecimal) + "})?)||(\\.)?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend)
    {
        CharSequence s1 = dest.subSequence(0, dstart);
        CharSequence s2 = dest.subSequence(dend, dest.length());
        String newValue = s1.toString() + source + s2;

        Matcher matcher = mPattern.matcher(newValue);
        if (!matcher.matches())
        {
            CharSequence o = dest.subSequence(dstart, dend);
            if(o.toString().contains("."))
            {
                return o;
            }
            return "";
        }
        return null;
    }
}
