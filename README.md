# RoflanUtils

Plugin that adds commands and tweaks to your server, that might improve player's experience on your server, however there are some commands that are could be useful for redstone builders, learn more about it down below.

I should make an important point, that `report` command doesn't store all reports in some kind of database (sqlite, mysql) but **in-memory**, since this command was just an experiement, that might be removed from this plugin.

# Commands

## invsee

### Description

Old-Fashioned Invsee from Essentials or EssentialsX, currently *(at the time of writing)* doesn't have armor support, and have some... strange bugs with inventories causing some items to appear in wrong slot when being taken or changed.

### Arguments

- `player` player that will be examined.

## item

### Description

Adds `rename` command to... rename items with MiniMessage format feature.

## report

### Description

It was added for some server, and this plugin at all was private at first point, but I just decided to publish it, and this command is some kind of tickets that are being saved **in-memory of server** and not in database, so it will be lost upon server's restart, so this is pretty much useless for actual servers, this might be improved in future, or removed at all, but currently this will be like that.

It will send an message to all moderators, that someone got reported. If there moderator joined later and report case was still not closed, it will be sent to them upon connection with all details.

### Arguments

- `send`
  - `player` player that will be reported for some reason
  - `reason` why {player} is bad and should be banned
- `close`
  - `id` id of report that must be closed *(moderators retrieves all reports upon connection to server)*

## tweak

### Description

It's something similar to `fabric-carpet` and his addons, actually all of these tweaks were taken from them, but just made in Paper, there is not alot of them, but some useful for admins or redstoners are already there.

### Arguments

- `tweak` name of the tweak that will be changed
- `value` value that will be applied to tweak, look in `config.yml` for more information

## vanish

### Description

Absolutely vanishes you or some selected player from server, like they are not on the server at all.

### Arguments

- `player` player that will be in vanish, it's optional argument