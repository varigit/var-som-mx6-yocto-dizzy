# Variscite BSP default providers

PREFERRED_PROVIDER_virtual/xserver = "xserver-xorg"
PREFERRED_PROVIDER_u-boot = "u-boot-imx-variscite"
PREFERRED_PROVIDER_virtual/kernel_mx6 = "linux-variscite"
PREFERRED_PROVIDER_u-boot ?= "u-boot-fslc"
PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-fslc"

PREFERRED_PROVIDER_wpa-supplicant-cli = "wpa-supplicant-wl18xx"
PREFERRED_PROVIDER_wpa-supplicant-passphrase = "wpa-supplicant-wl18xx"
PREFERRED_PROVIDER_wpa-supplicant = "wpa-supplicant-wl18xx"

