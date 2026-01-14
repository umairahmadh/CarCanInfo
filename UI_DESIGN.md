# CarCanInfo UI Design

## Main Dashboard Layout

```
┌─────────────────────────────────────────────────────────────────────┐
│  🚗 Connected                                    12.4 V             │  Status Bar
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐│
│   │     SPEED        │  │       RPM        │  │  COOLANT TEMP    ││
│   │                  │  │                  │  │                  ││
│   │        45        │  │      2400       │  │        88        ││  Row 1
│   │       km/h       │  │       RPM       │  │        °C        ││
│   │                  │  │                  │  │                  ││
│   └──────────────────┘  └──────────────────┘  └──────────────────┘│
│                                                                     │
│   ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐│
│   │   FUEL LEVEL     │  │  ENGINE LOAD     │  │    THROTTLE      ││
│   │                  │  │                  │  │                  ││
│   │        68        │  │       42        │  │        35        ││  Row 2
│   │        %         │  │        %        │  │        %         ││
│   │                  │  │                  │  │                  ││
│   └──────────────────┘  └──────────────────┘  └──────────────────┘│
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

## Color Scheme

### Dark Theme (Default)
- **Background**: `#121212` (Dark gray)
- **Cards**: `#1E1E1E` (Slightly lighter gray)
- **Primary Color**: `#2196F3` (Material Blue)
- **Text Primary**: `#FFFFFF` (White)
- **Text Secondary**: `#B3B3B3` (Light gray)
- **Card Border**: `#2196F3` (Blue accent)

### Typography
- **Data Labels**: 14sp, Medium weight
- **Data Values**: 36sp, Light weight (large for quick glancing)
- **Data Units**: 16sp, Regular weight
- **Status Text**: 14sp, Regular weight

## Screen Dimensions

- **Orientation**: Landscape only
- **Target Resolution**: 800x480 (common head unit size)
- **Supported**: 480p to 1080p displays
- **Aspect Ratio**: 16:10 to 16:9

## Layout Structure

### Status Bar
- Height: 56dp
- Layout: Horizontal LinearLayout
- Components:
  - Connection icon (24x24dp)
  - Status text (flexible width)
  - Battery voltage (right-aligned)

### Data Grid
- Layout: GridLayout (3 columns × 2 rows)
- Card Padding: 16dp
- Card Margin: 8dp
- Card Elevation: 4dp
- Card Corner Radius: 8dp

### Data Cards
- Width: 0dp (equally distributed)
- Height: wrap_content
- Layout: Vertical LinearLayout
- Alignment: Center
- Components (top to bottom):
  1. Label (e.g., "SPEED")
  2. Value (e.g., "45")
  3. Unit (e.g., "km/h")

## Responsive Behavior

### Small Screens (< 600dp width)
- Reduce font sizes slightly
- Adjust card padding to 12dp
- Maintain 3-column layout

### Large Screens (> 1024dp width)
- Increase font sizes for better readability
- Add more spacing between cards
- Consider showing additional data

## Animation & Transitions

- **Data Updates**: Smooth fade/scale animation (100ms)
- **Connection Status**: Color transition (300ms)
- **Screen Transitions**: None (single screen app)

## Accessibility

- **Touch Targets**: Minimum 48dp (not needed, display-only)
- **Contrast Ratio**: 7:1 (WCAG AAA compliant)
- **Font Scaling**: Respects system font size
- **Color Blindness**: Color is not the only indicator

## User Interaction

### Current Version
- **No interaction needed**: Pure display dashboard
- **Auto-connect**: Automatically connects on launch
- **Auto-update**: Data refreshes every 200ms

### Future Enhancements
- Tap card to see detailed view
- Long press for settings
- Swipe for additional pages

## Visual States

### Connection States
1. **Disconnected**: Gray icon, "Disconnected" text
2. **Connecting**: Blue animated icon, "Connecting..." text
3. **Connected**: Green icon, "Connected" text
4. **Error**: Red icon, "Connection error" text

### Data States
1. **No Data**: `--` displayed
2. **Valid Data**: Numeric value displayed
3. **Out of Range**: Warning color (orange)
4. **Critical**: Danger color (red)

## Theming

### Night Mode (Default)
- Optimized for low-light conditions
- Dark background reduces eye strain
- High contrast for easy reading

### Day Mode (Future)
- Lighter background
- Adjusted contrast for sunlight visibility
- Optional auto-switching based on time

## Design Principles

1. **Glanceable**: All critical info visible without interaction
2. **Minimal Distraction**: No unnecessary animations or colors
3. **High Contrast**: Easy to read in all lighting conditions
4. **Large Text**: Readable from arm's length
5. **Automotive Focus**: Designed for in-vehicle use

## Inspiration

Design inspired by:
- Modern automotive digital dashboards
- Material Design 3 guidelines
- OEM head unit interfaces
- Racing telemetry displays
