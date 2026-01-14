# CarCanInfo - App Preview

## Dashboard Preview

The CarCanInfo app provides a clean, automotive-optimized dashboard for monitoring real-time CAN bus data.

### Main Screen Layout

```
╔═══════════════════════════════════════════════════════════════════════════╗
║  🚗 Connected                                              12.8 V          ║
╠═══════════════════════════════════════════════════════════════════════════╣
║                                                                           ║
║    ╔══════════════════╗    ╔══════════════════╗    ╔══════════════════╗  ║
║    ║     SPEED        ║    ║       RPM        ║    ║  COOLANT TEMP    ║  ║
║    ║                  ║    ║                  ║    ║                  ║  ║
║    ║       65         ║    ║      2850        ║    ║       92         ║  ║
║    ║      km/h        ║    ║       RPM        ║    ║       °C         ║  ║
║    ║                  ║    ║                  ║    ║                  ║  ║
║    ╚══════════════════╝    ╚══════════════════╝    ╚══════════════════╝  ║
║                                                                           ║
║    ╔══════════════════╗    ╔══════════════════╗    ╔══════════════════╗  ║
║    ║   FUEL LEVEL     ║    ║  ENGINE LOAD     ║    ║    THROTTLE      ║  ║
║    ║                  ║    ║                  ║    ║                  ║  ║
║    ║       75         ║    ║       45         ║    ║       38         ║  ║
║    ║        %         ║    ║        %         ║    ║        %         ║  ║
║    ║                  ║    ║                  ║    ║                  ║  ║
║    ╚══════════════════╝    ╚══════════════════╝    ╚══════════════════╝  ║
║                                                                           ║
╚═══════════════════════════════════════════════════════════════════════════╝
```

### Color Theme (Dark Mode)

```
┌─────────────────────────────────────────────────────────────┐
│ Background: Dark Gray (#121212)                             │
│ Cards: Slightly Lighter Gray (#1E1E1E)                      │
│ Border: Blue (#2196F3)                                      │
│ Text (Primary): White (#FFFFFF)                             │
│ Text (Secondary): Light Gray (#B3B3B3)                      │
└─────────────────────────────────────────────────────────────┘
```

### Key Features Illustrated

1. **Status Bar** (Top)
   - Connection indicator with icon
   - Connection status text
   - Real-time battery voltage

2. **Data Grid** (Main Area)
   - 3 columns × 2 rows layout
   - Large, readable values (36sp)
   - Clear labels and units
   - Consistent spacing

3. **Update Rate**
   - Refreshes every 200ms (5 times per second)
   - Smooth, real-time updates
   - No lag or delay

### Screen States

#### Disconnected State
```
╔═══════════════════════════════════════════════╗
║  🚗 Disconnected                       -- V   ║
╠═══════════════════════════════════════════════╣
║                                               ║
║  ╔═══════════╗  ╔═══════════╗  ╔═══════════╗ ║
║  ║  SPEED    ║  ║    RPM    ║  ║  COOLANT  ║ ║
║  ║     0     ║  ║     0     ║  ║    --     ║ ║
║  ╚═══════════╝  ╚═══════════╝  ╚═══════════╝ ║
║                                               ║
╚═══════════════════════════════════════════════╝
```

#### Connecting State
```
╔═══════════════════════════════════════════════╗
║  🚗 Connecting...                      -- V   ║
╠═══════════════════════════════════════════════╣
║                                               ║
║         Initializing adapter...               ║
║                                               ║
╚═══════════════════════════════════════════════╝
```

#### Connected State (Active)
```
╔═══════════════════════════════════════════════╗
║  🚗 Connected                         12.8 V  ║
╠═══════════════════════════════════════════════╣
║        [All data displaying normally]         ║
╚═══════════════════════════════════════════════╝
```

### Data Value Ranges

| Parameter          | Range      | Unit   | Warning Level |
|--------------------|------------|--------|---------------|
| Speed              | 0-180      | km/h   | N/A           |
| RPM                | 0-6500     | RPM    | > 6000        |
| Coolant Temp       | 0-120      | °C     | > 105         |
| Fuel Level         | 0-100      | %      | < 15          |
| Engine Load        | 0-100      | %      | > 90          |
| Throttle Position  | 0-100      | %      | N/A           |
| Battery Voltage    | 10.0-15.0  | V      | < 11.5        |

### Typical Use Case

**Scenario: Highway Driving**
```
Speed: 100 km/h
RPM: 2500
Coolant: 90°C
Fuel: 62%
Load: 35%
Throttle: 42%
Battery: 13.8V
```

**Scenario: City Stop-and-Go**
```
Speed: 25 km/h
RPM: 1200
Coolant: 88°C
Fuel: 68%
Load: 18%
Throttle: 15%
Battery: 13.5V
```

**Scenario: Acceleration**
```
Speed: 65 km/h
RPM: 4200
Coolant: 92°C
Fuel: 70%
Load: 78%
Throttle: 85%
Battery: 13.2V
```

### UI Advantages

✅ **At a Glance**
- All critical data visible simultaneously
- No scrolling or tapping required
- Large, clear numbers

✅ **Night Optimized**
- Dark theme reduces eye strain
- High contrast for readability
- Minimal brightness

✅ **Automotive Focus**
- Landscape orientation
- No unnecessary animations
- Distraction-free design

✅ **Professional Look**
- Material Design components
- Consistent spacing
- Modern aesthetics

### Comparison with Stock Car Info Apps

| Feature                | CarCanInfo | Stock App |
|------------------------|------------|-----------|
| Update Rate            | 200ms      | 1000ms+   |
| Dark Theme             | ✓          | ✗         |
| Large Font             | ✓          | ✗         |
| Customizable           | ✓          | ✗         |
| Open Source            | ✓          | ✗         |
| Landscape Optimized    | ✓          | ~         |
| Minimal Size           | ✓          | ✗         |
| Real-time Updates      | ✓          | ~         |

### Future UI Enhancements

**Planned Improvements:**
- Settings screen with preferences
- Multiple gauge styles (analog, bar graphs)
- Customizable dashboard layouts
- Additional data pages (swipe left/right)
- DTC display screen
- Data logging view
- Graph/chart view for trends

**Requested Features:**
- Day/Night mode auto-switch
- Configurable data panels
- Additional color themes
- Warning alerts for critical values
- Trip computer display

---

## Screenshots

*Note: Actual screenshots will be added once the app is built and tested on hardware.*

To generate screenshots:
1. Build the app
2. Run on Android device or emulator
3. Capture screen with ADB: `adb exec-out screencap -p > screenshot.png`

---

**CarCanInfo** - A better way to view your car's data! 🚗📊
