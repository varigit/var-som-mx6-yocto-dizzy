DESCRIPTION = "Chromium browser"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=537e0b52077bf0a616d0a0c8a79bc9d5"
DEPENDS = "xz-native pciutils pulseaudio cairo nss zlib-native libav libgnome-keyring cups ninja-native gconf libexif pango libdrm"
SRC_URI = "\
        http://gsdview.appspot.com/chromium-browser-official/${P}.tar.xz \
        file://include.gypi \
        file://oe-defaults.gypi \
        ${@bb.utils.contains('PACKAGECONFIG', 'component-build', 'file://component-build.gypi', '', d)} \
        ${@bb.utils.contains('PACKAGECONFIG', 'ignore-lost-context', 'file://0001-Remove-accelerated-Canvas-support-from-blacklist.patch', '', d)} \
        ${@bb.utils.contains('PACKAGECONFIG', 'impl-side-painting', 'file://0001-Disable-rasterization-whitelist-unlocking-impl-side-.patch', '', d)} \
        ${@bb.utils.contains('PACKAGECONFIG', 'disable-api-keys-info-bar', 'file://0002-Disable-API-keys-info-bar.patch', '', d)} \
        file://0001-Remove-hard-coded-values-for-CC-and-CXX.patch \
        file://unistd-2.patch \
        file://google-chrome \
        file://google-chrome.desktop \
"
SRC_URI[md5sum] = "be4d3ad6944e43132e4fbde5a23d1ab8"
SRC_URI[sha256sum] = "d3303519ab471a3bc831e9b366e64dc2fe651894e52ae5d1e74de60ee2a1198a"

# PACKAGECONFIG explanations:
#
# * use-egl : Without this packageconfig, the Chromium build will use GLX for creating an OpenGL context in X11,
#             and regular OpenGL for painting operations. Neither are desirable on embedded platforms. With this
#             packageconfig, EGL and OpenGL ES 2.x are used instead. On by default.
#
# * disable-api-keys-info-bar : This disables the info bar that warns: "Google API keys are missing". With some
#                               builds, missing API keys are considered OK, so the bar needs to go.
#                               Off by default.
#
# * component-build : Enables component build mode. By default, all of Chromium (with the exception of FFmpeg)
#                     is linked into one big binary. The linker step requires at least 8 GB RAM. Component mode
#                     was created to facilitate development and testing, since with it, there is not one big
#                     binary; instead, each component is linked to a separate shared object.
#                     Use component mode for development, testing, and in case the build machine is not a 64-bit
#                     one, or has less than 8 GB RAM. Off by default.
#
# * ignore-lost-context : There is a flaw in the HTML Canvas specification. When the canvas' backing store is
#                         some kind of hardware resource like an OpenGL texture, this resource might get lost.
#                         In case of OpenGL textures, this happens when the OpenGL context gets lost. The canvas
#                         should then be repainted, but nothing in the Canvas standard reflects that.
#                         This packageconfig is to be used if the underlying OpenGL (ES) drivers do not lose
#                         the context, or if losing the context is considered okay (note that canvas contents can
#                         vanish then). Off by default.
#
# * impl-side-painting : This is a new painting mechanism. Still in development stages, it can improve performance.
#                        See http://www.chromium.org/developers/design-documents/impl-side-painting for more.
#                        Off by default.

# conditionally add shell integration patch (ozone-wayland contains a patch that makes
# this one redundant, therefore use this patch only for X11 builds)
SRC_URI += "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'file://0001-shell-integration-conditionally-compile-routines-for.patch', '', d)}"

# conditionally add ozone-wayland and its patches to the Chromium sources

ENABLE_X11 = "${@base_contains('DISTRO_FEATURES', 'x11', '1', '0', d)}"
# only enable Wayland if X11 isn't already enabled
ENABLE_WAYLAND = "${@base_contains('DISTRO_FEATURES', 'x11', '0', \
                     base_contains('DISTRO_FEATURES', 'wayland', '1', \
                     '0', d),d)}"

# variable for extra ozone-wayland patches, typically extended by BSP layer .bbappends
# IMPORTANT: do not simply add extra ozone-wayland patches to the SRC_URI in a
# .bbappend, since the base ozone-wayland patches need to be applied first (see below)

OZONE_WAYLAND_EXTRA_PATCHES = " \
        file://0001-Remove-X-libraries-from-GYP-files.patch \
"

OZONE_WAYLAND_GIT_DESTSUFFIX = "ozone-wayland-git"
OZONE_WAYLAND_GIT_BRANCH = "Milestone-Harvest"
OZONE_WAYLAND_GIT_SRCREV = "0f8b830730d9b696a667c331c50ac6333bb85c13"
SRC_URI += "${@base_conditional('ENABLE_WAYLAND', '1', 'git://github.com/01org/ozone-wayland.git;destsuffix=${OZONE_WAYLAND_GIT_DESTSUFFIX};branch=${OZONE_WAYLAND_GIT_BRANCH};rev=${OZONE_WAYLAND_GIT_SRCREV}', '', d)}"

do_unpack[postfuncs] += "${@base_conditional('ENABLE_WAYLAND', '1', 'copy_ozone_wayland_files', '', d)}"
do_patch[prefuncs] += "${@base_conditional('ENABLE_WAYLAND', '1', 'add_ozone_wayland_patches', '', d)}"

copy_ozone_wayland_files() {
	# ozone-wayland sources must be placed in an "ozone"
	# subdirectory in ${S} in order for the .gyp build
	# scripts to work
	cp -r ${WORKDIR}/ozone-wayland-git ${S}/ozone
}

python add_ozone_wayland_patches() {
    import glob
    srcdir = d.getVar('S', True)
    # find all ozone-wayland patches and add them to SRC_URI
    upstream_patches_dir = srcdir + "/ozone/patches"
    # using 00*.patch to skip WebRTC patches
    # the WebRTC patches remove X11 libraries from the linker flags, which is
    # already done by another patch (see above). Furthermore, to be able to use
    # these patches, it is necessary to update the git repository in third_party/webrtc,
    # which would further complicate this recipe.
    upstream_patches = glob.glob(upstream_patches_dir + "/00*.patch")
    upstream_patches.sort()
    for upstream_patch in upstream_patches:
        d.appendVar('SRC_URI', ' file://' + upstream_patch)
    # then, add the extra patches to SRC_URI order matters;
    # extra patches may depend on the base ozone-wayland ones
    d.appendVar('SRC_URI', ' ' + d.getVar('OZONE_WAYLAND_EXTRA_PATCHES'))
}


# include.gypi exists only for armv6 and armv7a and there isn't something like COMPATIBLE_ARCH afaik
COMPATIBLE_MACHINE = "(-)"
COMPATIBLE_MACHINE_i586 = "(.*)"
COMPATIBLE_MACHINE_x86-64 = "(.*)"
COMPATIBLE_MACHINE_armv6 = "(.*)"
COMPATIBLE_MACHINE_armv7a = "(.*)"

inherit gettext

PACKAGECONFIG ??= "use-egl"

# this makes sure the dependencies for the EGL mode are present; otherwise, the configure scripts
# automatically and silently fall back to GLX
PACKAGECONFIG[use-egl] = ",,virtual/egl virtual/libgles2"

EXTRA_OEGYP =	" \
	-Dangle_use_commit_id=0 \
	-Dclang=0 \
	-Dhost_clang=0 \
	-Ddisable_fatal_linker_warnings=1 \
	${@base_contains('DISTRO_FEATURES', 'ld-is-gold', '', '-Dlinux_use_gold_binary=0', d)} \
	${@base_contains('DISTRO_FEATURES', 'ld-is-gold', '', '-Dlinux_use_gold_flags=0', d)} \
	-I ${WORKDIR}/oe-defaults.gypi \
	-I ${WORKDIR}/include.gypi \
	${@bb.utils.contains('PACKAGECONFIG', 'component-build', '-I ${WORKDIR}/component-build.gypi', '', d)} \
	-f ninja \
"
ARMFPABI_armv7a = "${@bb.utils.contains('TUNE_FEATURES', 'callconvention-hard', 'arm_float_abi=hard', 'arm_float_abi=softfp', d)}"

CHROMIUM_EXTRA_ARGS ?= " \
	${@bb.utils.contains('PACKAGECONFIG', 'use-egl', '--use-gl=egl', '', d)} \
	${@bb.utils.contains('PACKAGECONFIG', 'ignore-lost-context', '--gpu-no-context-lost', '', d)} \
	${@bb.utils.contains('PACKAGECONFIG', 'impl-side-painting', '--enable-gpu-rasterization --enable-impl-side-painting', '', d)} \
"

GYP_DEFINES = "${ARMFPABI} release_extra_cflags='-Wno-error=unused-local-typedefs' sysroot=''"

python() {
    if d.getVar('ENABLE_X11', True) == '1':
        d.appendVar('DEPENDS', ' xextproto gtk+ libxi libxss ')
    if d.getVar('ENABLE_WAYLAND', True) == '1':
        d.appendVar('DEPENDS', ' wayland libxkbcommon ')
        d.appendVar('GYP_DEFINES', ' use_ash=1 use_aura=1 chromeos=0 use_ozone=1 ')
}

do_configure() {
	cd ${S}
	GYP_DEFINES="${GYP_DEFINES}" export GYP_DEFINES
	# replace LD with CXX, to workaround a possible gyp issue?
	LD="${CXX}" export LD
	CC="${CC}" export CC
	CXX="${CXX}" export CXX
	CC_host="${BUILD_CC}" export CC_host
	CXX_host="${BUILD_CXX}" export CXX_host
	build/gyp_chromium --depth=. ${EXTRA_OEGYP}
}

do_compile() {
	# build with ninja
	ninja -C ${S}/out/Release chrome chrome_sandbox
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 ${WORKDIR}/google-chrome ${D}${bindir}/

	# Add extra command line arguments to google-chrome script by modifying
	# the dummy "CHROME_EXTRA_ARGS" line
	sed -i "s/^CHROME_EXTRA_ARGS=\"\"/CHROME_EXTRA_ARGS=\"${CHROMIUM_EXTRA_ARGS}\"/" ${D}${bindir}/google-chrome

	install -d ${D}${datadir}/applications
	install -m 0644 ${WORKDIR}/google-chrome.desktop ${D}${datadir}/applications/

	install -d ${D}${bindir}/chrome/
	install -m 0755 ${S}/out/Release/chrome ${D}${bindir}/chrome/chrome
	install -m 0644 ${S}/out/Release/resources.pak ${D}${bindir}/chrome/
	install -m 0644 ${S}/out/Release/icudtl.dat ${D}${bindir}/chrome/
	install -m 0644 ${S}/out/Release/content_resources.pak ${D}${bindir}/chrome/
	install -m 0644 ${S}/out/Release/keyboard_resources.pak ${D}${bindir}/chrome/
	install -m 0644 ${S}/out/Release/chrome_100_percent.pak ${D}${bindir}/chrome/
	install -m 0644 ${S}/out/Release/product_logo_48.png ${D}${bindir}/chrome/
	install -m 0755 ${S}/out/Release/libffmpegsumo.so ${D}${bindir}/chrome/

	# Always adding this libdir (not just with component builds), because the
	# LD_LIBRARY_PATH line in the google-chromium script refers to it
	install -d ${D}${libdir}/chrome/
	if [ -n "${@bb.utils.contains('PACKAGECONFIG', 'component-build', 'component-build', '', d)}" ]; then
		install -m 0755 ${S}/out/Release/lib/*.so ${D}${libdir}/chrome/
	fi

	install -d ${D}${sbindir}
	install -m 4755 ${S}/out/Release/chrome_sandbox ${D}${sbindir}/chrome-devel-sandbox

	install -d ${D}${bindir}/chrome/locales/
	install -m 0644 ${S}/out/Release/locales/en-US.pak ${D}${bindir}/chrome/locales
}

FILES_${PN} = "${bindir}/chrome/ ${bindir}/google-chrome ${datadir}/applications ${sbindir}/ ${libdir}/chrome/"
FILES_${PN}-dbg += "${bindir}/chrome/.debug/ ${libdir}/chrome/.debug/"

PACKAGE_DEBUG_SPLIT_STYLE = "debug-without-src"

