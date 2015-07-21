# Copyright (C) 2013-14 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

require recipes-kernel/linux/linux-imx.inc
require recipes-kernel/linux/linux-dtb.inc

DEPENDS += "lzop-native bc-native"

COMPATIBLE_MACHINE = "(mx6)"

SRC_URI = "git://github.com/varigit/linux-2.6-imx.git;protocol=git;branch=imx_3.14.28-r0_var3"

SRCREV = "bfd9ecfd5f840a63a2b210963574c5d011f7dd6e"

FSL_KERNEL_DEFCONFIG = "imx_v7_var_defconfig"

LOCALVERSION = "-1.1.0"

IMX_TEST_SUPPORT = "y"

do_configure_prepend() {
   # In some cases we'll use a different defconfig
   # example is manufacturing image which uses imx_v7_mfg_defconfig
   # however need way to change it back during daily build

   if [ -z "${FSL_KERNEL_DEFCONFIG}" ] ; then
       echo " defconfig from local.conf not set"
       fsl_defconfig='imx_v7_defconfig'
   else
       echo " Use local.conf for defconfig to set"
       fsl_defconfig=${FSL_KERNEL_DEFCONFIG}
   fi

   # check that defconfig file exists
   if [ ! -e "${S}/arch/arm/configs/$fsl_defconfig" ]; then
       fsl_defconfig='imx_v7_defconfig'
   fi


    cp ${S}/arch/arm/configs/${fsl_defconfig} ${S}/.config
    cp ${S}/arch/arm/configs/${fsl_defconfig} ${S}/../defconfig
}

# copy zImage to deploy directory
# update uImage with defconfig ane git info in name
# this is since build script can build multiple ways
# and will overwrite previous builds

do_deploy_append () {
    install -d ${DEPLOY_DIR_IMAGE}

    if [ -z "${FSL_KERNEL_DEFCONFIG}" ] ; then
       fsl_defconfig='imx_v7_defconfig'
    else
       fsl_defconfig=${FSL_KERNEL_DEFCONFIG}
       # check that defconfig file exists
       if [ ! -e "${S}/arch/arm/configs/${FSL_KERNEL_DEFCONFIG}" ]; then
           fsl_defconfig='imx_v7_defconfig'
       fi
    fi

    install  arch/arm/boot/uImage ${DEPLOY_DIR_IMAGE}/uImage_$fsl_defconfig
    install  arch/arm/boot/zImage ${DEPLOY_DIR_IMAGE}/zImage_$fsl_defconfig
}
