package no.ovitas.compass2.config.settings;

public class Options implements Setting {

	private String tree;

	@Override
	public String dumpOut(String indent) {
		String ind = indent + " ";
		StringBuffer buffer = new StringBuffer(ind);

		buffer.append("Options: \n");

		buffer.append(ind);
		buffer.append("Tree: ");
		buffer.append(tree);
		buffer.append("\n");

		return buffer.toString();
	}

	public String getTree() {
		return tree;
	}

	public Tree getTreeEnum() {
		if (tree != null)
			return Tree.valueOf(tree.toUpperCase());

		return null;
	}

	public void setTree(String tree) {
		this.tree = tree;
	}

}
