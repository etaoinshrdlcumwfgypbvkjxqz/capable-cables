package $group__.client.ui.mvvm.views.events.bus;

import $group__.client.ui.events.bus.EventUI;
import $group__.client.ui.mvvm.core.views.components.IUIComponent;
import $group__.utilities.events.EnumEventHookStage;

public abstract class EventUIComponent extends EventUI {
	protected final IUIComponent component;

	protected EventUIComponent(EnumEventHookStage stage, IUIComponent component) {
		super(stage);
		this.component = component;
	}

	public IUIComponent getComponent() { return component; }

}