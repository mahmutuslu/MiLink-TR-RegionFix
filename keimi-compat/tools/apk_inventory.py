#!/usr/bin/env python3
"""Read-only APK/DEX inventory for KeiMi compatibility research; stdlib only.

This is not a DEX decompiler, and keyword hits are NOT proof of hook compatibility.
"""
import argparse
import hashlib
import json
import re
import zipfile
from pathlib import Path

KEYWORDS = (
    b"SmartHubUIUtil", b"enterSmartHubUI", b"quitSmartHubUI",
    b"startDiscovery", b"stopDiscovery", b"FusionUtils",
    b"mirror_computer_fusion_descriptors", b"resolve_mirror_computer_fusion",
    b"mirror.generic_windows_fusion_query",
)

def inspect(apk):
    file = Path(apk)
    result = {
        "filename": file.name,
        "sha256": hashlib.sha256(file.read_bytes()).hexdigest(),
        "size_bytes": file.stat().st_size,
        "dex_files": [],
        "keyword_dex_hits": {},
    }
    with zipfile.ZipFile(file) as z:
        dex_names = sorted(n for n in z.namelist() if re.fullmatch(r"classes(?:[2-9][0-9]*)?\.dex", n))
        for name in dex_names:
            raw = z.read(name)
            result["dex_files"].append({"name": name, "bytes": len(raw), "sha256": hashlib.sha256(raw).hexdigest()})
            for word in KEYWORDS:
                if word in raw:
                    result["keyword_dex_hits"].setdefault(word.decode(), []).append(name)
    return result

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("apk", nargs="+")
    args = parser.parse_args()
    results = []
    for name in args.apk:
        try:
            results.append(inspect(name))
        except (OSError, zipfile.BadZipFile) as exc:
            results.append({"filename": name, "error": str(exc)})
    print(json.dumps(results, indent=2, ensure_ascii=False))

if __name__ == "__main__":
    main()
