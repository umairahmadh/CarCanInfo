# Setup Guide for CarCanInfo

This guide will help you set up and run CarCanInfo on your Android head unit or device.

## Prerequisites

### Hardware Requirements
- Android head unit or tablet running Android 8.0 or higher
- OBD-II CAN adapter (ELM327, Raise, or compatible)
- Vehicle with OBD-II port (most vehicles 1996+ in US, 2001+ in EU)
- USB cable or Bluetooth connection for adapter

### Software Requirements
- Android Studio (for development)
- ADB (Android Debug Bridge) for installation
- Java Development Kit (JDK) 8 or higher

## Installation Methods

### Method 1: Install Pre-built APK (Recommended for Users)

1. Download the latest APK from the releases page
2. Enable "Unknown Sources" on your Android device:
   - Go to Settings → Security
   - Enable "Install from Unknown Sources" or "Allow from this source"
3. Transfer the APK to your device
4. Open the APK file and follow installation prompts

### Method 2: Install via ADB

1. Enable USB debugging on your Android device:
   - Go to Settings → About → Tap "Build Number" 7 times
   - Go to Settings → Developer Options → Enable USB Debugging
2. Connect your device to computer via USB
3. Install using ADB:
   ```bash
   adb install app-release.apk
   ```

### Method 3: Build from Source

1. Clone the repository:
   ```bash
   git clone https://github.com/umairahmadh/CarCanInfo.git
   cd CarCanInfo
   ```

2. Open in Android Studio:
   - File → Open → Select the CarCanInfo folder
   - Wait for Gradle sync to complete

3. Build the APK:
   ```bash
   ./gradlew assembleRelease
   ```
   The APK will be in `app/build/outputs/apk/release/`

4. Install on device:
   ```bash
   adb install app/build/outputs/apk/release/app-release.apk
   ```

## CAN Adapter Setup

### USB Adapter Setup

1. Connect the OBD-II adapter to your vehicle's OBD-II port
   - Usually located under the dashboard, near the steering wheel
   - Look for a 16-pin trapezoid connector

2. Connect the adapter to your Android device via USB
   - Use an OTG (On-The-Go) cable if needed
   - The device should recognize the adapter

3. Launch CarCanInfo
   - The app will automatically detect the USB adapter
   - Grant USB permission when prompted

### Bluetooth Adapter Setup

1. Pair the Bluetooth adapter with your Android device:
   - Turn on Bluetooth on your Android device
   - Put the adapter in pairing mode (usually automatic when plugged in)
   - Go to Settings → Bluetooth → Scan for devices
   - Select your adapter (usually named "OBD-II" or similar)
   - Pair using PIN (often "1234" or "0000")

2. Launch CarCanInfo
   - The app will search for paired adapters
   - Select your adapter from the list
   - Grant Bluetooth permissions when prompted

## First Time Setup

### 1. Launch the App

Start the CarCanInfo app from your launcher or app drawer.

### 2. Initial Connection

- The app will show "Connecting..." status
- Wait for the adapter to initialize (5-10 seconds)
- Status will change to "Connected" when ready
- If connection fails, check adapter and vehicle connection

### 3. Verify Data Display

You should see real-time data updating:
- Speed
- RPM
- Coolant Temperature
- Fuel Level
- Engine Load
- Throttle Position
- Battery Voltage (in status bar)

### 4. Start Driving

- Data updates automatically every 200ms
- All displays update in real-time
- No interaction needed while driving

## Troubleshooting

### App won't connect to adapter

**Check:**
- Adapter is properly plugged into OBD-II port
- Vehicle ignition is ON (key in ACC or ON position)
- USB/Bluetooth connection is active
- Adapter LED is blinking (indicating power and communication)

**Try:**
- Restart the app
- Disconnect and reconnect the adapter
- Turn vehicle off and on again
- Check adapter compatibility

### Data shows zeros or dashes

**Possible causes:**
- Vehicle is not running (start the engine)
- Adapter is not properly initialized
- Vehicle uses non-standard PIDs
- Communication error with ECU

**Solutions:**
- Start the engine and wait 10 seconds
- Restart the app
- Check adapter LED status
- Try a different adapter if issue persists

### App crashes on startup

**Try:**
- Clear app data: Settings → Apps → CarCanInfo → Clear Data
- Reinstall the app
- Check Android version compatibility (8.0+)
- Report issue on GitHub with device details

### Slow or laggy updates

**Check:**
- Adapter connection quality
- Device performance (close other apps)
- USB cable quality (for USB adapters)
- Bluetooth signal strength

**Optimize:**
- Close background apps
- Use a quality USB cable
- Keep adapter close to device (Bluetooth)

### Permission issues

**Grant required permissions:**
- Location: Required for Bluetooth scanning
- Storage: For data logging (future feature)
- USB: Required for USB adapter access

Go to Settings → Apps → CarCanInfo → Permissions

## Tips for Best Experience

### For Head Unit Installation

1. **Mounting**: Ensure device is securely mounted for easy viewing
2. **Cable Management**: Route cables safely away from pedals
3. **Power**: Connect to switched power source (turns off with ignition)
4. **Screen**: Adjust brightness for day/night conditions

### For Optimal Performance

1. **Start sequence**: Start vehicle, then launch app
2. **Warmup**: Allow 10-15 seconds for initial connection
3. **Placement**: Keep adapter secure in OBD-II port
4. **Updates**: Keep app updated for bug fixes and features

### Safety First

⚠️ **Important Safety Notes:**
- Never interact with the app while driving
- Set up and test before driving
- Ensure device is securely mounted
- Don't let cables interfere with pedals or steering
- Pull over safely if you need to check error codes

## Compatibility Notes

### Tested Adapters
- ELM327 v1.5 and later
- Raise CAN adapter
- Veepeak OBD-II adapters
- OBDLink adapters

### Tested Vehicles
- Volkswagen (2005-2015 pre-MQB)
- Audi (2005-2015)
- Most OBD-II compliant vehicles

### Known Issues
- Some vehicles may not support all PIDs
- Hybrid/Electric vehicles may show limited data
- Older vehicles (<2005) may have limited CAN support

## Getting Help

If you need assistance:

1. Check this setup guide thoroughly
2. Review the FAQ section in README.md
3. Search existing GitHub issues
4. Create a new issue with:
   - Device model and Android version
   - Adapter model
   - Vehicle make, model, and year
   - Screenshot of issue
   - Steps to reproduce

## Updates

Check for updates regularly:
- GitHub releases page
- Enable auto-update if installed from app store
- Follow project for announcements

## Next Steps

Once everything is working:
- Familiarize yourself with the display layout
- Note typical values for your vehicle
- Report any issues or suggestions
- Consider contributing to the project!

Enjoy using CarCanInfo! 🚗📊
