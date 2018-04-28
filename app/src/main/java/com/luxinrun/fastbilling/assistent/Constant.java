package com.luxinrun.fastbilling.assistent;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;

public class Constant {
    /**
     * 存储的三种登陆模式
     */
    public static String STATE_LOGIN = "0";
    public static String STATE_VISITOR = "1";
    public static String STATE_FIRST = "2";

    /**
     * 根据array数组的id图片返回int[]
     *
     * @param activity
     * @param id
     * @return
     */
    public static int[] changeDrawableArray(Activity activity, int id) {
        TypedArray array;
        int len;
        int[] resIds;
        array = activity.getResources().obtainTypedArray(id);
        len = array.length();
        resIds = new int[len];
        for (int i = 0; i < len; i++) {
            resIds[i] = array.getResourceId(i, 0);
        }
        array.recycle();
        return resIds;
    }

    /**
     * 根据array数组的id图片返回string[]
     *
     * @param activity
     * @param id
     * @return
     */
    public static String[] changeStringArray(Activity activity, int id) {
        Resources res = activity.getResources();
        String[] exp_string = res.getStringArray(id);
        return exp_string;
    }


    /**
     * 算法，小数点后两位数，小数点之前一共10位
     *
     * @param num
     * @param n
     * @return
     */
    public static String only_two_bit(String num, String n) {
        char[] numChar = num.toCharArray();
        Boolean FLAG = false;
        String return_num = "";
        for (int i = 0; i < numChar.length; i++) {
            if ((numChar[i] + "").equals(".")) {
                FLAG = true;
            }
        }
        if (FLAG) {
            String r_num = num + n;
            String[] numString = r_num.split("\\.");
            char[] toNumChar = numString[1].toCharArray();
            if (toNumChar.length == 1 || toNumChar.length == 2) {
                return_num = r_num;
            } else {
                return_num = num;
            }
        }
        if (return_num.equals("")) {
            if (!(numChar.length > 12)) {
                if (((numChar.length == 0) && (n.equals("0"))) || ((numChar.length == 1) && (num.equals("0")))) {
                    return_num = "0";
                } else {
                    return_num = num + n;
                }
            } else {
                return_num = num;
            }
        }
        return return_num;
    }

    /**
     * 判断小数点唯一性后返回
     * @param num
     * @return
     */
    public static String have_comma_or_not(String num) {
        String return_num = "";
        if (num.contains(".")) {
            return_num = num;
        } else if (num.equals("")) {
            return_num = "0.";
        } else {
            return_num = num + ".";
        }
        return return_num;
    }

    /**
     * 删除最后一个字符后返回
     */
    public static String del_num(String num) {
        char[] numString = num.toCharArray();
        String return_num = "";
        for (int i = 0; i < numString.length - 1; i++) {
            return_num = return_num + numString[i];
        }
        return return_num;
    }

}
