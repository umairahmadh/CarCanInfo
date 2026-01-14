# CarCanInfo

A lightweight, fast native Android app for displaying CAN data on Android head units. Designed as a better alternative to the default Car Info apps, with support for CAN adapters like Raise and optimized for older vehicles such as Volkswagen pre-MQB platform.

## Features

- **Real-time CAN Data Display**: Monitor vehicle parameters in real-time
  - Speed (km/h or mph)
  - Engine RPM
  - Coolant Temperature
  - Fuel Level
  - Engine Load
  - Throttle Position
  - Battery Voltage
  - Intake Temperature

- **Optimized for Head Units**: 
  - Landscape orientation
  - Large, easy-to-read displays
  - Dark theme for night driving
  - Immersive fullscreen UI

- **Small & Fast**: 
  - Minimal APK size
  - Optimized for performance
  - Low memory footprint
  - Smooth 60fps updates

- **CAN Adapter Support**:
  - Compatible with standard OBD-II/CAN adapters
  - Support for Raise adapter
  - USB and Bluetooth connectivity
  - ELM327 protocol support

## Requirements

- Android 8.0 (API 26) or higher
- Android head unit or tablet
- CAN adapter (OBD-II/ELM327 compatible)
- Vehicle with CAN bus (OBD-II port)

## Installation

### From Source

1. Clone the repository:
```bash
git clone https://github.com/umairahmadh/CarCanInfo.git
cd CarCanInfo
```

2. Build the APK:
```bash
./gradlew assembleRelease
```

3. Install the APK on your Android device:
```bash
adb install app/build/outputs/apk/release/app-release.apk
```

## Usage

1. Connect your CAN adapter to the vehicle's OBD-II port
2. Connect the adapter to your Android device (USB or Bluetooth)
3. Launch the CarCanInfo app
4. The app will automatically connect to the adapter and display vehicle data

## Development

### Project Structure

```
CarCanInfo/
├── app/
│   ├── src/main/
│   │   ├── java/com/carcaninfo/
│   │   │   ├── MainActivity.kt          # Main UI activity
│   │   │   ├── can/
│   │   │   │   ├── CanAdapter.kt        # CAN adapter interface
│   │   │   │   ├── ObdCanAdapter.kt     # OBD-II implementation
│   │   │   │   └── SimulatedCanAdapter.kt # Testing adapter
│   │   │   └── model/
│   │   │       ├── VehicleData.kt       # Data models
│   │   │       └── ConnectionStatus.kt
│   │   └── res/
│   │       ├── layout/
│   │       │   └── activity_main.xml    # Main dashboard layout
│   │       └── values/
│   │           ├── strings.xml
│   │           ├── colors.xml
│   │           └── themes.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

### Building

```bash
# Debug build
./gradlew assembleDebug

# Release build (optimized, minified)
./gradlew assembleRelease

# Run on connected device
./gradlew installDebug
```

### Testing

The app includes a simulated CAN adapter for testing without hardware:
- Edit `MainActivity.kt` to switch between `SimulatedCanAdapter` and `ObdCanAdapter`
- The simulated adapter generates realistic vehicle data for UI testing

## Customization

### Adding Custom PIDs

Edit `ObdCanAdapter.kt` to add support for vehicle-specific PIDs:

```kotlin
private const val PID_CUSTOM = "22XXXX"  // Your custom PID
```

### UI Customization

- Modify `activity_main.xml` to change layout
- Edit `colors.xml` and `themes.xml` to customize appearance
- Update refresh rate in `MainActivity.kt` (default: 200ms)

## Supported Vehicles

While designed for Volkswagen pre-MQB platform vehicles, the app works with any vehicle that has:
- OBD-II port (standard since 1996 in US, 2001 in EU)
- CAN bus support
- Standard OBD-II PIDs

## Roadmap

- [ ] Bluetooth adapter support
- [ ] DTC (Diagnostic Trouble Code) reading and clearing
- [ ] Data logging and export
- [ ] Customizable dashboard layouts
- [ ] Additional gauge styles (analog, bar graphs)
- [ ] Multi-language support
- [ ] Settings screen for configuration

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues.

## License

This project is open source. See LICENSE file for details.

## Acknowledgments

- Built for the automotive enthusiast community
- Designed to work with Raise CAN adapters and similar hardware
- Optimized for Volkswagen pre-MQB vehicles but compatible with most OBD-II vehicles