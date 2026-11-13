#!/usr/bin/env python3
"""Read-only DEX API comparison for Mirror OEM APKs. Put dex_methods.py alongside."""
import sys
from collections import defaultdict
import zipfile,struct,re

def uleb(b,p):
    out=0; shift=0
    for _ in range(5):
        x=b[p];p+=1;out|=(x&127)<<shift
        if x<128:return out,p
        shift+=7
    raise ValueError('invalid uleb')

def parse_dex(b):
    u32=lambda p:struct.unpack_from('<I',b,p)[0]
    strings=[]
    for i in range(u32(0x38)):
        off=u32(u32(0x3c)+4*i);_,off=uleb(b,off)
        strings.append(b[off:b.index(b'\\0',off)].decode('utf-8','replace'))
    types=[strings[u32(u32(0x44)+4*i)] for i in range(u32(0x40))]
    protos=[]
    for i in range(u32(0x48)):
        _,ret,args=struct.unpack_from('<III',b,u32(0x4c)+12*i)
        params=[] if not args else [types[struct.unpack_from('<H',b,args+4+2*j)[0]] for j in range(u32(args))]
        protos.append('('+''.join(params)+')'+types[ret])
    for i in range(u32(0x58)):
        cl,pr,n=struct.unpack_from('<HHI',b,u32(0x5c)+8*i)
        yield types[cl],strings[n],protos[pr]

def apk_methods(path):
    with zipfile.ZipFile(path) as z:
        for name in z.namelist():
            if re.fullmatch(r'classes(?:\\d+)?\\.dex',name):
                yield name,parse_dex(z.read(name))

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
