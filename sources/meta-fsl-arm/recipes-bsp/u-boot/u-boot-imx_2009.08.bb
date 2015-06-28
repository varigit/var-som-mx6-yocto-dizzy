# Copyright (C) 2011-2014 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "U-Boot provided by Freescale with focus on i.MX reference boards."
require recipes-bsp/u-boot/u-boot.inc

PROVIDES += "u-boot"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=4c6cde5df68eff615d36789dc18edd3b"

DEPENDS_mxs += "elftosb-native"

PR = "r18"

SRCBRANCH_mxs = "imx_v2009.08_10.12.01"
SRCREV_mxs = "e4437f1c192a1a68028e6fcff3f50ff50352041d"

SRCBRANCH_mx5 = "imx_v2009.08_11.09.01"
SRCREV_mx5 = "897922d01c812be802e4a928b937535ea1b8e076"
SRC_URI_append_imx5 = " \
           file://mx53_loco_bootenv.patch \
"

SRCBRANCH_mx6 = "imx_v2009.08_3.0.35_4.0.0"
SRCREV_mx6 = "5899674bf39544bec47e209649a723cf7348d3ba"
SRC_URI_append_mx6 = " \
   file://mx6q_sabreauto-Fix-the-patch-for-the-default-environ.patch \
   file://mx6q_sabresd-Change-default-environment-to-work-with.patch \
   file://mx6sl_evk-Fix-the-patch-for-the-default-environment-.patch \
   file://mx6dl_sabresd-Change-default-environment-to-work-wit.patch \
"

SRC_URI = "git://git.freescale.com/imx/uboot-imx.git;branch=${SRCBRANCH}"

UBOOT_MACHINE_imx53qsb = "mx53_loco_config"
UBOOT_MACHINE_imx53ard = "mx53_ard_ddr3_config"
UBOOT_MACHINE_imx51evk = "mx51_bbg_config"
UBOOT_MACHINE_imx6qsabrelite = "mx6q_sabrelite_config"
UBOOT_MACHINE_imx6qsabreauto = "mx6q_sabreauto_config"
UBOOT_MACHINE_imx6qsabresd = "mx6q_sabresd_config"
UBOOT_MACHINE_imx6dlsabresd = "mx6dl_sabresd_config"
UBOOT_MACHINE_imx6slevk = "mx6sl_evk_config"
UBOOT_MACHINE_imx28evk = "mx28_evk_config"

UBOOT_MAKE_TARGET = "u-boot.bin"

# Please, add the following variables to conf/local.conf
# in order to use this u-boot version
# UBOOT_SUFFIX = "bin"
# UBOOT_PADDING = "2"
# PREFERRED_PROVIDER_u-boot = "u-boot-imx"

S = "${WORKDIR}/git"
EXTRA_OEMAKE += 'HOSTSTRIP=true'

inherit fsl-u-boot-localversion

LOCALVERSION ?= "-${SRCBRANCH}"

do_compile_prepend() {
	if [ "${@base_contains('DISTRO_FEATURES', 'ld-is-gold', 'ld-is-gold', '', d)}" = "ld-is-gold" ] ; then
		sed -i 's/$(CROSS_COMPILE)ld/$(CROSS_COMPILE)ld.bfd/g' config.mk
	fi
}

COMPATIBLE_MACHINE = "(imx28evk|mx5|mx6)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
