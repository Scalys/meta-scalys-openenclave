SUMMARY = "OP-TEE Client API"
DESCRIPTION = "Open Portable Trusted Execution Environment - Normal World Client side of the TEE"
HOMEPAGE = "https://www.op-tee.org/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=69663ab153298557a59c67a60a743e5b"

#PV = "3.6.0-ms-iot-openenvlave"

inherit pkgconfig cmake python3native systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "tee-supplicant.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

#DEPENDS = "libgcc glibc"
#DEPENDS = "cmake-openenclave-native ninja-native"

SRCREV = "4e91c54a6384f7975779d61d88c8da1a5e0dd799"
SRC_URI = " \
    git://github.com/ms-iot/optee_client;nobranch=1 \
    file://tee-supplicant.service \
"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

SYSTEMD_SERVICE_${PN} = "tee-supplicant.service"

do_configure() {
    
    cd ${B}
    cmake \
        -DCMAKE_INSTALL_PREFIX=${D} \
        ${S}
}

FILES_${PN} = " \
    /usr/sbin/tee-supplicant \
    /include/tee_bench.h \
    /include/tee_client_api_extensions.h \
    /include/tee_client_api.h \
    /include/teec_trace.h \
"

do_install() {
    #oe_runmake install

    install -D -p -m0755 ${B}/tee-supplicant/tee-supplicant ${D}${sbindir}/tee-supplicant

    install -D -p -m0644 ${B}/libteec/libteec.a ${D}${libdir}/libteec.a
#    ln -sf libteec.so.1.0 ${D}${libdir}/libteec.so
#    ln -sf libteec.so.1.0 ${D}${libdir}/libteec.so.1
#
    install -d ${D}${includedir}
    install -p -m0644 ${S}/public/*.h ${D}${includedir}

#    sed -i -e s:/etc:${sysconfdir}:g \
#           -e s:/usr/bin:${bindir}:g \
#              ${WORKDIR}/tee-supplicant.service
#
    install -D -p -m0644 ${WORKDIR}/tee-supplicant.service ${D}${systemd_system_unitdir}/tee-supplicant.service
}

COMPATIBLE_MACHINE = "(td-host|trustsom)"
