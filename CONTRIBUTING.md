# Contributing to CarCanInfo

Thank you for your interest in contributing to CarCanInfo! This document provides guidelines for contributing to the project.

## How to Contribute

### Reporting Issues

If you find a bug or have a feature request:

1. Check if the issue already exists in the GitHub Issues
2. If not, create a new issue with:
   - Clear, descriptive title
   - Detailed description of the issue or feature
   - Steps to reproduce (for bugs)
   - Expected vs actual behavior
   - Device and Android version information
   - CAN adapter model (if applicable)

### Contributing Code

1. **Fork the Repository**
   ```bash
   git clone https://github.com/umairahmadh/CarCanInfo.git
   cd CarCanInfo
   ```

2. **Create a Feature Branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Make Your Changes**
   - Follow Kotlin coding conventions
   - Write clear, commented code
   - Keep changes focused and atomic
   - Test your changes thoroughly

4. **Commit Your Changes**
   ```bash
   git commit -m "Add: description of your changes"
   ```
   
   Use conventional commit messages:
   - `Add:` for new features
   - `Fix:` for bug fixes
   - `Update:` for updates to existing features
   - `Remove:` for removing features
   - `Docs:` for documentation changes

5. **Push to Your Fork**
   ```bash
   git push origin feature/your-feature-name
   ```

6. **Create a Pull Request**
   - Provide a clear description of the changes
   - Reference any related issues
   - Include screenshots for UI changes
   - Wait for review and address feedback

## Development Guidelines

### Code Style

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Keep functions small and focused
- Add comments for complex logic
- Use Kotlin idioms where appropriate

### Architecture

- Follow the existing project structure
- Keep concerns separated (UI, data, business logic)
- Use coroutines for asynchronous operations
- Implement interfaces for testability

### Testing

- Test with the simulated adapter before hardware
- Verify changes on actual Android devices
- Test on different screen sizes if UI changes
- Ensure backward compatibility

### UI/UX

- Maintain consistency with existing design
- Optimize for readability while driving
- Use large touch targets (48dp minimum)
- Test in both day and night conditions
- Keep animations smooth (60 FPS target)

## Project Priorities

1. **Performance**: Keep the app fast and responsive
2. **Simplicity**: Maintain a clean, intuitive interface
3. **Compatibility**: Support a wide range of devices and adapters
4. **Reliability**: Ensure stable connection and accurate data

## Areas for Contribution

### High Priority
- Real USB/Bluetooth adapter implementation
- DTC (error code) reading and clearing
- Unit tests for core functionality
- Documentation improvements

### Medium Priority
- Settings screen implementation
- Data logging functionality
- Additional gauge styles
- Internationalization (i18n)

### Nice to Have
- Additional vehicle-specific PIDs
- Theme customization options
- Trip computer features
- Performance metrics

## Testing

Before submitting a pull request:

1. Build the app successfully
   ```bash
   ./gradlew assembleDebug
   ```

2. Test on actual hardware if possible
3. Verify with simulated adapter
4. Check for memory leaks
5. Test on different Android versions

## Questions?

If you have questions about contributing:
- Open a discussion on GitHub
- Check existing issues and PRs
- Review the ARCHITECTURE.md file

## License

By contributing to CarCanInfo, you agree that your contributions will be licensed under the MIT License.
