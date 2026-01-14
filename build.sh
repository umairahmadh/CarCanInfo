#!/bin/bash

# CarCanInfo Build Script
# This script helps build the Android app with various options

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Functions
print_header() {
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}  CarCanInfo Build Script${NC}"
    echo -e "${BLUE}========================================${NC}"
    echo ""
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

check_requirements() {
    print_info "Checking requirements..."
    
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed. Please install JDK 8 or higher."
        exit 1
    fi
    
    print_success "Java found: $(java -version 2>&1 | head -n 1)"
}

clean_build() {
    print_info "Cleaning previous build..."
    ./gradlew clean
    print_success "Clean completed"
}

build_debug() {
    print_info "Building debug APK..."
    ./gradlew assembleDebug
    print_success "Debug build completed"
    print_info "APK location: app/build/outputs/apk/debug/app-debug.apk"
}

build_release() {
    print_info "Building release APK..."
    ./gradlew assembleRelease
    print_success "Release build completed"
    print_info "APK location: app/build/outputs/apk/release/app-release.apk"
}

install_debug() {
    print_info "Installing debug APK to connected device..."
    
    if ! command -v adb &> /dev/null; then
        print_error "ADB is not installed. Please install Android SDK Platform Tools."
        exit 1
    fi
    
    if ! adb devices | grep -q "device$"; then
        print_error "No device connected. Please connect a device with USB debugging enabled."
        exit 1
    fi
    
    ./gradlew installDebug
    print_success "App installed successfully"
}

show_usage() {
    echo "Usage: $0 [option]"
    echo ""
    echo "Options:"
    echo "  clean       - Clean build directory"
    echo "  debug       - Build debug APK"
    echo "  release     - Build release APK"
    echo "  install     - Build and install debug APK to connected device"
    echo "  all         - Clean, build debug and release"
    echo "  help        - Show this help message"
    echo ""
}

# Main script
print_header

if [ $# -eq 0 ]; then
    print_info "No option specified. Building debug APK..."
    check_requirements
    build_debug
    exit 0
fi

case "$1" in
    clean)
        check_requirements
        clean_build
        ;;
    debug)
        check_requirements
        build_debug
        ;;
    release)
        check_requirements
        build_release
        ;;
    install)
        check_requirements
        build_debug
        install_debug
        ;;
    all)
        check_requirements
        clean_build
        build_debug
        build_release
        ;;
    help)
        show_usage
        ;;
    *)
        print_error "Unknown option: $1"
        show_usage
        exit 1
        ;;
esac

echo ""
print_success "Build script completed successfully!"
