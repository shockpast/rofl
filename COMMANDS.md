# Commands

## fly

Flying like in Creative, but without urge to enter specified gamemode.

### Arguments
- `None`

## invsee

Allows to view player's inventory *(currently without armor support)* and modify it in anyway.

### Arguments
- `Player` player

## item

Adds ability to rename items with colors and other neat stuff and some more features in it.

### Arguments
- `sub/rename` rename item name with colors and other formatting from [MiniMessage](https://docs.advntr.dev/minimessage/format.html).
  - `String` name

- `sub/sign` just signs item with your name and date when it was executed, nothing special.
  - `None`

## pm

Private Messaging like in default Minecraft, with customizable message formats in [`config.yml`](https://github.com/xoderton/RoflanUtils/blob/main/src/main/resources/config.yml)

### Aliases
- Minecraft Commands were replaced with custom RoflanUtils direct messaging "service"

### Arguments
- `Player` player
- `String` message that will be sent to player

## reply

Replies to last sent message to you, or player that you were talking through private messages, expires after 30 seconds by default.

### Arguments
- `String` message that will be sent to player

## tweak

[fabric-carpet](https://github.com/gnembon/fabric-carpet) features but transfered to Paper, because there was no alternatives, and cubicmetre was angry about some stuff :)

### Arguments
- `String` name of the tweak, read [`config.yml`](https://github.com/xoderton/RoflanUtils/blob/main/src/main/resources/config.yml) for more info
- `Any` mostly `true/false`, but depends on tweak

## vanish

`vanish` simply hides you from other players on server.

### Arguments
- `None`