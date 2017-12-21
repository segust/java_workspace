package model;

import cqupt.util.Rate;
import cqupt.util.getHoliday;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;

/**
 * 因夜间摄像头工作不稳定，故未考虑夜间车辆停留时间以及地点。
 */
public class RegularPoints {

    int NumOfWorkStay = 0;
    private ResultSet rs = null;
    private Map<String, Integer> holidayMap = new HashMap<String, Integer>();
    private List<String> timeList = new ArrayList<String>();

    private Map<String, Integer> workdaystayMap;
    private Map<String, Integer> weekendstayMap;
    private Map<String, Integer> holidaystayMap;

    private Map<String, Integer> workdaypassMap;
    private Map<String, Integer> weekendpassMap;
    private Map<String, Integer> holidaypassMap;


    public RegularPoints(ResultSet rs) {
        this.rs = rs;
        this.holidayMap = getHoliday.getMap();

        workdaystayMap = new HashMap<String, Integer>();
        weekendstayMap = new HashMap<String, Integer>();
        holidaystayMap = new HashMap<String, Integer>();
        workdaypassMap = new HashMap<String, Integer>();
        weekendpassMap = new HashMap<String, Integer>();
        holidaypassMap = new HashMap<String, Integer>();
    }

    public void getRegularPoints() {

        try {

            //声明左窗口参数
            String lastlocation;
            Timestamp lasttime;
            Double lastlat;
            Double lastlng;

            //声明右窗口参数
            String currentlocation;
            Timestamp currenttime;
            Double currentlat;
            Double currentlng;

            //窗口属性
            Double currentrate;
            long IntervalTime = 0;

            //初始化模板参数
            Timestamp TemTimeA = null;
            Timestamp TemTimeB = null;
            String TemRegionA = null;
            String TemRegionB = null;
            Double TemRate = 9999999999.0;

            if (rs.next()) {

                //初始化左窗口
                lastlocation = rs.getString("pass_port_name");
                lasttime = rs.getTimestamp("pass_time");
                lastlat = Double.valueOf(rs.getString("lat"));
                lastlng = Double.valueOf(rs.getString("lng"));

                while (rs.next()) {
                    //右窗口赋值
                    currentlocation = rs.getString("pass_port_name");
                    currenttime = rs.getTimestamp("pass_time");
                    if (!rs.getString("lat").equals("0") && !rs.getString("lat").equals("")) {
                        currentlat = Double.parseDouble(rs.getString("lat"));
                        currentlng = Double.parseDouble(rs.getString("lng"));
                    } else continue;

                    IntervalTime = currenttime.getTime() - lasttime.getTime();

                    //加入经过地点Map
                    int mark = holidayMap.get(String.valueOf((lasttime.getMonth() + 1) * 100 + lasttime.getDate()));
                    AddPassMap(workdaypassMap, weekendpassMap, holidaypassMap, lastlocation, mark);

                    //窗口速度赋值
                    currentrate = Rate.getRate(lastlat, lastlng, currentlat, currentlng, lasttime, currenttime);

                    if (currenttime.getMonth() == lasttime.getMonth()) {

                        //同月处理逻辑
                        //日期改变处理逻辑
                        if (currenttime.getDate() != lasttime.getDate()) {

                            //将模板参数添加进容器
                            if (TemTimeA != null) {
                                String time = TemTimeA.toString() + " - " + TemTimeB.toString();
                                timeList.add(time);
                                AddStayMap(workdaystayMap, weekendstayMap, holidaystayMap, TemRegionA, TemRegionB, mark);
                            }
                            //重新初始化模板参数
                            TemTimeA = null;
                            TemTimeB = null;
                            TemRegionA = null;
                            TemRegionB = null;
                            TemRate = 99999999999.0;

                        } else {

                            //日期不变处理逻辑
                            //模板改变标准为速度变小并且停留时间大于3小时
                            if (currentrate.compareTo(TemRate) < 0 && IntervalTime > 0.5 * 60 * 60 * 1000) {
                                TemRegionA = lastlocation;
                                TemRegionB = currentlocation;
                                TemTimeA = lasttime;
                                TemTimeB = currenttime;
                                TemRate = currentrate;
                            }
                        }

                        //窗口向右移动后左窗口相应参数调整
                        lastlocation = currentlocation;
                        lasttime = currenttime;
                        lastlat = currentlat;
                        lastlng = currentlng;

                        //数据集末尾处理逻辑
                        if (rs.isLast()) {
                            TemRegionA = "?";
                            TemRegionB = rs.getString("pass_port_name");
                            TemRate = 999999999999.0;

                            System.out.println(currenttime.getMonth() + 1 + "月数据：");

                            System.out.println("工作日常留地点：\n");
                            OrderByValueAndOut(workdaystayMap);
                            System.out.println("周末常留地点：\n");
                            OrderByValueAndOut(weekendstayMap);
                            System.out.println("节假日常留地点：\n");
                            OrderByValueAndOut(holidaystayMap);
                            System.out.println("工作日常经过地点：\n");
                            OrderByValueAndOut(workdaypassMap);
                            System.out.println("周末常经过地点：\n");
                            OrderByValueAndOut(weekendpassMap);
                            System.out.println("节假日常经过地点：\n");
                            OrderByValueAndOut(holidaypassMap);
                            System.out.println("经常停留时间段：\n");
                            for (String time : timeList)
                                System.out.println(time);
                            //由于节假日及周末不确定性太多，故选择工作日常留地点众数来评价该车辆日常规律程度
                            double Mark = 0.0;
                            int num = 0;
                            for (Entry<String, Integer> en : workdaystayMap.entrySet()) {
                                if (en.getValue() > 2)
                                    num += en.getValue();
                            }
                            Mark = (float) num / (float) NumOfWorkStay;
                            System.out.println();
                            System.out.println("车辆规律程度：" + Mark * 100 + "%");
                            System.out.println();


                        }
                    } else {

                        //不同月处理逻辑
                        System.out.println(lasttime.getMonth() + 1 + "月数据：");

                        //将模板参数添加进容器
                        if (TemTimeA != null) {
                            String Time = TemTimeA.toString() + " - " + TemTimeB.toString();
                            timeList.add(Time);
                            AddStayMap(workdaystayMap, weekendstayMap, holidaystayMap, TemRegionA, TemRegionB, mark);
                        }
                        //输出相应结果
                        System.out.println("工作日常留地点：\n");
                        OrderByValueAndOut(workdaystayMap);
                        System.out.println("周末常留地点：\n");
                        OrderByValueAndOut(weekendstayMap);
                        System.out.println("节假日常留地点：\n");
                        OrderByValueAndOut(holidaystayMap);
                        System.out.println("工作日常经过地点：\n");
                        OrderByValueAndOut(workdaypassMap);
                        System.out.println("周末常经过地点：\n");
                        OrderByValueAndOut(weekendpassMap);
                        System.out.println("节假日常经过地点：\n");
                        OrderByValueAndOut(holidaypassMap);
                        System.out.println("经常停留时间段：\n");
                        for (String time : timeList)
                            System.out.println(time);

                        //由于节假日及周末不确定性太多，故选择工作日常留地点众数来评价该车辆日常规律程度
                        double Mark = 0.0;
                        int num = 0;
                        for (Entry<String, Integer> en : workdaystayMap.entrySet()) {
                            if (en.getValue() > 2)
                                num += en.getValue();
                        }
                        Mark = (float) num / (float) NumOfWorkStay;
                        System.out.println();
                        System.out.println("车辆规律程度：" + Mark * 100 + "%");
                        System.out.println();

                        //初始化左窗口参数
                        lastlocation = currentlocation;
                        lasttime = currenttime;
                        lastlat = currentlat;
                        lastlng = currentlng;
                        NumOfWorkStay = 0;

                        //初始化模板数据
                        TemRegionA = null;
                        TemRegionB = null;
                        TemRate = 999999999999.0;

                        //重新初始化结果容器
                        timeList = new ArrayList<String>();

                        workdaystayMap = new HashMap<String, Integer>();
                        weekendstayMap = new HashMap<String, Integer>();
                        holidaystayMap = new HashMap<String, Integer>();

                        workdaypassMap = new HashMap<String, Integer>();
                        weekendpassMap = new HashMap<String, Integer>();
                        holidaypassMap = new HashMap<String, Integer>();


                    }
                }
            } else {
                System.out.println("车牌号输入有误或无该车牌号信息！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void AddStayMap(Map<String, Integer> workdaystayMap, Map<String, Integer> weekendstayMap, Map<String, Integer> holidaystayMap, String TemRegionA, String TemRegionB, int mark) {
        String situA = TemRegionA + " 至 " + TemRegionB;
        String situB = TemRegionB + " 至 " + TemRegionA;
        if (mark == 2) {
            if (!holidaystayMap.containsKey(situA) && !holidaystayMap.containsKey(situB)) {
                holidaystayMap.put(situA, 1);
            } else {
                if (holidaystayMap.containsKey(situA)) {
                    int n = holidaystayMap.get(situA);
                    holidaystayMap.put(situA, ++n);
                } else {
                    int n = holidaystayMap.get(situB);
                    holidaystayMap.put(situB, ++n);
                }
            }

        } else if (mark == 1) {

            if (!weekendstayMap.containsKey(situA) && !weekendstayMap.containsKey(situB)) {
                weekendstayMap.put(situA, 1);
            } else {
                if (weekendstayMap.containsKey(situA)) {
                    int n = weekendstayMap.get(situA);
                    weekendstayMap.put(situA, ++n);
                } else {
                    int n = weekendstayMap.get(situB);
                    weekendstayMap.put(situB, ++n);
                }
            }

        } else {
            NumOfWorkStay++;
            if (!workdaystayMap.containsKey(situA) && !workdaystayMap.containsKey(situB)) {
                workdaystayMap.put(situA, 1);
            } else {
                if (workdaystayMap.containsKey(situA)) {
                    int n = workdaystayMap.get(situA);
                    workdaystayMap.put(situA, ++n);
                } else {
                    int n = workdaystayMap.get(situB);
                    workdaystayMap.put(situA, ++n);
                }
            }

        }

    }

    private void AddPassMap(Map<String, Integer> workdaypassMap, Map<String, Integer> weekendpassMap, Map<String, Integer> holidaypassMap, String lastlocation, int mark) {
        if (mark == 2) {
            if (!holidaypassMap.containsKey(lastlocation)) {
                holidaypassMap.put(lastlocation, 1);
            } else {
                int n = holidaypassMap.get(lastlocation);
                holidaypassMap.put(lastlocation, ++n);
            }
        } else if (mark == 1) {
            if (!weekendpassMap.containsKey(lastlocation)) {
                weekendpassMap.put(lastlocation, 1);
            } else {
                int n = weekendpassMap.get(lastlocation);
                weekendpassMap.put(lastlocation, ++n);
            }
        } else {
            if (!workdaypassMap.containsKey(lastlocation)) {
                workdaypassMap.put(lastlocation, 1);
            } else {
                int n = workdaypassMap.get(lastlocation);
                workdaypassMap.put(lastlocation, ++n);
            }
        }

    }

    private void OrderByValueAndOut(Map<String, Integer> map) {
        List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(map.entrySet());

        Collections.sort(list, new Comparator<Entry<String, Integer>>() {
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return (o2.getValue() - o1.getValue());
            }
        });
        for (int i = 0; i < list.size() && i < 5; i++)
            System.out.print(list.get(i).getKey() + ":" + list.get(i).getValue() + "\t");
        System.out.println();
        System.out.println();

    }

}