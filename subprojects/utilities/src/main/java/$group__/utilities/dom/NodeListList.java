package $group__.utilities.dom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.AbstractList;

public class NodeListList
		extends AbstractList<Node> {
	protected final NodeList delegate;

	public NodeListList(NodeList delegate) { this.delegate = delegate; }

	@Override
	public Node get(int index) { return getDelegate().item(index); }

	protected NodeList getDelegate() { return delegate; }

	@Override
	public int size() { return getDelegate().getLength(); }
}
