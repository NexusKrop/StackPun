package x.nexuskrop.stackpun.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import x.nexuskrop.stackpun.services.Items;

public class InventoryListener implements Listener {
	@EventHandler
	public void onInventoryMove(InventoryMoveItemEvent event) {
		if (!Items.isLegalQuick(event.getItem())) {
			event.setCancelled(true);
		}
	}
}
