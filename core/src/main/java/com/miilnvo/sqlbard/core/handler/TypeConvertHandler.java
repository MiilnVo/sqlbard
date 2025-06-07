package com.miilnvo.sqlbard.core.handler;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author MiilnVo
 * @since 2022-07-19
 */
public class TypeConvertHandler {

    private String booleanStrategy;

    private String enumStrategy;

    public TypeConvertHandler(String booleanStrategy, String enumStrategy) {
        this.booleanStrategy = booleanStrategy != null ? booleanStrategy : "toNumber";
        this.enumStrategy = enumStrategy != null ? enumStrategy : "toName";
    }

    public Object convert(Object param) {
        if (param instanceof byte[]) {
            return toStr(new String((byte[]) param));
        }
        if (param instanceof Number) {
            return param.toString();
        }
        if (param instanceof Boolean) {
            switch (booleanStrategy) {
                case "toString": return toStr(param);
                case "toNumber": return ((Boolean) param) ? "1" : "0";
            }
        }
        if (param instanceof Enum) {
            switch (enumStrategy) {
                case "toOrdinal": return ((Enum) param).ordinal();
                case "toName":    return toStr(((Enum) param).name());
            }
        }
        if (param instanceof Timestamp || param instanceof Time) {
            return toStr(param);
        }
        if (param instanceof Date) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return toStr(format.format(param));
        }

        return toStr(param);
    }

    private String toStr(Object param) {
        return "'" + param + "'";
    }

}
