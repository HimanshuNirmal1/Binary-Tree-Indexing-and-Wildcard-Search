
import java.util.*;

public class BTreeIndex {
	String[] myDocs;
	BinaryTree termList;
	BTNode root;

	/**
	 * Construct binary search tree to store the term dictionary
	 * 
	 * @param docs
	 *            List of input strings
	 * 
	 */
	public BTreeIndex(String[] docs) {
		String[] words;
		int i, j;
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		Map<String, ArrayList<Integer>> values = new HashMap<String, ArrayList<Integer>>();

		if (docs.length > 0 && docs[0].length() > 0) {
			words = docs[0].split("\\s+");
			indexes.add(0);
			termList = new BinaryTree();
			root = new BTNode(words[0], indexes);
			for (i = 0; i < words.length; i++) {
				// indexes = new ArrayList<Integer>();
				// indexes.add(0);
				// termList.add(root, new BTNode(words[i], indexes));
				indexes = new ArrayList<Integer>();
				indexes.add(0);
				if (!values.containsKey(words[i])) {
					values.put(words[i], indexes);
				}
			}
		}
		for (i = 1; i < docs.length; i++) {
			words = docs[i].split("\\s+");
			// indexes.add(i);
			for (j = 0; j < words.length; j++) {
				// System.out.println("adding word " + words[j] + " to values from doc " + i);
				// indexes = new ArrayList<Integer>();
				// termList.add(root, new BTNode(words[j], indexes));
				if (values.containsKey(words[j]) && values.get(words[j]).indexOf(i) == -1) {
					// add index to the word indexes in values
					values.get(words[j]).add(i);
				} else if (!values.containsKey(words[j])) {
					indexes = new ArrayList<Integer>();
					indexes.add(i);
					values.put(words[j], indexes);
				}
			}
			System.out.println();
		}
		ArrayList<BTNode> nodes = new ArrayList<BTNode>();
		SortedSet<String> keys = new TreeSet<>(values.keySet());
		for (String key : keys) {
			// System.out.println("adding key " + key);
			nodes.add(new BTNode(key, values.get(key)));
		}
		int middle = nodes.size() / 2;
		root = nodes.get(middle);
		termList.rootNode = root;
		// System.out.println("size of nodes " + nodes.size() + " root " + root.term);
		// System.out.println("in indexes " + values.get("in"));
		if (middle - 1 > 0) {
			// add the left part
			generateTree(root, nodes.subList(0, middle), termList);
		}
		if (nodes.size() > middle + 1) {
			generateTree(root, nodes.subList(middle + 1, nodes.size()), termList);
		}
		// System.out.println("root ka left" + termList.rootNode.term);
		// for(String key : values.keySet()) {
		// System.out.println("key " + key + " values " + values.get(key));
		// }
	}

	private void generateTree(BTNode root, List<BTNode> nodes, BinaryTree termList) {
		int middle;
		if (nodes.size() > 1) {
			middle = nodes.size() / 2;
			// System.out.println("middle " + middle + " size " + nodes.size());
			termList.add(root, nodes.get(middle));
			if (middle - 1 >= 0) {
				generateTree(nodes.get(middle), nodes.subList(0, middle), termList);
			}
			if (nodes.size() > middle) {
				generateTree(nodes.get(middle), nodes.subList(middle + 1, nodes.size()), termList);
			}
		} else if (nodes.size() == 1) {
			//System.out.println("ADDING " + nodes.get(0).term);
			termList.add(root, nodes.get(0));
		}

	}

	/**
	 * Single keyword search
	 * 
	 * @param query
	 *            the query string
	 * @return doclists that contain the term
	 */
	public ArrayList<Integer> search(String query) {
		BTNode node = termList.search(root, query);
		if (node == null)
			return null;
		return node.docLists;
	}

	/**
	 * conjunctive query search
	 * 
	 * @param query
	 *            the set of query terms
	 * @return doclists that contain all the query terms
	 */
	public ArrayList<Integer> search(String[] query) {
		ArrayList<Integer> result = search(query[0]);
		int termId = 1;
		while (termId < query.length) {
			ArrayList<Integer> result1 = search(query[termId]);
			result = merge(result, result1);
			termId++;
		}
		return result;
	}

	/**
	 * 
	 * @param wildcard
	 *            the wildcard query, e.g., ho (so that home can be located)
	 * @return a list of ids of documents that contain terms matching the wild card
	 */
	public ArrayList<Integer> wildCardSearch(String wildcard) {
		ArrayList<Integer> docIndexes = new ArrayList<Integer>();
		ArrayList<BTNode> matchingNodes = termList.wildCardSearch(root, wildcard);
		int j;
		// System.out.println("matching length " + matchingNodes.size());
		ArrayList<Integer> docIds;
		//System.out.println("size " + matchingNodes.size());
		for (int i = 0; i < matchingNodes.size(); i++) {
			docIds = matchingNodes.get(i).docLists;
			//System.out.println("DocIds " + docIds);
			for (j = 0; j < docIds.size(); j++) {
				if (docIndexes.indexOf(docIds.get(j)) == -1) {
					// System.out.println("adding in docindex " + docIds.get(j));
					docIndexes.add(docIds.get(j));
				}
			}
		}
		return docIndexes;
		// TO BE COMPLETED
	}

	private ArrayList<Integer> merge(ArrayList<Integer> l1, ArrayList<Integer> l2) {
		ArrayList<Integer> mergedList = new ArrayList<Integer>();
		int id1 = 0, id2 = 0;
		while (id1 < l1.size() && id2 < l2.size()) {
			if (l1.get(id1).intValue() == l2.get(id2).intValue()) {
				mergedList.add(l1.get(id1));
				id1++;
				id2++;
			} else if (l1.get(id1) < l2.get(id2))
				id1++;
			else
				id2++;
		}
		return mergedList;
	}

	/**
	 * Test cases
	 * 
	 * @param args
	 *            commandline input
	 */
	public static void main(String[] args) {
		String[] docs = { "new home sales top forecasts", "home sales rise in july", "increase in home sales in july",
				"july new home sales rise" };
		BTreeIndex b = new BTreeIndex(docs);
		BinaryTree bt = new BinaryTree();
		System.out.println("Indexing of term dictionary");
		bt.printInOrder(b.root);
		// System.out.println("left " + b.root.left.term);
		// System.out.println("right " + b.root.right.term);
		System.out.println("*****Single term queries******");
		System.out.println("----------Test  Case 1('top')-------");
		String query = "top";
		ArrayList<Integer> result = new ArrayList<Integer>();
		result = b.search(query);
      if (result != null && result.size() > 0){
		System.out.println("Documents are:" + result);
      }
      else {
      System.out.println("No match found");
      }
		System.out.println("********Conjuctive queries********");
		System.out.println("----------Test  Case 2('home','sales')-------");
		String[] query1 = { "home", "sales" };
		ArrayList<Integer> result1 = new ArrayList<Integer>();
		result1 = b.search(query1);
      if (result1 != null && result1.size() > 0){
		System.out.println("Documents are:" + result1);
      }
      else {
      System.out.println("No Match found");
      }
		System.out.println("********Wildcard queries********");
		System.out.println("----------Test  Case 1('ju')-------");
		String query2 = "ju";
		ArrayList<Integer> result2 = new ArrayList<Integer>();
		result2 = b.wildCardSearch(query2);
      if (result2 != null && result2.size() > 0){
		System.out.println("Documents are:" + result2);
      } else {
      System.out.println("No Match found");
      }
		System.out.println("----------Test  Case 2('in')-------");
		String query3 = "in";
		ArrayList<Integer> result3 = new ArrayList<Integer>();
		result3 = b.wildCardSearch(query3);
      if (result3 != null && result3.size() > 0){
		System.out.println("Documents are:" + result3);
      } else {
      System.out.println("No match found");
      }
      System.out.println("----------Test  Case 3('ab')-------");
		String query4 = "ab";
		ArrayList<Integer> result4 = new ArrayList<Integer>();
		result4 = b.wildCardSearch(query4);
      if (result4 != null && result4.size() > 0){
		System.out.println("Documents are:" + result4);
      } else {
      System.out.println("No match found");
      }


      
      
      
      
		// System.out.println(b.root.term);
		// TO BE COMPLETED with testcases
	}
}