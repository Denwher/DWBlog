package com.dengwei.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Denwher
 * @version 1.0
 */
public class BeanCopyUtils {
    private BeanCopyUtils(){

    }

    public static <V> V copyBean(Object obj, Class<V> clazz) {
        V result = null;
        try {
            result = clazz.newInstance();
            BeanUtils.copyProperties(obj, result);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <O,V> List<V> copyBeanList(List<O> objs, Class<V> clazz){
        /*
        ArrayList<V> results = new ArrayList<>();
        for (Object obj: objs) {
            results.add(copyBean(obj,clazz));
        }
        return results;
        */
        //使用stream的方式
        return objs.stream().map(o -> copyBean(o,clazz))
                .collect(Collectors.toList());
    }
}
