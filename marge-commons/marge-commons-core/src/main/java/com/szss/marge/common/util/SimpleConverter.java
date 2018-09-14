package com.szss.marge.common.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 对象拷贝
 *
 * @author XXX
 * @date 2017/10/24
 */
@Slf4j
public class SimpleConverter {

    /**
     * 将原对象内容复制到目标对象
     *
     * @param orig 原始对象
     * @param dest 目标对象
     */
    public static void copyProperties(Object orig, Object dest) {
        BeanUtils.copyProperties(dest, orig);
    }

    /**
     *
     * 根据原生对象生成新对象
     * <p>
     *
     * <pre>
     * 例如把 User 对象 转为 UserDTO：
     *  UserDTO userDTO = SimpleConverter.convert(user, UserDTO.class);
     * </pre>
     *
     * @param orig 原始对象
     * @param dest 目标对象class
     * @param <T> 泛型
     * @return 目标对象实例
     */
    public static <T> T convert(Object orig, Class<T> dest) {
        return convert(orig, dest, (String[])null);
    }

    /**
     * 根据原生对象生成新对象，并忽略指定字段
     * <p>
     * 
     * @param orig 原始对象
     * @param dest 目标对象class
     * @param ignoreProperties 不做赋值属性
     * @param <T> 泛型
     * @return
     */
    public static <T> T convert(Object orig, Class<T> dest, String... ignoreProperties) {
        if (Objects.isNull(orig)) {
            return null;
        }
        T obj = null;
        try {
            obj = dest.newInstance();
            BeanUtils.copyProperties(orig, obj, ignoreProperties);
        } catch (Exception e) {
            throw new RuntimeException(MessageFormat.format("Convert error: {0} to {1}", orig, obj), e);
        }

        return obj;
    }

    /**
     * 根据原生集合生成新集合
     * <p>
     *
     * <pre>
     * List<UserDTO> list = Arrays.asList(new UserDTO("name", "country"));
     * List<UserVO> vos = SimpleConverter.convert(list, UserVO.class);
     * </pre>
     *
     * @param orig 原始list对象
     * @param dest 目标list对象的泛型
     * @param <F> 原始list对象的泛型
     * @param <T> 目标list对象的泛型
     * @return
     */
    public static <F, T> List<T> convert(List<F> orig, final Class<T> dest) {
        if (Objects.isNull(orig)) {
            return null;
        }
        List<T> list = new ArrayList<>();

        for (F o : orig) {
            T t = convert(o, dest);
            list.add(t);
        }

        return list;
    }



    /**
     * 解析指定分隔符的字符串为集合，去除空格及多余的分隔符
     *
     * @param str 字符串
     * @param separator 分隔符
     * @param isRepeat 是否允许重复
     * @return
     */
    // public static List<String> convertStr(String str, String separator, boolean isRepeat) {
    // Preconditions.checkNotNull(str);
    // Preconditions.checkNotNull(separator);
    // List<String> list = Splitter.on(separator).trimResults()
    // .omitEmptyStrings().splitToList(str);
    // if (!isRepeat) {
    // return ImmutableSet.copyOf(list).asList();
    // }
    // return list;
    // }

    /**
     * 将逗号分隔的字符串转换成集合，过滤掉多余的空格和逗号 <br>
     * convertStr(" a, b ,c ") returns ["a", "b", "c"].
     *
     * @param str 逗号分隔的字符串
     * @return
     */
    // public static List<String> convertStr(String str) {
    // return convertStr(str, ",", true);
    // }

    /**
     * 将集合按指定分隔符连接,排除空
     *
     * @param list 集合
     * @param separator 分隔符
     * @return
     *
     *         public static String convertList(List<?> list, String separator) { Preconditions.checkNotNull(list);
     *         Preconditions.checkNotNull(separator); return Joiner.on(separator).skipNulls().join(list.iterator()); }
     */


}
