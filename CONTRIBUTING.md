# Contributing to Add Large Backpack Mod

Thank you for your interest in contributing! Here are some guidelines.

## How to Contribute

### Reporting Bugs

1. Check if the bug has already been reported in [Issues](../../issues)
2. Create a new issue with:
   - Clear title and description
   - Steps to reproduce
   - Expected vs actual behavior
   - Minecraft/Forge versions
   - Crash logs (if applicable)

### Suggesting Features

1. Check existing [Issues](../../issues) and [Pull Requests](../../pulls)
2. Create a new issue with:
   - Clear description of the feature
   - Use cases and benefits
   - Possible implementation ideas

### Pull Requests

1. Fork the repository
2. Create a new branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Test thoroughly
5. Commit your changes (`git commit -m 'Add amazing feature'`)
6. Push to the branch (`git push origin feature/amazing-feature`)
7. Open a Pull Request

### Code Style

- Follow existing code style
- Use meaningful variable names
- Add comments for complex logic
- Keep methods focused and concise

### Testing

- Test in both single-player and multiplayer
- Test with different backpack sizes
- Verify item persistence
- Check for memory leaks

## Development Setup

```bash
# Clone your fork
git clone https://github.com/sedielkue/AddLargeBackpack.git
cd AddLargeBackpack

# Build the mod
.\gradlew build

# Run client for testing
.\gradlew runClient
```

## Questions?

Feel free to open an issue or discussion if you have any questions!
