package es.us.isa.ideas.repo;

import java.util.ArrayList;
import java.util.List;


public abstract class Node {

	private boolean isFolder;
	
	private String title;
	
	private List<Node> children;

	public List<Node> getChildren() {
		if (children == null ) {
			children = new ArrayList<Node>();
		}
		return children;
	}

	public void setChildren(List<Node> nodeList) {
		this.children = nodeList;
	}

	public boolean isFolder() {
		return isFolder;
	}

	public void setFolder(boolean isFolder) {
		this.isFolder = isFolder;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((children == null) ? 0 : children.hashCode());
		result = prime * result + (isFolder ? 1231 : 1237);
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (isFolder != other.isFolder)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		System.out.println("Should not use the parent class toString() method!");
		return "Node [isFolder=" + isFolder + ", title=" + title
				+ ", children=" + children + "]";
	}

}
