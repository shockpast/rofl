# RoflanUtils
[![Java CI with Gradle](https://github.com/xoderton/RoflanUtils/actions/workflows/gradle.yml/badge.svg)](https://github.com/xoderton/RoflanUtils/actions/workflows/gradle.yml)

Social Plugin for Bukkit/Paper servers, that could improve player(s) experience on server.

## Legend
- `<>` – Required
- `()` – Optional
- `[]` – Sub Commands

## Commands

### `/invsee <player>`
Inventory See, simply opens someone's inventory, also allowing you to edit it in any way.

### `/item [rename] <...arg>`
`ItemEdit` plugin but minimal and simple version, with price of 1 experience level.

### `/mute <player> <duration> <reason>`
I don't recommend using this command as actual security layer, since it doesn't being saved between restarts, and it stored in server's memory instead.

### `/report [send/close]`
Small and minimalistic ticket system for administrators, same as `mute`, where it's being saved in memory, instead of somewhere in database.

### `/[vanish/cloak/v]`
Vanish or Cloak, simply hides you or selected player from everyone.

### `/tweak`
`tweak` command is similar to `fabric-carpet` mod, implementing some features from `carpet-extra` and `carpet-tis`