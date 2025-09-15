/*
 * GNU General Public License v3
 *
 * PaperTweaks, a performant replacement for the VanillaTweaks datapacks.
 *
 * Copyright (C) 2021-2025 Machine_Maker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package me.machinemaker.papertweaks.modules.experimental.confetticreepers;

import com.google.inject.Inject;
import java.util.concurrent.ThreadLocalRandom;
import me.machinemaker.papertweaks.modules.ModuleListener;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.meta.FireworkMeta;

public class ExplosionListener implements ModuleListener {

    private static final FireworkEffect COLORFUL_EFFECT = FireworkEffect.builder()
        .flicker(false)
        .trail(false)
        .with(FireworkEffect.Type.BURST)
        .withColor(
            Color.fromRGB(11743532),
            Color.fromRGB(15435844),
            Color.fromRGB(14602026),
            Color.fromRGB(4312372),
            Color.fromRGB(6719955),
            Color.fromRGB(8073150),
            Color.fromRGB(14188952)
        ).build();

    private final Config config;

    @Inject
    public ExplosionListener(final Config config) {
        this.config = config;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExplosionPrime(final ExplosionPrimeEvent event) {
        if (event.getEntityType() != EntityType.CREEPER) return;
        if (this.config.allowChargedCreepers) {
            Creeper creeper = (Creeper) event.getEntity();
            if (creeper.isPowered()) return;
        }
        if (ThreadLocalRandom.current().nextDouble() < this.config.chance) {
            event.setFire(false);
            event.setRadius(0);
            event.getEntity().getWorld().spawn(event.getEntity().getLocation(), Firework.class, firework -> {
                final FireworkMeta fireworkMeta = firework.getFireworkMeta();
                fireworkMeta.setPower(0);
                fireworkMeta.addEffect(COLORFUL_EFFECT);
                firework.setFireworkMeta(fireworkMeta);
                firework.detonate();
            });
        }
    }
}
