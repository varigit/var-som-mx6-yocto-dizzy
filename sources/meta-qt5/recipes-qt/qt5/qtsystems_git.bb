require qt5-git.inc
require ${PN}.inc

QT_MODULE_BRANCH = "5.3"

# qtsystems wasn't released yet, last tag before this SRCREV is 5.0.0-beta1
# qt5-git PV is only to indicate that this recipe is compatible with qt5 5.2.1

SRCREV = "aa651c73bf7bc57c1b6b1bfcfa9afe901884a102"
