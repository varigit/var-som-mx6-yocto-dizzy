# As it can not overwrite the version in the layer meta-fsl-arm, we have to use
#   another file extension for new patch to the append in the meta-fsl-arm

# Append path for freescale layer to include alsa-state asound.conf
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_mx6 = " \
	file://asound.state.3.10.17 \
"

do_install_append_mx6() {
    install -m 0644 ${WORKDIR}/asound.state.3.10.17 ${D}${localstatedir}/lib/alsa/asound.state
}

