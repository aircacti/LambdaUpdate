# LambdaUpdate

LambdaUpdate is a Paper Minecraft plugin that automates the process of updating plugins on your Minecraft server. It periodically checks for new `.jar` files in a specified directory and, if conditions are met, moves them to main plugins folder and restarts the server to apply the updates.

## Features
- Automatically detects new plugin updates from a designated folder.
- Shuts down the server safely when an update is available.
- Kicks players with a custom message before shutdown.
- Backs up outdated plugins before replacing them.
- Configurable update check interval and player threshold.

## Installation
1. Download the latest release of `LambdaUpdate.jar`.
2. Place it in your server's `plugins` directory.
3. Start the server to generate the `config.yml` file.
4. Modify `config.yml` to fit your preferences.
5. Restart the server to apply changes.

## Configuration (`config.yml`)
```yaml
loop-interval-ticks #Interval for checking updates (1 minute = 1200 ticks)
players-online # Maximum number of online players before updating
kick-message # Message shown to kicked players
updates-dir-name # Folder where new plugin versions are stored
create-backups # Whether to back up replaced plugins
backups-dir-name # Folder where backups are stored
```

## How It Works
1. The plugin checks for new `.jar` files in the `updates` directory at the configured interval.
2. If updates are found and the number of online players is below the threshold, it proceeds with the update.
3. If enabled, outdated plugins are backed up from main plugins folder to the `backups` directory.
4. Players are kicked with a custom message (if enabled).
5. The new version of the plugin is moved to the main plugin folder.
6. The server shuts down, allowing the updated plugins to be loaded on the next startup.

## Commands
(Currently, the plugin operates automatically and does not provide user commands.)

## Permissions
(No permissions are required; only server operators can modify the plugin's behavior via `config.yml`.)

## Support
For issues, feature requests, or contributions, please visit the [GitHub repository](https://github.com/lambda-professional/LambdaUpdate).

## License
This project is licensed under the MIT License.
