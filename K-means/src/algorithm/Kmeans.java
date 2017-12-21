package algorithm;

import java.io.*;
import java.util.*;

public class Kmeans {

	int k = 3;
	ArrayList<ArrayList<ArrayList<Double>>> DataList = null;

	public Kmeans(int k) {

		this.k = k;
	}

	public ArrayList<ArrayList<ArrayList<Double>>> getDataList() throws IOException {

		/******** 初始化数据 **********/
		// 输入数据
		BufferedReader message = new BufferedReader(
				new InputStreamReader(new FileInputStream("E:/Workspace/K-means/src/algorithm/wine.txt")));
		ArrayList<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> e = new ArrayList<Double>();
		int num;
		int dimension;

		String mess;
		String[] d = null;
		for (num = 0; (mess = message.readLine()) != null; num++) {
			d = mess.split(",");
			for (int i = 0; i < d.length; i++)
				e.add(Double.valueOf(d[i]));
			data.add(e);
			e = new ArrayList<Double>();
		}
		dimension = d.length;
		message.close();

		// 随机选取质心
		Random random = new Random();
		ArrayList<ArrayList<Double>> centers = new ArrayList<ArrayList<Double>>(k);
		ArrayList<Double> c;
		for (int i = 0; i < k;) {
			int j = random.nextInt(num);
			c = new ArrayList<Double>(dimension);
			for (int m = 0; m < dimension; m++) {
				c.add(data.get(j).get(m));
			}
			if (!centers.contains(c)) {
				centers.add(c);
				c = new ArrayList<Double>(dimension);
				i++;
			}
		}

		// 迭代计算所有点与质心距离并聚类
		int count = 0;
		while (true) {

			int index = 0;
			double Distance = 99999999.0;
			double newDistance = 0.0;
			DataList = new ArrayList<ArrayList<ArrayList<Double>>>(k);
			ArrayList<Double> E = new ArrayList<Double>(dimension);
			for (int i = 0; i < k; i++)
				DataList.add(new ArrayList<ArrayList<Double>>());

			for (int i = 0; i < num; i++) {
				for (int j = 0; j < k; j++) {
					for (int m = 0; m < dimension; m++) {
						newDistance += (data.get(i).get(m) - centers.get(j).get(m))
								* (data.get(i).get(m) - centers.get(j).get(m));
					}
					if (Math.sqrt(newDistance) < Math.sqrt(Distance)) {
						Distance = newDistance;
						index = j;
					}
					newDistance = 0.0;
				}
				for (int n = 0; n < dimension; n++)
					E.add(data.get(i).get(n));
				DataList.get(index).add(E);
				Distance = 9999999.0;
				E = new ArrayList<Double>(dimension);
			}

			// 计算SSE
			double SSE = 0.0;
			for (int i = 0; i < k; i++) {
				for (int j = 0; j < DataList.get(i).size(); j++) {
					for (int m = 0; m < dimension; m++) {
						SSE += (DataList.get(i).get(j).get(m) - centers.get(i).get(m))
								* (DataList.get(i).get(j).get(m) - centers.get(i).get(m));
					}
				}
			}
			System.out.println("第" + count + "次迭代结果");
			System.out.println("SSE=" + Math.sqrt(SSE));

			// 计算新的质心
			ArrayList<ArrayList<Double>> newcenters = new ArrayList<ArrayList<Double>>(k);
			ArrayList<Double> newc = new ArrayList<Double>(dimension);
			for (int i = 0; i < k; i++) {
				for (int j = 0; j < dimension; j++) {
					double ave = 0;
					double sum = 0;
					for (int m = 0; m < DataList.get(i).size(); m++) {
						sum += DataList.get(i).get(m).get(j);
					}
					ave = sum / DataList.get(i).size();
					newc.add(ave);
				}
				newcenters.add(newc);
				newc = new ArrayList<Double>(dimension);
			}

			// 判断收敛并输出结果
			count++;
			if (centers.equals(newcenters)) {
				for (int i = 0; i < k; i++) {
					System.out.println("mu" + (i + 1) + "=[");
					for (int j = 0; j < DataList.get(i).size(); j++) {
						for (int m = 0; m < dimension; m++) {
							System.out.print(DataList.get(i).get(j).get(m) + "\t");
						}
						System.out.println("");
					}
					System.out.println("]");
				}
				System.out.println("center=[");
				for (int i = 0; i < k; i++) {
					for (int j = 0; j < centers.get(i).size(); j++)
						System.out.print(" " + centers.get(i).get(j));
					System.out.println("");
				}
				System.out.println("]");
				break;
			}
			centers = newcenters;
		}
		return DataList;
	}
}
