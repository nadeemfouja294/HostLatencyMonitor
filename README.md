# HostLatencyMonitor
A modern Android application that monitors host latency by pinging multiple endpoints and displaying real-time results. Built with Clean Architecture and latest Android development tools.

## Features
- Fetch hosts from JSON endpoint
- Real-time latency monitoring by pinging each host 5 times
- Show host name, average latency and associated image
- Sort results by name and latency
- Individual host retesting capability
- Elegant loading and error states
- Smooth animations and transitions

## Architecture
Clean Architecture by [Uncle Bob's](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) as described in this [book](https://www.amazon.com/Clean-Architecture-Android-Expert-led-Maintainable/dp/9355510497)

The project is separated into two main modules:

### PingLibrary Module
A reusable module responsible for:
- Executing ping operations
- Calculating average latency
- Managing ping counts (default: 5)
- Proper error handling
- Thread management

### App Module
The main application module that:
- Fetches host data from JSON
- Consumes PingLibrary for latency checks
- Manages UI state
- Handles image loading
- Provides sorting capabilities
- Enables individual host retesting

## Technical Stack

### Modern Android Development
- Kotlin and [Ktlint](https://ktlint.github.io/) for code formatting
- Coroutines for async operations
- StateFlow for reactive programming
- Dependency Injection with Hilt
- [Jetpack Compose](https://developer.android.com/jetpack/compose) for UI
- Material Design 3
- Coil for image loading

## Demo
<img align="centre" src="/demo.gif" width="360" height="640"/>