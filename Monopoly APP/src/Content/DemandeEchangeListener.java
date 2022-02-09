package Content;

import java.util.EventListener;

public interface DemandeEchangeListener extends EventListener   {

	void onEventCreated(EchangeEvent ev);
}