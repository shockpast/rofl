package me.shockpast.rofl;

import java.util.*;

public class SharedData {
    public final Set<UUID> vanished_players = new HashSet<>();
    public final Map<UUID, Long> muted_players = new HashMap<>();

    public final Map<UUID, String> player_reports = new HashMap<>();
    public final Map<String, Map<UUID, String>> report_cases = new HashMap<>();
}
