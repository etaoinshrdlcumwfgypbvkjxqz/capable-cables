package $group__.client.ui.mvvm.core.views.paths;

import java.awt.geom.Point2D;

public interface IUINodePathResolver<T extends IUINode> {
	IUINodePath resolvePath(Point2D point, boolean virtual);

	boolean addVirtualElement(T element,
	                          T virtualElement);

	boolean removeVirtualElement(T element,
	                             T virtualElement);
}