# LifeSteal Plugin

A Minecraft plugin that implements a life steal mechanic where players can steal hearts from each other.

## Features
- **Heart Theft**: Kill players to steal their max health
- **Health Limits**: Min 1 heart (2 HP), Max 20 hearts (40 HP)
- **Elimination**: Players die at 1 heart get banned
- **Heart Item**: Craftable heart item that adds max health
- **Health Conversion**: Convert current health to heart items

## Commands
- `/lifesteal withdraw <amount>` - Convert health to Heart items

## Installation
1. Build the plugin using Maven: `mvn clean package`
2. Copy the generated JAR from `target/` to your server's `plugins/` folder
3. Restart your server

## Crafting Recipe
- Shape: GGG / GNG / GGG
- G = Gold Block
- N = Nether Star