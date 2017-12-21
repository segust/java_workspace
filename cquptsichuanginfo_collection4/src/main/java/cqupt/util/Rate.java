package cqupt.util;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author
 * @date 2017年8月22日 下午8:59:39
 * @parameter
 */
public class Rate {
    public static Double getRate(Double beginlat, Double beginlng, Double endlat, Double endlng,
                                 Timestamp begintime, Timestamp endtime) {
        Double rate = ((beginlat - endlat) * (beginlat - endlat) + (beginlng - endlng) * (beginlng - endlng)) / (endtime.getTime() - begintime.getTime());
        return rate;
    }

}
