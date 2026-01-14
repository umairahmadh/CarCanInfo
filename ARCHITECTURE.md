# CarCanInfo App Architecture

## Overview
CarCanInfo is a lightweight Android application designed to display real-time CAN bus data on Android head units. The app provides a clean, automotive-optimized interface for monitoring vehicle parameters.

## Key Features

### 1. Real-Time Data Display
The main dashboard displays 6 key vehicle parameters in an easy-to-read grid layout:
- **Speed**: Current vehicle speed in km/h or mph
- **RPM**: Engine revolutions per minute
- **Coolant Temperature**: Engine coolant temperature in °C or °F
- **Fuel Level**: Remaining fuel percentage
- **Engine Load**: Current engine load percentage
- **Throttle Position**: Accelerator pedal position percentage

### 2. Status Bar
- Connection status indicator (Disconnected/Connecting/Connected/Error)
- Real-time battery voltage display
- Visual connection icon

### 3. UI Design Principles
- **Dark Theme**: Optimized for night driving with reduced eye strain
- **Large Fonts**: Easy to read while driving with a quick glance
- **Landscape Orientation**: Optimized for head unit displays
- **Immersive Mode**: Fullscreen display without system bars
- **Material Design**: Modern, clean interface with card-based layout

## Technical Architecture

### Data Layer
```
CanAdapter Interface
├── ObdCanAdapter (Production)
│   ├── ELM327 Protocol
│   ├── Standard OBD-II PIDs
│   └── USB/Bluetooth Communication
└── SimulatedCanAdapter (Testing)
    └── Generates realistic test data
```

### Model Layer
```
VehicleData
├── speed: Int
├── rpm: Int
├── coolantTemp: Int
├── fuelLevel: Int
├── engineLoad: Int
├── throttlePosition: Int
├── batteryVoltage: Float
└── intakeTemp: Int

ConnectionStatus (Enum)
├── DISCONNECTED
├── CONNECTING
├── CONNECTED
└── ERROR
```

### UI Layer
```
MainActivity
├── Connection Management
├── Data Updates (200ms interval)
├── UI Updates (Main Thread)
└── Lifecycle Management
```

## OBD-II PIDs Supported

| Parameter | PID | Formula | Unit |
|-----------|-----|---------|------|
| Engine RPM | 010C | ((A*256)+B)/4 | rpm |
| Vehicle Speed | 010D | A | km/h |
| Coolant Temp | 0105 | A-40 | °C |
| Fuel Level | 012F | A*100/255 | % |
| Engine Load | 0104 | A*100/255 | % |
| Throttle Position | 0111 | A*100/255 | % |
| Intake Temp | 010F | A-40 | °C |
| Battery Voltage | ATRV | String | V |

## Compatibility

### Supported Android Versions
- Minimum: Android 8.0 (API 26)
- Target: Android 14 (API 34)
- Optimized for: Android Auto head units

### Supported CAN Adapters
- ELM327-based adapters (USB/Bluetooth)
- Raise CAN adapter
- Generic OBD-II adapters with CAN support

### Supported Vehicles
- Any vehicle with OBD-II port (1996+ US, 2001+ EU)
- Optimized for: Volkswagen pre-MQB platform
- Compatible with: Most modern vehicles with CAN bus

## Performance Specifications

- **APK Size**: < 5 MB (optimized with ProGuard)
- **Memory Usage**: < 50 MB RAM
- **Update Rate**: 5 Hz (200ms refresh interval)
- **UI Frame Rate**: 60 FPS target
- **Startup Time**: < 2 seconds

## Build Configuration

### Debug Build
- No optimization
- Full logging enabled
- Simulated adapter active

### Release Build
- ProGuard enabled
- Code shrinking and obfuscation
- Resource shrinking
- Optimized for size and speed

## Future Enhancements

1. **Data Logging**: Record trip data for analysis
2. **DTC Support**: Read and clear diagnostic trouble codes
3. **Customizable Dashboard**: User-configurable data displays
4. **Additional Gauges**: Analog gauge styles, graphs
5. **Settings Screen**: Unit preferences, theme options
6. **Multi-language**: i18n support
7. **Data Export**: CSV/JSON export for logged data
8. **Advanced PIDs**: Vehicle-specific extended PIDs

## Security & Permissions

The app requires the following permissions:
- **BLUETOOTH/BLUETOOTH_ADMIN**: For Bluetooth adapter communication
- **BLUETOOTH_CONNECT/BLUETOOTH_SCAN**: Android 12+ Bluetooth
- **ACCESS_FINE_LOCATION**: Required for Bluetooth scanning
- **USB_PERMISSION**: For USB adapter communication
- **FOREGROUND_SERVICE**: For continuous monitoring

All permissions are used solely for CAN adapter communication and data display.
