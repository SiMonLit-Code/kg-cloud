package com.plantdata.kgcloud.domain.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RemindDateUtils {

    /**
     * 获取  当前年、半年、季度、月、日、小时 开始结束时间
     */
    private final SimpleDateFormat shortSdf;
    private final SimpleDateFormat longHourSdf;
    private final SimpleDateFormat longSdf;
    private final SimpleDateFormat longMINUTESdf;
    public RemindDateUtils() {
        this.shortSdf = new SimpleDateFormat("yyyy-MM-dd");
        this.longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
        this.longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.longMINUTESdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }


    /**
     * 获取当天第一时
     *
     * @return
     */
    public Date getCurrentHHStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            c.setTime(longSdf.parse(longHourSdf.format(c.getTime()) + ":00:00"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }


    /**
     * 获取当天最后
     *
     * @return
     */
    public Date getCurrentHHEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            c.setTime(longSdf.parse(longHourSdf.format(c.getTime()) + ":59:59"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获取当天最后
     *
     * @return
     */
    public Date addCurrentHHTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            c.add(Calendar.HOUR, 1); //把日期往后增加一天
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获取当天第一时
     *
     * @return
     */
    public Date getCurrentMMStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            c.setTime(longSdf.parse(longMINUTESdf.format(c.getTime()) + ":00"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }


    /**
     * 获取当天最后
     *
     * @return
     */
    public Date getCurrentMMEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            c.setTime(longSdf.parse(longMINUTESdf.format(c.getTime()) + ":59"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }


    /**
     * 获取当天最后
     *
     * @return
     */
    public Date addCurrentMMTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            c.add(Calendar.MINUTE, 1); //把日期往后增加一天
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }


    /**
     * 获取当天第一时
     *
     * @return
     */
    public Date getCurrentDayStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }


    /**
     * 获取当天最后
     *
     * @return
     */
    public Date getCurrentDayEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }



    /**
     * 加一天
     *
     * @return
     */
    public Date addCurrentDayTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            c.add(Calendar.DATE, 1); //把日期往后增加一天
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }


    /**
     * 获得本周的最后一天，周日
     *
     * @return
     */
    public Date getCurrentWeekDayEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DATE, 8 - weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }


    /**
     * 将时间退后一周
     *
     * @return
     */
    public Date addCurrentWeekDayTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            c.add(Calendar.WEEK_OF_MONTH, 1); //把日期往后增加一周，整数往后推，负数往前移
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得本月的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public Date getCurrentMonthStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }


    /**
     * 当前月的结束时间，即2012-01-31 23:59:59
     *
     * @return
     */
    public Date getCurrentMonthEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 将时间向后推一个月
     *
     * @return
     */
    public Date addCurrentMonthTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date now = null;
        try {
            c.add(Calendar.MONTH, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 当前年的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public Date getCurrentYearStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date now = null;
        try {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }


    /**
     * 当前年的结束时间，即2012-12-31 23:59:59
     *
     * @return
     */
    public Date getCurrentYearEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date now = null;
        try {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }


    /**
     * 当前年的结束时间，即2012-12-31 23:59:59
     *
     * @return
     */
    public Date addCurrentYearTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Date now = null;
        try {
            c.add(Calendar.YEAR, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 当前季度的开始时间，即2012-01-1 00:00:00
     *
     * @return
     */
    public Date getCurrentQuarterStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }


    /**
     * 当前季度的结束时间，即2012-03-31 23:59:59
     *
     * @return
     */
    public Date getCurrentQuarterEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 时间加一个季度
     *
     * @return
     */
    public Date addCurrentQuarterTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            c.add(Calendar.MONTH, 4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }


}