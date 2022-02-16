package Content;

import java.util.EventListener;

public interface EchangeListener extends EventListener {

	void onEventCreated(EchangeEvent ev);
}
