# Add Large Backpack Mod

A Minecraft Forge mod that adds a customizable large backpack with scrollable inventory.

![Minecraft Version](https://img.shields.io/badge/Minecraft-1.20.1-brightgreen)
![Forge Version](https://img.shields.io/badge/Forge-47.3.0-orange)
![License](https://img.shields.io/badge/License-MIT-blue)

## 🌟 Features

- 🎒 **Large Backpack** - Expandable storage with configurable rows (default: 10 rows / 90 slots)
- 📜 **Scrollable GUI** - Smooth scrollbar interface for accessing all items
- ⌨️ **Key Binding** - Press `B` key to quickly open your backpack
- 🔧 **Configurable** - Adjust backpack size via config file (1-20 rows)
- 🌐 **Multilingual** - Supports English and Japanese
- 💾 **NBT Storage** - Items persist in backpack data
- 🔄 **Client-Server Sync** - Smooth multiplayer experience

## 📥 Installation

1. Download [Forge 1.20.1 (47.3.0)](https://files.minecraftforge.net/)
2. Download the latest release from [Releases](../../releases)
3. Place the `.jar` file in your `mods` folder
4. Launch Minecraft with Forge profile

## 🛠️ Crafting Recipe

```
[Leather] [String]  [Leather]
[Leather] [Chest]   [Leather]
[Leather] [Leather] [Leather]
```

**Materials:**
- 7x Leather
- 1x String
- 1x Chest

## 🎮 Usage

### Opening the Backpack
- **Right-click** while holding the backpack
- Press **B key** (configurable) with backpack in inventory
  - Automatically opens the leftmost backpack (hotbar → main inventory)

### Using the Backpack
- **Scroll** using mouse wheel or drag the scrollbar
- **Click** to pick up/place items
- **Shift+Click** to quick-transfer items
- Access up to **90 slots** (default: 10 rows × 9 columns)

## ⚙️ Configuration

Config file location: `config/addlargebackpack-common.toml`

```toml
[backpack]
    # Number of rows in the backpack (1-20)
    # Default: 10
    backpackRows = 10
```

Change `backpackRows` to adjust the backpack size (1-20 rows).

## ⌨️ Key Bindings

**Default:** `B` key opens backpack

To change:
1. Options → Controls → Key Binds
2. Find "Large Backpack" category
3. Click "Open Backpack" and press desired key

## 🔧 Development

### Build from Source

```bash
# Clone the repository
git clone https://github.com/sedielkue/AddLargeBackpack.git
cd AddLargeBackpack

# Build the mod
.\gradlew build

# Output: build/libs/addlargebackpack-1.0.jar
```

### Run in Development

```bash
# Run client
.\gradlew runClient

# Run server
.\gradlew runServer
```

## 📁 Project Structure

```
AddLargeBackpack/
├── src/main/java/com/addlargebackpack/
│   ├── client/              # Client-side code
│   │   ├── gui/            # GUI screens
│   │   ├── KeyBindings.java
│   │   ├── KeyInputHandler.java
│   │   └── ClientSetup.java
│   ├── config/             # Configuration
│   ├── inventory/          # Inventory management
│   ├── items/              # Backpack item
│   ├── menu/               # Container menus
│   └── network/            # Network packets
├── src/main/resources/
│   ├── assets/             # Textures, models, lang files
│   └── data/               # Recipes, loot tables
└── build.gradle
```

## 🔍 Technical Details

### Features
- **Client-Server Synchronization** - Scroll position synced across network
- **Dynamic Slot Mapping** - Efficient slot indexing for scrollable inventory
- **NBT Storage** - Items persist in backpack data
- **Smart Slot Restriction** - Prevents placing backpack inside itself
- **Left-Priority Search** - Keybind opens leftmost backpack first

### Requirements
- Minecraft: `1.20.1`
- Forge: `47.3.0` or higher
- Java: `17` or higher

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Credits

- **Developer:** sedielkue
- **Texture:** Custom backpack texture
- **Framework:** Minecraft Forge

## 🐛 Support

- **Issues:** [GitHub Issues](../../issues)
- **Wiki:** [Documentation](../../wiki)

## 📝 Changelog

### Version 1.0.0 (Initial Release)
- ✅ Configurable large backpack (up to 20 rows / 180 slots)
- ✅ Scrollable GUI with smooth scrollbar
- ✅ Key binding support (B key, configurable)
- ✅ Crafting recipe (leather + string + chest)
- ✅ NBT item storage with persistence
- ✅ Client-server synchronization
- ✅ Left-priority backpack search
- ✅ English and Japanese translations
- ✅ Custom backpack texture

---

**Enjoy your adventures with extra storage! 🎒✨**