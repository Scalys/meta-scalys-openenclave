SUMMARY = "Confidential Package Manager (CPM)"
DESCRIPTION = "Trusted Application (TA) managing the host deployment of Confidential Packages"
HOMEPAGE = "https://www.github.com/Scalys/ConfidentialPackageManager"
LICENSE = "MIT"

#inherit cmake
BBCLASSEXTEND += " native" 
DEPENDS = "cmake-native oeedger8r-cpp-native openenclave-staticdev python3-native python3-pycryptodome-native confidential-package-specification"


SRC_URI = "git://git@github.com:/Scalys/ConfidentialPackageManager;protocol=ssh;branch=main"
SRCREV = "${AUTOREV}"
PV = "0.1+git${SRCPV}"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=e2260224dcb209442c2b976690f3061f"

SECURITY_CFLAGS_remove = "-fstack-protector-strong"

do_configure() {

    # Get the target compilers without arguments 
    TARGET_CC_BASE=`echo ${CC} | cut -d " " -f1 | sed 's/-gcc//g'`
    TARGET_CC=${TARGET_CC_BASE}-gcc
    TARGET_CXX=${TARGET_CC_BASE}-g++
    TARGET_CXX_VERSION=`${CXX} -dumpversion`

    cd ${B}

    # create custom cmake cross-compile file
    echo "#Custom yocto recipe generated toolchain file" > ${B}/arm-cross.cmake
    echo "set(CMAKE_SYSTEM_NAME Linux)" >> ${B}/arm-cross.cmake
    echo "set(CMAKE_SYSTEM_PROCESSOR aarch64)" >> ${B}/arm-cross.cmake
    echo "set(CMAKE_C_COMPILER_ID GNU)" >> ${B}/arm-cross.cmake
    echo "set(CMAKE_C_COMPILER ${TARGET_CC})" >> ${B}/arm-cross.cmake
    echo "set(CMAKE_CXX_COMPILER ${TARGET_CXX})" >> ${B}/arm-cross.cmake
    echo "set(CMAKE_SYSROOT "${PKG_CONFIG_SYSROOT_DIR}")" >> ${B}/arm-cross.cmake
    echo "set(LIBGCC_PATH \"${PKG_CONFIG_SYSROOT_DIR}/usr/lib/${TARGET_CC_BASE}/${TARGET_CXX_VERSION}/\")" >> ${B}/arm-cross.cmake

    cmake \
        -DOE_TEE=OP-TEE \
        -DOE_PACKAGE_OPTEE_PLATFORM=${MACHINE}  \
        -DCMAKE_BUILD_TYPE=release \
        -DCMAKE_TOOLCHAIN_FILE=${B}/arm-cross.cmake \
        -DOE_PACKAGE_PREFIX=${PKG_CONFIG_SYSROOT_DIR}/opt/oe/ \
        -DCPS_DIR=${STAGING_DIR_TARGET}/${includedir}/ \
        ${S}
}


FILES_${PN} = "\
    /lib/optee_armtz/d3c5bbbc-adad-11eb-bd25-4362002e2749.ta \
    /usr/bin/EnclaveFibonacci \
"

do_install() {
    install -D -p -m0750 ${B}/out/bin/d3c5bbbc-adad-11eb-bd25-4362002e2749.ta ${D}/lib/optee_armtz/d3c5bbbc-adad-11eb-bd25-4362002e2749.ta
}
