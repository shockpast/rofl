package me.shockpast.roflan;

import java.util.*;

public class SharedData {
    public final Set<UUID> vanished_players = new HashSet<>();

    public final Map<UUID, String> player_reports = new HashMap<>();
    public final Map<String, Map<UUID, String>> report_cases = new HashMap<>();
}
