package no.ovitas.compass2.util;

import java.util.Collection;
import java.util.Comparator;

import no.ovitas.compass2.model.TopicTreeNode;

public class TopicTreeNodeComparator implements Comparator<TopicTreeNode> {

	private String pattern;
	private int hopCount;
	
	public TopicTreeNodeComparator(String pattern, int hopcount){
		this.pattern = pattern;
		this.hopCount = hopcount;
	}
	
	@Override
	public int compare(TopicTreeNode node1, TopicTreeNode node2) {
		Collection<TopicTreeNode> children1 = node1.getChildren();
		Collection<TopicTreeNode> children2 = node2.getChildren();
		
		Double node1Boost = node1.getBoost();
		Double node2Boost = node2.getBoost();
		
		String node1Name = node1.getName();
		String node2Name = node2.getName();
		
		int level = hopCount;
		
		if(node1Boost.equals(node2Boost)) {
			Integer p1 = 0;
			Integer p2 = 0;
			
			p1 += levenShteinDistance(node1Name, pattern);
			p2 += levenShteinDistance(node2Name, pattern);
			
			for (TopicTreeNode child : children1){
				p1 += collectPoints(child, level-1);
			}
			
			for (TopicTreeNode child : children2){
				p2 += collectPoints(child, level-1);
			}
						
			return p1.compareTo(p2);
			
		} else {
			return node1Boost.compareTo(node2Boost);
		}
		
		
	}

	private Integer collectPoints(TopicTreeNode child, int level) {
		Integer points = 0;
		
		if (level >= 0) {	
			points = levenShteinDistance(child.getName(), pattern);
			for (TopicTreeNode c : child.getChildren()){
				points += collectPoints(c, level-1);
			}
		}
		
		return points;
	}

	/**
	 * The implementation of Levensthein algorith
	 * http://www.merriampark.com/ld.htm
	 * @param s the sopurce
	 * @param t the target
	 * @return
	 */
	public static int levenShteinDistance(String s, String t) {
		int cost = 0;
		char s_i; // i th character of s
		char t_j; // j th character of t
		
		// Set n to be the length of s.
		int n = s.length();
		
		// Set m to be the length of t.
		int m = t.length();
		
		// If n = 0, return m and exit.
		if (n == 0) return m;
		
		// If m = 0, return n and exit.
		if (m == 0) return n;
		
		// Construct a matrix containing 0..m rows and 0..n columns. 
		int[][] d = new int[n+1][m+1];
		
		// Initialize the first row to 0..n.
		for (int i = 0; i <= n; i++)
			d[i][0] = i;

		// Initialize the first column to 0..m.
		for (int j = 0; j <= m; j++)
			d[0][j] = j;
		
		// Examine each character of s (i from 1 to n).
		// Examine each character of t (j from 1 to m).
		for (int i = 1; i <= n; i++) {
			s_i = s.charAt(i-1);
			for (int j = 1; j <= m; j++) {
				t_j = t.charAt(j-1);
				// If s[i] equals t[j], the cost is 0.
				if (s_i == t_j)
					cost = 0;
				// If s[i] doesn't equal t[j], the cost is 1.
				else
					cost = 1;
				
				// Set cell d[i,j] of the matrix equal to the minimum of:
				// a. The cell immediately above plus 1: d[i-1,j] + 1.
				// b. The cell immediately to the left plus 1: d[i,j-1] + 1.
				// c. The cell diagonally above and to the left plus the cost: d[i-1,j-1] + cost.

				int a = d[i-1][j] + 1;
				int b = d[i][j-1] + 1;
				int c = d[i-1][j-1] + cost;

				int mi = a;
			    if (b < mi) {
			      mi = b;
			    }
			    if (c < mi) {
			      mi = c;
			    }
				
				d[i][j] = mi;
			}
		}
		
		return d[n][m];
		
	} 

}
