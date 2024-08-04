package me.shockpast.roflan;

import java.util.*;

public class SharedData {
    public final Set<UUID> vanished_players = new HashSet<>();

    public final Map<UUID, Long> reply_memory = new HashMap<>();
    public final Map<UUID, UUID> reply_data = new HashMap<>();
}