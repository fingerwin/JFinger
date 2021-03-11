package org.jfinger.cloud.utils.mybatis.plus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.jfinger.cloud.enumerate.QueryCondition;
import org.jfinger.cloud.utils.common.DateUtils;
import org.jfinger.cloud.utils.reflect.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.Date;
import java.util.Map;

/**
 * @Description Mybatis plus查询包装工具
 * @Author finger
 * @Date 2021/3/9 0009
 * @Version 1.0
 */
@Slf4j
public class WrapperUtils {
    /**
     * 时间开始
     */
    private static final String BEGIN = "_begin";

    /**
     * 时间结束
     */
    private static final String END = "_end";

    /**
     * 数字类型字段，拼接此后缀 接受多值参数
     */
    private static final String MULTI = "_MultiString";

    /**
     * 星号
     */
    private static final String STAR = "*";

    /**
     * 逗号
     */
    private static final String COMMA = ",";

    /**
     * 不等于
     */
    private static final String NOT_EQUAL = "!";

    /**
     * 单引号
     */
    public static final String SQL_SQ = "'";

    /**
     * 排序列
     */
    private static final String ORDER_COLUMN = "column";

    /**
     * 排序方式
     */
    private static final String ORDER_TYPE = "order";

    /**
     * 正序
     */
    private static final String ORDER_TYPE_ASC = "ASC";

    /**
     * 构造查询条件封装
     *
     * @param entity 实体对象
     * @param param  参数map
     * @param <T>
     * @return
     */
    public static <T> QueryWrapper<T> buildQueryWrapper(T entity, Map<String, String[]> param) {
        QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
        //获取实体属性
        PropertyDescriptor properties[] = PropertyUtils.getPropertyDescriptors(entity);
        String name;
        Class type;
        for (int i = 0; i < properties.length; i++) {
            name = properties[i].getName();
            try {
                if (isUselessField(name) || !PropertyUtils.isReadable(entity, name)) {
                    continue;
                }
                //判断是否有区间值
                String endValue = null, beginValue = null;
                if (param != null && param.containsKey(name + BEGIN)) {
                    beginValue = param.get(name + BEGIN)[0].trim();
                    appendCondition(queryWrapper, name, beginValue, QueryCondition.GE);
                }
                if (param != null && param.containsKey(name + END)) {
                    endValue = param.get(name + END)[0].trim();
                    appendCondition(queryWrapper, name, endValue, QueryCondition.LE);
                }
                //多值查询
                if (param != null && param.containsKey(name + MULTI)) {
                    endValue = param.get(name + MULTI)[0].trim();
                    appendCondition(queryWrapper, name.replace(MULTI, ""), endValue, QueryCondition.IN);
                }
                //模糊查询
                Object value = PropertyUtils.getSimpleProperty(entity, name);
                if (value instanceof String && (((String) value).startsWith(STAR) || ((String) value).endsWith(STAR))) {
                    String val = (String) value;
                    QueryCondition rule = null;
                    if (val.startsWith(STAR) && val.endsWith(STAR)) {
                        rule = QueryCondition.LIKE;
                        val = "'%" + val.substring(1, val.length() - 1) + "%'";
                    } else if (val.startsWith(STAR)) {
                        rule = QueryCondition.LEFT_LIKE;
                        val = "'%" + val.substring(1) + "'";
                    } else if (val.endsWith(STAR)) {
                        rule = QueryCondition.RIGHT_LIKE;
                        val = "'" + val.substring(0, val.length() - 1) + "%'";
                    }
                    appendCondition(queryWrapper, name, val, rule);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return queryWrapper;
    }

    /**
     * 是否为不处理字段
     *
     * @param name 字段名
     * @return
     */
    private static boolean isUselessField(String name) {
        return "class".equals(name) || "ids".equals(name)
                || "page".equals(name) || "rows".equals(name)
                || "sort".equals(name) || "order".equals(name);
    }

    /**
     * 添加查询条件
     *
     * @param queryWrapper wrapper
     * @param name         参数属性名称
     * @param value        参数值
     * @param rule         查询条件
     */
    private static void appendCondition(QueryWrapper<?> queryWrapper, String name, Object value, QueryCondition rule) {
        if (rule == null || StringUtils.isEmpty(value)) {
            return;
        }
        name = ClassUtils.Camel2Line(name);
        if (value instanceof Date) {
            switch (rule) {
                //大于或大于等于
                case GT:
                case GE:
                    value = DateUtils.dayStart((Date) value);
                    break;
                case LT:
                case LE:
                    value = DateUtils.dayEnd((Date) value);
                    break;
                default:
                    value = DateUtils.dayStart((Date) value);
                    break;
            }
        }
        switch (rule) {
            case GT:
                queryWrapper.gt(name, value);
                break;
            case GE:
                queryWrapper.ge(name, value);
                break;
            case LT:
                queryWrapper.lt(name, value);
                break;
            case LE:
                queryWrapper.le(name, value);
                break;
            case EQ:
                queryWrapper.eq(name, value);
                break;
            case NE:
                queryWrapper.ne(name, value);
                break;
            case IN:
                if (value instanceof String) {
                    queryWrapper.in(name, (Object[]) value.toString().split(","));
                } else if (value instanceof String[]) {
                    queryWrapper.in(name, (Object[]) value);
                } else {
                    queryWrapper.in(name, value);
                }
                break;
            case LIKE:
                queryWrapper.like(name, value);
                break;
            case LEFT_LIKE:
                queryWrapper.likeLeft(name, value);
                break;
            case RIGHT_LIKE:
                queryWrapper.likeRight(name, value);
                break;
            default:
                break;
        }
    }
}
