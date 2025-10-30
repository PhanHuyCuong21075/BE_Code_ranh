package com.cuongph.be_code.common.ws;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class SqlHelperUtil {
    public static Boolean isPrimitiveTypeOrWrapper(Class<?> clazz){
        return BeanUtils.isSimpleValueType(clazz);
    }
    public static Object getDefaultValue(Class<?> clazz) {
        if (Boolean.class == clazz || boolean.class == clazz) {
            return Boolean.FALSE;
        } else if (Integer.class == clazz || int.class == clazz) {
            return 0;
        } else if (Long.class == clazz || long.class == clazz) {
            return 0L;
        } else if (Float.class == clazz || float.class == clazz) {
            return 0f;
        } else if (Double.class == clazz || double.class == clazz) {
            return 0d;
        } else if (String.class == clazz) {
            return "";
        } else if (List.class == clazz) {
            return new ArrayList<>();
        }
        return null;
    }
}
