package com.dsh105.vortex.listeners;

import com.dsh105.vortex.VortexPlugin;
import com.dsh105.vortex.environment.volcano.Volcano;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.metadata.FixedMetadataValue;


public class FallingBlockDespawnListener implements Listener {

	@EventHandler
	public void onBlockChange(EntityChangeBlockEvent event) {
		if (event.getEntity() instanceof FallingBlock) {
			FallingBlock fb = (FallingBlock) event.getEntity();
			if (fb.hasMetadata("tornado")) {
                event.getBlock().setMetadata("tornado", new FixedMetadataValue(VortexPlugin.getInstance(), "true"));
            }

            for (int i = 0; i <= Volcano.volcanoes; i++) {
                if (fb.hasMetadata("volcano-" + i)) {
                    event.getBlock().setMetadata("volcano-" + i, new FixedMetadataValue(VortexPlugin.getInstance(), "true"));
                }
            }

            if (fb.hasMetadata("volcanolava")) {

            }
		}
	}
}