package $group__.ui.core.mvvm.views.events;

import java.util.Optional;

public interface IUIEventFocus extends IUIEvent {
	Optional<? extends IUIEventTarget> getRelatedTarget();
}
