#!/usr/bin/env python3
"""Read DEX method reference tables and compare Xiaomi APKs without Android Studio.

Method references include externally referenced methods; they DO NOT establish that
the method is implemented by the APK. Do not use this as a hook compatibility proof.
"""
import argparse
import json
import re
import struct
import zipfile
from pathlib import Path

def uleb(data, pos):
    value = shift = 0
    for i in range(5):
        byte = data[pos + i]
        value |= (byte & 127) << shift
        if byte < 128:
            return value, i + 1
        shift += 7
    raise ValueError("Invalid ULEB128")

def parse_method_refs(data):
    if data[:4] != b"dex\n":
        return set()
    u32 = lambda i: struct.unpack_from("<I", data, i)[0]
    strings = []
    for i in range(u32(0x38)):
        offset = u32(u32(0x3c) + 4 * i)
        _, n = uleb(data, offset)
        start = offset + n
        strings.append(data[start:data.index(0, start)].decode("utf-8", "replace"))
    types = [strings[u32(u32(0x44) + 4*i)] for i in range(u32(0x40))]
    protos = []
    for i in range(u32(0x48)):
        _, ret, args_ptr = struct.unpack_from("<III", data, u32(0x4c) + i*12)
        args = []
        if args_ptr:
            args = [types[struct.unpack_from("<H", data, args_ptr+4+2*j)[0]]
                    for j in range(u32(args_ptr))]
        protos.append("(" + "".join(args) + ")" + types[ret])
    result = set()
    for i in range(u32(0x58)):
        cls, proto, name = struct.unpack_from("<HHI", data, u32(0x5c) + i*8)
        result.add((types[cls], strings[name], protos[proto]))
    return result

def apk_method_refs(path):
    result = set()
    with zipfile.ZipFile(path) as apk:
        for name in apk.namelist():
            if name.startswith("classes") and name.endswith(".dex"):
                result.update(parse_method_refs(apk.read(name)))
    return result

def text(x):
    return x[0] + "->" + x[1] + x[2]

def main():
    p = argparse.ArgumentParser()
    p.add_argument("apks", nargs="+")
    p.add_argument("--filter", default=r"(?i)(fusion|computer|discover|smarthub|smart_hub)")
    args = p.parse_args()
    rx = re.compile(args.filter)
    outputs = {}
    for path in args.apks:
        methods = apk_method_refs(path)
        outputs[Path(path).name] = {
            "total_references": len(methods),
            "matching_method_references": sorted(text(m) for m in methods if rx.search(m[0] + " " + m[1]))
        }
    print(json.dumps(outputs, indent=2))

if __name__ == "__main__":
    main()
