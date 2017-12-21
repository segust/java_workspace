package PageRank;

import java.util.*;

public class PageRankNode {
	private String id;
	private List<String> destNodes = new ArrayList<String>();
	private double oldPR;
	private double newPR;
	private int numDest;

	public PageRankNode() {

	}

	public PageRankNode(String id) {
		this.id = id;
	}

	public static String toStringWithOutID(PageRankNode node) {
		StringBuffer temp = new StringBuffer();
		temp.append(node.getOldPR());
		temp.append("\t" + node.getNewPR());
		temp.append("\t" + node.getNumDest());
		for (String dest : node.getDestNodes()) {
			temp.append("\t" + dest);
		}
		return temp.toString();
	}

	public static PageRankNode InstanceFormString(String nodeStr) {
		PageRankNode node = new PageRankNode();
		String[] res = nodeStr.split("\\s+");
		node.setId(res[0]);

		if (res.length == 2) {
			node.setNewPR(Double.valueOf(res[1]));
		} else if (res.length > 4) {
			node.setOldPR(Double.valueOf(res[1]));
			node.setNewPR(Double.valueOf(res[2]));
			node.setNumDest(Integer.valueOf(res[3]));
			for (int i = 4; i < res.length; i++) {
				node.getDestNodes().add(res[i]);
			}
			assert (node.getNumDest() == node.getDestNodes().size());
		}
		return node;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getDestNodes() {
		return destNodes;
	}

	public void setDestNodes(List<String> destNodes) {
		this.destNodes = destNodes;
	}

	public double getOldPR() {
		return oldPR;
	}

	public void setOldPR(double oldPR) {
		this.oldPR = oldPR;
	}

	public double getNewPR() {
		return newPR;
	}

	public void setNewPR(double newPR) {
		this.newPR = newPR;
	}

	public int getNumDest() {
		return numDest;
	}

	public void setNumDest(int numDest) {
		this.numDest = numDest;
	}

}
