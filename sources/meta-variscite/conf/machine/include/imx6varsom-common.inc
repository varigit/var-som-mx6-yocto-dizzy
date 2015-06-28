# Provides the i.MX6 VAR-SOM-MX6 common settings

require conf/machine/include/var-imx-base.inc
require conf/machine/include/tune-cortexa9.inc

SERIAL_CONSOLE = "115200 ttymxc0"

#MACHINE_FIRMWARE_append_mx6 = " linux-firmware-wl12xx"
#MACHINE_FIRMWARE_append_mx6 = " wl18xx-firmware"

MACHINE_FEATURES += " pci bluetooth touchscreen wifi"

