package idc.utils;


import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

/**
 * Created by sky on 2017/3/11.
 */
public class StringUtils {

    public static ArrayList<String> dumpJson(String sJson) {
        if (sJson == null) {
            return null;
        }

        ArrayList<String> list = new ArrayList<String>();
        Object[] array = JSON.parseArray(sJson).toArray();
        for (Object object : array) {
            list.add(object.toString());
        }
        return list;
    }

}
