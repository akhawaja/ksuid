# ksuid - Java Version

A Java implementation of the [Segment's KSUID library](https://github.com/segmentio/ksuid). For a full treatment of why this came about, you can read the [blog post](https://segment.com/blog/a-brief-history-of-the-uuid/) on their website.

# Maven Central
This library can be downloaded from Maven Central by adding the following as a dependency in your pom.xml file:

``` xml
<dependency>
    <groupId>com.amirkhawaja</groupId>
    <artifactId>ksuid</artifactId>
    <version>1.0.0</version>
</dependency>
```

# Quick Start
``` java
final Ksuid ksuid = new Ksuid();
final String uid = ksuid.generate();
// e.g. output: Be785NYYxP29BJiAJPupfsXuGpR

final String decoded = ksuid.parse(uid);
// e.g. output: Time: 2017-07-08T21:13:08Z[UTC]
//              Timestamp: 1499548388
//              Payload: [-42, 24, -60, -3, -66, 38, 32, 9, 62, -22, 95, -79, 123, -122, -91, 0] 
```

# History
- 1.0.0: Initial public release.
