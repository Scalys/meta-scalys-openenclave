SUMMARY = "Confidential Package Specification (CPS)"
DESCRIPTION = "Definition of the Confidential Package Specification (CPS)"
HOMEPAGE = "https://www.github.com/Scalys/ConfidentialPackageSpecification"
LICENSE = "MIT"

SRC_URI = "git://git@github.com:/Scalys/ConfidentialPackageSpecification;protocol=ssh;branch=main"
SRCREV = "${AUTOREV}"
PV = "0.1+git${SRCPV}"

BBCLASSEXTEND += " native"

S = "${WORKDIR}/git"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=e2260224dcb209442c2b976690f3061f"

do_install() {
    install -D -p -m0644 ${S}/ConfidentialPackageSpecification.edl ${D}${includedir}/ConfidentialPackageSpecification.edl
}
