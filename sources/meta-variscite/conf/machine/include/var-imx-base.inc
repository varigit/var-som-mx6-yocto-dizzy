# Provides the i.MX common settings

include conf/machine/include/var-default-settings.inc
include conf/machine/include/var-default-versions.inc
include conf/machine/include/var-default-providers.inc

include conf/machine/include/soc-family.inc

# Disable perf features as 2.6.35.3 fail to build otherwise
PERF_FEATURES_ENABLE = ""

#
RDEPENDS_kernel-base = ""
#
MACHINE_ESSENTIAL_EXTRA_RDEPENDS += " wlconf \
					iw \
					hostap-daemon-wl18xx \
					libnl \
					crda \
					wl18xx-target-scripts \
                                      ti-compat-wireless-wl18xx \
                                      wpa-supplicant-wl18xx \
					bluez5 \
                                      wl18xx-firmware"


# Set specific make target and binary suffix
UBOOT_MAKE_TARGET = "all"
UBOOT_SUFFIX ?= "img"
SPL_BINARY = "SPL"

UBOOT_ENTRYPOINT_mx6  = "0x10008000"

XSERVER_DRIVER = "xf86-video-fbdev"
XSERVER_DRIVER_mx5 = "xf86-video-imxfb"
XSERVER_DRIVER_mx6 = "xf86-video-imxfb-vivante"
XSERVER = "xserver-xorg \
           xf86-input-evdev \
           ${XSERVER_DRIVER}"

# FIXME: Use older versions until Vivante GPU is made compatible
PREFERRED_VERSION_xserver-xorg_mx6 = "2:1.14.4"
PREFERRED_VERSION_directfb_mx6 = "1.6.3"
PREFERRED_VERSION_directfb-examples_mx6 = "1.6.0"

# Ship kernel modules
MACHINE_EXTRA_RRECOMMENDS = "kernel-modules"

# Float-Point setting
DEFAULTTUNE_mx6 ?= "cortexa9hf-neon"

# Sub-architecture support
MACHINE_SOCARCH ?= ""
MACHINE_SOCARCH_mx3 = "${TUNE_PKGARCH}-mx3"
MACHINE_SOCARCH_mx5 = "${TUNE_PKGARCH}-mx5"
MACHINE_SOCARCH_mx6 = "${TUNE_PKGARCH}-mx6"
MACHINE_SOCARCH_vf60 = "${TUNE_PKGARCH}-vf60"
PACKAGE_EXTRA_ARCHS_append = " ${MACHINE_SOCARCH}"

MACHINE_ARCH_FILTER = "virtual/kernel"
MACHINE_SOCARCH_FILTER = "libfslcodec libfslvpuwrap libfslparser gst-fsl-plugins"
MACHINE_SOCARCH_FILTER_append_mx5 = " virtual/libgles1 virtual/libgles2 virtual/egl virtual/mesa virtual/libgl"
MACHINE_SOCARCH_FILTER_append_mx6 = " virtual/libgles1 virtual/libgles2 virtual/egl virtual/mesa virtual/libgl"

INHERIT += "fsl-dynamic-packagearch"

SHRINK_UBI = "Y"


IMAGE_FSTYPES ?= "tar.bz2 ext3 sdcard"
# IMAGE_FSTYPES = "ubi tar.bz2 ext3 sdcard"


# Firmware
MACHINE_FIRMWARE ?= ""
MACHINE_FIRMWARE_append_mx6 = " firmware-imx-vpu-imx6q"
MACHINE_FIRMWARE_append_mx6 = " firmware-imx-vpu-imx6d"

# FIXME: Needs addition of firmware-imx of official BSPs
#MACHINE_FIRMWARE_append_mx35 = " firmware-imx-sdma-imx35"
#MACHINE_FIRMWARE_append_mx31 = " firmware-imx-sdma-imx31"
#MACHINE_FIRMWARE_append_mx27 = " firmware-imx-vpu-imx27"
#MACHINE_FIRMWARE_append_mx25 = " firmware-imx-sdma-imx25"

MACHINE_EXTRA_RRECOMMENDS += "${MACHINE_FIRMWARE}"

# Extra audio support
# FIXME: Add support for ALL SoC families
MACHINE_EXTRA_RRECOMMENDS_append_mx6 = " fsl-alsa-plugins"

# Extra udev rules
MACHINE_EXTRA_RRECOMMENDS += "udev-extraconf"

# GStreamer 0.10 plugins
MACHINE_GSTREAMER_PLUGIN ?= "gst-fsl-plugin"
MACHINE_GSTREAMER_PLUGIN_mx3 ?= ""
MACHINE_GSTREAMER_PLUGIN_vf60 ?= ""

# GStreamer 1.0 plugins
MACHINE_GSTREAMER_1_0_PLUGIN ?= ""
MACHINE_GSTREAMER_1_0_PLUGIN_mx6 ?= "gstreamer1.0-plugins-imx-meta"

# mx5 GPU libraries
GPU_FLAVOUR = "${@base_contains('DISTRO_FEATURES', 'x11', 'x11-', '', d)}"
PREFERRED_PROVIDER_virtual/egl_mx5 ?= "amd-gpu-${GPU_FLAVOUR}bin-mx51"
PREFERRED_PROVIDER_virtual/libgles1_mx5 ?= "amd-gpu-${GPU_FLAVOUR}bin-mx51"
PREFERRED_PROVIDER_virtual/libgles2_mx5 ?= "amd-gpu-${GPU_FLAVOUR}bin-mx51"

# mx6 GPU libraries
PREFERRED_PROVIDER_virtual/egl_mx6 ?= "gpu-viv-bin-mx6q"
PREFERRED_PROVIDER_virtual/libgl_mx6 ?= "gpu-viv-bin-mx6q"
PREFERRED_PROVIDER_virtual/libgles1_mx6 ?= "gpu-viv-bin-mx6q"
PREFERRED_PROVIDER_virtual/libgles2_mx6 ?= "gpu-viv-bin-mx6q"

PREFERRED_PROVIDER_virtual/kernel_mx3 ?= "linux-fslc"

EXTRA_IMAGEDEPENDS = "u-boot"

SDCARD_ROOTFS ?= "${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.ext3"

IMAGE_FSTYPES ?= "tar.bz2 ext3 sdcard"

 


SERIAL_CONSOLE = "115200 ttymxc0"

KERNEL_IMAGETYPE = "uImage"

MACHINE_FEATURES = "apm usbgadget usbhost vfat alsa touchscreen"
