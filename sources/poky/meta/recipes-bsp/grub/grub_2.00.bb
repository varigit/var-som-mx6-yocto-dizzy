SUMMARY = "GRUB2 is the next-generation GRand Unified Bootloader"

DESCRIPTION = "GRUB2 is the next generaion of a GPLed bootloader \
intended to unify bootloading across x86 operating systems. In \
addition to loading the Linux kernel, it implements the Multiboot \
standard, which allows for flexible loading of multiple boot images."

HOMEPAGE = "http://www.gnu.org/software/grub/"
SECTION = "bootloaders"

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "autogen-native flex-native bison-native xz"
RDEPENDS_${PN} = "diffutils freetype"
PR = "r1"

SRC_URI = "ftp://ftp.gnu.org/gnu/grub/grub-${PV}.tar.gz \
          file://grub-install.in.patch \
          file://grub-2.00-fpmath-sse-387-fix.patch \
          file://remove-gets.patch \
          file://check-if-liblzma-is-disabled.patch \
          file://fix-issue-with-flex-2.5.37.patch \
          file://grub-2.00-add-oe-kernel.patch \
          file://fix-endianness-problem.patch \
          file://grub2-remove-sparc64-setup-from-x86-builds.patch \
          "

SRC_URI[md5sum] = "e927540b6eda8b024fb0391eeaa4091c"
SRC_URI[sha256sum] = "65b39a0558f8c802209c574f4d02ca263a804e8a564bc6caf1cd0fd3b3cc11e3"

COMPATIBLE_HOST = '(x86_64.*|i.86.*)-(linux|freebsd.*)'

FILES_${PN}-dbg += "${libdir}/${BPN}/i386-pc/.debug"


inherit autotools gettext texinfo

PACKAGECONFIG ??= ""
PACKAGECONFIG[grub-mount] = "--enable-grub-mount,--disable-grub-mount,fuse"
PACKAGECONFIG[device-mapper] = "--enable-device-mapper,--disable-device-mapper,lvm2"

EXTRA_OECONF = "--with-platform=pc --disable-grub-mkfont --program-prefix="" \
               --enable-liblzma=no --enable-device-mapper=no --enable-libzfs=no"

do_install_append () {
    install -d ${D}${sysconfdir}/grub.d
}

INSANE_SKIP_${PN} = "arch"
INSANE_SKIP_${PN}-dbg = "arch"
