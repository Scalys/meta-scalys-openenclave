SUMMARY = "An implementation of oeedger8r in C++"
DESCRIPTION = "While oeedger8r is normally build (native) during the Openenclave build, this clashes with the normal OpenEmbedded flow. \
               OpenEmbedded uses the navive and target build seperatly. To work around this we remove oeedger8r from the openenclave build, \
	       and pull it in as a dependency"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=95942ce8cea2174b68f8d2f596aa21d2"

BBCLASSEXTEND += " native" 
DEPENDS = "cmake"

SRC_URI = "git://github.com/openenclave/oeedger8r-cpp.git;protocol=https;nobranch=1;"
SRCREV = "024b3de4f4b3e873b86d35a6b1f5b3d3bb7b9fe8"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

do_configure() {

    cd ${B}
    cmake ${S}
}

do_compile() {
    cd ${B}
    make
}

do_install() {
    install -D -p -m0750 ${B}/src/oeedger8r ${D}/${bindir}/oeedger8r
}
