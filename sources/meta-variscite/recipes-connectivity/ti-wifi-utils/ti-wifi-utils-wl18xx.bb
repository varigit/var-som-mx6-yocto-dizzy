DESCRIPTION = "The calibrator and other useful utilities for TI wireless solution based on wl12xx driver"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=4725015cb0be7be389cf06deeae3683d"

DEPENDS = "libnl"
RDEPENDS_${PN} = "wl18xx-firmware"

PV = "R8.5+git${SRCPV}"

PR ="r3"

#Tag: R8.5
SRCREV = "dcf0800f30ba449cd7f3a20f8b3f4853dc829652"
SRC_URI = "git://git.ti.com/wilink8-wlan/18xx-ti-utils.git \
"

PROVIDES += "ti-wifi-utils"
RPROVIDES_${PN} += "ti-wifi-utils"
RREPLACES_${PN} +=  "ti-wifi-utils"
RCONFLICTS_${PN} +=  "ti-wifi-utils"

S = "${WORKDIR}/git"

#export CROSS_COMPILE = "${TARGET_PREFIX}"

EXTRA_OEMAKE = 'CFLAGS="${CFLAGS} -I${STAGING_INCDIR}/libnl3/ -DCONFIG_LIBNL32 " \
		LDFLAGS="${LDFLAGS} -L${STAGING_LIBDIR}" \
		NLVER=3'

do_install() {
    install -d ${D}${bindir}
    install -m 0755 calibrator ${D}${bindir}/
}
