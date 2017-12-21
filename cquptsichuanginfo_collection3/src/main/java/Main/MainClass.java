package Main;

import cqupt.db.DBManager;
import model.RegularPoints;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        System.out.println("输入\n车牌号 开始日期 结束日期\n例如：\n皖A49739 20170501 20170601");
        Scanner scan = new Scanner(System.in);
        String in = scan.nextLine();
        String[] info = in.split(" ");
        String license_no = info[0];
        String start_time = info[1];
        String end_time = info[2];

        scan.close();
        Connection conn = null;
        ResultSet rs = null;
        Statement st = null;

        long startTime = System.currentTimeMillis();

        conn = DBManager.getConnection();
        try {
            st = conn.createStatement();
            rs = st.executeQuery(
                    "select * from `info_collection` where license_no='" + license_no + "' and pass_time > '" + start_time + "' and pass_time < '" + end_time + "' order by pass_time");
            RegularPoints re = new RegularPoints(rs);
            re.getRegularPoints();
            System.out.println("\n查询时间：" + (System.currentTimeMillis() - startTime));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBManager.closeConnection(conn, st, rs);
        }

    }
}