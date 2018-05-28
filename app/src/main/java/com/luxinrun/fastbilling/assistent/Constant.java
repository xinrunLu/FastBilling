package com.luxinrun.fastbilling.assistent;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;

import com.luxinrun.fastbilling.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Constant {
    /**
     * 存储的三种登陆模式
     */
    public static String STATE_LOGIN = "0";
    public static String STATE_VISITOR = "1";
    public static String STATE_FIRST = "2";

    public static final int to_Login = 0;
    public static final int LOGIN_SUCCESS = 1;

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


    // 如果没有小数点默认加.00
    public static String change_int_to_float(String num) {
        char[] numChar = num.toCharArray();
        Boolean FLAG = false;
        String return_num = null;
        for (int i = 0; i < numChar.length; i++) {
            if ((numChar[i] + "").equals(".")) {
                FLAG = true;
            }
        }
        if (FLAG) {
            String[] numString = num.split("\\.");
            if (numString.length == 1) {
                return_num = num + "00";
            } else {
                if (numString[1].length() == 1) {
                    return_num = num + "0";
                } else {
                    return_num = num;
                }
            }
        } else {
            return_num = num + ".00";
        }
        return return_num;
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
     *
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

    /**
     * 若是一位数，则前面补0返回
     *
     * @param num
     * @return
     */
    public static String num_Format(int num) {
        String return_num;
        if ((num + "").length() == 1) {
            return_num = "0" + num;
        } else {
            return_num = num + "";
        }
        return return_num;
    }

    // 获取支出所有数据的总和
    public static String get_totle_money(ArrayList<Map<String, Object>> data) {
        float[] money_array = new float[data.size()];
        float sum = 0;
        for (int i = 0; i < data.size(); i++) {
            money_array[i] = Float.parseFloat(data.get(i).get("money").toString());
            sum = sum + money_array[i];
        }
        DecimalFormat decimalFormat = new DecimalFormat(".00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(sum);
    }

    private static ContentValues get_same_classify_sum(int[] classify_num_array, float[] money_array, int index) {
        float sum = 0;
        int num = 0;
        ContentValues values = new ContentValues();
        for (int i = 0; i < classify_num_array.length; i++) {
            if (index == classify_num_array[i]) {
                sum = sum + money_array[i];
                num = num + 1;
            }
        }
        values.put("sum",sum);
        values.put("num", num);
        return values;
    }

    public static ArrayList getSameClassifyData(Activity activity, ArrayList<Map<String, Object>> data, int classify_num){
        ArrayList<Map<String,Object>> arrayList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (Integer.valueOf(data.get(i).get("classify_num").toString()) == classify_num){
                Log.d("lxr","step="+2);
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("same_classify_date",data.get(i).get("date_time").toString());
                item.put("same_classify_summary",data.get(i).get("summary").toString());
                item.put("same_classify_money",data.get(i).get("money").toString());
                arrayList.add(item);
            }
        }
        return  arrayList;
    }


    /**
     * 获取饼状图的各个数据
     */
    public static ArrayList getPieChartData(Activity activity, ArrayList<Map<String, Object>> data, String expORincome) {
        ArrayList<Map<String,Object>> arrayList = new ArrayList<>();
        int[] classify_num_array = new int[data.size()];
        String[] classify_title_array = new String[data.size()];
        float[] money_array = new float[data.size()];
        for (int i = 0; i < data.size(); i++) {
            classify_num_array[i] = Integer.valueOf(data.get(i).get("classify_num").toString());
            classify_title_array[i] = data.get(i).get("classify_title").toString();
            money_array[i] = Float.parseFloat(data.get(i).get("money").toString());
        }
        for (int index = 0; index < 15; index++) {
            ContentValues values = get_same_classify_sum(classify_num_array, money_array, index);
            float sum = Float.parseFloat(values.get("sum").toString());
            String money_num = values.get("num").toString();
            if (sum != 0.0) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("classify_num",index+"");
                item.put("money",Constant.change_int_to_float(sum+""));
                item.put("money_num",money_num);
                if (expORincome.equals("0")){
                    item.put("classify_title",Constant.changeStringArray(activity, R.array.classify_title_exp)[index]);
                    item.put("color",Constant.changeStringArray(activity, R.array.classify_title_exp_color)[index]);
                }else {
                    item.put("classify_title",Constant.changeStringArray(activity, R.array.classify_title_income)[index]);
                    item.put("color",Constant.changeStringArray(activity, R.array.classify_title_income_color)[index]);
                }
                arrayList.add(item);
                Log.d("lxr","方法2="+index+Constant.changeStringArray(activity, R.array.classify_title_exp)[index]+sum);
            }
        }
        return arrayList;
    }

}
