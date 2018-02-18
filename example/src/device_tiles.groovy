tiles(scale: 2) {
    multiAttributeTile(name: "updown", type: "generic", width: 6, height: 2) {
        tileAttribute("device.uploadSpeedMbps", key: "PRIMARY_CONTROL", width: 3) {
            attributeState "uploadSpeedMbps", label: 'Up: ${currentValue} Mbps', icon: "st.thermostat.thermostat-up"
        }
        tileAttribute("device.downloadSpeedMbps", key: "PRIMARY_CONTROL", width: 3) {
            attributeState "downloadSpeedMbps", label: 'Down: ${currentValue} Mbps', icon: "st.thermostat.thermostat-down"
        }
    }

    main "updown"
    details(["updown"])
}