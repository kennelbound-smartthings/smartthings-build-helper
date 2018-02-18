definition(name: "KB: eero Mesh Network", namespace: "kennelbound-smartthings/eero-unofficial", author: "Kennelbound") {
    capability "Actuator"
    capability "Sensor"

    attribute "name", "string"
    attribute "version", "string"
    attribute "uploadSpeedMbps", "number"
    attribute "downloadSpeedMbps", "number"
}