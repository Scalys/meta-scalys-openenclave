SUMMARY = "SDK for developing enclaves"
DESCRIPTION = "The Open Enclave SDK is a hardware-agnostic open source library for developing applications that utilize Hardware-based Trusted Execution Environments, also known as Enclaves."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=ecf7715ae2f3910d1baf6908a6725b09"

DEPENDS = "cmake-native ninja-native python3-native python3-pycryptodome-native optee-os-msiot openssl"

SRC_URI = " \
    gitsm://github.com/openenclave/openenclave.git;protocol=https;nobranch=1;tag=v${PV} \
    file://0001-Fix-libgcc-path-and-cmake-byproducts.patch \
"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

PACKAGES = "${PN} ${PN}-dev ${PN}-dbg ${PN}-staticdev"

do_configure() {

    # Add CFLAGS to 3rd party part of build flow
    # We modify it in the source directory so we first revert it to make sure we dont have
    # multiple modifications to is when configure is run multiple times
    cd ${S}
    git checkout 3rdparty/CMakeLists.txt
    sed -i '91i\                 -DCMAKE_C_FLAGS='\''--sysroot=${STAGING_DIR_TARGET}'\''' 3rdparty/CMakeLists.txt
    
    # Get the target compilers without arguments 
    TARGET_CC_BASE=`echo ${CC} | cut -d " " -f1 | sed 's/-gcc//g'`
    TARGET_CC=${TARGET_CC_BASE}-gcc
    TARGET_CXX=${TARGET_CC_BASE}-g++
    TARGET_CXX_VERSION=`${CXX} -dumpversion`

    # modify cmake crosscompile toolchain file to match yocto build config
    git checkout cmake/arm-cross.cmake
    echo "set(CMAKE_C_COMPILER ${TARGET_CC})" >> cmake/arm-cross.cmake
    echo "set(CMAKE_CXX_COMPILER ${TARGET_CXX})" >> cmake/arm-cross.cmake
    echo "set(CMAKE_SYSROOT "${PKG_CONFIG_SYSROOT_DIR}")" >> cmake/arm-cross.cmake
    echo "set(CMAKE_CXX_LINK_FLAGS \"-L${PKG_CONFIG_SYSROOT_DIR}/usr/lib/${TARGET_CC_BASE}/${TARGET_CXX_VERSION}/\")" >> cmake/arm-cross.cmake

    # Help cmake to find the correct compiler for the compilation of oeedger
    BUILD_CC_WITH_PATH=`which ${BUILD_CC}`
    BUILD_CXX_WITH_PATH=`which ${BUILD_CXX}`

    cd ${B}

    cmake ${S} \
        -G Ninja \
        -DCMAKE_INSTALL_PREFIX=${D}/opt/oe/enclave/openenclave \
        -DOE_TA_DEV_KIT_DIR=${STAGING_DIR_HOST}/usr/include/optee/export-user_ta/ \
        -DCMAKE_BUILD_TYPE=Debug \
        -DCMAKE_TOOLCHAIN_FILE=${S}/cmake/arm-cross.cmake \
        -DENABLE_REFMAN=OFF \
        -DEDGER8R_C_COMPILER=${BUILD_CC_WITH_PATH} \
        -DEDGER8R_CXX_COMPILER=${BUILD_CXX_WITH_PATH} 
}

do_compile() {
    ninja -v ${PARALLEL_MAKE}
}


FILES_${PN}-staticdev =" \
    /opt/oe/ \
"

SYSROOT_DIRS += "/opt"
PROVIDES = "${PN}-staticdev"

do_install_append () {
    SDKDIR="${D}/opt/oe"
    
    #install -d -m0644 ${SDKDIR}/3rdparty/libc
    #install -d -m0644 ${SDKDIR}/3rdparty/libcxx
    #install -d -m0644 ${SDKDIR}/3rdparty/libmbedtls
    #install -d -m0644 ${SDKDIR}/enclave/openenclave
   
    # Assemble openeclave SDK compatible export
    install -d -m0644 ${SDKDIR}/
    install -m0644 ${S}/3rdparty/optee/optee_os/ta/arch/arm/ta.ld.S             ${SDKDIR}/
    install -D -m0544 ${S}/3rdparty/optee/optee_os/scripts/sign.py              ${SDKDIR}/sign.py
    sed -i 's|#!/usr/bin/env python|#!/usr/bin/env python3|g'                   ${SDKDIR}/sign.py
    install -m0644 ${S}/3rdparty/optee/optee_os/keys/default_ta.pem             ${SDKDIR}/default_ta.pem
    
    
    install -d -m0644 ${SDKDIR}/${MACHINE}/bin
    install -d -m0644 ${SDKDIR}/${MACHINE}/include/openenclave/bits/
    install -d -m0644 ${SDKDIR}/${MACHINE}/include/openenclave/3rdparty/optee/liboeutee
    install -d -m0644 ${SDKDIR}/${MACHINE}/include/openenclave/corelibc/
    install -d -m0644 ${SDKDIR}/${MACHINE}/include/openenclave/corelibc/bits
    install -d -m0644 ${SDKDIR}/${MACHINE}/share
    install -d -m0644 ${SDKDIR}/${MACHINE}/lib/openenclave/cmake
    install -d -m0644 ${SDKDIR}/${MACHINE}/lib/openenclave/host
    install -d -m0644 ${SDKDIR}/${MACHINE}/lib/openenclave/enclave
    
    # Install OPTEE TA 
    cp -R --no-dereference --preserve=mode,links -v ${PKG_CONFIG_SYSROOT_DIR}/usr/include/optee/export-user_ta/include/* ${SDKDIR}/${MACHINE}/include/openenclave/3rdparty/
    
    install -m0644 ${PKG_CONFIG_SYSROOT_DIR}/usr/include/optee/export-user_ta/host_include/conf.h ${SDKDIR}/${MACHINE}/include/openenclave/3rdparty/optee/
    
    install -D -m0644 ${S}/include/openenclave/host.h                           ${SDKDIR}/${MACHINE}/include/openenclave/host.h
    install -D -m0644 ${S}/include/openenclave/host_verify.h                    ${SDKDIR}/${MACHINE}/include/openenclave/host_verify.h
    install -D -m0644 ${S}/include/openenclave/enclave.h                        ${SDKDIR}/${MACHINE}/include/openenclave/enclave.h
    install -D -m0644 ${S}/include/openenclave/edger8r/host.h                   ${SDKDIR}/${MACHINE}/include/openenclave/edger8r/host.h
    
    for F in ${B}/cmake/* ; do
        install -m0644 $F ${SDKDIR}/${MACHINE}/lib/openenclave/cmake/
    done

    for F in ${B}/CMakeFiles/Export/lib/openenclave/cmake/* ; do
        install -m0644 $F ${SDKDIR}/${MACHINE}/lib/openenclave/cmake/
    done

    for F in ${B}/3rdparty/optee/libutee/liboeutee/*.h ; do
        install -m0644 $F ${SDKDIR}/${MACHINE}/include/openenclave/3rdparty/optee/liboeutee
    done
    
    for F in ${S}/include/openenclave/corelibc/*.h ; do
        install -m0644 $F ${SDKDIR}/${MACHINE}/include/openenclave/corelibc
    done
    
    for F in ${S}/include/openenclave/corelibc/bits/*.h ; do
        install -m0644 $F ${SDKDIR}/${MACHINE}/include/openenclave/corelibc/bits
    done
    

    cp -R --no-dereference --preserve=mode,links -v ${B}/output/include/openenclave/libc ${SDKDIR}/${MACHINE}/include/openenclave/3rdparty/libc

    # Include files
    install -D -m0644 ${B}/output/include/openenclave/bits/time.h              ${SDKDIR}/${MACHINE}/include/openenclave/bits/time.h
    install -D -m0644 ${B}/output/include/openenclave/bits/asym_keys.h         ${SDKDIR}/${MACHINE}/include/openenclave/bits/asym_keys.h 
    install -D -m0644 ${S}/include/openenclave/bits/defs.h                     ${SDKDIR}/${MACHINE}/include/openenclave/bits/defs.h 
    install -D -m0644 ${S}/include/openenclave/bits/eeid.h                     ${SDKDIR}/${MACHINE}/include/openenclave/bits/eeid.h 
    install -D -m0644 ${S}/include/openenclave/bits/evidence.h                 ${SDKDIR}/${MACHINE}/include/openenclave/bits/evidence.h 
    install -D -m0644 ${S}/include/openenclave/bits/exception.h                ${SDKDIR}/${MACHINE}/include/openenclave/bits/exception.h 
    install -D -m0644 ${S}/include/openenclave/bits/fs.h                       ${SDKDIR}/${MACHINE}/include/openenclave/bits/fs.h 
    install -D -m0644 ${S}/include/openenclave/bits/types.h                    ${SDKDIR}/${MACHINE}/include/openenclave/bits/types.h 
    install -D -m0644 ${S}/include/openenclave/bits/module.h                   ${SDKDIR}/${MACHINE}/include/openenclave/bits/module.h 
    install -D -m0644 ${S}/include/openenclave/bits/properties.h               ${SDKDIR}/${MACHINE}/include/openenclave/bits/properties.h
    install -D -m0644 ${S}/include/openenclave/bits/report.h                   ${SDKDIR}/${MACHINE}/include/openenclave/bits/report.h
    install -D -m0644 ${S}/include/openenclave/bits/result.h                   ${SDKDIR}/${MACHINE}/include/openenclave/bits/result.h 
    install -D -m0644 ${S}/include/openenclave/bits/optee/opteeproperties.h    ${SDKDIR}/${MACHINE}/include/openenclave/bits/optee/opteeproperties.h
    install -D -m0644 ${S}/include/openenclave/edger8r/enclave.h               ${SDKDIR}/${MACHINE}/include/openenclave/edger8r/enclave.h
    install -D -m0644 ${S}/include/openenclave/edger8r/common.h                ${SDKDIR}/${MACHINE}/include/openenclave/edger8r/common.h  

    
    # Static libraries
    install -D -m0644 ${B}/output/lib/openenclave/host/liboehost.a             ${SDKDIR}/${MACHINE}/lib/openenclave/host/liboehost.a
    install -D -m0644 ${B}/output/lib/openenclave/enclave/liboeenclave.a       ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/liboeenclave.a
    install -D -m0644 ${B}/output/lib/openenclave/enclave/liboecore.a          ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/liboecore.a
    install -D -m0644 ${B}/output/lib/openenclave/enclave/liboelibc.a          ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/liboelibc.a
    install -D -m0644 ${B}/output/lib/openenclave/enclave/liboecryptombedtls.a ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/liboecryptombedtls.a
    install -D -m0644 ${B}/output/lib/openenclave/enclave/liboedebugmalloc.a   ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/liboedebugmalloc.a
    install -D -m0644 ${B}/output/lib/enclave/liboelibcxx.a                    ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/liboelibcxx.a
    install -D -m0644 ${B}/syscall/liboesyscall.a                              ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/liboesyscall.a
    install -D -m0644 ${B}/syscall/devices/hostfs/liboehostfs.a                ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/liboehostfs.a
    install -D -m0644 ${B}/syscall/devices/hostresolver/liboehostresolver.a    ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/liboehostresolver.a
    install -D -m0644 ${B}/syscall/devices/hostsock/liboehostsock.a            ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/liboehostsock.a
    install -D -m0644 ${B}/syscall/devices/hostepoll/liboehostepoll.a          ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/liboehostepoll.a


    install -D -m0644 ${B}/3rdparty/mbedtls/libmbedcrypto.a                    ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/libmbedcrypto.a
    install -D -m0644 ${B}/3rdparty/mbedtls/libmbedtls.a                       ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/libmbedtls.a
    install -D -m0644 ${B}/3rdparty/mbedtls/libmbedx509.a                      ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/libmbedx509.a

    # TODO, make sure these match the acual running optee version
    install -D -m0644 ${B}/3rdparty/optee/libutee/liboeutee.a                  ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/liboeutee.a
    install -D -m0644 ${B}/3rdparty/optee/libutee/liboeuteeasm.a               ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/liboeuteeasm.a
    install -D -m0644 ${B}/3rdparty/optee/libutee/liboeutee.a                  ${SDKDIR}/${MACHINE}/lib/openenclave/enclave/liboeutee.a
    
    install -D -m0644 ${B}/3rdparty/optee_client-wrap-prefix/src/optee_client-wrap-build/libteec/libteec.a ${SDKDIR}/${MACHINE}/lib/openenclave/optee/libteec/libteec.a

    #Remove paths from commands, as we assume everything external to be put in place by yocto
    sed -i 's|${OE_BINDIR}/||g' ${SDKDIR}/${MACHINE}/lib/openenclave/cmake/openenclave-config.cmake

}
