SUMMARY = "Userspace NFS server v3 protocol"
SECTION = "console/network"
LICENSE = "unfs3"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9475885294e17c0cc0067820d042792e"

DEPENDS = "flex-native bison-native flex"
DEPENDS_append_class-nativesdk += "flex-nativesdk"

SRC_URI[md5sum] = "3687acc4ee992e536472365dd99712a7"
SRC_URI[sha256sum] = "274b43ada9c6eea1da26eb7010d72889c5278984ba0b50dff4e093057d4d64f8"

MOD_PV = "490"
S = "${WORKDIR}/trunk"
SRC_URI = "svn://svn.code.sf.net/p/unfs3/code;module=trunk;rev=${MOD_PV} \
           file://unfs3_parallel_build.patch \
           file://alternate_rpc_ports.patch \
           file://fix_pid_race_parent_writes_child_pid.patch \
           file://fix_compile_warning.patch \
           file://rename_fh_cache.patch \
           file://relative_max_socket_path_len.patch \
           file://fix_warnings.patch \
           file://tcp_no_delay.patch \
          "
BBCLASSEXTEND = "native nativesdk"

inherit autotools

# Turn off these header detects else the inode search
# will walk entire file systems and this is a real problem
# if you have 2 TB of files to walk in your file system
CACHED_CONFIGUREVARS = "ac_cv_header_mntent_h=no ac_cv_header_sys_mnttab_h=no"
