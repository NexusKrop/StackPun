package x.nexuskrop.stackpun.services;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public final class Items {
	private Items() {}
	
	/**
	 * Perform a fast check to determine whether the item specified is legitimate.
	 * @param stack The item to check.
	 * @return {@code true} if the specified item is basically legitimate; otherwise, {@code false}.
	 */
	public static boolean isLegalQuick(ItemStack stack) {
		if (stack.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES)
				|| stack.hasItemFlag(ItemFlag.HIDE_ENCHANTS)
				|| stack.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) {
			return false;
		}
		
		var meta = stack.getItemMeta();
		
		if (meta.hasAttributeModifiers()) {
			return false;
		}
		
		if (meta.isUnbreakable()) {
			return false;
		}
		
		return true;
	}
}
