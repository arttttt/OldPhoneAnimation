# Rotary Phone Dial Animation

A Jetpack Compose implementation of a classic rotary phone dial with realistic mechanical behavior and visual feedback. This component provides an authentic vintage phone experience with proper rotation constraints and smooth animations.

## Overview

This project recreates the iconic rotary dial mechanism found in vintage telephones. It features:

- ✨ Realistic rotation mechanics
- 🔄 Smooth animations
- 🎯 Accurate digit selection
- 🔒 Mechanical constraints
- 🎨 Authentic visual design

## Demo

<img src="graphics/demo_1.gif" width="300" alt="Demo">

## Implementation Highlights

The component is built entirely with Jetpack Compose and features:

- Custom gesture handling for rotation
- Physical constraints matching real rotary phones
- Smooth animations powered by `animateFloatAsState`
- Vector-based rendering using Compose Canvas
- Accurate hit detection for digits

## Usage

Add the component to your Compose UI:

```kotlin
@Composable
fun YourScreen() {
    OldPhone(
        modifier = Modifier.padding(16.dp),
        onDigitSelected = {
            // code
        },
    )
}
```

## Technical Details

### Key Features

- Individual digit areas with proper hit detection
- Rotation limit calculated per digit (27° segments)
- Spring-back animation on release
- Visual feedback for selected digits
- Mechanical stopper element at 0°

### Implementation Details

The dial is implemented using:
- Compose Canvas for drawing
- Custom pointer input handling
- State management for rotation
- Animated state transitions

## Article

For a detailed explanation of the implementation (in Russian), check out the article on Habr: https://habr.com/ru/articles/871956/

The article provides a step-by-step guide on creating this component, explaining the mechanics, animations, and design decisions behind the implementation.

## License

```
MIT License

Copyright (c) 2025 Artem Bambalov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```