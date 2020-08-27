package $group__.ui.core.mvvm.binding;

import $group__.utilities.interfaces.IHasGenericClass;
import $group__.utilities.interfaces.IValue;
import io.reactivex.rxjava3.core.ObservableSource;

public interface IObservableField<T> extends IField<T>, IHasGenericClass<T> {
	ObservableSource<? extends IValue<T>> getNotifier();

}
