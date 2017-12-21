package util;

import java.sql.*;
import java.util.*;

import model.SMS;

public class LoadData {

	int spamNum;
	int hamNum;

	public int getSpamNum() {
		return spamNum;
	}

	public int getHamNum() {
		return hamNum;
	}

	public ArrayList<SMS> getSMSList(String req) {

		ArrayList<SMS> SMSList = new ArrayList<SMS>();

		try {
			spamNum = 0;
			hamNum = 0;
			Statement stm = null;
			Connection con = JDBC.getConnection();
			stm = con.createStatement();
			String Sql = null;

			if (req.equals("dataSet")) {
				Sql = "select * from bayestrainSet";
			} else if (req.equals("trainSet")) {
				Sql = "select * from bayesdataSet";
			} else {
				System.out.println("��������");
			}
			ResultSet rs = stm.executeQuery(Sql);

			if (rs != null)
				System.out.println("���ݼ��سɹ�");
			while (rs.next()) {
				SMS SMS = new SMS();
				SMS.setContent(rs.getString("content"));
				SMS.setClassify(rs.getString("classify"));
				SMSList.add(SMS);
				if (rs.getString("classify").equals("spam"))
					spamNum++;
				else if (rs.getString("classify").equals("ham"))
					hamNum++;
				else
					System.out.println("��������");
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return SMSList;

	}
}