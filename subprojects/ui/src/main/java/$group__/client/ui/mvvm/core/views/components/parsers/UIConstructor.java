package $group__.client.ui.mvvm.core.views.components.parsers;

import $group__.client.ui.core.structures.shapes.descriptors.IShapeDescriptor;

import java.lang.annotation.*;
import java.lang.invoke.MethodType;
import java.util.Map;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface UIConstructor {
	ConstructorType type();

	enum ConstructorType {
		SHAPE_DESCRIPTOR__MAPPING(MethodType.methodType(void.class, IShapeDescriptor.class, Map.class)),
		;

		protected final MethodType methodType;

		ConstructorType(MethodType methodType) { this.methodType = methodType; }

		public MethodType getMethodType() { return methodType; }
	}
}
