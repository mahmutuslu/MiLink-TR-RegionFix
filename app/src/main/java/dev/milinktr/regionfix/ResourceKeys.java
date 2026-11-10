package dev.milinktr.regionfix;

import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.InflaterInputStream;

/** Resource names extracted from user-supplied MiLink + LightOS overlay APKs. */
final class ResourceKeys {
    private ResourceKeys() {}
    static String[] all() {
        final String payload =
            "eNqtXMmy4yoS3dfXvKobHdH77m1HvD9QYIRsypLQA3Tv9d93MkgWiGSwa1MDeU4yJUmSIJNloVzSdSSadWRkUneXVT26mV2J5p/sB0EAi1A8C9DsW+NCrseIOkhyndisu2dRzz45" +
            "ZR0lsgea1mJuoqj10l6P+uKa3t7iWmL38w1qdx3FhYyva/j1OvXjDeor7W4a5pEr7U0B2DNl40tMMQ9cTk1UY87dLGbWzlKMSHrj8zWkakEAAUXdRL47RSVjpmnrrDHcLLTSZBxT" +
            "crOsUuWpUfISMc+MatanhAPhSdKNX2G22bQwSfQqWQozCtIjvYUewNJaFgGOwI/TQvpaJK0F3k7zhGL1ZzUwP2LQjw3ArXXKJMo0rQKnVkqZUrEXCYW/csKPHwfP2/cdgT7dmNzX" +
            "npi1FKOxqyNw7bnorlKsi1+YT9mF0PthBWlj9T0Du+WL6ckBSckE5gHOgCjFlCNq0V3Et3cUTMru4+dfOMV6hlVt5UUgmFsR2q/LyKldoZyM4nrqYBXe2mszh7ZTrA23ssCaWylf" +
            "fOCdWhi5M/kWGf6jb96P4Ypg6sVuhIdlFjBk1gBkYdYDuV2WCUzvbZ3eyHy1s5qwhxPKrt4MbhnJY6u1Bobr67nyXiKWePfqZ8ApVJpJJvLIC5n7AmJcmRbgJvKwcEbz2BsjvWKF" +
            "Pvi9T8HeV2hhBeSLQEASQyZ+NDsfS2YxZgb3IDCLrEO5xo1k0WJBkDc2YqKK1qAmt8mtrZWU4AbpEZsCWLtyglCvgDKtyiJhW/V7ys8i4lcR8REjlOY0dmzP4q6TbBL6uRuy74XN" +
            "/ba9/RfZ3Q4KiN1sUWnfY6LLSOZ7Q0VUTMvINMPlrgf1GnsJLTfrhv2z8sUEsRiyeVRmgUnC8a5DQeAAkztVgik0EZ/wGCyEBN9BbCjG52XVLxPRpYVpqK7rJqZapXf2uAiQV8JH" +
            "NtQ2AuxjrYQu4qt6AiSDU8RcC4bDR217E24WQX6KcZ1ySzWJl6xfKTotio3uoBDJwVNtu8ZjpgnpsI5jKoZy0itT5tDVUYjF7mBwkuVAhxg9AxP3rDC9GRwg6Rk8ANTIe1aSo9VM" +
            "j+iIsgkgdjRniyAXUEL5cz8GYz3X7sTuz1D2RIXClXGKNp7FIYhP2AE2S4YKEerCF7A0U5JtwiL5J6EP+Bu00ZS/BlDeIQMANUYftbk9uCTvTEIAxYihqMb2NyW+keQqUBByjBmt" +
            "Voz3TaySJtWCX0nQxDSBfcFSkI/OlARVuqF1oZW3xBsPd9sj5DTrmnTkCjXGZSYjRKa42Cz3buQ6cYTcpHnBlsQL5eyb66hoAdvhEEJAf/aTQ9LiQ+yVabAGm8hivemFqiBNhBoX" +
            "bUKWCrS4aMLNvjyY0NPu0WWSZPaoCGZhzrdlvD2LVONKIC0kwc6haWhFzR5ZgGlJZjWAFzHBYE9AWFb9BeHrM8o32U1SAKcUOvbJ3qIUlV8z6VzTdrx6LMyuBvx0ekKez6YnyMR/" +
            "c5IDBJmdszRaaCcAknzJ4pA8i9m8vK2l83cWYPbdpBRN9Eyw5MCIGqBRiqwMj0apSAiyXGe0n9eWDqQpxY5gtGKHEGLQMYOB2Y5NcKMes6pRiIdBXN71r3/XQMOu47Coqyjw1LUY" +
            "lfA5KVg1prKzWEIax5ZHJp3BxZHJsdkT9DOFeJ8RWcD0XJGLybiFoUUKOgtuM6tRzJyCimHIIWzykQ0DhJfH27xKRoNytcA2TsYifOAmICYSG/wMuvuCf5kt0IQNFaNT1HA6bbyi" +
            "QrP8dJZU1E2IDWtrx+5C4JQEgS4lqgYWJRkwWHy4j3EUAv1reIqIIRN/wSJjkpGbkIje2yrLG3OMLtvzJMzZmGkNzVAJ3HMzQU31Cdl3IGxLrKUg3q9MQ2yqSEz6xicryF5sYnfH" +
            "RKi9TLeXTFN0KRAj/1m5hMPI5dG5S9KbUBqHc7mIXj0dujn3wHEDHHo1xzTqSwR5k5AQXZulb8pCSs8Gso45eebSLYSCF/CHwRq0iygb1B9HoQWbHbEYaOelHm1S4UlH4fA+T2IM" +
            "43DaXWc2m10XpSk2Dt26XCXpcZDmExMrPgQp/rjU3cVbZPgcKiVMSuLEkiv85rqh6oGx/vBy4CD+vU6LiYPiAHcUcAh3F0dhdsMLnFmGIussqxplkc5U41LvbA/FM9NgG/urh+iC" +
            "bpOe/O8ucBe49jENeBe/PhPeFyFsPvB/hNaD//5PPZb/HXjzIjpy4gjeTF1zXw2pqb9HQk2fj/i6foeM+r5vnHXenhAttdUt9OgTWQUrXrgxbE9l53EmXizO1QGU79J+G5+A+Haj" +
            "i8YmdLuRrDM1O7FLiC2qAumzpwFWLMzkAcMwwr0JMY9KmWnpqljijUgSxMYRpLISZtM2LdhTIm1nJJ/CBFJwd/fw0usgTr6UieRpBeoxM3l9pHYPMNZc7QfpfUoK0q2ihUYF8lCz" +
            "e1xXnNknLD2skTwx6SEi0cgjIN3NE6JQzRFzMqws8GxVMbyiA/DHPYEzB017bjxcbNjnTSWMQvT46MxATFm4H6dQSpMgekxj0pXFFh0KYlouuaqOT0VdZ8PnVmdA3HAE4R1mARg2" +
            "NAmoURQ/ntpWvz+N0MA8I2G4I8RCmpFFW+tTbF5Qf6Ykm3kjDdrEmOazHW+S+5Ru6gSrgG7v9hJyzUYW16bJ3T/y08f3MImQV3/a2zyzWYXqzaNjsuhVnorTZggCrjqy6pvbgpdQ" +
            "Zl9mdGTU8Gd08xxLI8WfvGfCt4W51+GY0EccJ3lwO7pFJz6/YbpvT/TmuohRMYor/G884y425xSXmlTAVZiU1PZC21u1KmO3R9g4cGFSiRmOHtux4YQcRbJVYprWeeuViVbsLFbB" +
            "9kuxM9oVQKP89VoG4x4AoABbH5NIp4RctlALkXqDx2VuZLZnFBnc/kGIG/Is1gTIg797nSZSQO8fX+DoK4M1DNO7T7gLLLctuxK+xaE4wTfg2DrrM1vw6REvMbrPX2WSZODd3GOb" +
            "ErRugCL0yBf/Nq2OMPCR1WLDQ8BrrKpBckQwQYir/IqtmsM07+UqW3p5ZqLO5T6Lr3nP1MTSiXuNmizbu5sYMw8UFe3r9vgkOo0zOyFLDCtWvkim2Ky3gVUKvNb+MKoOjLRmf0Ql" +
            "YH9KOA/JBlB3SwnQ5aTMlb5PTyek/kuwpM98urP8IOZXaHFF5lZgyd1FawsOjt3A7MdiHTc2PxD7aN6Z6DqPgt5fUAIoJmFyTHT/ji67Hv6stvc1vMCcCL0IcX+n8ombb2f+sIKk" +
            "iyvqeKFyYxX/rOZdrM35pBdenv5G19+e+Y36ZjfyNR+3ghqMDcv3T2ra8JlgLkF0qfUKID8GjFUMd23wIgGPGiuJGftPcEfxtV+It9d8ZGf3hQTX3XvCFt8wUC+i2yzjTKsd0hTz" +
            "o5HZXFVtDebCyS/aKoKbHz6ZlA1Jhg1FTpNFxdx6e9otqXnhGd9nSdXo57i39C1k1vdMMsr4p4l/L634l0w/w681zKMKFzs+D4kuK/yeEuwk0abl89efaQ3oeatBh3urVv57g/re" +
            "aK7zcHxj3siutCT7qUmt5W/gl8weI7e29FXvv/NPJ9YWJ7NreYfU3OUkfs8KlbN+QQYJzf6d80w4BrXpQJ5teO4oHUOyirLHWiQ1lMru4NiK6ku5quYcVZ7wUSKUk1kvJLHak1fF" +
            "pFWqmlWZGwvzcZT7fjmWm08lekm+jO2bnM+P/RcA1GP7t3ueLle1S5+59fg1ojmbmiFCgdvPYm1JNOSTkBm2m8tji418xodIaX5lwvbkXLQ/Bg6/u/P/W6T47b/xO5XYp7OEwrGA" +
            "m+9TVRlhF5JJQB3y5uANvl5kmhErM6n79RrnLMtw84sc3bo0Ni9m1TVNDMP+HWJ1Aw8ktzGXKdEXmw2VxcymGjVrHEX39eQXn3s4cdY3MkV7o8bKyQuo2LjAwpLmRZL92a6SHGkz" +
            "+ybmNxq6XtDVrlj79mDiK//5rxo4PpAYtEE52nEPvJrPMUFjXZs3dLnJEbJedbnBE6tu7cSqmvqEVSrFGmnTWedi5ah4UzwAU5st35tsN1D/xMF9LGjut9PF9rtsdYOVIZkyDzaN" +
            "ts//A1VKKJM=";
        try {
            byte[] packed = Base64.decode(payload, Base64.NO_WRAP);
            ByteArrayOutputStream buf = new ByteArrayOutputStream(22000);
            byte[] block = new byte[2048];
            try (InflaterInputStream inflater =
                         new InflaterInputStream(new ByteArrayInputStream(packed))) {
                int n;
                while ((n = inflater.read(block)) != -1) buf.write(block, 0, n);
            }
            return new String(buf.toByteArray(), StandardCharsets.UTF_8).split("\n");
        } catch (Throwable t) {
            android.util.Log.w("MiLinkTR", "Offline resource index unavailable", t);
            return new String[0];
        }
    }
}
