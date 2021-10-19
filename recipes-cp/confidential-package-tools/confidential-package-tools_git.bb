SUMMARY = "Confidential Package Manager (CPM)"
DESCRIPTION = "Trusted Application (TA) managing the host deployment of Confidential Packages"
HOMEPAGE = "https://www.github.com/Scalys/ConfidentialPackageManager"
LICENSE = "MIT"

BBCLASSEXTEND += " native"
DEPENDS = "openenclave-staticdev oeedger8r-cpp-native llvm-native clang-native confidential-package-specification"
#TODO, fix native build to allow building cpk-tool for host use (building confidential packages)
#DEPENDS_remove_class-native = "openenclave-staticdev"

SRC_URI = "git://git@github.com:/Scalys/ConfidentialPackageTools;protocol=ssh;branch=phProtoTooling"

# Generated dependencies with cargo-bitbake
SRC_URI += " \
    crate://crates.io/aho-corasick/0.7.15 \
    crate://crates.io/ansi_term/0.11.0 \
    crate://crates.io/ansi_term/0.12.1 \
    crate://crates.io/anyhow/1.0.43 \
    crate://crates.io/arrayvec/0.5.2 \
    crate://crates.io/atty/0.2.14 \
    crate://crates.io/autocfg/0.1.7 \
    crate://crates.io/autocfg/1.0.1 \
    crate://crates.io/base64/0.13.0 \
    crate://crates.io/base64ct/1.0.1 \
    crate://crates.io/bincode/1.3.3 \
    crate://crates.io/bindgen/0.53.3 \
    crate://crates.io/bitflags/1.2.1 \
    crate://crates.io/bitvec/0.19.5 \
    crate://crates.io/block-buffer/0.9.0 \
    crate://crates.io/bumpalo/3.7.0 \
    crate://crates.io/byteorder/1.4.3 \
    crate://crates.io/bytes/0.5.6 \
    crate://crates.io/bytes/1.1.0 \
    crate://crates.io/cc/1.0.70 \
    crate://crates.io/cexpr/0.4.0 \
    crate://crates.io/cfg-if/0.1.10 \
    crate://crates.io/cfg-if/1.0.0 \
    crate://crates.io/chrono/0.4.19 \
    crate://crates.io/clang-sys/0.29.3 \
    crate://crates.io/clap/2.33.3 \
    crate://crates.io/clap/3.0.0-beta.2 \
    crate://crates.io/clap_derive/3.0.0-beta.2 \
    crate://crates.io/cmake/0.1.45 \
    crate://crates.io/const-oid/0.6.0 \
    crate://crates.io/core-foundation-sys/0.8.2 \
    crate://crates.io/core-foundation/0.9.1 \
    crate://crates.io/cpufeatures/0.1.5 \
    crate://crates.io/crypto-bigint/0.2.5 \
    crate://crates.io/data-encoding/2.3.2 \
    crate://crates.io/der-oid-macro/0.4.0 \
    crate://crates.io/der-parser/5.1.2 \
    crate://crates.io/der/0.4.1 \
    crate://crates.io/derivative/2.2.0 \
    crate://crates.io/digest/0.9.0 \
    crate://crates.io/either/1.6.1 \
    crate://crates.io/encoding_rs/0.8.28 \
    crate://crates.io/env/0.0.0 \
    crate://crates.io/env_logger/0.7.1 \
    crate://crates.io/env_logger/0.8.4 \
    crate://crates.io/fixedbitset/0.2.0 \
    crate://crates.io/fnv/1.0.7 \
    crate://crates.io/foreign-types-shared/0.1.1 \
    crate://crates.io/foreign-types/0.3.2 \
    crate://crates.io/form_urlencoded/1.0.1 \
    crate://crates.io/funty/1.1.0 \
    crate://crates.io/futures-channel/0.3.17 \
    crate://crates.io/futures-core/0.3.17 \
    crate://crates.io/futures-io/0.3.17 \
    crate://crates.io/futures-sink/0.3.17 \
    crate://crates.io/futures-task/0.3.17 \
    crate://crates.io/futures-util/0.3.17 \
    crate://crates.io/generic-array/0.14.4 \
    crate://crates.io/getrandom/0.2.3 \
    crate://crates.io/glob/0.3.0 \
    crate://crates.io/h2/0.3.4 \
    crate://crates.io/hashbrown/0.11.2 \
    crate://crates.io/heck/0.3.3 \
    crate://crates.io/hermit-abi/0.1.19 \
    crate://crates.io/http-body/0.4.3 \
    crate://crates.io/http/0.2.4 \
    crate://crates.io/httparse/1.5.1 \
    crate://crates.io/httpdate/1.0.1 \
    crate://crates.io/humantime/1.3.0 \
    crate://crates.io/humantime/2.1.0 \
    crate://crates.io/hyper-tls/0.5.0 \
    crate://crates.io/hyper/0.14.12 \
    crate://crates.io/idna/0.2.3 \
    crate://crates.io/indexmap/1.7.0 \
    crate://crates.io/ipnet/2.3.1 \
    crate://crates.io/itertools/0.8.2 \
    crate://crates.io/itoa/0.4.8 \
    crate://crates.io/js-sys/0.3.53 \
    crate://crates.io/lazy_static/1.4.0 \
    crate://crates.io/lazycell/1.3.0 \
    crate://crates.io/lexical-core/0.7.6 \
    crate://crates.io/libc/0.2.98 \
    crate://crates.io/libloading/0.5.2 \
    crate://crates.io/libm/0.2.1 \
    crate://crates.io/log/0.4.14 \
    crate://crates.io/matches/0.1.9 \
    crate://crates.io/memchr/2.3.4 \
    crate://crates.io/mime/0.3.16 \
    crate://crates.io/mio/0.7.13 \
    crate://crates.io/miow/0.3.7 \
    crate://crates.io/multimap/0.8.3 \
    crate://crates.io/native-tls/0.2.8 \
    crate://crates.io/nom/5.1.2 \
    crate://crates.io/nom/6.2.1 \
    crate://crates.io/ntapi/0.3.6 \
    crate://crates.io/num-bigint-dig/0.7.0 \
    crate://crates.io/num-bigint/0.3.3 \
    crate://crates.io/num-bigint/0.4.2 \
    crate://crates.io/num-complex/0.3.1 \
    crate://crates.io/num-derive/0.3.3 \
    crate://crates.io/num-integer/0.1.44 \
    crate://crates.io/num-iter/0.1.42 \
    crate://crates.io/num-rational/0.3.2 \
    crate://crates.io/num-traits/0.2.14 \
    crate://crates.io/num/0.3.1 \
    crate://crates.io/num_cpus/1.13.0 \
    crate://crates.io/oid-registry/0.1.5 \
    crate://crates.io/oid/0.2.1 \
    crate://crates.io/once_cell/1.8.0 \
    crate://crates.io/opaque-debug/0.3.0 \
    crate://crates.io/openssl-probe/0.1.4 \
    crate://crates.io/openssl-sys/0.9.66 \
    crate://crates.io/openssl/0.10.36 \
    crate://crates.io/os_str_bytes/2.4.0 \
    crate://crates.io/parsec-client/0.12.0 \
    crate://crates.io/parsec-interface/0.24.0 \
    crate://crates.io/peeking_take_while/0.1.2 \
    crate://crates.io/pem-rfc7468/0.2.0 \
    crate://crates.io/percent-encoding/2.1.0 \
    crate://crates.io/petgraph/0.5.1 \
    crate://crates.io/picky-asn1-der/0.2.5 \
    crate://crates.io/picky-asn1-x509/0.6.1 \
    crate://crates.io/picky-asn1/0.3.3 \
    crate://crates.io/pin-project-lite/0.2.7 \
    crate://crates.io/pin-utils/0.1.0 \
    crate://crates.io/pkcs1/0.2.3 \
    crate://crates.io/pkcs8/0.7.5 \
    crate://crates.io/pkg-config/0.3.19 \
    crate://crates.io/ppv-lite86/0.2.10 \
    crate://crates.io/proc-macro-error-attr/1.0.4 \
    crate://crates.io/proc-macro-error/1.0.4 \
    crate://crates.io/proc-macro2/1.0.27 \
    crate://crates.io/prost-build/0.6.1 \
    crate://crates.io/prost-derive/0.6.1 \
    crate://crates.io/prost-types/0.6.1 \
    crate://crates.io/prost/0.6.1 \
    crate://crates.io/psa-crypto-sys/0.8.0 \
    crate://crates.io/psa-crypto/0.8.0 \
    crate://crates.io/quick-error/1.2.3 \
    crate://crates.io/quote/1.0.9 \
    crate://crates.io/radium/0.5.3 \
    crate://crates.io/rand/0.8.4 \
    crate://crates.io/rand_chacha/0.3.1 \
    crate://crates.io/rand_core/0.6.3 \
    crate://crates.io/rand_hc/0.3.1 \
    crate://crates.io/redox_syscall/0.2.10 \
    crate://crates.io/regex-syntax/0.6.25 \
    crate://crates.io/regex/1.4.6 \
    crate://crates.io/remove_dir_all/0.5.3 \
    crate://crates.io/reqwest/0.11.4 \
    crate://crates.io/ring/0.16.20 \
    crate://crates.io/rsa/0.5.0 \
    crate://crates.io/rustc-hash/1.1.0 \
    crate://crates.io/rusticata-macros/3.2.0 \
    crate://crates.io/ryu/1.0.5 \
    crate://crates.io/same-file/1.0.6 \
    crate://crates.io/schannel/0.1.19 \
    crate://crates.io/secrecy/0.7.0 \
    crate://crates.io/security-framework-sys/2.3.0 \
    crate://crates.io/security-framework/2.3.1 \
    crate://crates.io/serde/1.0.126 \
    crate://crates.io/serde_bytes/0.11.5 \
    crate://crates.io/serde_derive/1.0.126 \
    crate://crates.io/serde_json/1.0.67 \
    crate://crates.io/serde_urlencoded/0.7.0 \
    crate://crates.io/sha2/0.9.5 \
    crate://crates.io/shlex/0.1.1 \
    crate://crates.io/slab/0.4.4 \
    crate://crates.io/smallvec/1.6.1 \
    crate://crates.io/socket2/0.4.1 \
    crate://crates.io/spin/0.5.2 \
    crate://crates.io/spki/0.4.0 \
    crate://crates.io/static_assertions/1.1.0 \
    crate://crates.io/strsim/0.10.0 \
    crate://crates.io/strsim/0.8.0 \
    crate://crates.io/structopt-derive/0.4.15 \
    crate://crates.io/structopt/0.3.22 \
    crate://crates.io/subtle/2.4.1 \
    crate://crates.io/syn/1.0.74 \
    crate://crates.io/synstructure/0.12.5 \
    crate://crates.io/tap/1.0.1 \
    crate://crates.io/tempfile/3.2.0 \
    crate://crates.io/termcolor/1.1.2 \
    crate://crates.io/textwrap/0.11.0 \
    crate://crates.io/textwrap/0.12.1 \
    crate://crates.io/thiserror-impl/1.0.26 \
    crate://crates.io/thiserror/1.0.26 \
    crate://crates.io/time/0.1.43 \
    crate://crates.io/tinyvec/1.3.1 \
    crate://crates.io/tinyvec_macros/0.1.0 \
    crate://crates.io/tokio-native-tls/0.3.0 \
    crate://crates.io/tokio-util/0.6.8 \
    crate://crates.io/tokio/1.11.0 \
    crate://crates.io/tower-service/0.3.1 \
    crate://crates.io/tracing-core/0.1.19 \
    crate://crates.io/tracing/0.1.26 \
    crate://crates.io/try-lock/0.2.3 \
    crate://crates.io/typenum/1.13.0 \
    crate://crates.io/unicode-bidi/0.3.6 \
    crate://crates.io/unicode-normalization/0.1.19 \
    crate://crates.io/unicode-segmentation/1.8.0 \
    crate://crates.io/unicode-width/0.1.8 \
    crate://crates.io/unicode-xid/0.2.2 \
    crate://crates.io/untrusted/0.7.1 \
    crate://crates.io/url/2.2.2 \
    crate://crates.io/users/0.10.0 \
    crate://crates.io/uuid/0.8.2 \
    crate://crates.io/vcpkg/0.2.15 \
    crate://crates.io/vec_map/0.8.2 \
    crate://crates.io/version_check/0.9.3 \
    crate://crates.io/walkdir/2.3.2 \
    crate://crates.io/want/0.3.0 \
    crate://crates.io/wasi/0.10.2+wasi-snapshot-preview1 \
    crate://crates.io/wasm-bindgen-backend/0.2.76 \
    crate://crates.io/wasm-bindgen-futures/0.4.26 \
    crate://crates.io/wasm-bindgen-macro-support/0.2.76 \
    crate://crates.io/wasm-bindgen-macro/0.2.76 \
    crate://crates.io/wasm-bindgen-shared/0.2.76 \
    crate://crates.io/wasm-bindgen/0.2.76 \
    crate://crates.io/web-sys/0.3.53 \
    crate://crates.io/which/3.1.1 \
    crate://crates.io/winapi-i686-pc-windows-gnu/0.4.0 \
    crate://crates.io/winapi-util/0.1.5 \
    crate://crates.io/winapi-x86_64-pc-windows-gnu/0.4.0 \
    crate://crates.io/winapi/0.3.9 \
    crate://crates.io/winreg/0.7.0 \
    crate://crates.io/wyz/0.2.0 \
    crate://crates.io/x509-parser/0.10.0 \
    crate://crates.io/zeroize/1.4.1 \
    crate://crates.io/zeroize_derive/1.1.0 \
"

SRCREV = "${AUTOREV}"
PV = "0.1+git"
S = "${WORKDIR}/git"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=e2260224dcb209442c2b976690f3061f"

CARGO_SRC_DIR = "cpk-tool"

inherit cargo

do_compile_prepend() {

    export OE_PACKAGE_PREFIX="${STAGING_DIR_TARGET}/opt/oe/${MACHINE}"
    export CPS_DIR="${STAGING_DIR_TARGET}/${includedir}/" 
}

do_compile_prepend_class-native () {
    export RUSTFLAGS="${RUSTFLAGS} --features cpm-simulator"
}
