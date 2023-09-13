package org.apache.click.service;

import lombok.NonNull;
import ognl.DefaultTypeConverter;
import ognl.OgnlContext;
import ognl.OgnlOps;
import ognl.OgnlRuntime;
import ognl.TypeConverter;

import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * Provides an OGNL TypeConverter class.
 * <p>
 * This class is adapted from the OGNL <tt>DefaultTypeConverter</tt>, by
 * Luke Blanshard and Drew Davidson, and provides additional Date conversion
 * capabilities.
 * @see DefaultTypeConverter
 */
public class OGNLTypeConverter implements TypeConverter {

    /**
     * Converts the given value to a given type.  The OGNL context, target,
     * member and name of property being set are given.  This method should be
     * able to handle conversion in general without any context, target, member
     * or property name specified.
     *
     * @param context OGNL context under which the conversion is being done
     * @param target target object in which the property is being set
     * @param member member (Constructor, Method or Field) being set
     * @param propertyName property name being set
     * @param value value to be converted
     * @param toType type to which value is converted
     * @return Converted value of type toType or TypeConverter.NoConversionPossible
     *  to indicate that the conversion was not possible.
     */
    @Override public Object convertValue(OgnlContext context, Object target, Member member, String propertyName, Object value, Class<?> toType){
        return convertValue(value, toType);
    }

    // ------------------------------------------------------ Protected Methods

    /**
     * Return the converted value for the given value object and target type.
     *
     * @param value the value object to convert
     * @param toType the target class type to convert the value to
     * @return a converted value into the specified type
     */
    protected Object convertValue(Object value, Class<?> toType) {
        Object result = null;

        if (value != null) {

            // If array -> array then convert components of array individually
            if (value.getClass().isArray() && toType.isArray()) {
                Class<?> componentType = toType.getComponentType();

                result =
                    Array.newInstance(componentType, Array.getLength(value));

                for (int i = 0, icount = Array.getLength(value); i < icount; i++) {
                    Array.set(result,
                              i,
                              convertValue(Array.get(value, i),
                              componentType));
                }

            } else {
                if ((toType == Integer.class) || (toType == Integer.TYPE)) {
                    result = (int) OgnlOps.longValue(value);

                } else if ((toType == Double.class) || (toType == Double.TYPE)) {
                    result = OgnlOps.doubleValue(value);

                } else if ((toType == Boolean.class) || (toType == Boolean.TYPE)) {
                    result = Boolean.valueOf(value.toString());

                } else if ((toType == Byte.class) || (toType == Byte.TYPE)) {
                    result = (byte) OgnlOps.longValue(value);

                } else if ((toType == Character.class) || (toType == Character.TYPE)) {
                    result = (char) OgnlOps.longValue(value);

                } else if ((toType == Short.class) || (toType == Short.TYPE)) {
                    result = (short) OgnlOps.longValue(value);

                } else if ((toType == Long.class) || (toType == Long.TYPE)) {
                    result = OgnlOps.longValue(value);

                } else if ((toType == Float.class) || (toType == Float.TYPE)) {
                    result = (float) OgnlOps.doubleValue(value);

                } else if (toType == BigInteger.class) {
                    result = OgnlOps.bigIntValue(value);

                } else if (toType == BigDecimal.class) {
                    result = bigDecValue(value);

                } else  if (toType == String.class) {
                    result = OgnlOps.stringValue(value);

                } else if (toType == java.util.Date.class) {
                    long time = getTimeFromDateString(value.toString());
                    if (time > Long.MIN_VALUE) {
                        result = new java.util.Date(time);
                    }

                } else if (toType == java.sql.Date.class) {
                    long time = getTimeFromDateString(value.toString());
                    if (time > Long.MIN_VALUE) {
                        result = new java.sql.Date(time);
                    }

                } else if (toType == java.sql.Time.class) {
                    long time = getTimeFromDateString(value.toString());
                    if (time > Long.MIN_VALUE) {
                        result = new java.sql.Time(time);
                    }

                } else if (toType == java.sql.Timestamp.class) {
                    long time = getTimeFromDateString(value.toString());
                    if (time > Long.MIN_VALUE) {
                        result = new java.sql.Timestamp(time);
                    }
                }
            }

        } else {
            if (toType.isPrimitive()) {
                result = OgnlRuntime.getPrimitiveDefaultValue(toType);
            }
        }
        return result;
    }

    /**
     * Return the time value in milliseconds of the given date value string,
     * or Long.MIN_VALUE if the date could not be determined.
     *
     * @param value the date value string
     * @return the time value in milliseconds or Long.MIN_VALUE if not determined
     */
    protected long getTimeFromDateString (@NonNull String value) {
        value = value.trim();

        if (value.length() == 0) {
            return Long.MIN_VALUE;
        }

        if (isTimeValue(value)) {
            return Long.parseLong(value);
        }

        java.util.Date date = createDateFromSqlString(value);
        if (date != null) {
            return date.getTime();
        }

        try {
            DateFormat format = DateFormat.getDateInstance();

            date = format.parse(value);

            return date.getTime();

        } catch (ParseException pe) {
            return Long.MIN_VALUE;
        }
    }

    /**
     * Return true if the given string value is a long time value.
     *
     * @param value the string value to test
     * @return true if the given string value is a long time value.
     */
    protected boolean isTimeValue(String value) {
        for (int i = 0, size = value.length(); i < size; i++) {
            char aChar = value.charAt(i);
            if (i == 0) {
                if (!Character.isDigit(aChar) && aChar != '-') {
                    return false;
                }
            } else {
                if (!Character.isDigit(aChar)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Return a new date object from the give SQL format date string, or null
     * if the value is invalid.
     *
     * @param value the SQL format date string
     * @return a new date object from the give SQL format date string
     */
    @SuppressWarnings("deprecation")
    protected java.util.Date createDateFromSqlString(String value) {
        if (value.length() != 10) {
            return null;
        }

        for (int i = 0, size = value.length(); i < size; i++) {
            char aChar = value.charAt(i);
            if (!Character.isDigit(aChar) && aChar != '-') {
                return null;
            }
        }

        int firstDash = value.indexOf('-');
        int secondDash = value.indexOf('-', firstDash + 1);

        if ((firstDash > 0)
            & (secondDash > 0)
            & (secondDash < value.length() - 1)) {

            try {
                int year = Integer.parseInt(value.substring(0, firstDash)) - 1900;

                int month = Integer.parseInt(value.substring(firstDash + 1, secondDash)) - 1;

                int day = Integer.parseInt(value.substring(secondDash + 1));

                return new java.util.Date(year, month, day);

            } catch (NumberFormatException nfe) {
                return null;
            }

        } else {
            return null;
        }
    }

    /**
     * Convert the given value into a BigDecimal.
     *
     * @param value the object to convert into a BigDecimal
     * @return the converted BigDecimal value
     */
    private BigDecimal bigDecValue(Object value) {
        if (value == null) {
            return BigDecimal.valueOf(0L);
        }
        Class<?> c = value.getClass();
        if (c == BigDecimal.class) {
            return (BigDecimal) value;
        }
        if (c == BigInteger.class) {
            return new BigDecimal((BigInteger) value);
        }

        if (c == Boolean.class) {
            return BigDecimal.valueOf((Boolean) value ? 1 : 0);
        }
        if (c == Character.class) {
            return BigDecimal.valueOf((Character) value);
        }

        return new BigDecimal(value.toString().trim());
    }
}