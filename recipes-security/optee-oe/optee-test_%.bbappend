COMPATIBLE_MACHINE = "trustsom"

inherit pythonnative

DEPENDS_append = "optee-client-msiot optee-os-msiot python-pycrypto-native"
DEPENDS_remove = "optee-client optee-os python3-pycryptodomex-native"

PV = "3.6.0"
SRCREV = "40aacb6dc33bbf6ee329f40274bfe7bb438bbf53"


