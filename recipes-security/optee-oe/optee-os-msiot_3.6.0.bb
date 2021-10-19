SUMMARY = "OP-TEE Trusted OS with OpenEnclave support (OCALL)"
DESCRIPTION = "OPTEE OS"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=c1f21c4f72f372ef38a5a4aee55ec173"

DEPENDS = "python-pycrypto-native python-pyelftools-native"

inherit deploy pythonnative

SRCREV = "e2d570599f761dd2a3b05ce315b317888504b39c"
SRC_URI = " \
    gitsm://github.com/ms-iot/optee_os.git;protocol=https;branch=ms-iot-openenclave-3.6.0 \
    file://0001-allow-setting-sysroot-for-libgcc-lookup.patch \
    file://0002-use-python3-instead-of-python.patch \
"
S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "(trustsom)"

OPTEEMACHINE ?= "${MACHINE}"
OPTEEMACHINE_trustsom = "ls1012grapeboard"

EXTRA_OEMAKE = " \
    PLATFORM=ls-${OPTEEMACHINE} \
    CFG_ARM64_core=y \
    CROSS_COMPILE_core=${HOST_PREFIX} \
    CROSS_COMPILE_ta_arm64=${HOST_PREFIX} \
    LIBGCC_LOCATE_CFLAGS=--sysroot=${STAGING_DIR_HOST} \
    CFG_TEE_TA_LOG_LEVEL=0 \
"

do_compile() {
    unset LDFLAGS

    oe_runmake all O=${B}/out/${OPTEEMACHINE} PLATFORM=ls-${OPTEEMACHINE} CFG_TEE_CORE_LOG_LEVEL=3 CFG_TEE_TA_LOG_LEVEL=3

    ${OBJCOPY} -v -O binary ${B}/out/${OPTEEMACHINE}/core/tee.elf   ${B}/out/${OPTEEMACHINE}/core/tee.bin
    
}

do_install() {
    #install core on boot directory
    install -d ${D}/lib/firmware/
    
    install -m 644 ${B}/out/${OPTEEMACHINE}/core/tee.bin ${D}/lib/firmware/tee_${OPTEEMACHINE}.bin
    #install TA devkit
    install -d ${D}/usr/include/optee/export-user_ta/

    for f in  ${B}/out/${OPTEEMACHINE}/export-ta_arm64/* ; do
        cp -aR  $f  ${D}/usr/include/optee/export-user_ta/
    done

    # Make use the sign script used OE python, and not the HOST python 
    sed -i 's|SIGN ?= $(ta-dev-kit-dir$(sm))/scripts/sign.py|SIGN ?= python $(ta-dev-kit-dir$(sm))/scripts/sign.py|g' ${D}/usr/include/optee/export-user_ta/mk/link.mk
    sed -i 's|SIGN ?= $(TA_DEV_KIT_DIR)/scripts/sign.py|SIGN ?= python $(TA_DEV_KIT_DIR)/scripts/sign.py|g' ${D}/usr/include/optee/export-user_ta/mk/link_shlib.mk
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_deploy() {

    # create an archive of the OP-TEE SDK, stripping path information
    SOURCE_PATH=`echo ${B} | cut -c2-`
    tar -cz --transform "s|${SOURCE_PATH}/out/${OPTEEMACHINE}/export-ta_arm64|${OPTEEMACHINE}|" -f ${DEPLOYDIR}/optee_${OPTEEMACHINE}_sdk.tgz ${B}/out/${OPTEEMACHINE}/export-ta_arm64/*
}

addtask deploy after do_install

FILES_${PN} = "/lib/firmware/"
FILES_${PN}-dev = "/usr/include/optee"

INSANE_SKIP_${PN}-dev = "staticdev"

INHIBIT_PACKAGE_STRIP = "1"

