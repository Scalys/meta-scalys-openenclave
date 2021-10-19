SUMMARY = "Scalys OpenEnclave development and test image"
DESCRIPTION = ""
LICENSE = "GPLV2"
LIC_FILES_CHKSUM = ""

inherit image

IMAGE_FSTYPES += " tar.gz"

BOOT_DEPS = "\
    kernel-image \
    kernel-modules \
    \
    base-files \
    base-passwd \
    busybox \
    systemd \
    syslog-ng \
"

IMAGE_INSTALL += "\
    ${BOOT_DEPS} \
    net-tools \
    iproute2 \
    ifupdown \
    iptables \
    wpa-supplicant \
    iw \
    systemd \
    libqmi \
    \
    openssh \
    \
    tpm2-tools \
    tpm2-tss-engine \
    libtss2-tcti-device \
    tpm2-abrmd \
    linux-firmware-iwlwifi \
    \
    optee-os-msiot \
    optee-client-msiot \
    optee-test \
    \
    confidential-package-manager \
    confidential-package-tools \
"


inherit extrausers
EXTRA_USERS_PARAMS = "\
    usermod -P root root; \
"
