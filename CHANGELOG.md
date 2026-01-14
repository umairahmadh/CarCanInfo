# Changelog

All notable changes to the CarCanInfo project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned Features
- Real USB/Bluetooth adapter implementation
- DTC (Diagnostic Trouble Code) reading and clearing
- Settings screen with unit preferences
- Data logging functionality
- Trip computer features
- Additional gauge styles

## [1.0.0] - 2024-01-14

### Added
- Initial release of CarCanInfo
- Real-time CAN data monitoring dashboard
- Support for 8 key vehicle parameters:
  - Vehicle Speed (km/h)
  - Engine RPM
  - Coolant Temperature (°C)
  - Fuel Level (%)
  - Engine Load (%)
  - Throttle Position (%)
  - Battery Voltage (V)
  - Intake Temperature (°C)
- Dark theme optimized for automotive displays
- Landscape orientation for head unit compatibility
- Status bar with connection status and battery voltage
- Modular CAN adapter architecture
- OBD-II/ELM327 protocol support (structure)
- Simulated adapter for testing and development
- Material Design 3 UI components
- Immersive fullscreen mode
- ProGuard optimization for minimal APK size
- USB device filter configuration
- Bluetooth and USB permission handling
- 200ms refresh rate for smooth updates
- Comprehensive documentation:
  - README with usage instructions
  - ARCHITECTURE documentation
  - SETUP guide
  - CONTRIBUTING guidelines
  - LICENSE (MIT)

### Technical Details
- Built with Kotlin and Android SDK 34
- Minimum Android version: 8.0 (API 26)
- Target Android version: 14 (API 34)
- Coroutine-based asynchronous operations
- ViewBinding for efficient view access
- Material Design components
- Optimized for automotive head units

### Known Limitations
- Real adapter communication not yet implemented (uses simulated data)
- No settings screen for unit preferences
- No DTC reading capability
- No data logging
- Limited to standard OBD-II PIDs

## Release Notes Format

### Added
For new features.

### Changed
For changes in existing functionality.

### Deprecated
For soon-to-be removed features.

### Removed
For now removed features.

### Fixed
For any bug fixes.

### Security
For vulnerability fixes.
