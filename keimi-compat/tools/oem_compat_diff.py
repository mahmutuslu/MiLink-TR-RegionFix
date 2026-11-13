#!/usr/bin/env python3
"""Read-only DEX API comparison for Mirror OEM APKs. Put dex_methods.py alongside."""
import sys
from collections import defaultdict
from dex_methods import apk_methods

def index(path):
    result=defaultdict(set)
    for _,methods in apk_methods(path):
        for cls,name,proto in methods:
            result[cls].add((name,proto))
    return result

def main(old_apk,new_apk):
    old,new=index(old_apk),index(new_apk)
    focus=(
        'Lcom/xiaomi/continuity/networking/TrustedDeviceInfo;',
        'Lcom/xiaomi/continuity/networking/BusinessServiceInfo;',
        'Lcom/xiaomi/mirror/message/ScreenActionMessage;',
    )
    for cls in focus:
        a,b=old.get(cls,set()),new.get(cls,set())
        print('\n'+cls,'old',len(a),'new',len(b))
        for n,p in sorted(a-b):print('REMOVED',n+p)
        for n,p in sorted(b-a):print('ADDED  ',n+p)

if __name__=='__main__':
    if len(sys.argv)!=3:
        raise SystemExit('Usage: python oem_compat_diff.py MirrorOS4.apk Mirror_18.03.01.apk')
    main(sys.argv[1],sys.argv[2])
