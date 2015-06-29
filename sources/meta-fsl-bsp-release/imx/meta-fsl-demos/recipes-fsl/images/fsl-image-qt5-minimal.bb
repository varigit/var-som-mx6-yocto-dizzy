DESCRIPTION = "Freescale Image - Adds Qt5"
LICENSE = "MIT"

require recipes-fsl/images/fsl-image-gui.bb

inherit distro_features_check

CONFLICT_DISTRO_FEATURES = "directfb"

X11_IMAGE_INSTALL = "${@base_contains('DISTRO_FEATURES', 'x11', \
    'libxkbcommon', '', d)}"

WLD_IMAGE_INSTALL = "${@base_contains('DISTRO_FEATURES', 'wayland', \
    'qtwayland \
     qtwayland-plugins', '', d)}"

OPENCV_INSTALL = "opencv opencv-dev opencv-apps opencv-samples"

QT5_IMAGE_INSTALL = ""
QT5_IMAGE_INSTALL_common = " \
    packagegroup-qt5-core \
    packagegroup-qt5-qtdeclarative \
    packagegroup-qt5-qtdeclarative-qml \
    ${X11_IMAGE_INSTALL} \
    ${WLD_IMAGE_INSTALL} \
    ${OPENCV_INSTALL} \
    "
QT5_IMAGE_INSTALL_mx6 = " \
    ${QT5_IMAGE_INSTALL_common} \
    "


IMAGE_INSTALL += " \
${QT5_IMAGE_INSTALL} \
minicom \
tcf-agent \
openssh-sftp-server \
fio \
"
export IMAGE_BASENAME = "fsl-image-qt5-minimal"
