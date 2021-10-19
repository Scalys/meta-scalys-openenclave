
#KERNEL_DEVICETREE = "freescale/trustsom-tbdconnect.dtb"
#KERNEL_DEVICETREE = "freescale/scalys-tbe201.dtb"

SRC_URI_remove = "git://git@github-readonly-shared/Scalys/trustsom-linux.git;nobranch=1;protocol=ssh"
SRC_URI_append = " \
    git:///home/sinu7/sam/trustsom-linux-openenclave;protocol=file;nobranch=1 \
    file://fullconfig.cfg \
    file://switch_config.cfg \
    file://usb_config.cfg \
    file://m2_ax200.cfg \
    file://lte.cfg \
"
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"


#SRCREV_trustsom = "${AUTOREV}"
#SRCPV = "5.4.42069"
