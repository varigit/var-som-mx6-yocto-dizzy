FILESEXTRAPATHS_prepend := "${THISDIR}/qtwayland-git"

SRC_URI = "git://code.qt.io/qt/qtwayland.git;protocol=http;branch=5.4 \
           file://0001-examples.pro-include-server-buffer-only-when-buildin.patch"
