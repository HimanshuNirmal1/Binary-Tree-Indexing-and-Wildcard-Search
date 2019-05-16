
import java.util.*;
import java.util.regex.*;
/**
 * 
 * @author 
 * a node in a binary search tree
 */
class BTNode {
	BTNode left, right;
	String term;
	ArrayList<Integer> docLists;
	
	/**
	 * Create a tree node using a term and a document list
	 * @param term the term in the node
	 * @param docList the ids of the documents that contain the term
	 */
	public BTNode(String term, ArrayList<Integer> docList)
	{
		this.term = term;
		this.docLists = docList;
	}
	
}

/**
 * 
 * @author 
 * Binary search tree structure to store the term dictionary
 */
public class BinaryTree {

	/**
	 * insert a node to a subtree 
	 * @param node root node of a subtree
	 * @param iNode the node to be inserted into the subtree
	 */
	BTNode rootNode;
	public void add(BTNode node, BTNode iNode)
	{
//		System.out.println("adding " + iNode.term);
		if(node == null) {
			this.rootNode = iNode;
			return;
		}
		BTNode currentNode;
		currentNode = node;
		while(true) {
			if(currentNode.term.compareTo(iNode.term) > 0) {
				if(currentNode.left == null) {
					currentNode.left = iNode;
					break;
				}
				else {
					currentNode = currentNode.left;
				}
			}
			else if(currentNode.term.compareTo(iNode.term) == 0) {
				if(currentNode.docLists.indexOf(iNode.docLists.get(0)) == -1) {
					currentNode.docLists.add(iNode.docLists.get(0));
				}
//				else {
//					currentNode.docLists.add(iNode.docLists.get(0));
//				}
				break;
			}
			else {
				if(currentNode.right == null) {
					currentNode.right = iNode;
				}
				else {
					currentNode = currentNode.right;
				}
			}
		}
	}
	
	/**
	 * Search a term in a subtree
	 * @param n root node of a subtree
	 * @param key a query term
	 * @return tree nodes with term that match the query term or null if no match
	 */
	public BTNode search(BTNode n, String key)
	{
		//TO BE COMPLETED
		BTNode currentNode = n;
		while(true) {
			if(currentNode != null) {
				if(currentNode.term.compareTo(key) > 0) {
					currentNode = currentNode.left;
				}
				else if(currentNode.term.compareTo(key) == 0) {
					return currentNode;
				}
				else {
					currentNode = currentNode.right;
				}
			}
			else {
				return null;
			}
		}
	}
	
	/**
	 * Do a wildcard search in a subtree
	 * @param n the root node of a subtree
	 * @param key a wild card term, e.g., ho (terms like home will be returned)
	 * @return tree nodes that match the wild card
	 */
	public ArrayList<BTNode> wildCardSearch(BTNode n, String key)
	{
		//TO BE COMPLETED
		ArrayList<BTNode> matchingNodes = new ArrayList<BTNode>();
		Pattern p = Pattern.compile(key + ".*");
		wildSearch(n, matchingNodes, p, key, false);
		return matchingNodes;
	}
	
	
	public void wildSearch(BTNode currentNode, ArrayList<BTNode> matchingNode, Pattern p, String key, boolean match) {
		if(currentNode != null) {
//			System.out.println("currentNode " + currentNode.term);
			Matcher m = p.matcher(currentNode.term);
			if(m.matches()) {
				//System.out.println("matched " + currentNode.term );
				matchingNode.add(currentNode);
				match = true;
				wildSearch(currentNode.left, matchingNode, p, key, match);
				wildSearch(currentNode.right, matchingNode, p, key, match);				
			}
			else {
				if(key.compareTo(currentNode.term) >= 0) {
					wildSearch(currentNode.right, matchingNode, p, key, match);				
				}
				else {
					wildSearch(currentNode.left, matchingNode, p, key, match);
				}
			}
		}
	}
	
	/**
	 * Print the inverted index based on the increasing order of the terms in a subtree
	 * @param node the root node of the subtree
	 */
	public void printInOrder(BTNode node)
	{
		if(node == null) {
			return;
		}
		printInOrder(node.left);
		System.out.println(node.term + "\t" +node.docLists);
		printInOrder(node.right);
		//TO BE COMPLETED
	}
}

