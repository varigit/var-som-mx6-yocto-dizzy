require recipes-graphics/xorg-xserver/xserver-xorg.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=bc098b9774ed096943f6c37b5beeef13"

# Misc build failure for master HEAD
SRC_URI += "file://crosscompile.patch \
            file://fix_open_max_preprocessor_error.patch \
            file://mips64-compiler.patch \
            file://aarch64.patch \
           "

SRC_URI[md5sum] = "9d68a30258c67faa3c036a4a85e8bf97"
SRC_URI[sha256sum] = "608ccfaafb845f6e559884a30f946d365209172416710d687b190e9e1ff65dc3"

# Remove unknown options configured in 'xserver-xorg.inc'
EXTRA_OECONF_remove = "--disable-xshmfence --disable-libunwind --disable-dri3"

# These extensions are now integrated into the server, so declare the migration
# path for in-place upgrades.

RREPLACES_${PN} =  "${PN}-extension-dri \
                    ${PN}-extension-dri2 \
                    ${PN}-extension-record \
                    ${PN}-extension-extmod \
                    ${PN}-extension-dbe \
                   "
RPROVIDES_${PN} =  "${PN}-extension-dri \
                    ${PN}-extension-dri2 \
                    ${PN}-extension-record \
                    ${PN}-extension-extmod \
                    ${PN}-extension-dbe \
                   "
RCONFLICTS_${PN} = "${PN}-extension-dri \
                    ${PN}-extension-dri2 \
                    ${PN}-extension-record \
                    ${PN}-extension-extmod \
                    ${PN}-extension-dbe \
                   "
