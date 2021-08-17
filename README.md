# neswarden

### Description

Custom plugin extending some server features that could not be found in any other existing project

#### Features
##### 1. Custom broadcast commands  
- `/nbr [text...]` - broadcasts raw text to all players w/ color formatting 
- `/npbr [text...]` - broadcasts raw text to all players w/ color formatting and prefix defined in config
- `/npmsg <player> [text...]` - send raw text to selected player w/ color formatting  
Permission: `neswarden.broadcast`  
Config prefix key: `broadcast-prefix:`
##### 2. Per world command override (currently attached commands: spawn)
Selected commands are overriden by executing aliases defined in config  
Example: Calling `/spawn` in world_nether looks for config key `alias-spawn-world_nether` and executes its value  
If the value is not found, plugin fallbacks to executing value under key `alias-spawn`  
Permission: `neswarden.alias`
##### 3. Joining multiple commands in one line
Useful for plugins that allow only one command execution per "some" events  
Usage: `/and <command1> [&& command2] [&& command3] ...`  
Example: `/and say Hello && say World`  
Permission: `neswarden.logic`
##### 4. Custom commands (micro commands/µc/uc)
Similiar to `commands.yml` but requires permission for each µc  
Usage: `/uc <command> [args...]`  
Example: User calls `/uc abc def`; plugin searches for config entry named `uc-abc` and executes its contents  
µc entry is a list containing commands executed sequentially.  
Each list entry can contain variables that will be replaced during execution:  
- `%0%` to `%args.len - 1%` - argument passed to the main `/uc` command
- `%player%` - name of player calling the command
- `%world%` - name of the world
- `%r%` - random int in range 0-9
- `%c%` - random char in range a-z  
If command starts with `@`, it will be executed as console sender.
##### 5. Executing commands on player first appearance on given world
File `joins.yml` contains players for whom the commands have already been executed  
If player visits a world for the first time, plugin will execute commands saved in config  
Config key structure: `action-joinw-worldname`  
##### 6. Custom timeout for any command
Usage: `/to <player> <time> <can move: 1|0|true|false> <command...>`  
Creates timeout **for a player** (the command is meant to be executed by console or µc mentioned above) and executes given command after given time.  
The action cancels if the `can move` flag is set to false and player moves.  
The action can be cancelled manually using `/cancel`  
Permissions:
- `/to`: `neswarden.timeout`
- `/cancel`: `neswarden.cancel`
- omitting timeouts completely: `neswarden.notimeout`
##### 7. Disabling/hiding commands
Useful for hiding commands that are not useful to a non-admin user, like `/version` command  
Adding entry to list `disable-command` will remove that command from tab autocomplete and trigger `disable-response` message on execution  
Permission for omitting this feature: `neswarden.disabled`  
  
### Build

Project is compiled in VS Code using Java extension pack  
Dependencies:  
`spigot.jar` in `/lib` folder  
